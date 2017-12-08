package newsite.repository;

import java.util.ArrayList;
import java.util.List;
import newsite.domain.Category;
import newsite.domain.News;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * News repository.
 * @author twviiala
 */
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Find News based on Category.
     * @param categories
     * @return
     */
    List<News> findByCategories(List<Category> categories);

    /**
     * Find News based on Category and amount of Views.
     * @param categories
     * @param pageable
     * @return
     */
    List<News> findByCategoriesAndViews(List<Category> categories, Pageable pageable);

    /**
     * Find News based on Category and pageable.
     * @param categories
     * @param pageable
     * @return
     */
    List<News> findByCategories(List<Category> categories, Pageable pageable);
    
    /**
     * Find News based on header.
     * @param header
     * @return
     */
    News findByHeader(String header);

}
