package newsite.repository;

import newsite.domain.Writer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Writer repository.
 * @author twviiala
 */
public interface WriterRepository extends JpaRepository<Writer, Long> {

    /**
     * Finds writer by writer name.
     * @param name a
     * @return a
     */
    Writer findByName(String name);
}
