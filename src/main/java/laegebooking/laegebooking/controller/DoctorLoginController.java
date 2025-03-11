package laegebooking.laegebooking.controller;

import laegebooking.laegebooking.dto.DocLoginRequest;
import laegebooking.laegebooking.dto.DocLoginResponse;
import laegebooking.laegebooking.model.Doctor;
import laegebooking.laegebooking.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class DoctorLoginController {

    private final DoctorService doctorService;

    public DoctorLoginController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/doclogin")
    public ResponseEntity<?> loginPatient(@RequestBody DocLoginRequest loginRequest) {
        Optional<Doctor> doctorOptional = doctorService.findByEmpId(loginRequest.getEmpId());

        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();

            String hashedInputPassword = doctorService.hashMD5(loginRequest.getPassword());

            if (doctor.getPassword().equals(hashedInputPassword)) {
                return ResponseEntity.ok(new DocLoginResponse(doctor.getId(), doctor.getEmpId()));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
}