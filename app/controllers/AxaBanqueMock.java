package controllers;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import mock.AxaAccount;
import mock.AxaTransaction;
import mock.TransactionMock;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;




public class AxaBanqueMock extends Controller {

	public static Result allAccounts(String customerId) {

		AxaAccount a= new AxaAccount();
		a.id= new BigInteger("20000001510");
		a.customer= 1000000;
		a.type= "checking";
		a.currency= "EUR";
		a.bic= "AXABFRPP***";
		a.label= "CC";

		AxaAccount  a1= new AxaAccount();
		a1.id= new BigInteger("20000001510");
		a1.customer= 1000000;
		a1.type= "checking";
		a1.currency= "EUR";
		a1.bic= "AXABFRPP***";
		a1.label= "CC";


		List<AxaAccount> c = new ArrayList<AxaAccount>();
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

	public static Result allTransactions(String accountId) {

		String language = "en";
		String country = "EN";
		java.util.Locale locale = new java.util.Locale(language,country);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", locale); // Mon Jan 09 00:00:00 GMT 2012
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));


		AxaTransaction t= new AxaTransaction();
		t.accountId = new BigInteger("20000005100");
		t.id= new BigInteger("2003913");
		t.type= "debit";
		t.date = sdf.format(new Date (112,5,12,4,3));
		t.currency= "EUR";	
		t.amount = 2000;
		t.label= "VIRT DE SALAIRE DECEMBRE 2008";


		List<AxaTransaction> c = new ArrayList<AxaTransaction>();
		c.add(t);

		t= new AxaTransaction();
		t.accountId = new BigInteger("20000005100");
		t.id= new BigInteger("2003913");
		t.type= "debit";
		t.date = sdf.format(new Date (112,5,15,4,2));
		t.currency= "EUR";	
		t.amount = -10;
		t.label= "GUSTO";
		c.add(t);

		return ok(Json.toJson(c));

	}


	public static Result todo() {
		return TODO;

	}


}