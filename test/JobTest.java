import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;

import java.lang.reflect.Type;
import java.util.Collection;

import mock.AxaTransaction;

import org.junit.Test;

import play.Logger;
import play.core.j.JavaResultExtractor;
import play.mvc.Result;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class JobTest {
	
	@Test
	public void testJobGetAllTransactions() throws Exception {
		
		Result result = callAction(controllers.routes.ref.AxaBanqueMock  
				.allAccounts("1000000"));
		assertThat(status(result)).isEqualTo(OK);
		
		result = callAction(controllers.routes.ref.AxaBanqueMock  
				.allTransactions("20000001510"));
		assertThat(status(result)).isEqualTo(OK);
		
		
		 
		 byte[] content =  JavaResultExtractor.getBody(result);
		
	    Gson gson = new Gson();
	    AxaTransaction f;
	    
	   
	  Type collectionType = new TypeToken<Collection<AxaTransaction>>(){}.getType();
	  
	 Logger.debug(new String(content));
	  
	 Collection<AxaTransaction> allTransactions = gson.fromJson(new String(content), collectionType);
	 
	 
	 
		
		
		
}
}  