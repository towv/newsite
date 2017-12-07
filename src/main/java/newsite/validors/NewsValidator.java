package newsite.validors;

import java.util.ArrayList;
import java.util.List;
import newsite.repository.CategoryRepository;
import newsite.repository.PhotoRepository;
import newsite.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class NewsValidator {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private PhotoRepository photoRepository;

    public List<String> validateNews(String header, String lead,
            String text, List<Long> writers, List<Long> categories, MultipartFile photo) {
        List<String> errors = new ArrayList<>();
        errors = validateHeader(errors, header);
        errors = validateLead(errors, lead);
        errors = validateText(errors, text);
        errors = validateImage(errors, photo);
        errors = validateWriters(errors, writers);
        errors = validateCategories(errors, categories);

        return errors;
    }
    
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

    public List<String> validateHeader(List<String> errors, String header) {
        if (header.length() > 50) {
            errors.add("The header is too long, max 60 characters.");
        }
        if (header.length() < 5) {
            errors.add("The header is too short, minimum 5 characters");
        }
        return errors;
    }

    public List<String> validateLead(List<String> errors, String lead) {

        if (lead.length() > 50) {
            errors.add("The lead is too long, max 60 characters.");
        }
        if (lead.length() < 5) {
            errors.add("The lead is too short, minimum 5 characters");
        }
        return errors;
    }

    public List<String> validateText(List<String> errors, String text) {

        if (text.length() > 255) {
            errors.add("The text is too long, max 60 characters.");
        }
        if (text.length() < 5) {
            errors.add("The text is too short, minimum 5 characters");
        }
        return errors;
    }

    public List<String> validateImage(List<String> errors, MultipartFile photo) {

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
