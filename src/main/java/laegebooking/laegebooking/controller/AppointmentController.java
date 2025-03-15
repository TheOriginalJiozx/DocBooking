package laegebooking.laegebooking.controller;

import laegebooking.laegebooking.model.Booking;
import laegebooking.laegebooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/my-appointments")
    public List<Booking> getAppointments(@RequestParam String email) {
        return bookingService.getAppointmentsByPatientEmail(email);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable Long id) {
        String message = bookingService.cancelAppointment(id);
        if (message.equals("Appointment not found")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PutMapping("/reschedule/{id}")
    public ResponseEntity<Map<String, String>> rescheduleAppointment(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String newBookingTime = request.get("newBookingTime");
        if (newBookingTime == null || newBookingTime.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "New booking time is required"));
        }

        String message = bookingService.rescheduleAppointment(id, newBookingTime);
        if (message.equals("Appointment not found") || message.equals("Time not available")) {
            return ResponseEntity.badRequest().body(Map.of("message", message));
        }

        return ResponseEntity.ok(Map.of("message", message));
    }
}