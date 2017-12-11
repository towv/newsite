package newsite.service;

import java.util.ArrayList;
import java.util.List;
import newsite.domain.Category;
import newsite.domain.News;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import newsite.validors.CategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * Category service.
 * Services category controller.
 * @author twviiala
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private NewsRepository newsRepository;

    private CategoryValidator categoryValidator = new CategoryValidator();
    
    /**
     * Sets the modify model.
     * @param model a
     * @param id a
     */
    public void setModifyModel(Model model, Long id) {
        model.addAttribute("category", categoryRepository.getOne(id));
    }

    /**
     * Adds a new category if validators pass.
     * Otherwise returns error messages.
     * @param name a 
     * @return a
     */
    public List<String> addCategory(String name) {
        List<String> errors = categoryValidator.validateCategories(name, categoryRepository);
        if (!errors.isEmpty()) {
            return errors;
        }

        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);

        return errors;
    }
    
    /**
     * Modifies category.
     * Otherwise returns error messages.
     * @param name a
     * @param id a
     * @return a
     */
    public List<String> modifyCategory(String name, Long id) {
        List<String> errors = categoryValidator.validateCategories(name, categoryRepository);
        if (!errors.isEmpty()) {
            return errors;
        }
        
        Category category = categoryRepository.getOne(id);
        category.setName(name);
        categoryRepository.save(category);
        return errors;
    }

    /**
     * Deletes as category.
     * @param id a 
     * @return a
     */
    public String deleteCategory(Long id) {
        Category category = categoryRepository.getOne(id);
        removeCategoriesFromaNew(category);
        categoryRepository.delete(category);
        return category.getName();
    }

    /**
     * Removes News from categories when the News is deleted.
     * @param anew a
     */
    public void removeNewsFromCategories(News anew) {
        for (Category cy : anew.getCategories()) {
            Category category = categoryRepository.getOne(cy.getId());
            List<News> news = category.getNews();
            news.remove(anew);
            category.setNews(news);
            categoryRepository.save(category);
        }
    }

    /**
     * Adds categories to news and visa versa.
     * @param anew a
     * @param categories a
     */
    public void setCategoriesToNews(News anew, List<Long> categories) {
        anew.setCategories(new ArrayList<>());
        for (Long category : categories) {
            Category c = categoryRepository.getOne(category);
            anew.getCategories().add(c);
            c.getNews().add(anew);
            categoryRepository.save(c);
        }
    }

    /**
     * Removes categories from a single News.
     * @param category a
     */
    public void removeCategoriesFromaNew(Category category) {
        for (News anew : category.getNews()) {
            anew.getCategories().remove(category);
            newsRepository.save(anew);
        }
    }
}
