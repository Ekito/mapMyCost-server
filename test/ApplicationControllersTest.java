import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import play.mvc.Result;
import play.test.FakeRequest;

public class ApplicationControllersTest {

	@Test
	public void testTransactionMapping() throws Exception {

		Map<String, String> data = new HashMap<String, String>();
		data.put("id", "12345");
		data.put("latitude", "43.604652");
		data.put("longitude", "1.444209");

		// TODO add the picture to the request in the body

		FakeRequest fakeRequest = new FakeRequest();
		fakeRequest.withFormUrlEncodedBody(data);

		Result result = callAction(controllers.routes.ref.Application
				.transactionMapping());

		// TODO assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testAllTransactions() throws Exception {
		Result result = callAction(controllers.routes.ref.Application
				.allTransactions());

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("application/json");
		assertThat(contentAsString(result)).contains("Gusto");
		assertThat(contentAsString(result)).doesNotContain("summary");
	}

	@Test
	public void testTransactionById() throws Exception {
		Result result = callAction(controllers.routes.ref.Application
				.transaction("12345"));

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("application/json");
		assertThat(contentAsString(result)).contains("Gusto");
		assertThat(contentAsString(result)).doesNotContain("summary");
	}

	@Test
	public void testTransactionByIdnotFound() throws Exception {
		Result result = callAction(controllers.routes.ref.Application
				.transaction("not exists"));

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testHeatpoints() throws Exception {
		Result result = callAction(controllers.routes.ref.Application
				.heatpoints());

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("application/json");
	}

}
