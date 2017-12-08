package newsite.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import newsite.domain.Category;
import newsite.domain.News;
import newsite.domain.Photo;
import newsite.domain.Writer;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import newsite.repository.PhotoRepository;
import newsite.repository.WriterRepository;
import newssite.ModelStub;
import newssite.MultipartFileStub;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PhotoServiceTest {

    @Autowired
    private ModeratorService moderatorService;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private WriterRepository writerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private NewsService newsService;
    @Autowired
    private PhotoService photoService;

    private MultipartFileStub photo;
    private News news;

    @Before
    public void setUp() {
        this.photo = new MultipartFileStub("photo", "photo.jpg", "image/jpg", 2L, new byte[10]);

        Writer writer = new Writer();
        writer.setName("writer");
        writerRepository.save(writer);

        List<Writer> writers = new ArrayList();
        writers.add(writer);

        Category category = new Category();
        category.setName("category");
        categoryRepository.save(category);

        List<Category> categories = new ArrayList();
        categories.add(category);

        News anew = new News();
        anew.setHeader("headerheader");
        anew.setLead("leadlead");
        anew.setText("texttext");

        anew.setCategories(categories);
        anew.setWriters(writers);

        List<News> news = new ArrayList();
        news.add(anew);

        category.setNews(news);
        categoryRepository.save(category);

        writer.setNews(news);
        writerRepository.save(writer);

        newsRepository.save(anew);
        this.news = anew;
    }

    @Test
    public void testChangePhoto() throws IOException {
        assertEquals(newsRepository.findAll().size(), 1);

        List<Writer> writers = writerRepository.findAll();
        assertEquals(newsRepository.findAll().size(), 1);
        List<Category> categories = categoryRepository.findAll();
        assertEquals(newsRepository.findAll().size(), 1);
        List<Long> writerIds = new ArrayList<>();
        List<Long> categoryIds = new ArrayList<>();

        for (Category category : categories) {
            categoryIds.add(category.getId());
        }
        assertEquals(categoryIds.size(), 1);
        for (Writer writer : writers) {
            writerIds.add(writer.getId());
        }
        assertEquals(writerIds.size(), 1);

        moderatorService.addNews("header2", "lead2", "text2", writerIds, categoryIds, this.photo);
        News anew = newsRepository.findByHeader("header2");

        MultipartFileStub photo2 = new MultipartFileStub("photo2", "photo.jpg", "image/jpg", 2L, new byte[10]);
        List<News> news = new ArrayList();
        news.add(anew);

        Photo photo = new Photo();
        photo.setContent(new byte[10]);
        photo.setContentLength(2L);
        photo.setContentType("image/jpg");
        photo.setName("photo");
        photo.setNews(anew);
        photoRepository.save(photo);

        moderatorService.modifyNews(anew.getId(),
                "NewHeader", "NewLead", photo2, "NewText", writerIds, categoryIds);

        assertEquals(photoRepository.findByNews(newsRepository.findByHeader("NewHeader")).getName(), "photo.jpg");

        MultipartFileStub photo22 = new MultipartFileStub("photo2", "photo2.jpg", "image/jpg", 2L, new byte[10]);
        photoService.changePhoto(photo22, anew);
        assertEquals(photoRepository.findByNews(anew).getName(), "photo2.jpg");
    }
    
    @Test
    public void testCreatePhoto() throws IOException {
        assertEquals(newsRepository.findAll().size(), 1);

        List<Writer> writers = writerRepository.findAll();
        assertEquals(newsRepository.findAll().size(), 1);
        List<Category> categories = categoryRepository.findAll();
        assertEquals(newsRepository.findAll().size(), 1);
        List<Long> writerIds = new ArrayList<>();
        List<Long> categoryIds = new ArrayList<>();

        for (Category category : categories) {
            categoryIds.add(category.getId());
        }
        assertEquals(categoryIds.size(), 1);
        for (Writer writer : writers) {
            writerIds.add(writer.getId());
        }
        assertEquals(writerIds.size(), 1);

        moderatorService.addNews("header2", "lead2", "text2", writerIds, categoryIds, this.photo);
        News anew = newsRepository.findByHeader("header2");

        MultipartFileStub photo2 = new MultipartFileStub("photo2", "photo.jpg", "image/jpg", 2L, new byte[10]);
        List<News> news = new ArrayList();
        news.add(anew);

        Photo photo = new Photo();
        photo.setContent(new byte[10]);
        photo.setContentLength(2L);
        photo.setContentType("image/jpg");
        photo.setName("photo");
        photo.setNews(anew);
        photoRepository.save(photo);

        moderatorService.modifyNews(anew.getId(),
                "NewHeader", "NewLead", photo2, "NewText", writerIds, categoryIds);

        assertEquals(photoRepository.findByNews(newsRepository.findByHeader("NewHeader")).getName(), "photo.jpg");

        
        
        MultipartFileStub photo22 = new MultipartFileStub("photo2", "photo2.jpg", "image/jpg", 2L, new byte[10]);
        photoService.createPhoto(photo22, anew);
        assertEquals(photoRepository.findByNews(anew).getName(), "photo.jpg");
        
    }

}
