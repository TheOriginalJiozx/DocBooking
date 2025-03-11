package laegebooking.laegebooking.controller;

import laegebooking.laegebooking.dto.LoginRequest;
import laegebooking.laegebooking.dto.LoginResponse;
import laegebooking.laegebooking.model.Patient;
import laegebooking.laegebooking.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class PatientLoginController {

    private final PatientService patientService;

    public PatientLoginController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginPatient(@RequestBody LoginRequest loginRequest) {
        Optional<Patient> patientOptional = patientService.findByEmail(loginRequest.getEmail());

        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();

            String hashedInputPassword = patientService.hashMD5(loginRequest.getPassword());

            if (patient.getPassword().equals(hashedInputPassword)) {
                return ResponseEntity.ok(new LoginResponse(patient.getId(), patient.getEmail()));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
}