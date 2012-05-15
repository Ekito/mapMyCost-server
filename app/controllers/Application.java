package controllers;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import mock.TransactionsListMock;
import models.MappingInfo;
import models.Transaction;
import models.TransactionSummary;
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
		Transaction transaction = TransactionsListMock.findTransaction(id);

		if (transaction != null) {
			return ok(Json.toJson(transaction));
		}

		return notFound();
	}

	public static Result transactionMapping() {
		Form<MappingInfo> mappingInfoForm = form(MappingInfo.class);

		MappingInfo mappingInfo = mappingInfoForm.bindFromRequest().get();

		// retrieve the file from the request
		FilePart picture = request().body().asMultipartFormData()
				.getFile("picture");

		if (picture != null) {
			File file = picture.getFile();
		}

		// TODO search the transaction

		// TODO store the mapping

		// return the detailed description of the transaction
		return transaction(mappingInfo.id);
	}

}