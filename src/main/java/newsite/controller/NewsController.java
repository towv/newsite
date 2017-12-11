package newsite.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.transaction.annotation.Transactional;
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
import newsite.service.NewsService;

/**
 * News Controller for showing news pages.
 * @author twviiala
 */
@Transactional
@Controller
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsService newsService;

    /**
     * Return index page.
     * @param model a
     * @return a
     */
    @GetMapping("/")
    public String index(Model model) {
        newsService.setModelIndex(model);
        return "index";
    }

    /**
     * Return a single news in detail.
     * @param model a
     * @param id a
     * @return a
     */
    @GetMapping("/news/{id}")
    public String article(Model model, @PathVariable Long id) {
        newsService.setModelArticle(id, model);
        return "article";
    }

    /**
     * List news based on publishing date.
     * @param model a
     * @param title a
     * @param index a
     * @return a
     */
    @GetMapping("/news/{title}/listByDate/{index}")
    public String listByDate(Model model, @PathVariable String title, @PathVariable Integer index) {
        newsService.setModelListByDate(title, index, model);
        return "news";
    }

    /**
     * List news based on views last week.
     * @param model a
     * @param title a
     * @param index a
     * @return a
     */
    @GetMapping("/news/{title}/listByViews/{index}")
    public String listByViews(Model model, @PathVariable String title, @PathVariable Integer index) {
        newsService.setModelListByViews(title, index, model);
        return "news";
    }

}
