package laegebooking.laegebooking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String empId;

    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String email;
    private String password;

    public Admin() {}

    public Admin(String empId, String firstName, String middleName, String lastName, String phone, String email, String password) {
        this.empId = empId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
}