package newsite.service;

import java.io.IOException;
import java.util.List;
import newsite.controller.PhotoController;
import newsite.domain.Category;
import newsite.domain.News;
import newsite.domain.Writer;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import newsite.repository.PhotoRepository;
import newsite.repository.ViewRepository;
import newsite.repository.WriterRepository;
import newsite.validors.NewsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

/**
 * Moderator service.
 * Services moderator controller.
 * @author twviiala
 */
@Service
public class ModeratorService {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private WriterRepository writerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private WriterService writerService;
    @Autowired
    private NewsService newsService;

    private NewsValidator newsValidator = new NewsValidator();

    /**
     * Adds necessary items to model.
     * @param model
     */
    public void setModelList(Model model) {
        model.addAttribute("news", newsRepository.findAll());
        model.addAttribute("writers", writerRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
    }

    /**
     * Adds necessary items to model to modify one article.
     * @param model
     * @param id
     */
    public void setModifyModel(Model model, Long id) {
        model.addAttribute("anew", newsRepository.getOne(id));
        model.addAttribute("writers", writerRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
    }

    /**
     * Adds a new News.
     * Otherwise if validators fail returns errors messages.
     * @param header
     * @param lead
     * @param text
     * @param writers
     * @param categories
     * @param photo
     * @return
     * @throws IOException
     */
    public List<String> addNews(String header, String lead, String text, List<Long> writers, List<Long> categories, MultipartFile photo) throws IOException {
        List<String> errors = newsValidator.validateNews(header, lead, text, writers, categories, photo, categoryRepository, writerRepository, photoRepository);
        if (!errors.isEmpty()) {
            return errors;
        }

        News news = new News();
        newsRepository.save(news);
        news.setHeader(header);
        news.setLead(lead);
        news.setText(text);

        addWriterToNews(writers, news);

        addCategoryToNews(categories, news);

        newsRepository.save(news);

        photoService.createPhoto(photo, news);

        return errors;
    }

    /**
     * Modifies News item.
     * @param id
     * @param header
     * @param lead
     * @param photo
     * @param text
     * @param writers
     * @param categories
     * @return
     * @throws IOException
     */
    public List<String> modifyNews(Long id, String header, String lead, MultipartFile photo, String text, List<Long> writers, List<Long> categories) throws IOException {
        List<String> errors = newsValidator.validateEditNews(header, lead, text, writers, categories);
        if (!errors.isEmpty()) {
            return errors;
        }

        News anew = newsRepository.getOne(id);
        anew.setHeader(header);
        anew.setLead(lead);
        anew.setText(text);

        writerService.setWritersToNews(anew, writers);

        categoryService.setCategoriesToNews(anew, categories);

        newsRepository.save(anew);

        photoService.changePhoto(photo, anew);
        return errors;
    }

    /**
     * Deletes a news item.
     * @param id
     * @return
     */
    public String deleteNews(Long id) {
        News anew = newsRepository.getOne(id);

        categoryService.removeNewsFromCategories(anew);
        writerService.removeNewsFromWriters(anew);
        newsService.removeNewsFromViews(anew);

        photoRepository.delete(photoRepository.findByNews(anew));
        newsRepository.delete(anew);
        return anew.getHeader();
    }

    /**
     * Adds a writer or writers to a news item.
     * @param writers
     * @param news
     */
    public void addWriterToNews(List<Long> writers, News news) {
        for (Long longWriter : writers) {
            Writer writer = writerRepository.getOne(longWriter);
            news.getWriters().add(writer);
            writer.getNews().add(news);
            writerRepository.save(writer);
        }
    }

    /**
     * Adds a category or categories to a news item.
     * @param categories
     * @param news
     */
    public void addCategoryToNews(List<Long> categories, News news) {
        for (Long longCategory : categories) {
            Category category = categoryRepository.getOne(longCategory);
            news.getCategories().add(category);
            category.getNews().add(news);
            categoryRepository.save(category);
        }
    }

}
