package laegebooking.laegebooking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginRequest {
    private String empId;
    private String password;
}