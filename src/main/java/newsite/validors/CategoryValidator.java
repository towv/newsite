package newsite.validors;

import java.util.ArrayList;
import java.util.List;
import newsite.domain.Category;
import newsite.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Category validator.
 * @author twviiala
 */
public class CategoryValidator {

    /**
     * Validates that category is in the expected format.
     * Makes sure there are not too many categories to make a mess of the site.
     * Makes sure that new category is not created if an old one with the same name already exists.
     * Checks the length of the category name.
     * @param name
     * @param categoryRepository
     * @return
     */
    public List<String> validateCategories(String name, CategoryRepository categoryRepository) {
        List<String> errors = new ArrayList();

        List<Category> categories = categoryRepository.findAll();
        if (categories.size() > 7) {
            errors.add("The maximum number of categories is 7, delete a category to create a new one.");
        }
        if (name.trim().isEmpty() || name == null) {
            errors.add("Category name cannot be empty.");
        }
        try {
            if (categoryRepository.findByName(name) != null) {
                errors.add("Category with the given name already exists.");
            }
        } catch (Exception e) {
        }
        if (name.length() > 15) {
            errors.add("Maximum length for category name is 15 characters.");
        }
        return errors;
    }

}
