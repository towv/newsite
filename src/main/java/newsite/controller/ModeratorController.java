package newsite.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import newsite.repository.PhotoRepository;
import newsite.repository.ViewRepository;
import newsite.repository.WriterRepository;
import newsite.service.CategoryService;
import newsite.service.ModeratorService;
import newsite.service.NewsService;
import newsite.service.PhotoService;
import newsite.service.WriterService;
import newsite.validors.NewsValidator;
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
 * @author twviiala
 */
@Transactional
@Controller
public class ModeratorController {

    @Autowired
    private ModeratorService moderatorService;

    /**
     * Get moderate page.
     * @param model
     * @return
     */
    @GetMapping("/moderate")
    public String list(Model model) {
        moderatorService.setModelList(model);
        return "moderate";
    }

    /**
     * Add new News.
     * @param redirectModel
     * @param header
     * @param lead
     * @param text
     * @param writers
     * @param categories
     * @param photo
     * @return
     * @throws IOException
     */
    @PostMapping("/news/moderate")
    public String postAdd(RedirectAttributes redirectModel, @RequestParam(value = "header", required = false) String header, @RequestParam(value = "lead", required = false) String lead,
            @RequestParam(value = "text", required = false) String text, @RequestParam(value = "writers", required = false) List<Long> writers,
            @RequestParam(value = "categories", required = false) List<Long> categories, @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {

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
     * @param redirectModel
     * @param id
     * @return
     */
    @DeleteMapping("/news/{id}")
    public String delete(RedirectAttributes redirectModel, @PathVariable Long id) {
        String header = moderatorService.deleteNews(id);

        List<String> messages = new ArrayList();
        messages.add("News have been deleted! It seems forgotton that " + header + ".");
        redirectModel.addFlashAttribute("messages", messages);

        return "redirect:/moderate";
    }

    /**
     * Get modifying page, parameter id News.
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/news/{id}/modify")
    public String modify(Model model, @PathVariable Long id) {
        moderatorService.setModifyModel(model, id);
        return "modify";
    }

    /**
     * Modify parameter id news with other parameters.
     * @param redirectModel
     * @param id
     * @param header
     * @param lead
     * @param text
     * @param writers
     * @param categories
     * @param photo
     * @return
     * @throws IOException
     */
    @PostMapping("/moderator/news/{id}")
    public String postModify(RedirectAttributes redirectModel, @PathVariable Long id, @RequestParam(value = "header", required = false) String header, @RequestParam(value = "lead", required = false) String lead,
            @RequestParam(value = "text", required = false) String text, @RequestParam(value = "writers", required = false) List<Long> writers,
            @RequestParam(value = "categories", required = false) List<Long> categories, @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
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
