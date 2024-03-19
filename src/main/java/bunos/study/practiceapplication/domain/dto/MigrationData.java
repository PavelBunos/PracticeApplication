package bunos.study.practiceapplication.domain.dto;

import bunos.study.practiceapplication.domain.model.Connection;
import lombok.Data;

@Data
public class MigrationData {
    private String tablenameSource;
    private String tablenameOutput;
    private Connection connection;
}
