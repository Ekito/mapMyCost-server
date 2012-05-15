package controllers;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import mock.PicturesListMock;
import mock.TransactionsListMock;
import models.MappingInfo;
import models.Picture;
import models.Transaction;
import models.TransactionSummary;
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
			File file = picture.getFile();

			// store the file
			// try {
			// byte[] fileAsBytes = FileUtils.readFileToByteArray(file);
			//
			// addPicture(mappingInfo.id, fileAsBytes);
			//
			// } catch (IOException e) {
			// return badRequest(e.getMessage());
			// }
		}

		// store the mapping
		transaction.mapped = true;
		updateTransaction(transaction);

		// return the detailed description of the transaction
		return transaction(mappingInfo.id);
	}

	// ---------- mocked services ------------------ //

	private static Transaction findTransaction(final String id) {
		return TransactionsListMock.findTransaction(id);
	}

	private static Transaction updateTransaction(final Transaction transaction) {
		return TransactionsListMock.transactions.put(transaction.id,
				transaction);
	}

	private static Picture addPicture(String id, byte[] bytes) {
		return PicturesListMock.addPicture(id, bytes);
	}

}