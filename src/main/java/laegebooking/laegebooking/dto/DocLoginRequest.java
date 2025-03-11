package laegebooking.laegebooking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocLoginRequest {
    private String empId;
    private String password;
}