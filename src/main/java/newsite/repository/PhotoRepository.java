
package newsite.repository;

import newsite.domain.News;
import newsite.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Photo findByNews(News news);
}
