
package newsite.repository;

import java.util.Date;
import java.util.List;
import newsite.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import newsite.domain.View;

/**
 * View repository.
 * @author twviiala
 */
public interface ViewRepository extends JpaRepository<View, Long> {
    
    /**
     * Finds all views related to certain News and created after the Date.
     * @param news a
     * @param date a
     * @return a
     */
    List<View> findByNewsAndWhenViewedAfter(News news, Date date);
    
}
