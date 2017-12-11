package newsite.service;

import java.util.ArrayList;
import java.util.List;
import newsite.domain.News;
import newsite.domain.Writer;
import newsite.repository.NewsRepository;
import newsite.repository.WriterRepository;
import newsite.validors.WriterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * Writer service.
 * Writer related services being provided here.
 * @author twviiala
 */
@Service
public class WriterService {

    @Autowired
    private WriterRepository writerRepository;
    @Autowired
    private NewsRepository newsRepository;

    private WriterValidator writerValidator = new WriterValidator();
    
    /**
     * Sets the model for modify writer.
     * @param model a
     * @param id a
     */
    public void setModifyModel(Model model, Long id) {
        model.addAttribute("writer", writerRepository.getOne(id));
    }

    /**
     * Adds a new writer.
     * @param name a
     * @return a
     */
    public List<String> addWriter(String name) {
        List<String> errors = writerValidator.validateWriter(name, writerRepository);
        if (!errors.isEmpty()) {
            return errors;
        }

        Writer writer = new Writer();
        writer.setName(name);
        writerRepository.save(writer);

        return errors;
    }

    /**
     * Modifies an existing writer.
     * @param name a
     * @param id a
     * @return a
     */
    public List<String> modifyWriter(String name, Long id) {
        List<String> errors = writerValidator.validateWriter(name, writerRepository);
        if (!errors.isEmpty()) {
            return errors;
        }

        Writer writer = writerRepository.getOne(id);
        writer.setName(name);
        writerRepository.save(writer);

        return errors;
    }

    /**
     * Deletes an existing writer.
     * @param id a
     * @return a
     */
    public String deleteWriter(Long id) {
        Writer writer = writerRepository.getOne(id);
        removeWritersFromaNew(writer);
        writerRepository.delete(writer);
        return writer.getName();
    }

    /**
     * Removes news from writers.
     * @param anew a
     */
    public void removeNewsFromWriters(News anew) {
        for (Writer wr : anew.getWriters()) {
            Writer writer = writerRepository.getOne(wr.getId());
            List<News> news = writer.getNews();
            news.remove(anew);
            writer.setNews(news);
            writerRepository.save(writer);
        }
    }

    /**
     * Sets writers to news.
     * @param anew A
     * @param writers A
     */
    public void setWritersToNews(News anew, List<Long> writers) {
        anew.setWriters(new ArrayList<>());
        for (Long writer : writers) {
            anew.getWriters().add(writerRepository.getOne(writer));
            Writer w = writerRepository.getOne(writer);
            w.getNews().add(anew);
            writerRepository.save(w);
        }
    }

    /**
     * Removes writes from a news article.
     * @param writer a
     */
    public void removeWritersFromaNew(Writer writer) {
        for (News anew : writer.getNews()) {
            anew.getWriters().remove(writer);
            newsRepository.save(anew);
        }
    }
}
