package controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import mock.PicturesListMock;
import mock.TransactionsListMock;
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
		return ok(views.html.map.render());

	}

	/**
	 * Reset the dataset
	 * 
	 * @return
	 */
	public static Result reset() {

		TransactionsListMock.addSampleDataset();

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

	private static Transaction updateTransaction(final Transaction transaction) {
		return TransactionsListMock.transactions.put(transaction.id,
				transaction);
	}

	private static Picture addPicture(String id, byte[] bytes,
			String contentType) {
		return PicturesListMock.addPicture(id, bytes, contentType);
	}

	private static Picture findPicture(String id) {
		return PicturesListMock.findPicture(id);
	}

	private static Collection<Heatpoint> findHeatpoints() {

		Collection<Transaction> mappedTransactions = new HashSet<Transaction>();
		float total = 0;
		for (Transaction transaction : TransactionsListMock.transactions
				.values()) {
			if (transaction.mapped) {
				mappedTransactions.add(transaction);

				try {
					// parse the amount to a float
					total += Float.valueOf(transaction.amount);
				} catch (NumberFormatException e) {
					Logger.error("Error on the format of the amout: "
							+ transaction.amount, e);
				}
			}
		}
		Logger.debug("Total amount is " + total);

		Collection<Heatpoint> heatpoints = new HashSet<Heatpoint>();
		for (Transaction transaction : mappedTransactions) {
			try {
				// parse the amount to a float

				// int percentage = (int) (Float.valueOf(transaction.amount)
				// / total * 100);
				Heatpoint heatpoint = new Heatpoint(transaction.latitude,
						transaction.longitude, (int) Float.valueOf(
								transaction.amount).floatValue());

				heatpoints.add(heatpoint);
			} catch (NumberFormatException e) {
				Logger.error("Error on the format of the amout: "
						+ transaction.amount, e);
			}
		}

		return heatpoints;
	}
}