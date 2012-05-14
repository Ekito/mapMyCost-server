package controllers;

import mock.TransactionsListMock;
import models.MappingInfo;
import models.Transaction;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	public static Result index() {
		return redirect(routes.Application.showMap());
	}

	public static Result showMap() {
		return ok(views.html.map.render());

	}

	public static Result allTransactions() {

		return ok(Json.toJson(TransactionsListMock.transactions.values()));
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

		// TODO search the transaction

		// TODO store the mapping

		// return the detailed description of the transaction
		return transaction(mappingInfo.transactionId);
	}

}