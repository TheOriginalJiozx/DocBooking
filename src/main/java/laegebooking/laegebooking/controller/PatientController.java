package laegebooking.laegebooking.controller;

import jakarta.validation.Valid;
import laegebooking.laegebooking.dto.PatientDTO;
import laegebooking.laegebooking.service.BookingService;
import laegebooking.laegebooking.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private BookingService bookingService;

    @PostMapping("/register")
    public ResponseEntity<PatientDTO> registerPatient(@Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO savedPatient = patientService.registerPatient(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        PatientDTO patient = patientService.getPatientById(id);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(patient);
    }

    @PostMapping("/booking")
    public ResponseEntity<?> bookAppointment(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email"); // Get email from request body
            String doctorName = request.get("doctorName");
            String bookingTime = request.get("bookingTime");

            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
            }

            String message = bookingService.bookAppointment(doctorName, bookingTime, email);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}