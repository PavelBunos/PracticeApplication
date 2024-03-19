package bunos.study.practiceapplication.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="t_connections")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="conn_id")
    private long connectionId;

    @JoinColumn(name="f_database_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Database fromDatabase;

    @JoinColumn(name="s_database_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Database toDatabase;
}
