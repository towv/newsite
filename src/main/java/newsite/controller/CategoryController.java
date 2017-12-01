
package newsite.controller;

import newsite.domain.Category;
import newsite.domain.Writer;
import newsite.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
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
}
