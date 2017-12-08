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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ModeratorServiceTest {

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

    private MultipartFileStub photo;

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

    }

    @Test
    public void testAddNews() throws IOException {
        assertEquals(newsRepository.findAll().size(), 1);

        List<Writer> writers = writerRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        List<Long> writersId = new ArrayList<>();
        List<Long> categoriesId = new ArrayList<>();

        for (Category category : categories) {
            categoriesId.add(category.getId());
        }
        for (Writer writer : writers) {
            writersId.add(writer.getId());
        }
        moderatorService.addNews("header2", "lead2", "text2", writersId, categoriesId, this.photo);
        assertEquals(newsRepository.findAll().size(), 2);

        assertEquals(newsRepository.findByHeader("header2").getLead(), "lead2");
        assertEquals(newsRepository.findByHeader("header2").getText(), "text2");
    }

    @Test
    public void testModifyNews() throws IOException {
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
        assertTrue(newsRepository.findByHeader("NewHeader") != null);
    }

    @Test
    public void testDeleteNews() throws IOException {
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
        assertTrue(newsRepository.findByHeader("NewHeader") != null);

        assertEquals(newsRepository.findAll().size(), 2);
        moderatorService.deleteNews(anew.getId());
        assertEquals(newsRepository.findAll().size(), 1);
    }

    @Test
    public void testAddWriterToNews() {
        Writer writer = new Writer();
        writer.setName("writer2");
        writerRepository.save(writer);

        List<Writer> writers = writerRepository.findAll();
        List<Long> writersId = new ArrayList<>();

        writersId.add(writer.getId());

        News anew = newsRepository.findByHeader("headerheader");
        assertEquals(anew.getWriters().size(), 1);
        moderatorService.addWriterToNews(writersId, anew);
        assertEquals(2, anew.getWriters().size());
    }
    
    @Test
    public void testAddCategoryToNews() {
        Category category = new Category();
        category.setName("category2");
        categoryRepository.save(category);

        List<Long> categoriesId = new ArrayList<>();

        categoriesId.add(category.getId());

        News anew = newsRepository.findByHeader("headerheader");
        assertEquals(anew.getCategories().size(), 1);
        moderatorService.addCategoryToNews(categoriesId, anew);
        assertEquals(2, anew.getCategories().size());
    }
    
    @Test
    public void testSetModelList() {
        ModelStub model = new ModelStub();
        moderatorService.setModelList(model);
        assertTrue(model.containsAttribute("news"));
        assertTrue(model.containsAttribute("writers"));
        assertTrue(model.containsAttribute("categories"));
    }
    
    @Test
    public void testSetModifyModel() {
        ModelStub model = new ModelStub();
        moderatorService.setModifyModel(model, 1L);
        assertTrue(model.containsAttribute("anew"));
        assertTrue(model.containsAttribute("writers"));
        assertTrue(model.containsAttribute("categories"));
    }

}
