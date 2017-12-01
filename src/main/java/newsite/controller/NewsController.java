package newsite.controller;

import java.util.ArrayList;
import newsite.domain.Category;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String index(Model model) {

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "pDate");
        model.addAttribute("news", newsRepository.findAll(pageable));

        return "index";
    }

    @GetMapping("/news/{id}")
    public String article(Model model, @PathVariable Long id) {
        model.addAttribute("anew", newsRepository.findById(id));
        return "article";
    }

    @GetMapping("/categories/{name}")
    public String list(Model model, @PathVariable String name) {
        ArrayList<Category> categories = new ArrayList();
        categories.add(categoryRepository.findByName(name));
        model.addAttribute("news", newsRepository.findByCategories(categories));
        model.addAttribute("title", name);
        model.addAttribute("categories", categoryRepository.findAll());
        return "news";
    }

//    @GetMapping("/news/{title}/listByViews")
//    public String listByDate(Model model, @PathVariable String title) {
//        
//    }
}
