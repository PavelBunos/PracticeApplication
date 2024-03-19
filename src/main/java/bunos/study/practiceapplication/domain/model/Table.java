package bunos.study.practiceapplication.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Table {
    private String name;
    private List<Column> columns;
}
