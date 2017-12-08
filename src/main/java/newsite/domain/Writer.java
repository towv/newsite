package newsite.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Writer table.
 * @author twviiala
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Writer extends AbstractPersistable<Long> {

    @ManyToMany(mappedBy = "writers")
    private List<News> news = new ArrayList();
    private String name;
}
