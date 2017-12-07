
package newsite.repository;

import java.util.Date;
import java.util.List;
import newsite.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import newsite.domain.View;

public interface ViewRepository extends JpaRepository<View, Long> {
    
     List<View> findByNewsAndWhenViewedAfter(News news, Date date);
    
}
