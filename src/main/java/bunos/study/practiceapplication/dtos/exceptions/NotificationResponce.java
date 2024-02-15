package bunos.study.practiceapplication.dtos.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponce {
    private String title;
    private String message;
    private boolean isBad;
}
