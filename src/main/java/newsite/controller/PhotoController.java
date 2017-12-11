package newsite.controller;

import org.springframework.transaction.annotation.Transactional;
import newsite.repository.NewsRepository;
import newsite.repository.PhotoRepository;
import newsite.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Photo controller.
 * @author twviiala
 */
@Transactional
@Controller
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    /**
     * Returns the photo related to the id news.
     * @param id a
     * @return a
     */
    @GetMapping(path = "/news/{id}/photo", produces = "image/jpeg")
    @ResponseBody
    public byte[] photo(@PathVariable Long id) {
        return photoService.getPhoto(id);
    }
}
