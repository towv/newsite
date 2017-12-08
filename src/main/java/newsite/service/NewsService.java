package newsite.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import newsite.domain.Category;
import newsite.domain.News;
import newsite.domain.View;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import newsite.repository.ViewRepository;
import newsite.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * News service.
 * Services News controller.
 * @author twviiala
 */
@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ViewRepository viewRepository;
    
    /**
     * Sets index model.
     * Also importantly header and footer data to be able to see lists of 
     * News in the footer based on views and publishing date.
     * @param model
     */
    public void setModelIndex(Model model) {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "pDate");
        model.addAttribute("news", newsRepository.findAll(pageable));
        addFooterHeaderData(model);
    }

    /**
     * Sets model article.
     * For viewing a single news article.
     * @param id
     * @param model
     */
    public void setModelArticle(Long id, Model model) {
        News news = newsRepository.getOne(id);
        addViewToNews(news);
        model.addAttribute("anew", news);
        addFooterHeaderData(model);
    }

    /**
     * Set model for list by date.
     * @param title
     * @param index
     * @param model
     */
    public void setModelListByDate(String title, Integer index, Model model) {
        ArrayList<Category> categories = getCategory(title);
        Pageable pageable = PageRequest.of(index - 1, 5, Sort.Direction.DESC, "pDate");
        model.addAttribute("news", newsRepository.findByCategories(categories, pageable));
        model.addAttribute("title", title);
        model.addAttribute("listing", "byDate");
        findListSize(model, title);
        addFooterHeaderData(model);
    }

    /**
     * Set model for list by views.
     * @param title
     * @param index
     * @param model
     */
    public void setModelListByViews(String title, Integer index, Model model) {
        ArrayList<Category> categories = getCategory(title);

        List<News> news = getCategoryRelatedNews(categories);
        setViewsForLastWeek(news);

        addViewsLastWeekToModel(index, model, categories);
        model.addAttribute("title", title);
        model.addAttribute("listing", "byViews");
        findListSize(model, title);

        addFooterHeaderData(model);
    }

    /**
     * Adds to model news based on views from last week.
     * @param index
     * @param model
     * @param categories
     */
    public void addViewsLastWeekToModel(Integer index, Model model, ArrayList<Category> categories) {
        Pageable pageable = PageRequest.of(index - 1, 5, Sort.Direction.DESC, "viewsLastWeek");
        model.addAttribute("news", newsRepository.findByCategories(categories, pageable));
    }

    /**
     * Finds news related to a certain category.
     * @param categories
     * @return
     */
    public List<News> getCategoryRelatedNews(ArrayList<Category> categories) {
        // Setting newsLastWeek for News
        List<News> news = newsRepository.findByCategories(categories);
        return news;
    }

    /**
     * Removes news from views.
     * There is no seperate view service nor controller so this is handled here.
     * @param anew
     */
    public void removeNewsFromViews(News anew) {
        for (View vw : anew.getViews()) {
            View view = viewRepository.getOne(vw.getId());
            view.setNews(null);
            viewRepository.delete(view);
        }
    }

    /**
     * Adds views to news.
     * @param news
     */
    public void addViewToNews(News news) {
        View view = new View();
        view.setNews(news);
        viewRepository.save(view);
        news.getViews().add(view);
        newsRepository.save(news);
    }

    /**
     * Adds to model data necessary for model.
     * @param model
     */
    public void addFooterHeaderData(Model model) {
        Pageable published = PageRequest.of(0, 5, Sort.Direction.DESC, "pDate");
        model.addAttribute("newsByDate", newsRepository.findAll(published));

        List<News> news = newsRepository.findAll();
        setViewsForLastWeek(news);
        Pageable views = PageRequest.of(0, 5, Sort.Direction.DESC, "viewsLastWeek");
        model.addAttribute("newsByViews", newsRepository.findAll(views));

        model.addAttribute("categories", categoryRepository.findAll());
    }

    /**
     * Sets viewsLastWeek for News.
     * This is done in order to sort based on last weeks views.
     * @param news
     */
    public void setViewsForLastWeek(List<News> news) {
        for (News anew : news) {
            anew.setViewsLastWeek(viewRepository.findByNewsAndWhenViewedAfter(anew, new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7))).size());
        }
    }

    /**
     * Finds how many news are in the given category.
     * @param model
     * @param category
     * @return
     */
    public Model findListSize(Model model, String category) {

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(categoryRepository.findByName(category));
        List<News> news = newsRepository.findByCategories(categories);
        model.addAttribute("listSize", news.size());

        return model;
    }

    /**
     * Returns the category with name title.
     * Title is used here instead of name because name would otherwise have been disabled.
     * @param title
     * @return
     */
    public ArrayList<Category> getCategory(String title) {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(categoryRepository.findByName(title));
        return categories;
    }


}
