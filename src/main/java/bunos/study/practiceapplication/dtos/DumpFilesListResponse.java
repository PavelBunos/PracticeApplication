package bunos.study.practiceapplication.dtos;

import lombok.Data;

import java.util.List;

@Data
public class DumpFilesListResponse {
    private String path;
    private List<String> fileNames;
}
