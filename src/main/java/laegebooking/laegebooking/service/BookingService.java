package laegebooking.laegebooking.service;

import laegebooking.laegebooking.model.Booking;
import laegebooking.laegebooking.model.Patient;
import laegebooking.laegebooking.repository.BookingRepository;
import laegebooking.laegebooking.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PatientRepository patientRepository;

    public String bookAppointment(String doctorName, String bookingTime, String email) {
        // Fetch patient details using email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);

        if (patientOpt.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }

        Patient patient = patientOpt.get();

        Booking booking = new Booking();
        booking.setPatientId(patient.getId()); // Automatically set patient_id
        booking.setPatientName(patient.getFirstName() + " " + patient.getLastName()); // Set patient name
        booking.setDoctorName(doctorName); // Set doctor name
        booking.setBookingTime(LocalDateTime.parse(bookingTime)); // Set booking time

        bookingRepository.save(booking);
        return "Booking successful!";
    }
}