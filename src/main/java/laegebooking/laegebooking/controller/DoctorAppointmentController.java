package laegebooking.laegebooking.controller;

import laegebooking.laegebooking.model.Booking;
import laegebooking.laegebooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor/appointments")
public class DoctorAppointmentController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/my-patients")
    public ResponseEntity<List<Booking>> getDoctorAppointments(@RequestParam String empId) {
        try {
            List<Booking> appointments = bookingService.getAppointmentsByDoctorEmpId(empId);
            if (appointments.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(appointments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/held/{id}")
    public ResponseEntity<Map<String, String>> updateAppointmentStatus(@PathVariable Long id) {
        String message = bookingService.heldAppointment(id);

        if (message == null || message.equals("Appointment not found")) {
            return ResponseEntity.status(404).body(Map.of("message", "Appointment not found"));
        }

        return ResponseEntity.ok(Map.of("message", message));
    }
}