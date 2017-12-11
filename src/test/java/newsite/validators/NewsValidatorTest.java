package newsite.validators;

import java.util.ArrayList;
import java.util.List;
import newsite.domain.Category;
import newsite.domain.Photo;
import newsite.domain.Writer;
import newsite.validors.NewsValidator;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class NewsValidatorTest {

    private NewsValidator newsValidator;

    @Before
    public void setUp() {
        newsValidator = new NewsValidator();

        Writer sibelius = new Writer(null, "Jean Sibelius");
        Writer sipser = new Writer(null, "Michael Sipser");

    }

    @Test
    public void testValidateHeader() {
        List<String> errors = new ArrayList<>();

        errors = newsValidator.validateHeader(errors, "");
        assertEquals("The header is too short, minimum 3 characters", errors.get(0));

        errors = newsValidator.validateHeader(errors, "TooLongStringlalalalalalalalalalalalalalalala"
                + "lalalalalalalalalalalalalalaa");
        assertEquals(errors.get(1), "The header is too long, max 50 characters.");
    }

    @Test
    public void testValidateLead() {
        List<String> errors = new ArrayList<>();

        errors = newsValidator.validateLead(errors, "");
        assertEquals(errors.get(0), "The lead is too short, minimum 3 characters");

        errors = newsValidator.validateLead(errors, "TooLongStringlalalalalalalalalalalalalalalalala"
                + "lalalalalalalalalalalalalaa");
        assertEquals(errors.get(1), "The lead is too long, max 50 characters.");
    }

    @Test
    public void testValidateText() {
        List<String> errors = new ArrayList<>();

        errors = newsValidator.validateText(errors, "");
        assertEquals(errors.get(0), "The text is too short, minimum 3 characters");

        errors = newsValidator.validateText(errors, "TooLongStringlalalalalalalalalalalalalalalala"
                + "lalalalalalalalalalalalalalaaTooLongStringlalalalalalalalalalalalalalalalalalala"
                + "lalalalalalalalalalalaaTooLongStringlalalalalalalalalalalalalalalalalalalalalala"
                + "lalalalalalalalaaTooLongStringlalalalalalalalalalalalalalalalalalalalalalalalala"
                + "lalalalalaaTooLongStringlalalalalalalalalalalalalalalalalalalalalalalalalalalalalalaa");
        assertEquals(errors.get(1), "The text is too long, max 255 characters.");
    }

    @Test
    public void testValidateCategories() {
        List<String> errors = new ArrayList<>();
        List<Long> categories = new ArrayList<>();

        errors = newsValidator.validateCategories(errors, categories);
        assertEquals(errors.get(0), "Select one or more categories for the article.");

        categories = null;
        errors = newsValidator.validateCategories(errors, categories);
        assertEquals(errors.get(1), "Select one or more categories for the article.");
    }

    @Test
    public void testValidateWriters() {
        List<String> errors = new ArrayList<>();
        List<Long> writers = new ArrayList<>();

        errors = newsValidator.validateWriters(errors, writers);
        assertEquals(errors.get(0), "Select one or more writers for the article.");

        writers = null;
        errors = newsValidator.validateWriters(errors, writers);
        assertEquals(errors.get(1), "Select one or more writers for the article.");
    }

    @Test
    public void testValidateEditNews() {
        List<String> errors = new ArrayList<>();

        List<Long> categories = new ArrayList<>();
        List<Long> writers = new ArrayList<>();

        errors = newsValidator.validateEditNews("", "", "", writers, categories);
        assertEquals(errors.size(), 5);
    }
}
