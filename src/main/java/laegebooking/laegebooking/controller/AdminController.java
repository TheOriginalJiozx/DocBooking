package laegebooking.laegebooking.controller;

import laegebooking.laegebooking.model.Admin;
import laegebooking.laegebooking.model.Booking;
import laegebooking.laegebooking.model.Patient;
import laegebooking.laegebooking.repository.PatientRepository;
import laegebooking.laegebooking.service.AdminService;
import laegebooking.laegebooking.service.BookingService;
import laegebooking.laegebooking.service.PatientCourseService;
import laegebooking.laegebooking.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PatientCourseService patientCourseService;
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/appointments")
    public ResponseEntity<List<Booking>> getAllAppointments() {
        List<Booking> allAppointments = bookingService.getAllAppointments();
        return ResponseEntity.ok(allAppointments);
    }

    @PutMapping("/patient-course/{userEmail}")
    public ResponseEntity<Map<String, String>> updatePatientCourseStatus(
            @PathVariable String userEmail,
            @RequestParam String courseStatus) {
        boolean updated = patientCourseService.updateCourseStatus(userEmail, courseStatus);

        Map<String, String> response = new HashMap<>();
        if (updated) {
            response.put("message", "Patient course status updated successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Patient or course status not found for the given email: " + userEmail);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/patient-emails")
    public ResponseEntity<List<String>> getPatientEmails() {
        List<String> patientEmails = patientService.getAllPatientEmails();
        return ResponseEntity.ok(patientEmails);
    }

    @GetMapping("/patient-names")
    public ResponseEntity<List<Map<String, String>>> getPatientNames() {
        List<Map<String, String>> patientList = new ArrayList<>();

        List<Patient> patients = patientService.getAllPatients();

        for (Patient patient : patients) {
            Map<String, String> patientData = new HashMap<>();
            String patientName = patient.getFirstName() + " " +
                    (patient.getMiddleName() != null ? patient.getMiddleName() + " " : "") +
                    patient.getLastName();
            patientData.put("name", patientName);
            patientData.put("email", patient.getEmail());

            patientList.add(patientData);
        }

        return ResponseEntity.ok(patientList);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAdmin(@RequestHeader("Authorization") String empId) {
        if (adminService.isAdmin(empId)) {
            return ResponseEntity.ok().body("Authorized");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
    }

    @GetMapping("/exists/{empId}")
    public ResponseEntity<Boolean> checkAdminExists(@PathVariable String empId) {
        Optional<Admin> admin = adminService.findByEmpId(empId);
        return admin.isPresent() ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }

    @GetMapping("/{empId}")
    public ResponseEntity<Map<String, String>> getAdminEmail(@PathVariable String empId) {
        Optional<String> emailOpt = adminService.getAdminEmailByEmpId(empId);

        if (emailOpt.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("email", emailOpt.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Admin not found"));
        }
    }
}