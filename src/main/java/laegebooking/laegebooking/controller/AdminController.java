package laegebooking.laegebooking.controller;

import laegebooking.laegebooking.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/check")
    public ResponseEntity<?> checkAdmin(@RequestHeader("Authorization") String empId) {
        if (adminService.isAdmin(empId)) {
            return ResponseEntity.ok().body("Authorized");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
    }
}