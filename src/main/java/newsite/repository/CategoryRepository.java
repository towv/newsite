package newsite.repository;

import newsite.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Category repository.
 * @author twviiala
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find based on category name.
     * @param name a
     * @return a
     */
    Category findByName(String name);
}
