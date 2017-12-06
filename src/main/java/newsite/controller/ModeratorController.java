package newsite.controller;

import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;
import newsite.domain.News;
import newsite.domain.Photo;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import newsite.repository.PhotoRepository;
import newsite.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
//    private ViewsRepository viewsRepository;

    @GetMapping("/moderate")
    public String list(Model model) {
        model.addAttribute("news", newsRepository.findAll());
        model.addAttribute("writers", writerRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "moderate";
    }

    @PostMapping("/news/moderate")
    public String post(@RequestParam String header, @RequestParam String lead,
            @RequestParam String text, @RequestParam List<Long> writers, @RequestParam List<Long> categories, @RequestParam("photo") MultipartFile photo) throws IOException {

        News news = new News();
        newsRepository.save(news);
        news.setHeader(header);
        news.setLead(lead);
        news.setText(text);
        news.setViews(0);
//        Views views = new Views();
//        views.setTimes(0);
//        views.setNews(news);
//        viewsRepository.save(views);
//        
//        news.setTimesViewed(views);
        
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
}
