package bunos.study.practiceapplication.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Column {
    private String name;
    private String type;
    private boolean isNullable;
    private String defaultValue;
    private boolean isAutoincrement;
}
