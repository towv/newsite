
package newsite.controller;

import javax.transaction.Transactional;
import newsite.domain.Writer;
import newsite.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Transactional
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
