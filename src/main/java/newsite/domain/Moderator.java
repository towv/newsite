
package newsite.domain;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Moderator extends AbstractPersistable<Long> {
    
    private String name;
    private String password;
    
    public void theModerator(Moderator moderator) {
        moderator.setName("moderator");
        moderator.setPassword("mod");
    }
}
