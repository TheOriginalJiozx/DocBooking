package laegebooking.laegebooking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocLoginResponse {
    private Long userId;
    private String empId;

    public DocLoginResponse(Long userId, String empId) {
        this.userId = userId;
        this.empId = empId;
    }
}