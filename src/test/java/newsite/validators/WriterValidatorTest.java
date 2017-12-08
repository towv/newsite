
package newsite.validators;

import java.util.ArrayList;
import java.util.List;
import newsite.repository.WriterRepository;
import newsite.validors.WriterValidator;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WriterValidatorTest {
    
    private WriterValidator writerValidator;
    private WriterRepository writerRepository;

    @Before
    public void setUp() {
    }
    
    @Test
    public void testCreateWriterValidator() {
        writerValidator = new WriterValidator();
    }
    
//    @Test
//    public void testValidateWriter() {
//        List<String> errors = new ArrayList();
//
//        errors = writerValidator.validateWriter("", writerRepository);
//        assertEquals(errors.get(0), "Writer name cannot be empty.");
//    }
    

}
