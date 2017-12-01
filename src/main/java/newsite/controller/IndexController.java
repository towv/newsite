
package newsite.controller;

import newsite.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    @Autowired
    private CategoryRepository categoryReposity;
    
//    @GetMapping("/")
//    public String index(Model model) {
//        model.addAttribute("categories", categoryReposity.findAll());
//        return "index";
//    }
}
