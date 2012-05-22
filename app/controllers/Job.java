package controllers;

import java.lang.reflect.Type;
import java.util.Collection;

import mock.AxaTransaction;

 
import org.w3c.dom.Document;

import com.google.gson.Gson;
 
import com.google.gson.reflect.TypeToken;

import play.Logger;
import play.libs.F.Function;
import play.libs.*;
import play.mvc.Controller;
import play.mvc.Result;



public class Job extends Controller {



	public static Result index() {

		//setQueryParameter("customer_id", "1000000")
		/*	Logger.debug("requete API /customers ");
		String feedUrl = "https://sandbox-api.axabanque.fr/customers";
		AsyncResult r1 =  async(WS.url(feedUrl)
				.setQueryParameter("client_id", "2544355445982401905")
				.setQueryParameter("access_token", "3907304021428063542")
				.setQueryParameter("customer_id", "1000000").get()

				.map(new Function<WS.Response, Result>() {
					@Override
					public Result apply(WS.Response response) {
						try {
							// String res = WSUtil.getCityTemperature(response);
							Document doc = response.asXml();
							Logger.debug(doc.toString());
							Logger.debug("****");
							Logger.debug(response.toString());

							String result = "City: "
									+ XPath.selectText("//City", doc)
									+ ", weather: "
									+ XPath.selectText("//Description", doc)
									+ ", temperature: "
									+ XPath.selectText("//Temperature", doc);
							return ok();
						} catch (Exception e) {

							Logger.debug(response.asJson().toString());
							return TODO;
						}
					}
				}

				));
		 */


		AsyncResult r3 = async(
		        WS.url("https://sandbox-api.axabanque.fr/accounts/20000001520/transactions")
		        .setQueryParameter("client_id", "2544355445982401905")
				.setQueryParameter("access_token", "3907304021428063542")
				.setQueryParameter("customer_id", "1000000")
				.setQueryParameter("Count", "100")
		        .get().map(
			    new Function<WS.Response, Result>() {
			        public Result apply(WS.Response response) {
			        	Logger.debug("tio");
			        	Logger.debug(response.asJson().findPath("transactions").toString());
			        	
			        	for (int i = 0; i < response.asJson().findPath("transactions").size(); i++) { 
			        		Logger.debug("héhé:"+    response.asJson().findPath("transactions").get(i).toString() );
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


}
