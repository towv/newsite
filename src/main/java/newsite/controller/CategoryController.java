package newsite.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import newsite.domain.Category;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import newsite.service.CategoryService;
import newsite.validors.CategoryValidator;
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
 * Category Controller.
 *
 * @author twviiala
 */
@Transactional
@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Add new Category.
     *
     * @param redirectModel redirectmodel
     * @param name name
     * @param session a
     * @return model
     */
    @PostMapping("/categories")
    public String add(HttpSession session, RedirectAttributes redirectModel, @RequestParam String name) {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        List<String> errors = categoryService.addCategory(name);
        if (!errors.isEmpty()) {
            redirectModel.addFlashAttribute("messages", errors);
            return "redirect:/moderate";
        }

        List<String> messages = new ArrayList();
        messages.add("There might be news about  " + name + "? hmmm...");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }

    /**
     * Delete parameter Category.
     *
     * @param redirectModel r
     * @param id i
     * @param session a
     * @return r
     */
    @DeleteMapping("/categories/{id}")
    public String delete(HttpSession session, RedirectAttributes redirectModel, @PathVariable Long id) {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        String name = categoryService.deleteCategory(id);

        List<String> messages = new ArrayList();
        messages.add("Category " + name + " has been deleted.");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }

    /**
     * Get modify category page.
     *
     * @param model m
     * @param id i
     * @param session a
     * @return r
     */
    @GetMapping("/categories/{id}/modify")
    public String modify(HttpSession session, Model model, @PathVariable Long id) {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        categoryService.setModifyModel(model, id);
        return "modifyCategory";
    }

    /**
     * Modify id category by changing the name to name.
     *
     * @param redirectModel r
     * @param id i
     * @param name n
     * @param session a
     * @return r
     */
    @PostMapping("/moderator/categories/{id}")
    public String postModify(HttpSession session, RedirectAttributes redirectModel, @PathVariable Long id, @RequestParam String name) {
        if (session.getAttribute("moderator") == null) {
            return "redirect:/";
        }
        List<String> errors = categoryService.modifyCategory(name, id);
        if (!errors.isEmpty()) {
            redirectModel.addFlashAttribute("messages", errors);
            return "redirect:/moderate";
        }

        List<String> messages = new ArrayList();
        messages.add("It seems  " + name + " is a new important news category.");
        redirectModel.addFlashAttribute("messages", messages);

        return "redirect:/moderate";
    }
}
