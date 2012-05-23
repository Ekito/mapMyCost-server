package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import mock.PicturesListMock;
import mock.TransactionsListMock;
import models.Area;
import models.Heatpoint;
import models.MappingInfo;
import models.Picture;
import models.Transaction;
import models.TransactionSummary;

import org.apache.commons.io.FileUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

public class Application extends Controller {

	public static Result index() {
		return redirect(routes.Application.showMap());
	}

	public static Result showMap() {
		return ok(views.html.map.render());

	}

	public static Result phonemap() {
		return ok(views.html.phonemap.render());

	}

	public static Result documentation() {
		return TODO;

	}

	/**
	 * Reset the dataset
	 * 
	 * @return
	 */
	public static Result reset() {

		TransactionsListMock.addSampleDataset();
		Job.getAxaTransactions("1000000","20000001500", 200,false); 

		return ok(views.html.map.render());

	}

	public static Result allTransactions() {

		// a small mapping to send light objects to the client
		Collection<TransactionSummary> summaries = new HashSet<TransactionSummary>();

		for (Transaction transaction : TransactionsListMock.transactions
				.values()) {
			TransactionSummary summary = new TransactionSummary(transaction.id,
					transaction.date, transaction.amount, transaction.title,
					transaction.mapped);
			summaries.add(summary);
		}

		return ok(Json.toJson(summaries));
	}

	public static Result transaction(String id) {

		// find the transaction
		Transaction transaction = findTransaction(id);

		if (transaction != null) {
			return ok(Json.toJson(transaction));
		}

		return notFound();
	}

	public static Result transactionMapping() {
		Form<MappingInfo> mappingInfoForm = form(MappingInfo.class);

		MappingInfo mappingInfo = mappingInfoForm.bindFromRequest().get();

		// search the transaction
		Transaction transaction = findTransaction(mappingInfo.id);

		if (transaction == null) {
			return notFound();
		}

		// retrieve the file from the request
		FilePart picture = request().body().asMultipartFormData()
				.getFile("picture");

		if (picture != null) {
			Logger.debug("Getting picture " + picture.getFilename());

			// store the file
			try {
				byte[] fileAsBytes = FileUtils.readFileToByteArray(picture
						.getFile());

				addPicture(mappingInfo.id, fileAsBytes,
						picture.getContentType());

			} catch (IOException e) {
				return badRequest(e.getMessage());
			}
		}

		// store the mapping
		transaction.mapped = true;
		// store the lat & long
		transaction.longitude = mappingInfo.longitude;
		transaction.latitude = mappingInfo.latitude;

		// store the url of the picture
		transaction.picture = controllers.routes.Application.picture(
				mappingInfo.id).url();
		updateTransaction(transaction);

		// return the detailed description of the transaction
		return transaction(mappingInfo.id);
	}

	public static Result transactionsInArea() {

		Form<Area> areaForm = form(Area.class);
		Area area = areaForm.bindFromRequest().get();

		// look for transactions in this area
		List<TransactionSummary> transactionSummaries = findTransactionsInArea(area);

		// order the transactions by date
		Collections.sort(transactionSummaries,
				new Comparator<TransactionSummary>() {
					@Override
					public int compare(TransactionSummary o1,
							TransactionSummary o2) {
						if (o1.date.before(o2.date)) {
							return -1;
						} else {
							return 1;
						}
					}
				});

		return ok(Json.toJson(transactionSummaries));
	}

	public static Result picture(String id) {

		Picture picture = findPicture(id);

		if (picture != null) {

			response().setContentType(picture.contentType);

			return ok(picture.bytes);
		}

		return notFound();

	}

	public static Result heatpoints() {

		Collection<Heatpoint> heatpoints = findHeatpoints();

		return ok(Json.toJson(heatpoints));
	}

	// ---------- mocked services ------------------ //

	private static Transaction findTransaction(final String id) {
		return TransactionsListMock.findTransaction(id);
	}

	private static void updateTransaction(final Transaction transaction) {
		TransactionsListMock.transactions.put(transaction.id, transaction);
	}

	private static List<TransactionSummary> findTransactionsInArea(Area area) {
		List<TransactionSummary> transactionSummaries = new ArrayList<TransactionSummary>();

		for (Transaction transaction : TransactionsListMock.transactions
				.values()) {
			if (transaction.mapped) {
				if (transaction.latitude >= area.bottom
						&& transaction.latitude <= area.top) {
					if (transaction.longitude <= area.right
							&& transaction.longitude >= area.left) {
						TransactionSummary transactionSummary = new TransactionSummary(
								transaction.id, transaction.date,
								transaction.amount, transaction.title,
								transaction.mapped);

						transactionSummaries.add(transactionSummary);
					}
				}
			}
		}

		return transactionSummaries;
	}

	private static Picture addPicture(String id, byte[] bytes,
			String contentType) {
		return PicturesListMock.addPicture(id, bytes, contentType);
	}

	private static Picture findPicture(String id) {
		return PicturesListMock.findPicture(id);
	}

	private static Collection<Heatpoint> findHeatpoints() {

		Collection<Heatpoint> heatpoints = new HashSet<Heatpoint>();
		for (Transaction transaction : TransactionsListMock.transactions
				.values()) {
			try {
				if (transaction.mapped) {
					// parse the amount to a float

					Heatpoint heatpoint = new Heatpoint(transaction.latitude,
							transaction.longitude, (int) Float.valueOf(
									transaction.amount).floatValue());

					heatpoints.add(heatpoint);
				}
			} catch (NumberFormatException e) {
				Logger.error("Error on the format of the amout: "
						+ transaction.amount, e);
			}
		}

		return heatpoints;
	}
}