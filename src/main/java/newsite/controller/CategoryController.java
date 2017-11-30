
package newsite.controller;

import newsite.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CategoryController {
    
    @Autowired
    private CategoryRepository categoryRepository;
}
