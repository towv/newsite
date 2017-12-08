package newsite.service;

import javax.transaction.Transactional;
import newsite.repository.ModeratorRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private ModeratorRepository moderatorRepository;

    @Test
    public void testCreateModerator() {
        assertEquals(moderatorRepository.findAll().size(), 0);
        loginService.createModerator();
        assertEquals(moderatorRepository.findAll().size(), 1);
    }

}
