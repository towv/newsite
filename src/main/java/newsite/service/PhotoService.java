package newsite.service;

import java.io.IOException;
import newsite.domain.News;
import newsite.domain.Photo;
import newsite.repository.NewsRepository;
import newsite.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Photo service.
 * Services.
 * @author twviiala
 */
@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;
    
    @Autowired
    private NewsRepository newsRepository;
    
    /**
     * Returns the photo that is linked to id news.
     * @param id a
     * @return a
     */
    public byte[] getPhoto(Long id) {
        return photoRepository.findByNews(newsRepository.getOne(id)).getContent();
    }

    /**
     * Change the photo related to a News article.
     * @param photo a
     * @param anew a
     * @throws IOException a
     */
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
    
    /**
     * Creates a photo for a news article.
     * @param photo a
     * @param news a
     * @throws IOException a
     */
    public void createPhoto(MultipartFile photo, News news) throws IOException  {
        if (photo.getContentType().equals("image/jpeg")) {
            Photo p = new Photo();
            p.setName(photo.getOriginalFilename());
            p.setContentLength(photo.getSize());
            p.setContent(photo.getBytes());
            p.setContentType(photo.getContentType());
            p.setNews(news);
            photoRepository.save(p);
        }
    }
}
