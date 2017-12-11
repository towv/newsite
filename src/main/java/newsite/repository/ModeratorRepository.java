package newsite.repository;

import newsite.domain.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Moderator repository.
 * @author twviiala
 */
public interface ModeratorRepository extends JpaRepository<Moderator, Long> {

    /**
     * Find based on moderator name.
     * @param username a
     * @return a
     */
    public Moderator findByName(String username);

}
