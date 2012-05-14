import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import play.mvc.Result;
import play.test.FakeRequest;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;
import static play.test.Helpers.OK;

import static org.fest.assertions.Assertions.assertThat;

public class ApplicationControllersTest {
	
	@Test
	public void testTransactionMapping() throws Exception {
		
		Map<String,String> data = new HashMap<String, String>();
		data.put("transactionId", "12345");
		data.put("latitude", "43.604652");
		data.put("longitude", "1.444209");
		
		FakeRequest fakeRequest = new FakeRequest();
		fakeRequest.withFormUrlEncodedBody(data);
		
		Result result = callAction(
			      controllers.routes.ref.Application.transactionMapping()
			    );
		
		// TODO assertThat(status(result)).isEqualTo(OK);
	}
	
	@Test
	public void testAllTransactions() throws Exception {
		Result result = callAction(
			      controllers.routes.ref.Application.allTransactions()
			    );
		
		assertThat(status(result)).isEqualTo(OK);
	}

}
