package newsite.repository;

import newsite.domain.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeratorRepository extends JpaRepository<Moderator, Long> {

    public Moderator findByName(String username);

}
