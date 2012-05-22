package controllers;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.TimeZone;

import mock.AxaTransaction;
import mock.TransactionsListMock;
import models.Transaction;
import models.TransactionSummary;


import org.codehaus.jackson.JsonNode;
import org.w3c.dom.Document;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;

import play.Logger;
import play.libs.F.Function;
import play.libs.*;
import play.mvc.Controller;
import play.mvc.Result;



public class Job extends Controller {



	public static Result getAxaTransactions(String customerId, String accountId) {
		//exemple : customerId: "1000000", accountId: 20000001520


		AsyncResult r3 = async(
				WS.url("https://sandbox-api.axabanque.fr/accounts/"+accountId+ "/transactions")
				.setQueryParameter("client_id", "2544355445982401905")
				.setQueryParameter("access_token", "3907304021428063542")
				.setQueryParameter("customer_id", customerId)
				.setQueryParameter("Count", "100")
				.get().map(
						new Function<WS.Response, Result>() {
							public Result apply(WS.Response response) {
								Logger.debug("tio");
								Logger.debug(response.asJson().findPath("transactions").toString());

								for (int i = 0; i < response.asJson().findPath("transactions").size(); i++) { 
									Logger.debug("import from API AXABANQUE:"+    response.asJson().findPath("transactions").get(i).toString() );
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
									Logger.debug("axaTransaction.accountId:"+axaTransaction.accountId.toString());
									Logger.debug("axaTransaction.amount:"+new Float(axaTransaction.amount).toString());
									TransactionsListMock.axaTransactions.put(axaTransaction.id.toString(), axaTransaction);

									String language = "en";
									String country = "EN";
									java.util.Locale locale = new java.util.Locale(language,country);
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", locale);  
									//sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

									Logger.debug("$"+stripLeadingAndTrailingQuotes(axaTransaction.date)+"$");


									TransactionSummary transactionSummary;
									try {
										transactionSummary = new TransactionSummary(axaTransaction.id.toString(),
												sdf.parse(stripLeadingAndTrailingQuotes(axaTransaction.date)), new Float(axaTransaction.amount).toString(), axaTransaction.label, false);

										Transaction transaction = new Transaction(transactionSummary);

										TransactionsListMock.transactions.put(transactionSummary.id, transaction);
										
										
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}




								}


								return ok(response.asJson().findPath("transactions"));   //
							}
						}
						)
				);

		/*
		Gson gson = new Gson();

		Logger.debug("Type collectionType = new TypeToken<Collection<AxaTransaction>>(){}.getType();");
		Type collectionType = new TypeToken<Collection<AxaTransaction>>(){}.getType();

		Logger.debug("Collection<AxaTransaction> allTransactions = gson.fromJson(response.asJson().toString(), collectionType);");
		Logger.debug(r3.toString());
		//Collection<AxaTransaction> allTransactions = gson.fromJson(r3.toString(), collectionType);

		//Logger.debug(new Integer(allTransactions.size()).toString());
		 */

		return r3;


	}

	 static String stripLeadingAndTrailingQuotes(String str)
	  {
	      if (str.startsWith("\""))
	      {
	          str = str.substring(1, str.length());
	      }
	      if (str.endsWith("\""))
	      {
	          str = str.substring(0, str.length() - 1);
	      }
	      return str;
	  }

}
