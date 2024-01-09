package bunos.study.practiceapplication.models.migration;

import jakarta.persistence.*;

@Entity
@Table(name="t_test_entity")
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "te_id")
    private long id;

    @Column(name = "te_data")
    private String data;
}
