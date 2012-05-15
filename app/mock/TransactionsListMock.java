package mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.Transaction;
import models.TransactionSummary;

public class TransactionsListMock {

	public static Map<String, Transaction> transactions = new HashMap<String, Transaction>();

	static {

		addSampleTransaction("12345", "12.34", "Resto Gusto Café");

		addSampleTransaction("67890", "35.34", "FNAC", 43.605412f, 1.448543f);

		addSampleTransaction("13579", "23.34", "Carouf", 43.615793f, 1.398311f);

		addSampleTransaction("24680", "82.35", "Galeries Lafayette");

		addSampleTransaction("12457", "182.35", "Castorama");

		addSampleTransaction("98765", "15.99", "Midica", 43.600257f, 1.444783f);

		addSampleTransaction("11111", "31.78", "Mac Do", 43.605221f, 1.448312f);

		addSampleTransaction("22222", "51.56", "Celio", 43.604184f, 1.445539f);

		addSampleTransaction("33333", "26.90", "Celio Sport", 43.601543f,
				1.443715f);

		addSampleTransaction("44444", "69.55", "Monoprix", 43.603407f,
				1.445475f);

		addSampleTransaction("55555", "19.90", "Castela", 43.604899f, 1.442921f);

		addSampleTransaction("66666", "39.90", "Pharmacie", 43.60681f,
				1.442299f);

		addSampleTransaction("77777", "49.50", "Cordonier", 43.606701f,
				1.444756f);

		addSampleTransaction("88888", "35.90", "Virgin Megastore", 43.604621f,
				1.445687f);

		addSampleTransaction("99999", "58.90", "Pharmacie St Cyp", 43.598202f,
				1.431398f);

	}

	private static void addSampleTransaction(String id, String amount,
			String title) {
		TransactionSummary transactionSummary = new TransactionSummary(id,
				new Date(), amount, title, false);

		Transaction transaction = new Transaction(transactionSummary);

		transactions.put(transactionSummary.id, transaction);
	}

	private static void addSampleTransaction(String id, String amount,
			String title, float lat, float lon) {
		TransactionSummary transactionSummary = new TransactionSummary(id,
				new Date(), amount, title, true);

		Transaction transaction = new Transaction(transactionSummary, lat, lon,
				controllers.routes.Application.picture(transactionSummary.id)
						.url());

		transactions.put(transactionSummary.id, transaction);
	}

	public static Transaction findTransaction(String id) {

		return transactions.get(id);
	}

}
