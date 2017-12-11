package newsite.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import newsite.domain.Moderator;
import newsite.repository.ModeratorRepository;
import newsite.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Login Controller.
 * @author twviiala
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * Get login page.
     * @return a
     */
    @GetMapping("/login")
    public String getLogin() {

        loginService.createModerator();

        return "login";
    }

    /**
     * Login to be able moderate news.
     * @param model a
     * @param username a
     * @param password a
     * @param session a
     * @return a
     */
    @PostMapping("/login")
    public String login(Model model, @RequestParam String username, @RequestParam String password, HttpSession session) {
        List<String> messages = new ArrayList();
        if (loginService.attemptLogin(username, password, session)) {
            messages.add("Logged in! You can now add and edit news :)");
            model.addAttribute("messages", messages);
            return "login";
        }

        messages.add("Wrong username or password!");
        model.addAttribute("messages", messages);

        return "login";
    }

    /**
     * Logout.
     * @param model a
     * @param session a
     * @return a
     */
    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        List<String> messages = new ArrayList();
        session.setAttribute("moderator", null);
        messages.add("Logged out");
        model.addAttribute("messages", messages);
        return "login";
    }
}
