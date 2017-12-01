package newsite.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class News extends AbstractPersistable<Long> {

    private String header;
    private String lead;
    private Date pDate = new Date();
    private String text;
    @ManyToMany
    private List<Writer> writers = new ArrayList<>();
    @ManyToMany
    private List<Category> categories = new ArrayList<>();
//    @OneToOne(mappedBy = "News")
//    private Views timesViewed;
}
