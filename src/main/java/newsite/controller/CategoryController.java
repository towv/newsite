package newsite.controller;

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

@Transactional
@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NewsRepository newsRepository;

//    @GetMapping("/categories")
//    public String list(Model model) {
//        model.addAttribute("categories", categoryRepository.findAll());
//        return "moderate";
//    }
    @PostMapping("/categories")
    public String add(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return "redirect:/moderate";
    }

    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable Long id) {
        Category category = categoryRepository.getOne(id);

        for (News anew : category.getNews()) {
            anew.getCategories().remove(category);
            newsRepository.save(anew);
        }

        categoryRepository.delete(category);
        return "redirect:/moderate";
    }

    @GetMapping("/categories/{id}/modify")
    public String modify(Model model, @PathVariable Long id) {
        model.addAttribute("category", categoryRepository.getOne(id));
        return "modifyCategory";
    }

    @PostMapping("/moderator/categories/{id}")
    public String postModify(@PathVariable Long id, @RequestParam String name) {
        Category category = categoryRepository.getOne(id);
        category.setName(name);
        categoryRepository.save(category);
        return "redirect:/moderate";
    }
}
