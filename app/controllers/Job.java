package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mock.AxaTransaction;
import mock.Merchant;
import mock.TransactionsListMock;
import models.Transaction;
import models.TransactionSummary;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.libs.F.Function;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;



public class Job extends Controller {


	public static Result getAxaTransactionsAPIwithGeo (String customerId, String accountId, String nbOp ) {
		return getAxaTransactions( customerId,  accountId, new Integer (nbOp),true ); 
	}

	public static Result getAxaTransactionsAPIwithoutGeo (String customerId, String accountId, String nbOp ) {
		return getAxaTransactions( customerId,  accountId, new Integer (nbOp), false ); 
	}

	public static Result getAxaTransactions(String customerId, String accountId, Integer nbOp, boolean withGeo ) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -60);
		Date date = cal.getTime();

		Integer count = new Integer(50); 
		Integer nbPageMax =  new Integer( nbOp  / count+1);

		return getAxaTransactionsByPage (customerId,accountId,1,nbPageMax, date, count, withGeo);
	}

	public static Result getAxaTransactionsByPage(String customerId, String accountId, Integer page,Integer nbPageMax, Date fromDate, Integer count, boolean withGeo) {
		//exemple : customerId: "1000000", accountId: 20000001520

		final  String fcustomerId= customerId;
		final  String faccountId = accountId;
		final  Date ffromDate = fromDate;
		final  Integer fpage = page+1;
		final  Integer fnbPageMax = nbPageMax;
		final Integer fcount = count;
		final boolean fwithGeo = withGeo;


		String language = "en";
		String country = "EN";
		java.util.Locale locale = new java.util.Locale(language,country);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);  

		Logger.debug("Intérogation API AXA Banque récupération en cours page: "+page+"/"+ nbPageMax);

		AsyncResult r3 = async(
				WS.url("https://sandbox-api.axabanque.fr/accounts/"+accountId+ "/transactions")
				.setQueryParameter("client_id", "2544355445982401905")
				.setQueryParameter("access_token", "3907304021428063542")
				.setQueryParameter("customer_id", customerId)
				.setQueryParameter("count", count.toString())
				//.setQueryParameter("from_date", sdf.format(fromDate))
				//.setQueryParameter("to_date", sdf.format(new Date()))
				.setQueryParameter("page", page.toString())
				.get().map(
						new Function<WS.Response, Result>() {
							public Result apply(WS.Response response) {

								if (response.asJson().findValue("has_more").toString()=="true" && fpage<=fnbPageMax)
								{

									getAxaTransactionsByPage(fcustomerId, faccountId, fpage,fnbPageMax, ffromDate, fcount,fwithGeo);

								}

								for (int i = 0; i < response.asJson().findPath("transactions").size(); i++) { 

									JsonNode currentTransaction =  response.asJson().findPath("transactions").get(i);
									// On initialise TransactionsListMock.axaTransactions.values()

									AxaTransaction axaTransaction = new AxaTransaction();
									axaTransaction.id = currentTransaction.get("id").getBigIntegerValue();
									axaTransaction.accountId = currentTransaction.get("account").getBigIntegerValue();
									axaTransaction.type = currentTransaction.get("type").toString();
									axaTransaction.date = currentTransaction.get("date").toString();
									axaTransaction.currency = currentTransaction.get("currency").toString();
									axaTransaction.label = currentTransaction.get("label").toString();
									axaTransaction.amount = currentTransaction.get("amount").getLongValue();
									TransactionsListMock.axaTransactions.put(axaTransaction.id.toString(), axaTransaction);

									Logger.debug(currentTransaction.toString());
									JsonNode merchantNode = response.asJson().findPath("transactions").get(i).findPath("point_of_sale").get("merchant_registration_number");



									if (merchantNode != null)
									{
										axaTransaction.merchant_registration_number = 	merchantNode.toString();

										if (TransactionsListMock.merchants.get(axaTransaction.merchant_registration_number) == null)
										{
											Merchant merchant = new Merchant();
											merchant.merchant_registration_number = axaTransaction.merchant_registration_number;
											TransactionsListMock.merchants.put(merchant.merchant_registration_number, merchant);

											if (fwithGeo)
											{

												final String fcity = response.asJson().findPath("transactions").get(i).findPath("point_of_sale").findPath("location").get("city").toString();
												final String fpostal_code = response.asJson().findPath("transactions").get(i).findPath("point_of_sale").findPath("location").get("postal_code").toString();
												final String ftransaction = axaTransaction.id.toString();
												//***
												AsyncResult r3 = async(
														WS.url("http://maps.google.com/maps/geo")
														.setQueryParameter("q", stripLeadingAndTrailingQuotes(fcity)+","+stripLeadingAndTrailingQuotes(fpostal_code)+",france")
														.setQueryParameter("output", "json")
														.setQueryParameter("key", "ABQIAAAAOFKf-0uvhFXfdI1HCJYG-hT2yXp_ZAY8_ufC3CFXhHIE1NvwkxRXLC_hBZN6abys3xBh__jp8aNXLQ")
														.get().map(
																new Function<WS.Response, Result>() {
																	public Result apply(WS.Response response) {
																		 
																		Logger.debug("status:"+response.asJson().findPath("Status").get("code").toString());
																		if (new Integer(response.asJson().findPath("Status").get("code").toString())==200)
																		{
																			String coordinates = response.asJson().findPath("Placemark").get(0).findPath("Point").get("coordinates").toString();
																			Logger.debug ("########:"+coordinates);

																			String delims =  new String ("[\\[,\\]]");
																			String[] tokens = coordinates.split(delims);
																			
																			TransactionsListMock.axaTransactions.get(ftransaction).latitude = new Float(tokens[2]).floatValue();
																			TransactionsListMock.axaTransactions.get(ftransaction).longitude = new Float(tokens[1]).floatValue();

																			
																			TransactionsListMock.transactions.get(ftransaction).latitude = new Float(tokens[2]).floatValue();
																			TransactionsListMock.transactions.get(ftransaction).longitude = new Float(tokens[1]).floatValue();
																			TransactionsListMock.transactions.get(ftransaction).mapped = true;

																			//Logger.debug ("$$ coooooord transactions : "+new Float(TransactionsListMock.transactions.get(ftransaction).latitude).toString()+","+new Float(TransactionsListMock.transactions.get(ftransaction).longitude).toString());
																		}
																		else
																			Logger.debug ("######## reverse impossible");

																		return ok();
																	}
																}
																)
														);
											}
										}
									}
									 
									String language = "en";
									String country = "EN";
									java.util.Locale locale = new java.util.Locale(language,country);
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);  

									TransactionSummary transactionSummary;
									try {
										transactionSummary = new TransactionSummary
												(stripLeadingAndTrailingQuotes(axaTransaction.id.toString()),
														sdf.parse(stripLeadingAndTrailingQuotes(axaTransaction.date)), 
														new Float(axaTransaction.amount).toString(), 
														stripLeadingAndTrailingQuotes(axaTransaction.label), 
														false);

										Transaction transaction = new Transaction(transactionSummary);




										TransactionsListMock.transactions.put(transactionSummary.id, transaction);
										Logger.debug("ajout de l'enreg");

									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										Logger.debug(e.toString());
									}}
								return ok(response.asJson().findPath("transactions"));   //
							}
						}) );
		return r3;


	}

	static String stripLeadingAndTrailingQuotes(String str)
	{
		if (str.startsWith("\""))
		{str = str.substring(1, str.length());}
		if (str.endsWith("\""))
		{str = str.substring(0, str.length() - 1);}
		return str;
	}

}
