package bunos.study.practiceapplication.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
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

    @Column(name="\"name\"")
    private String name;

    @Column(name="\"user\"")
    private String user;

    @Column(name="\"password\"")
    private String password;

    private String hostname;

    private long port;

    public String getUrlWithUser() {
        return "jdbc:postgresql://" + user + ":" + password + "@" + hostname + ":" + port + "/" + name;
    }

    public String getUrl() {
        return "jdbc:postgresql://" + hostname + ":" + port + "/" + name;
    }

    public boolean getConnectionStatus() {
        try (Connection connection = DriverManager.getConnection(getUrl(), user, password); Statement st = connection.createStatement()) {
            if (st.execute("select version();")) {
                return true;
            }
        }
        catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return false;
    }
}
