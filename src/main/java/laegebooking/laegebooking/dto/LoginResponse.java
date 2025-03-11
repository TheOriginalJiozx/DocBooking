package laegebooking.laegebooking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private Long userId;
    private String email;

    public LoginResponse(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }
}