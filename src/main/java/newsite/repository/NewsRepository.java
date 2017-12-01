
package newsite.repository;

import java.util.List;
import newsite.domain.Category;
import newsite.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByCategories(List<Category> categories);
}
