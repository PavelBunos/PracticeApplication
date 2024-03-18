package bunos.study.practiceapplication.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "t_log")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private long logId;

    @Column(name = "data")
    private String data;

    @ManyToOne
    @JoinColumn(name = "journal_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Journal journal;

    @Column(name = "http_status_code")
    private int status;

    @Column(name = "time")
    private LocalTime time;

}
