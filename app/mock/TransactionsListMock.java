package mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.Transaction;
import models.TransactionSummary;

public class TransactionsListMock {

	public static Map<String, Transaction> transactions = new HashMap<String, Transaction>();

	static {
		TransactionSummary transactionSummary1 = new TransactionSummary(
				"12345", new Date(), "12,34", "Resto Gusto Café", false);

		Transaction transaction1 = new Transaction(transactionSummary1,
				43.604652f, 1.444209f);

		transactions.put(transactionSummary1.id, transaction1);

		TransactionSummary transactionSummary2 = new TransactionSummary(
				"67890", new Date(), "35,34", "FNAC", false);

		Transaction transaction2 = new Transaction(transactionSummary2,
				43.605412f, 1.448543f);

		transactions.put(transactionSummary2.id, transaction2);

	}

	public static Transaction findTransaction(String id) {

		return transactions.get(id);
	}

}
