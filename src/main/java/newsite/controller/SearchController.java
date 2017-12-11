package newsite.controller;

import newsite.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Search Controller.
 *
 * @author twviiala
 */
@Controller
public class SearchController {

    @Autowired
    private NewsService newsService;

    /**
     * Get search page.
     *
     * @param model a
     * @return a
     */
    @GetMapping("/search")
    public String getSearch(Model model) {
        newsService.addFooterHeaderData(model);
        return "search";
    }

    /**
     * Post a search.
     *
     * @param redirectModel a
     * @param searchWord a
     * @return a
     */
    @PostMapping("/search")
    public String postSearch(RedirectAttributes redirectModel, @RequestParam String searchWord) {
        newsService.search(redirectModel, searchWord);
        return "redirect:/search";
    }
}
