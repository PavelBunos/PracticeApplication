package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.model.Column;
import bunos.study.practiceapplication.domain.model.Database;
import bunos.study.practiceapplication.domain.model.Table;
import lombok.RequiredArgsConstructor;
import org.postgresql.Driver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseMetadataService {
    private DataSource dataSource;

    public ResponseEntity<?> getTables(Database database) {
        try {
            DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
            driverManagerDataSource.setUrl(database.getUrl());
            driverManagerDataSource.setUsername(database.getUser());
            driverManagerDataSource.setPassword(database.getPassword());

            dataSource = driverManagerDataSource;

            return new ResponseEntity<>(Response.builder()
                    .data(getTables())
                    .build(), HttpStatus.OK);
        } catch (SQLException ex) {
            return new ResponseEntity<>(Response.builder()
                    .data("Не удалось получить данные о структуре базы данных!")
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    public List<Table> getTables() throws SQLException {
        List<Table> tables = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             ResultSet tablesResultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {

            while (tablesResultSet.next()) {
                String tableName = tablesResultSet.getString("TABLE_NAME");
                Table table = Table.builder()
                        .name(tableName)
                        .columns(getColumns(connection, tableName))
                        .build();
                tables.add(table);
            }
        }

        return tables;
    }

    private List<Column> getColumns(Connection connection, String tableName) throws SQLException {
        List<Column> columns = new ArrayList<>();

        try (ResultSet columnsResultSet = connection.getMetaData().getColumns(null, null, tableName, "%")) {
            while (columnsResultSet.next()) {
                Column column = Column.builder()
                        .name(columnsResultSet.getString("COLUMN_NAME"))
                        .type(columnsResultSet.getString("TYPE_NAME"))
                        .isNullable(columnsResultSet.getString("IS_NULLABLE").equals("YES"))
                        .defaultValue(columnsResultSet.getString("COLUMN_DEF"))
                        .isAutoincrement(columnsResultSet.getString("IS_AUTOINCREMENT").equals("YES"))
                        .build();
                columns.add(column);
            }
        }

        return columns;
    }
}
