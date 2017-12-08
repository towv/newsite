package newsite.controller;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import newsite.domain.Writer;
import newsite.repository.NewsRepository;
import newsite.repository.WriterRepository;
import newsite.service.WriterService;
import newsite.validors.WriterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Writer Controller.
 * @author twviiala
 */
@Transactional
@Controller
public class WriterController {
    
    @Autowired
    private WriterService writerService;
    
    /**
     * Add a new writer.
     * @param redirectModel
     * @param name
     * @return
     */
    @PostMapping("/writers")
    public String add(RedirectAttributes redirectModel, @RequestParam String name) {
        
        List<String> errors = writerService.addWriter(name);
        if (!errors.isEmpty()) {
            redirectModel.addFlashAttribute("messages", errors);
            return "redirect:/moderate";
        }
        
        List<String> messages = new ArrayList();
        messages.add("Has  " + name + " written something interesting?");
        redirectModel.addFlashAttribute("messages", messages);
        
        return "redirect:/moderate";
    }
    
    /**
     * Delete parameter id Writer.
     * @param redirectModel
     * @param id
     * @return
     */
    @DeleteMapping("/writers/{id}")
    public String delete(RedirectAttributes redirectModel, @PathVariable Long id) {
        
        String name = writerService.deleteWriter(id);
        
        List<String> messages = new ArrayList();
        messages.add("It seems  " + name + " has stopped writing. Maybe " + name + " has never wrote anything!");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }
    
    /**
     * Get modifyWriter page.
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/writers/{id}/modify")
    public String modify(Model model, @PathVariable Long id) {
        writerService.setModifyModel(model, id);
        return "modifyWriter";
    }
    
    /**
     * Modify parameter id Writer.
     * @param redirectModel
     * @param id
     * @param name
     * @return
     */
    @PostMapping("/moderator/writers/{id}")
    public String postModify(RedirectAttributes redirectModel, @PathVariable Long id, @RequestParam String name) {
        List<String> errors = writerService.modifyWriter(name, id);
        if (!errors.isEmpty()) {
            redirectModel.addFlashAttribute("messages", errors);
            return "redirect:/moderate";
        }
        
        List<String> messages = new ArrayList();
        messages.add("It seems  " + name + " might be an alias for a writer.");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }
}
