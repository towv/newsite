package newsite.service;

import javax.transaction.Transactional;
import newsite.domain.Category;
import newsite.domain.Writer;
import newsite.repository.WriterRepository;
import newssite.ModelStub;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WriterServiceTest {

    @Autowired
    private WriterService writerService;
    @Autowired
    private WriterRepository writerRepository;

    @Test
    public void testAddWriter() {
        assertEquals(writerRepository.findAll().size(), 0);
        writerService.addWriter("writer");
        assertEquals(writerRepository.findAll().size(), 1);

    }

    @Test
    public void testModify() {
        Writer writer = new Writer();
        writer.setName("Category");
        writerRepository.save(writer);
        assertEquals("Category", writerRepository.getOne(writer.getId()).getName());
        writerService.modifyWriter("CategoryMod", writer.getId());
        assertEquals("CategoryMod", writerRepository.getOne(writer.getId()).getName());
    }

    @Test
    public void testDeleteCategory() {
        Writer category = new Writer();
        category.setName("Category");
        writerRepository.save(category);
        assertEquals(1, writerRepository.findAll().size());
        writerService.deleteWriter(writerRepository.findByName("Category").getId());
        assertEquals(0, writerRepository.findAll().size());
    }

    @Test
    public void testSetModifyModel() {
        ModelStub model = new ModelStub();
        writerService.setModifyModel(model, 1L);
        assertTrue(model.containsAttribute("writer"));
    }

}
