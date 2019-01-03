package smoke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;
import static org.awaitility.Awaitility.await;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassCustomerEventTest.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ClassCustomerEventTest {

    @Value("${stubrunner.url:localhost:8765}") String stubRunnerUrl;
    @Value("${application.url:localhost:8084}") String applicationUrl;

    private RestTemplate restTemplate = new RestTemplate();

    private static final Log log = LogFactory.getLog(MethodHandles.lookup().lookupClass());

    @Test
    public void should_calculate_client_total_by_classrooom_and_set_class_by_client() {

        String stubRunnerUrl = "http://" + this.stubRunnerUrl + "/triggers/CustomerRegistered";
        String applicationUrl = "http://" + this.applicationUrl + "/smokeverifier";

        log.info("Url stub runner boot: " + stubRunnerUrl);
        log.info("Url application: " + applicationUrl);

        this.clean(applicationUrl);

        ResponseEntity<Map> response = this.restTemplate.postForEntity(stubRunnerUrl, "", Map.class);
        then(response.getStatusCode().is2xxSuccessful()).isTrue();

        await().until(() -> countClasses(applicationUrl) == 1);
    }

    private void clean(String applicationUrl) {
        this.restTemplate.delete(applicationUrl);
    }

    private int countClasses(String applicationUrl) {
        return this.restTemplate.getForObject(applicationUrl, Integer.class);
    }
}
