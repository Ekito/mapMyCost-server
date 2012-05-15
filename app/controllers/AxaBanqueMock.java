package controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import mock.Account;
import mock.TransactionMock;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;




public class AxaBanqueMock extends Controller {

	public static Result allAccounts() {

		Account a= new Account();
		a.id= new BigInteger("20000001510");
		a.customer= 1000000;
		a.type= "checking";
		a.currency= "EUR";
		a.bic= "AXABFRPP***";
		a.label= "CC";
		
		Account  a1= new Account();
		a1.id= new BigInteger("20000001510");
		a1.customer= 1000000;
		a1.type= "checking";
		a1.currency= "EUR";
		a1.bic= "AXABFRPP***";
		a1.label= "CC";
		
		
		List<Account> c = new ArrayList<Account>();
		c.add(a);
		c.add(a1);
		
		return ok(Json.toJson(c));
	}

	public static Result transaction(String id) {

		// find the transaction 
		TransactionMock tm = new TransactionMock();
		tm.label = id;

		if (tm != null) {
			return ok(Json.toJson(tm));
		}

		return notFound();
	}
	
	
	
	
}