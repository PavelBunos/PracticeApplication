package bunos.study.practiceapplication.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="t_roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private long roleId;
    @NonNull
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
