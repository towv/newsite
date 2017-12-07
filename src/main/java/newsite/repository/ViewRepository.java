
package newsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import newsite.domain.View;

public interface ViewRepository extends JpaRepository<View, Long> {
    
}
