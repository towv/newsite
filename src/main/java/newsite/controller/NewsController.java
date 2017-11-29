
package newsite.controller;

import newsite.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NewsController {
    
    @Autowired
    private NewsRepository newsRepository;
    
    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "New news!";
    }
}
