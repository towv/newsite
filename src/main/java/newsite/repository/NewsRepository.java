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
 *
 * @author twviiala
 */
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Find News based on Category.
     *
     * @param categories a
     * @return a
     */
    List<News> findByCategories(List<Category> categories);

    /**
     * Find News based on Category and amount of Views.
     *
     * @param categories a
     * @param pageable a
     * @return a
     */
    List<News> findByCategoriesAndViews(List<Category> categories, Pageable pageable);

    /**
     * Find News based on Category and pageable.
     *
     * @param categories a
     * @param pageable a
     * @return a
     */
    List<News> findByCategories(List<Category> categories, Pageable pageable);

    /**
     * Find News based on header.
     *
     * @param header a
     * @return a
     */
    News findByHeader(String header);

    /**
     * Find news with searchWord
     *
     * @param searchWord a
     * @return a
     */
    List<News> findByHeaderContainingIgnoreCase(String searchWord);

}
