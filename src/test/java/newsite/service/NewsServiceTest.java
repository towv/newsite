package newsite.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import newsite.domain.Category;
import newsite.domain.News;
import newsite.domain.View;
import newsite.domain.Writer;
import newsite.repository.CategoryRepository;
import newsite.repository.NewsRepository;
import newsite.repository.ViewRepository;
import newsite.repository.WriterRepository;
import newssite.ModelStub;
import newssite.MultipartFileStub;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class NewsServiceTest {

    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ModeratorService moderatorService;
    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private WriterRepository writerRepository;

    private MultipartFileStub photo;
    private ModelStub model;

    @Before
    public void setUp() {
        this.model = new ModelStub();
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
    public void testAddRemoveViewToNews() throws IOException {
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
        News anew = newsRepository.findByHeader("header2");
        assertEquals(0, anew.getViews().size());
        newsService.addViewToNews(anew);
        assertEquals(1, anew.getViews().size());
        View view = anew.getViews().get(0);
        newsService.removeNewsFromViews(newsRepository.findByHeader("header2"));
        assertEquals(null, view.getNews());
    }
    
    @Test
    public void testSetModel() {
        newsService.setModelArticle(newsRepository.findByHeader("headerheader").getId(), this.model);
        assertTrue(this.model.containsAttribute("anew"));
        newsService.setModelIndex(this.model);
        assertTrue(this.model.containsAttribute("news"));
        newsService.addFooterHeaderData(this.model);
        assertTrue(this.model.containsAttribute("categories"));
        assertTrue(this.model.containsAttribute("newsByDate"));
        assertTrue(this.model.containsAttribute("newsByViews"));
        
    }

}