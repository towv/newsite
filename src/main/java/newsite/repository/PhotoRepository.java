
package newsite.repository;

import newsite.domain.News;
import newsite.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Photo repository.
 * @author twviiala
 */
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    /**
     * Find photo by News.
     * @param news
     * @return
     */
    Photo findByNews(News news);
}
