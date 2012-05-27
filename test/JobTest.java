import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

public class JobTest {

    @Test
    public void testJobGetAllTransactions() throws Exception {

        Result result = callAction(controllers.routes.ref.AxaBanqueMock.allAccounts("1000000"));
        assertThat(status(result)).isEqualTo(OK);

        result = callAction(controllers.routes.ref.AxaBanqueMock.allTransactions("20000001510"));
        assertThat(status(result)).isEqualTo(OK);

    }
}