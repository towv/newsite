
package newsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Redirects to news.
 * @author twviiala
 */

@Controller
public class DefaultController {
    
    @RequestMapping("*")
    public String byDefault() {
        return "redirect:/";
    }
}
