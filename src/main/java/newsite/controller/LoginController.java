package newsite.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import newsite.domain.Moderator;
import newsite.repository.ModeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private ModeratorRepository moderatorRepository;

    @GetMapping("/login")
    public String getLogin() {
        if (moderatorRepository.findByName("moderator") == null) {
            Moderator moderator = new Moderator();
            moderator.theModerator(moderator);
            moderatorRepository.save(moderator);
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, @RequestParam String username, @RequestParam String password, HttpSession session) {
        List<String> messages = new ArrayList();
        Moderator moderator = moderatorRepository.findByName(username);
        if (moderator != null) {
            if (moderator.getPassword().equals(password)) {
                session.setAttribute("moderator", moderator);
                messages.add("Logged in! You can now add and edit news :)");
                model.addAttribute("messages", messages);
                return "login";
            }
        }
        messages.add("Wrong username or password!");
        model.addAttribute("messages", messages);

        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        List<String> messages = new ArrayList();
        session.setAttribute("moderator", null);
        messages.add("Logged out");
        model.addAttribute("messages", messages);
        return "login";
    }
}
