package laegebooking.laegebooking.controller;

import jakarta.validation.Valid;
import laegebooking.laegebooking.dto.DoctorDTO;
import laegebooking.laegebooking.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/docregister")
    public ResponseEntity<DoctorDTO> registerDoctor(@Valid @RequestBody DoctorDTO doctorDTO) {
        DoctorDTO savedDoctor = doctorService.registerDoctor(doctorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        DoctorDTO doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(doctor);
    }
}