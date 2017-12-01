
package newsite.controller;

import newsite.domain.Writer;
import newsite.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WriterController {
    
    @Autowired
    private WriterRepository writerRepository;
    
//    @GetMapping("/writers")
//    public String list(Model model) {
//        model.addAttribute("writers", writerRepository.findAll());
//        return "moderate";
//    }
    
    @PostMapping("/writers")
    public String add(@RequestParam String name) {
        Writer writer = new Writer();
        writer.setName(name);
        writerRepository.save(writer);
        return "redirect:/moderate";
    }
}
