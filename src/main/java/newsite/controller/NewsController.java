package newsite.controller;

import java.util.ArrayList;
import javax.transaction.Transactional;
import newsite.domain.Category;
import newsite.domain.News;
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

@Transactional
@Controller
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
//    @Autowired
//    private ViewsRepository viewsRepository;

    @GetMapping("/")
    public String index(Model model) {

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "pDate");
        model.addAttribute("news", newsRepository.findAll(pageable));
        addFooterHeaderData(model);
        

        return "index";
    }

    @GetMapping("/news/{id}")
    public String article(Model model, @PathVariable Long id) {
        News news = newsRepository.getOne(id);
        news.setViews(news.getViews() + 1);
        newsRepository.save(news);

//        Views views = news.getTimesViewed();
//        views.setTimes(views.getTimes() + 1);
//        viewsRepository.save(views);
        model.addAttribute("anew", news);
        
        addFooterHeaderData(model);
        return "article";
    }

    @GetMapping("/categories/{name}")
    public String list(Model model, @PathVariable String name) {
        ArrayList<Category> categories = new ArrayList();
        categories.add(categoryRepository.findByName(name));
        model.addAttribute("news", newsRepository.findByCategories(categories));
        model.addAttribute("title", name);
        
        addFooterHeaderData(model);
        return "news";
    }

    @GetMapping("/news/{title}/listByDate")
    public String listByDate(Model model, @PathVariable String title) {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(categoryRepository.findByName(title));
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "pDate");
        model.addAttribute("news", newsRepository.findByCategories(categories, pageable));
        model.addAttribute("title", title);
        
        addFooterHeaderData(model);
        return "news";
    }

    @GetMapping("/news/{title}/listByViews")
    public String listByViews(Model model, @PathVariable String title) {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(categoryRepository.findByName(title));
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "views");
        model.addAttribute("news", newsRepository.findByCategories(categories, pageable));
        model.addAttribute("title", title);
        
        addFooterHeaderData(model);
        return "news";
    }
    
        private void addFooterHeaderData(Model model) {
        Pageable published = PageRequest.of(0, 5, Sort.Direction.DESC, "pDate");
        model.addAttribute("newsByDate", newsRepository.findAll(published));
        Pageable views = PageRequest.of(0, 5, Sort.Direction.DESC, "views");
        model.addAttribute("newsByViews", newsRepository.findAll(views));
        model.addAttribute("categories", categoryRepository.findAll());
    }
}
