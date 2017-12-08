
package newsite.validors;

import java.util.ArrayList;
import java.util.List;
import newsite.domain.Writer;
import newsite.repository.WriterRepository;

/**
 *
 * @author twviiala
 */
public class WriterValidator {
    
    private WriterRepository writerRepository;
    private List<Writer> writers;
    
    /**
     * Writer validator.
     * Makes sure there are not too many writers. There can be.
     * Makes sure that new writer is not created if an old one with the same name already exists.
     * Checks the length of the writer name.
     * @param name
     * @param writerRepository
     * @return
     */
    public List<String> validateWriter(String name, WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
        List<String> errors = new ArrayList();

        List<Writer> writers = writerRepository.findAll();
        if (writers.size() > 7) {
            errors.add("The maximum number of writers is 20, delete a writer to create a new one.");
        }
        if (name.trim().isEmpty() || name == null) {
            errors.add("Writer name cannot be empty.");
        }
        try {
            if (writerRepository.findByName(name) != null) {
                errors.add("Writer with the given name already exists.");
            }
        } catch (Exception e) {
        }
        if (name.length() > 15) {
            errors.add("Maximum length for writer name is 30 characters.");
        }
        return errors;
    }
    
}
