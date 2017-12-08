
package newsite.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Photo table.
 * @author twviiala
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Photo extends AbstractPersistable<Long> {
    
    private String name;
    private String contentType;
    private Long contentLength;
    @OneToOne
    private News news;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;
}
