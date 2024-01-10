package bunos.study.practiceapplication.models.migration;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="t_test_entity")
@Data
public class TestEntity {
    @Id
    //@GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "te_id")
    private long id;

    @Column(name = "te_data")
    private String data;
}
