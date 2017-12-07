
package newsite.controller;

import java.io.IOException;
import javax.transaction.Transactional;
import newsite.domain.News;
import newsite.domain.Photo;
import newsite.repository.NewsRepository;
import newsite.repository.PhotoRepository;
import newsite.validors.NewsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
    
    public void changePhoto(MultipartFile photo, News anew) throws IOException {
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
    }
}
