package newsite.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import newsite.service.LoginService;
import newsite.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Login Controller.
 * @author twviiala
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private NewsService newsService;

    /**
     * Get login page.
     * @return a
     */
    @GetMapping("/login")
    public String getLogin(Model model) {

        loginService.createModerator();
        newsService.addFooterHeaderData(model);
        return "login";
    }

    /**
     * Login to be able moderate news.
     * @param redirectAttributes a
     * @param username a
     * @param password a
     * @param session a
     * @return a
     */
    @PostMapping("/login")
    public String login(RedirectAttributes redirectAttributes, @RequestParam String username, @RequestParam String password, HttpSession session) {
        List<String> messages = new ArrayList();
        if (loginService.attemptLogin(username, password, session)) {
            messages.add("Logged in! You can now add and edit news :)");
            redirectAttributes.addFlashAttribute("messages", messages);
            return "redirect:/login";
        }

        messages.add("Wrong username or password!");
        redirectAttributes.addFlashAttribute("messages", messages);

        return "redirect:/login";
    }

    /**
     * Logout.
     * @param redirectAttributes a
     * @param session a
     * @return a
     */
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes, HttpSession session) {
        List<String> messages = new ArrayList();
        session.setAttribute("moderator", null);
        messages.add("Logged out");
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:/login";
    }
}
