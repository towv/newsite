package newsite.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import newsite.service.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Moderator controller for moderating News.
 *
 * @author twviiala
 */
@Transactional
@Controller
public class ModeratorController {

    @Autowired
    private ModeratorService moderatorService;

    /**
     * Get moderate page.
     *
     * @param model a
     * @param session a
     * @return a
     */
    @GetMapping("/moderate")
    public String list(HttpSession session, Model model) {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        moderatorService.setModelList(model);
        return "moderate";
    }

    /**
     * Add new News.
     *
     * @param redirectModel a
     * @param header a
     * @param lead a
     * @param text a
     * @param writers a
     * @param categories a
     * @param photo a
     * @param session a
     * @return a
     * @throws IOException a
     */
    @PostMapping("/news/moderate")
    public String postAdd(HttpSession session, RedirectAttributes redirectModel, @RequestParam(value = "header", required = false) String header, @RequestParam(value = "lead", required = false) String lead,
            @RequestParam(value = "text", required = false) String text, @RequestParam(value = "writers", required = false) List<Long> writers,
            @RequestParam(value = "categories", required = false) List<Long> categories, @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        List<String> errors = moderatorService.addNews(header, lead, text, writers, categories, photo);
        if (!errors.isEmpty()) {
            redirectModel.addFlashAttribute("messages", errors);
            return "redirect:/moderate";
        }

        List<String> messages = new ArrayList();
        messages.add("New news created! Did you know " + header + ".");
        redirectModel.addFlashAttribute("messages", messages);

        return "redirect:/moderate";
    }

    /**
     * Delete parameter News.
     *
     * @param redirectModel a
     * @param id a
     * @param session a
     * @return a
     */
    @DeleteMapping("/news/{id}")
    public String delete(HttpSession session, RedirectAttributes redirectModel, @PathVariable Long id) {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        String header = moderatorService.deleteNews(id);

        List<String> messages = new ArrayList();
        messages.add("News have been deleted! It seems forgotton that " + header + ".");
        redirectModel.addFlashAttribute("messages", messages);

        return "redirect:/moderate";
    }

    /**
     * Get modifying page, parameter id News.
     *
     * @param model a
     * @param id a
     * @param session a
     * @return a
     */
    @GetMapping("/news/{id}/modify")
    public String modify(HttpSession session, Model model, @PathVariable Long id) {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        moderatorService.setModifyModel(model, id);
        return "modify";
    }

    /**
     * Modify parameter id news with other parameters.
     *
     * @param redirectModel a
     * @param id a
     * @param header a
     * @param lead a
     * @param text a
     * @param writers a
     * @param categories a
     * @param photo a
     * @param session a
     * @return a
     * @throws IOException a
     */
    @PostMapping("/moderator/news/{id}")
    public String postModify(HttpSession session, RedirectAttributes redirectModel, @PathVariable Long id, @RequestParam(value = "header", required = false) String header, @RequestParam(value = "lead", required = false) String lead,
            @RequestParam(value = "text", required = false) String text, @RequestParam(value = "writers", required = false) List<Long> writers,
            @RequestParam(value = "categories", required = false) List<Long> categories, @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        List<String> errors = moderatorService.modifyNews(id, header, lead, photo, text, writers, categories);
        if (!errors.isEmpty()) {
            redirectModel.addFlashAttribute("messages", errors);
            return "redirect:/moderate";
        }

        List<String> messages = new ArrayList();
        messages.add("News have been edited successfully! It turns out that " + header + ".");
        redirectModel.addFlashAttribute("messages", messages);

        return "redirect:/moderate";
    }

}
