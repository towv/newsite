package newsite.selenium;

import org.fluentlenium.adapter.junit.FluentTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class LoginTest extends FluentTest {

    @LocalServerPort
    private Integer port;

    @Test
    public void testLogin() {
        goTo("http://localhost:" + port + "/login");
        assertEquals("Login", window().title());

    }
}
