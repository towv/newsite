package newsite.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * View table.
 * Related to news, news can get several views.
 * @author twviiala
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class View extends AbstractPersistable<Long> {

    private Date whenViewed = new Date();
    @ManyToOne
    private News news;
}
