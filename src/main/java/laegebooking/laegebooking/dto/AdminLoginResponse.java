package laegebooking.laegebooking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginResponse {
    private Long userId;
    private String empId;

    public AdminLoginResponse(Long userId, String empId) {
        this.userId = userId;
        this.empId = empId;
    }
}