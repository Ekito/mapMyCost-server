package mock;

import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.core.j.JavaResultExtractor;
import play.mvc.Result;

import controllers.AxaBanqueMock;

import models.Transaction;
import models.TransactionSummary;
import play.libs.Json;

public class TransactionsListMock {

	public static Map<String, Transaction> transactions = new HashMap<String, Transaction>();

	static {
		TransactionSummary transactionSummary1 = new TransactionSummary(
				"12345", new Date(), "12.34", "Resto Gusto Café", false);

		Transaction transaction1 = new Transaction(transactionSummary1);

		transactions.put(transactionSummary1.id, transaction1);

		TransactionSummary transactionSummary2 = new TransactionSummary(
				"67890", new Date(), "35.34", "FNAC", true);

		Transaction transaction2 = new Transaction(transactionSummary2,
				43.605412f, 1.448543f, controllers.routes.Application.picture(
						transactionSummary2.id).url());

		transactions.put(transactionSummary2.id, transaction2);

		TransactionSummary transactionSummary3 = new TransactionSummary(
				"13579", new Date(), "23.34", "Carouf", true);

		Transaction transaction3 = new Transaction(transactionSummary3,
				43.615793f, 1.398311f, controllers.routes.Application.picture(
						transactionSummary3.id).url());

		transactions.put(transactionSummary3.id, transaction3);
		
		//Result result = AxaBanqueMock.allTransactions("1000000");
		
		Result result =AxaBanqueMock.allTransactions("1000000");
		byte[] content =  JavaResultExtractor.getBody(result);
		AxaTransaction listT;
		Json.fromJson(Json.parse(new String(content)),Class<AxaTransaction>);
		
		

	}

	public static Transaction findTransaction(String id) {

		return transactions.get(id);
	}

}
