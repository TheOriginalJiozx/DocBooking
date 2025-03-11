package laegebooking.laegebooking.controller;

import jakarta.validation.Valid;
import laegebooking.laegebooking.dto.DoctorDTO;
import laegebooking.laegebooking.model.Doctor;
import laegebooking.laegebooking.service.AdminService;
import laegebooking.laegebooking.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/exists/{empId}")
    public ResponseEntity<Boolean> checkDoctorExists(@PathVariable String empId) {
        Optional<Doctor> doctor = doctorService.findByEmpId(empId);
        return doctor.isPresent() ? ResponseEntity.ok(true) : ResponseEntity.ok(false);
    }

    @PostMapping("/docregister")
    public ResponseEntity<?> registerDoctor(@RequestHeader("Authorization") String empId,
                                            @Valid @RequestBody DoctorDTO doctorDTO) {
        if (!adminService.isAdmin(empId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Only admins can register doctors");
        }
        DoctorDTO savedDoctor = doctorService.registerDoctor(doctorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
    }

    @GetMapping("/{empId}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long empId) {
        DoctorDTO doctor = doctorService.getDoctorById(empId);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(doctor);
    }
}