package newsite.controller;

import java.io.IOException;
import java.util.List;
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

@Controller
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private WriterRepository writerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/news")
    public String list(Model model) {
        model.addAttribute("news", newsRepository.findAll());
        model.addAttribute("writers", writerRepository.findAll());
        return "news";
    }

    @PostMapping("/news")
    public String post(@RequestParam String header, @RequestParam String lead,
            @RequestParam String text, @RequestParam List<Long> writers, @RequestParam("photo") MultipartFile photo) throws IOException {

        News news = new News();
        news.setHeader(header);
        news.setLead(lead);
        news.setText(text);
        
        for (Long writer : writers) {
            news.getWriters().add(writerRepository.getOne(writer));
            writerRepository.getOne(writer).getNews().add(news);
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

        return "redirect:/news";
    }
}
