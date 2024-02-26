package bunos.study.practiceapplication.models.databases;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="t_connections")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="conn_id")
    private long connectionId;

    @JoinColumn(name="f_database_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Database fromDatabase;

    @JoinColumn(name="s_database_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Database toDatabase;
}
