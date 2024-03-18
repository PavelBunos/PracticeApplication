package bunos.study.practiceapplication.domain.dto;

import lombok.Data;

@Data
public class UserData {
    private String username;
    private String password;
    private String role;

    @Override
    public String toString() {
        return username + " " + password + " " + role;
    }
}
