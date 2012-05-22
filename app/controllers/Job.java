package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mock.AxaTransaction;
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

	
	public static Result getAxaTransactionsAPI(String customerId, String accountId, String nbOp ) {
		return getAxaTransactions( customerId,  accountId, new Integer (nbOp) ); 
	}
	
	public static Result getAxaTransactions(String customerId, String accountId, Integer nbOp ) {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -60);
		Date date = cal.getTime();
		
		Integer count = new Integer(50); 
		Integer nbPageMax =  new Integer( nbOp  / count+1);
		
		return getAxaTransactionsByPage (customerId,accountId,1,nbPageMax, date, count);
	}

	public static Result getAxaTransactionsByPage(String customerId, String accountId, Integer page,Integer nbPageMax, Date fromDate, Integer count) {
		//exemple : customerId: "1000000", accountId: 20000001520

		final  String fcustomerId= customerId;
		final  String faccountId = accountId;
		final  Date ffromDate = fromDate;
		final  Integer fpage = page+1;
		final  Integer fnbPageMax = nbPageMax;
		final Integer fcount = count;

		
		
		String language = "en";
		String country = "EN";
		java.util.Locale locale = new java.util.Locale(language,country);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);  
		
		Logger.debug("Intérogation API AXA Banque récupération en cours page: "+page+"/"+ nbPageMax);
		
		//Logger.debug("Intérogation API AXA Banque récupération en cours page: "+page+"/"+ nbPageMax+  " ("+ sdf.format(fromDate)+"/"+sdf.format(new Date())+")");
		
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
 
								//Logger.debug(response.asJson().findPath("transactions").toString());
								//Logger.debug("***** tout : "+response.asJson().toString()+ "****");
								
								//Logger.debug("has_more:"+response.asJson().findValue("has_more").toString());
								if (response.asJson().findValue("has_more").toString()=="true" && fpage<=fnbPageMax)
								{
								  
									//Logger.debug("appel récursif : (fcustomerId:"+fcustomerId+", faccountId:"+faccountId+", fpage:"+fpage+",fnbPageMax:"+fnbPageMax+", ffromDate:"+ffromDate+", fcount:"+fcount+")");
									getAxaTransactionsByPage(fcustomerId, faccountId, fpage,fnbPageMax, ffromDate, fcount);
 
								}

								for (int i = 0; i < response.asJson().findPath("transactions").size(); i++) { 
									//Logger.debug("import from API AXABANQUE:"+    response.asJson().findPath("transactions").get(i).toString() );
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

									String language = "en";
									String country = "EN";
									java.util.Locale locale = new java.util.Locale(language,country);
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);  
									//sdf.setTimeZone(TimeZone.getTimeZone("GMT"));


									TransactionSummary transactionSummary;
									try {
										transactionSummary = new TransactionSummary(stripLeadingAndTrailingQuotes(axaTransaction.id.toString()),
												sdf.parse(stripLeadingAndTrailingQuotes(axaTransaction.date)), new Float(axaTransaction.amount).toString(), stripLeadingAndTrailingQuotes(axaTransaction.label), false);

										Transaction transaction = new Transaction(transactionSummary);

										TransactionsListMock.transactions.put(transactionSummary.id, transaction);


									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}}
								return ok(response.asJson().findPath("transactions"));   //
							}
						}) );
		return TODO;


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
