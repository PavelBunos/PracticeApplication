package bunos.study.practiceapplication.dtos;

import lombok.Data;

@Data
public class DumpRequest {
    private String path;
    private String dumpFileName;
    private String args;
    private String database;
}
