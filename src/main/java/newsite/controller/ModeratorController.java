package newsite.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import newsite.domain.Category;
import newsite.domain.News;
import newsite.domain.Photo;
import newsite.domain.View;
import newsite.domain.Writer;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import newsite.repository.PhotoRepository;
import newsite.repository.ViewRepository;
import newsite.repository.WriterRepository;
import newsite.validors.NewsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Transactional
@Controller
public class ModeratorController {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private WriterRepository writerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PhotoController photoController;
    @Autowired
    private ViewRepository viewRepository;

    private NewsValidator newsValidator = new NewsValidator();

    @GetMapping("/moderate")
    public String list(Model model) {
        model.addAttribute("news", newsRepository.findAll());
        model.addAttribute("writers", writerRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "moderate";
    }

    @PostMapping("/news/moderate")
    public String postAdd(RedirectAttributes redirectModel, @RequestParam(value = "header", required = false) String header, @RequestParam(value = "lead", required = false) String lead,
            @RequestParam(value = "text", required = false) String text, @RequestParam(value = "writers", required = false) List<Long> writers,
            @RequestParam(value = "categories", required = false) List<Long> categories, @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
        List<String> errors = newsValidator.validateNews(header, lead, text, writers, categories, photo);
        if (!errors.isEmpty()) {
            redirectModel.addFlashAttribute("messages", errors);
            return "moderate";
        }
        News news = new News();
        newsRepository.save(news);
        news.setHeader(header);
        news.setLead(lead);
        news.setText(text);

        for (Long writer : writers) {
            news.getWriters().add(writerRepository.getOne(writer));
            writerRepository.getOne(writer).getNews().add(news);
        }

        for (Long category : categories) {
            news.getCategories().add(categoryRepository.getOne(category));
            categoryRepository.getOne(category).getNews().add(news);
        }

        newsRepository.save(news);

        if (photo.getContentType().equals("image/jpeg")) {
            Photo p = new Photo();
            p.setName(photo.getOriginalFilename());
            p.setContentLength(photo.getSize());
            p.setContent(photo.getBytes());
            p.setContentType(photo.getContentType());
            p.setNews(news);
            photoRepository.save(p);
        }

        List<String> messages = new ArrayList();
        messages.add("New news created! Did you know " + news.getHeader() + ".");
        redirectModel.addFlashAttribute("messages", messages);

        return "redirect:/moderate";
    }

    @DeleteMapping("/news/{id}")
    public String delete(RedirectAttributes redirectModel, @PathVariable Long id) {
        News anew = newsRepository.getOne(id);

        for (Category cy : anew.getCategories()) {
            Category category = categoryRepository.getOne(cy.getId());
            List<News> news = category.getNews();
            news.remove(anew);
            category.setNews(news);
            categoryRepository.save(category);
        }
        for (Writer wr : anew.getWriters()) {
            Writer writer = writerRepository.getOne(wr.getId());
            List<News> news = writer.getNews();
            news.remove(anew);
            writer.setNews(news);
            writerRepository.save(writer);
        }
        
        for (View vw : anew.getViews()) {
            View view = viewRepository.getOne(vw.getId());
            view.setNews(null);
            viewRepository.delete(view);
        }
        photoRepository.delete(photoRepository.findByNews(anew));
        newsRepository.delete(anew);

        List<String> messages = new ArrayList();
        messages.add("News have been deleted! It seems forgotton that " + anew.getHeader() + ".");
        redirectModel.addFlashAttribute("messages", messages);

        return "redirect:/moderate";
    }

    @GetMapping("/news/{id}/modify")
    public String modify(Model model, @PathVariable Long id) {
        model.addAttribute("anew", newsRepository.getOne(id));
        model.addAttribute("writers", writerRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "modify";
    }

    @PostMapping("/moderator/news/{id}")
    public String postModify(RedirectAttributes redirectModel, @PathVariable Long id, @RequestParam(value = "header", required = false) String header, @RequestParam(value = "lead", required = false) String lead,
            @RequestParam(value = "text", required = false) String text, @RequestParam(value = "writers", required = false) List<Long> writers,
            @RequestParam(value = "categories", required = false) List<Long> categories, @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
        List<String> errors = newsValidator.validateEditNews(header, lead, text, writers, categories);
        if (!errors.isEmpty()) {
            redirectModel.addFlashAttribute("messages", errors);
            return "moderate";
        }

        News anew = newsRepository.getOne(id);
        anew.setHeader(header);
        anew.setLead(lead);
        anew.setText(text);

        anew.setWriters(new ArrayList<>());
        for (Long writer : writers) {
            anew.getWriters().add(writerRepository.getOne(writer));
            Writer w = writerRepository.getOne(writer);
            w.getNews().add(anew);
            writerRepository.save(w);
        }
        anew.setCategories(new ArrayList<>());
        for (Long category : categories) {
            Category c = categoryRepository.getOne(category);
            anew.getCategories().add(c);
            c.getNews().add(anew);
            categoryRepository.save(c);

        }
        newsRepository.save(anew);

        photoController.changePhoto(photo, anew);

        List<String> messages = new ArrayList();
        messages.add("News have been edited successfully! It turns out that " + anew.getHeader() + ".");
        redirectModel.addFlashAttribute("messages", messages);

        return "redirect:/moderate";
    }
}
