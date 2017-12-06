
package newsite.controller;

import javax.transaction.Transactional;
import newsite.repository.NewsRepository;
import newsite.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Transactional
@Controller
public class PhotoController {
    
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private NewsRepository newsRepository;
    
    @GetMapping(path = "/news/{id}/photo", produces ="image/jpeg")
    @ResponseBody
    public byte[] photo(@PathVariable Long id) {
       return photoRepository.findByNews(newsRepository.getOne(id)).getContent();
    }
}
