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
		

addSampleTransaction("100001", "51.56", "Celio", 43.604184f, 1.445539f);
addSampleTransaction("100002", "45", "filochard", 43.603019f,1.436055f);
addSampleTransaction("100003", "25", "otium", 43.601325f,1.441344f);
addSampleTransaction("100004", "100", "restaurant emile", 43.597332f,1.44556f);
addSampleTransaction("100005", "22", "restaurant le 19", 43.597495f,1.445569f);
addSampleTransaction("100006", "45", "restaurant Michel Sarran", 43.597328f,1.445153f);
addSampleTransaction("100007", "76", "resto Jazz", 43.597361f,1.44585f);
addSampleTransaction("100008", "33", "resto abder", 43.597306f,1.444989f);
addSampleTransaction("100009", "12", "bistro saint georges", 43.601885f, 1.447513f);
addSampleTransaction("100010", "9", "le bistro d'eric", 43.601574f, 1.447706f);
addSampleTransaction("100011", "12", "bistro campel", 43.60204f,1.451848f);
addSampleTransaction("100012", "7", "le bistro de julie", 43.601434f,1.451933f);
addSampleTransaction("100013", "8", "bistro de lille", 43.602351f,1.456461f);
addSampleTransaction("100014", "9", "le chevillard", 43.608737f,1.450346f);
addSampleTransaction("100015", "56", "eros & agap", 43.613242f,1.444595f);
addSampleTransaction("100016", "23", "storix", 43.613786f,1.443458f);
addSampleTransaction("100017", "45", "venus 2", 43.618726f,1.432085f);
addSampleTransaction("100018", "67", "mango", 43.603982f,1.443973f);
addSampleTransaction("100019", "89", "grenier d'anais", 43.604138f,1.444831f);
addSampleTransaction("100020", "32", "ethic et chix", 43.604542f,1.44556f);
addSampleTransaction("100021", "32", "carte blanche", 43.603609f,1.444187f);
addSampleTransaction("100022", "4", "corleone", 43.604674f,1.44291f);
addSampleTransaction("100023", "324", "zara ", 43.604969f,1.443619f);
addSampleTransaction("100024", "50", "groucho", 43.604627f,1.443769f);
addSampleTransaction("100125", "1300", "iConcept Toulouse", 43.599833f,1.441076f);
 



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
