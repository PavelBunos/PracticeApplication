package bunos.study.practiceapplication.domain.dto;

import lombok.Data;

@Data
public class Backup {
    private String path;
    private String dumpFileName;
    private String args;
    private String database;
}
