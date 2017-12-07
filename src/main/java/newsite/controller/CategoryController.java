package newsite.controller;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import newsite.domain.Category;
import newsite.domain.News;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Transactional
@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NewsRepository newsRepository;
    
    @PostMapping("/categories")
    public String add(RedirectAttributes redirectModel, @RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        
        List<String> messages = new ArrayList();
        messages.add("There might be news about  " + category.getName() + "? hmmm...");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }

    @DeleteMapping("/categories/{id}")
    public String delete(RedirectAttributes redirectModel, @PathVariable Long id) {
        Category category = categoryRepository.getOne(id);

        for (News anew : category.getNews()) {
            anew.getCategories().remove(category);
            newsRepository.save(anew);
        }

        categoryRepository.delete(category);
        
        List<String> messages = new ArrayList();
        messages.add("Category " + category.getName() + " has been deleted.");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }

    @GetMapping("/categories/{id}/modify")
    public String modify(Model model, @PathVariable Long id) {
        model.addAttribute("category", categoryRepository.getOne(id));
        return "modifyCategory";
    }

    @PostMapping("/moderator/categories/{id}")
    public String postModify(RedirectAttributes redirectModel, @PathVariable Long id, @RequestParam String name) {
        Category category = categoryRepository.getOne(id);
        category.setName(name);
        categoryRepository.save(category);
        
        List<String> messages = new ArrayList();
        messages.add("It seems  " + category.getName() + " is a new important news category.");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }
}
