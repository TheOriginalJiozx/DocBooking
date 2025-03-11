package laegebooking.laegebooking.controller;

import laegebooking.laegebooking.dto.AdminLoginRequest;
import laegebooking.laegebooking.dto.LoginResponse;
import laegebooking.laegebooking.model.Admin;
import laegebooking.laegebooking.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AdminLoginController {

    private final AdminService adminService;

    public AdminLoginController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/adminlogin")
    public ResponseEntity<?> loginAdmin(@RequestBody AdminLoginRequest loginRequest) {
        Optional<Admin> adminOptional = adminService.findByEmpId(loginRequest.getEmpId());

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            String hashedInputPassword = adminService.hashMD5(loginRequest.getPassword());

            if (admin.getPassword().equals(hashedInputPassword)) {
                return ResponseEntity.ok(new LoginResponse(admin.getId(), admin.getEmpId()));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
}