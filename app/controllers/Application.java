package controllers;

import models.MappingInfo;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	public static Result index() {
		return redirect(routes.Application.showMap());
	}

	public static Result showMap() {
		return ok(views.html.map.render());

	}
	
	public static Result allTransactions() {
		return TODO;
	}
	
	public static Result transaction(String id) {
		return TODO;
	}
	
	public static Result transactionMapping() {
		Form<MappingInfo> mappingInfoForm = form(MappingInfo.class);
		
		MappingInfo mappingInfo = mappingInfoForm.bindFromRequest().get();
		
		// TODO search the transaction
		
		// TODO store the mapping
		
		// return the detailed description of the transaction
		return transaction(mappingInfo.transactionId);
	}


}