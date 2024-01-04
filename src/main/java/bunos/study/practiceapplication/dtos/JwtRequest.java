package bunos.study.practiceapplication.dtos;

import lombok.Data;

@Data
public class JwtRequest {
    private String usermame;
    private String password;
}
