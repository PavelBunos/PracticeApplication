package bunos.study.practiceapplication.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@Entity
@Table(name="t_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private long userId;
    @NonNull
    private String username;
    @NonNull
    private String password;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinTable(
            name="t_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Collection<Role> roles;
}