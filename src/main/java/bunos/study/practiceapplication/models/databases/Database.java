package bunos.study.practiceapplication.models.databases;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="t_databases")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Database {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="database_id")
    private long databaseId;

    private String name;

    @Column(name="user")
    private String username;

    private String password;

    private String hostname;

    private long port;

    public String getUrl() {
        return "jdbc:postgresql://" + hostname + ":" + port + "/" + name;
    }
}
