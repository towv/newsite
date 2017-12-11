package newsite.validors;

import java.util.ArrayList;
import java.util.List;
import newsite.repository.CategoryRepository;
import newsite.repository.PhotoRepository;
import newsite.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * News validator.
 * @author twviiala
 */
public class NewsValidator {

//    private CategoryRepository categoryRepository;
//
//    private WriterRepository writerRepository;
//
//    private PhotoRepository photoRepository;

    /**
     * Runs all news related validators in one method.
     * 
     * @param header a
     * @param lead a
     * @param text a
     * @param writers a
     * @param categories a
     * @param photo a
     * @param categoryRepository a
     * @param writerRepository a
     * @param photoRepository a 
     * @return a
     */

    public List<String> validateNews(String header, String lead,
            String text, List<Long> writers, List<Long> categories, MultipartFile photo, 
            CategoryRepository categoryRepository, WriterRepository writerRepository, 
            PhotoRepository photoRepository) {
//        this.categoryRepository = categoryRepository;
//        this.writerRepository = writerRepository;
//        this.photoRepository = photoRepository;
        List<String> errors = new ArrayList<>();
        errors = validateHeader(errors, header);
        errors = validateLead(errors, lead);
        errors = validateText(errors, text);
        errors = validatePhoto(errors, photo);
        errors = validateWriters(errors, writers);
        errors = validateCategories(errors, categories);

        return errors;
    }
    
    /**
     * Validates news editing, the difference being no photo validator.
     * You might want to keep the old photo.
     * @param header a
     * @param lead a
     * @param text a
     * @param writers a
     * @param categories a
     * @return a
     */
    public List<String> validateEditNews(String header, String lead,
            String text, List<Long> writers, List<Long> categories) {
        List<String> errors = new ArrayList<>();
        errors = validateHeader(errors, header);
        errors = validateLead(errors, lead);
        errors = validateText(errors, text);
        errors = validateWriters(errors, writers);
        errors = validateCategories(errors, categories);

        return errors;
    }

    /**
     * Validates header.
     * The length of it.
     * @param errors a
     * @param header a
     * @return a
     */
    public List<String> validateHeader(List<String> errors, String header) {
        if (header.length() > 50) {
            errors.add("The header is too long, max 50 characters.");
        }
        if (header.length() < 3) {
            errors.add("The header is too short, minimum 3 characters");
        }
        return errors;
    }

    /**
     * Validates lead.
     * The length of it.
     * @param errors a
     * @param lead a
     * @return a
     */
    public List<String> validateLead(List<String> errors, String lead) {

        if (lead.length() > 50) {
            errors.add("The lead is too long, max 50 characters.");
        }
        if (lead.length() < 3) {
            errors.add("The lead is too short, minimum 3 characters");
        }
        return errors;
    }

    /**
     * Validates text.
     * The length of it.
     * @param errors a
     * @param text a
     * @return a
     */
    public List<String> validateText(List<String> errors, String text) {

        if (text.length() > 255) {
            errors.add("The text is too long, max 255 characters.");
        }
        if (text.length() < 3) {
            errors.add("The text is too short, minimum 3 characters");
        }
        return errors;
    }

    /**
     * Validates photo.
     * All attributes.
     * @param errors a
     * @param photo a
     * @return a
     */
    public List<String> validatePhoto(List<String> errors, MultipartFile photo) {

        if (photo == null) {
            errors.add("You need to add a photo to the article.");
        }
        if (photo.getContentType().toString().equals("jpeg") && photo.getContentType().toString().equals("image/jpg")
                && photo.getContentType().toString().equals("image") && photo.getContentType().toString().equals("image/jpeg")) {
            errors.add("The image is in wrong format");
        }
        if (photo.getSize() > 1048576) {
            errors.add("Photo is too big, maximum size 1 mb");
        }
        if (photo.getOriginalFilename().isEmpty()) {
            errors.add("Photo does not have a name. :(");
        }
        return errors;
    }

    /**
     * Validates categories.
     * You need to have one when creating news.
     * @param errors a
     * @param categories a
     * @return a
     */
    public List<String> validateCategories(List<String> errors, List<Long> categories) {
        try {
            if (categories.isEmpty() || categories == null) {
                errors.add("Select one or more categories for the article.");
            }
        } catch (Exception i) {
            errors.add("Select one or more categories for the article.");
        }
//        try {
//            for (Long category : categories) {
//
//                categoryRepository.getOne(category);
//                errors.add("Select an existing category.");
//            }
//        } catch (Exception e) {
//            errors.add("Select an existing category.");
//        }

        return errors;
    }

    /**
     * Validates writers.
     * News are written by someone.
     * @param errors a
     * @param writers a
     * @return a
     */
    public List<String> validateWriters(List<String> errors, List<Long> writers) {

        try {
            if (writers.isEmpty() || writers == null) {
                errors.add("Select one or more writers for the article.");
            }
        } catch (Exception e) {
            errors.add("Select one or more writers for the article.");
        }

//        try {
//            for (Long writer : writers) {
//
//                writerRepository.getOne(writer);
//                errors.add("Select an existing writer.");
//            }
//        } catch (Exception i) {
//            errors.add("Select an existing writer.");
//        }

        return errors;
    }
}
