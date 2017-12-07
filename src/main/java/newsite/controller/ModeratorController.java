package newsite.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.tools.FileObject;
import javax.transaction.Transactional;
import newsite.domain.Category;
import newsite.domain.News;
import newsite.domain.Photo;
import newsite.domain.Writer;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import newsite.repository.PhotoRepository;
import newsite.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
//    @Autowired
//    private ViewRepository viewRepository;

    @GetMapping("/moderate")
    public String list(Model model) {
        model.addAttribute("news", newsRepository.findAll());
        model.addAttribute("writers", writerRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "moderate";
    }

    @PostMapping("/news/moderate")
    public String postAdd(@RequestParam String header, @RequestParam String lead,
            @RequestParam String text, @RequestParam List<Long> writers, @RequestParam List<Long> categories, @RequestParam("photo") MultipartFile photo) throws IOException {

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

        return "redirect:/moderate";
    }
    
    @DeleteMapping("/news/{id}")
    public String delete(@PathVariable Long id) {
        News anew = newsRepository.getOne(id);

        for (Category category : anew.getCategories()) {
            Category c = categoryRepository.getOne(category.getId());
            List<News> n = c.getNews();
            n.remove(anew);
            c.setNews(n);
            categoryRepository.save(c);
        }
        for (Writer writer : anew.getWriters()) {
            Writer w = writerRepository.getOne(writer.getId());
            List<News> n = w.getNews();
            n.remove(anew);
            w.setNews(n);
            writerRepository.save(w);
        }
        photoRepository.delete(photoRepository.findByNews(anew));
        newsRepository.delete(anew);
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
    public String postModify(@PathVariable Long id, @RequestParam String header, 
            @RequestParam String lead, @RequestParam String text, @RequestParam("photo") MultipartFile photo, 
            @RequestParam List<Long> writers, @RequestParam List<Long> categories) throws IOException {
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

        if (photo != null && !photo.isEmpty()) {
            Photo old = photoRepository.findByNews(anew);
            Photo newPhoto = new Photo();

            newPhoto.setName(photo.getOriginalFilename());
            newPhoto.setContent(photo.getBytes());
            newPhoto.setContentLength(photo.getSize());
            newPhoto.setContentType(photo.getContentType());

            newPhoto.setNews(anew);
            photoRepository.save(newPhoto);
            photoRepository.delete(old);
        }

        return "redirect:/moderate";
}
}
