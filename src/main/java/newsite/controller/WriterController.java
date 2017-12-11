package newsite.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import newsite.domain.Writer;
import newsite.repository.NewsRepository;
import newsite.repository.WriterRepository;
import newsite.service.WriterService;
import newsite.validors.WriterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Writer Controller.
 *
 * @author twviiala
 */
@Transactional
@Controller
public class WriterController {

    @Autowired
    private WriterService writerService;

    /**
     * Add a new writer.
     *
     * @param redirectModel a
     * @param name a
     * @param session a
     * @return a
     */
    @PostMapping("/writers")
    public String add(HttpSession session, RedirectAttributes redirectModel, @RequestParam String name) {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        List<String> errors = writerService.addWriter(name);
        if (!errors.isEmpty()) {
            redirectModel.addFlashAttribute("messages", errors);
            return "redirect:/moderate";
        }

        List<String> messages = new ArrayList();
        messages.add("Has  " + name + " written something interesting?");
        redirectModel.addFlashAttribute("messages", messages);

        return "redirect:/moderate";
    }

    /**
     * Delete parameter id Writer.
     *
     * @param redirectModel a
     * @param id a
     * @param session a
     * @return a
     */
    @DeleteMapping("/writers/{id}")
    public String delete(HttpSession session, RedirectAttributes redirectModel, @PathVariable Long id) {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        String name = writerService.deleteWriter(id);

        List<String> messages = new ArrayList();
        messages.add("It seems  " + name + " has stopped writing. Maybe " + name + " has never wrote anything!");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }

    /**
     * Get modifyWriter page.
     *
     * @param model a
     * @param id a
     * @param session a
     * @return a
     */
    @GetMapping("/writers/{id}/modify")
    public String modify(HttpSession session, Model model, @PathVariable Long id) {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        writerService.setModifyModel(model, id);
        return "modifyWriter";
    }

    /**
     * Modify parameter id Writer.
     *
     * @param redirectModel a
     * @param id writer
     * @param name a
     * @param session a
     * @return a
     */
    @PostMapping("/moderator/writers/{id}")
    public String postModify(HttpSession session, RedirectAttributes redirectModel, @PathVariable Long id, @RequestParam String name) {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        List<String> errors = writerService.modifyWriter(name, id);
        if (!errors.isEmpty()) {
            redirectModel.addFlashAttribute("messages", errors);
            return "redirect:/moderate";
        }

        List<String> messages = new ArrayList();
        messages.add("It seems  " + name + " might be an alias for a writer.");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }
}
