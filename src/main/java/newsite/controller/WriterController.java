package newsite.controller;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import newsite.domain.News;
import newsite.domain.Writer;
import newsite.repository.NewsRepository;
import newsite.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Transactional
@Controller
public class WriterController {

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private NewsRepository newsRepository;

//    @GetMapping("/writers")
//    public String list(Model model) {
//        model.addAttribute("writers", writerRepository.findAll());
//        return "moderate";
//    }
    @PostMapping("/writers")
    public String add(RedirectAttributes redirectModel, @RequestParam String name) {
        Writer writer = new Writer();
        writer.setName(name);
        writerRepository.save(writer);
        List<String> messages = new ArrayList();
        messages.add("Has  " + writer.getName() + " written something interesting?");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }

    @DeleteMapping("/writers/{id}")
    public String delete(RedirectAttributes redirectModel, @PathVariable Long id) {
        Writer writer = writerRepository.getOne(id);

        for (News anew : writer.getNews()) {
            anew.getWriters().remove(writer);
            newsRepository.save(anew);
        }

        writerRepository.delete(writer);

        List<String> messages = new ArrayList();
        messages.add("It seems  " + writer.getName() + " has stopped writing. Maybe " + writer.getName() + " has never wrote anything!");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }

    @GetMapping("/writers/{id}/modify")
    public String modify(Model model, @PathVariable Long id) {
        model.addAttribute("writer", writerRepository.getOne(id));
        return "modifyWriter";
    }

    @PostMapping("/moderator/writers/{id}")
    public String postModify(RedirectAttributes redirectModel, @PathVariable Long id, @RequestParam String name) {
        Writer writer = writerRepository.getOne(id);
        writer.setName(name);
        writerRepository.save(writer);

        List<String> messages = new ArrayList();
        messages.add("It seems  " + writer.getName() + " might be an alias for a writer.");
        redirectModel.addFlashAttribute("messages", messages);
        return "redirect:/moderate";
    }
}
