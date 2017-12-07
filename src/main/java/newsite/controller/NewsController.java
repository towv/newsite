package newsite.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.transaction.Transactional;
import newsite.domain.Category;
import newsite.domain.News;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import newsite.repository.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import newsite.domain.View;

@Transactional
@Controller
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ViewRepository viewRepository;

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
//        news.setViews(news.getViews() + 1);
//        newsRepository.save(news);

        View view = new View();
        view.setNews(news);
        viewRepository.save(view);
        news.getViews().add(view);
        newsRepository.save(news);

        model.addAttribute("anew", news);

        addFooterHeaderData(model);
        return "article";
    }

//    @GetMapping("/categories/{name}")
//    public String list(Model model, @PathVariable String name) {
//        ArrayList<Category> categories = new ArrayList();
//        categories.add(categoryRepository.findByName(name));
//        model.addAttribute("news", newsRepository.findByCategories(categories));
//        model.addAttribute("title", name);
//        model.addAttribute("index", 1);
//
//        addFooterHeaderData(model);
//        return "news";
//    }
    
    @GetMapping("/news/{title}/listByDate/{index}")
    public String listByDate(Model model, @PathVariable String title, @PathVariable Integer index) {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(categoryRepository.findByName(title));
        Pageable pageable = PageRequest.of(index - 1, 5, Sort.Direction.DESC, "pDate");
        model.addAttribute("news", newsRepository.findByCategories(categories, pageable));
        model.addAttribute("title", title);
        model.addAttribute("listing", "byDate");
        findListSize(model, title);

        addFooterHeaderData(model);
        return "news";
    }

    @GetMapping("/news/{title}/listByViews/{index}")
    public String listByViews(Model model, @PathVariable String title, @PathVariable Integer index) {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(categoryRepository.findByName(title));

        // Setting newsLastWeek for News
        List<News> news = newsRepository.findByCategories(categories);
        setViewsForLastWeek(news);

        Pageable pageable = PageRequest.of(index - 1, 5, Sort.Direction.DESC, "viewsLastWeek");
        model.addAttribute("news", newsRepository.findByCategories(categories, pageable));
        model.addAttribute("title", title);
        model.addAttribute("listing", "byViews");
        findListSize(model, title);

        addFooterHeaderData(model);
        return "news";
    }

    private void addFooterHeaderData(Model model) {
        Pageable published = PageRequest.of(0, 5, Sort.Direction.DESC, "pDate");
        model.addAttribute("newsByDate", newsRepository.findAll(published));

        List<News> news = newsRepository.findAll();
        setViewsForLastWeek(news);
        Pageable views = PageRequest.of(0, 5, Sort.Direction.DESC, "viewsLastWeek");
        model.addAttribute("newsByViews", newsRepository.findAll(views));

        model.addAttribute("categories", categoryRepository.findAll());
    }

    private void setViewsForLastWeek(List<News> news) {
        for (News anew : news) {
            anew.setViewsLastWeek(viewRepository.findByNewsAndWhenViewedAfter(anew, new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7))).size());
        }
    }

    private Model findListSize(Model model, String category) {

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(categoryRepository.findByName(category));
        model.addAttribute("listSize", categories.size());

        return model;
    }
}
