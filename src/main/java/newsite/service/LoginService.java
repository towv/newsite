package newsite.service;

import java.util.List;
import javax.servlet.http.HttpSession;
import newsite.domain.Moderator;
import newsite.repository.ModeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Session;
import org.springframework.stereotype.Service;

/**
 * Login service.
 * Service login controller.
 * @author twviiala
 */
@Service
public class LoginService {

    @Autowired
    private ModeratorRepository moderatorRepository;

    /**
     * Creates a moderator when login is clicked.
     * Using this account news can be moderated.
     * The creation and editing of News, Categories and Writers is thus restricted.
     */
    public void createModerator() {
        if (moderatorRepository.findByName("moderator") == null) {
            Moderator moderator = new Moderator();
            moderator.theModerator(moderator);
            moderatorRepository.save(moderator);
        }
    }

    /**
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    public boolean attemptLogin(String username, String password, HttpSession session) {
        Moderator moderator = moderatorRepository.findByName(username);
        if (moderator != null) {
            if (moderator.getPassword().equals(password)) {
                session.setAttribute("moderator", moderator);
                return true;
            }
        }
        return false;
    }
}
