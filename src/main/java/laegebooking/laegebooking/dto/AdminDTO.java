package laegebooking.laegebooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {

    private Long id;

    @NotBlank(message = "Your first name cannot be empty")
    private String firstName;

    @NotBlank(message = "Your middle name cannot be empty")
    private String middleName;

    @NotBlank(message = "Your last name cannot be empty")
    private String lastName;

    @NotBlank(message = "Your phone number cannot be empty")
    private String phone;

    @NotBlank(message = "Your employee ID cannot be empty")
    private String empId;

    @Email(message = "Invalid email.")
    @NotBlank(message = "E-mail cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}