package laegebooking.laegebooking.service;

import laegebooking.laegebooking.model.AvailableHour;
import laegebooking.laegebooking.model.Booking;
import laegebooking.laegebooking.model.Doctor;
import laegebooking.laegebooking.model.Patient;
import laegebooking.laegebooking.repository.AvailableHoursRepository;
import laegebooking.laegebooking.repository.BookingRepository;
import laegebooking.laegebooking.repository.DoctorRepository;
import laegebooking.laegebooking.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AvailableHoursRepository availableHoursRepository;

    public List<Booking> getAllAppointments() {
        return bookingRepository.findAll();
    }

    public List<Booking> getAppointmentsByPatientEmail(String email) {
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isPresent()) {
            return bookingRepository.findByPatientId(patientOpt.get().getId());
        }
        throw new IllegalArgumentException("Patient not found with email: " + email);
    }

    public List<Booking> getAppointmentsByDoctorEmpId(String empId) {
        Optional<Doctor> doctorOpt = doctorRepository.findByEmpId(empId);
        if (doctorOpt.isPresent()) {
            return bookingRepository.findByDoctorName(doctorOpt.get().getFirstName() + " " + doctorOpt.get().getMiddleName() + " " + doctorOpt.get().getLastName());
        }
        throw new IllegalArgumentException("Doctor not found with empId: " + empId);
    }

    public String bookAppointment(String doctorName, String service, String bookingTime, String email) {
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);

        if (patientOpt.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }

        Patient patient = patientOpt.get();

        Booking booking = new Booking();
        booking.setPatientId(patient.getId());
        booking.setService(service);
        booking.setPatientName(patient.getFirstName() + " " + patient.getMiddleName() + " " + patient.getLastName());
        booking.setDoctorName(doctorName);
        booking.setBookingTime(LocalDateTime.parse(bookingTime));
        booking.setAppointmentStatus("Confirmed");

        AvailableHour availableHour = availableHoursRepository.findByDoctorName(doctorName)
                .stream()
                .filter(hour -> hour.getAvailableTime().equals(LocalDateTime.parse(bookingTime)))
                .findFirst()
                .orElse(null);

        if (availableHour != null) {
            availableHoursRepository.delete(availableHour);
        }

        bookingRepository.save(booking);
        return doctorName;
    }

    public String cancelAppointment(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found");
        }

        Booking booking = bookingOpt.get();
        booking.setAppointmentStatus("Cancelled");
        bookingRepository.save(booking);

        AvailableHour availableHour = new AvailableHour();
        availableHour.setDoctorName(booking.getDoctorName());
        availableHour.setAvailableTime(booking.getBookingTime());

        AvailableHour existingHour = availableHoursRepository.findByDoctorNameAndAvailableTime(
                booking.getDoctorName(), booking.getBookingTime());

        if (existingHour == null) {
            availableHoursRepository.save(availableHour);
        }

        return "Your appointment has been cancelled!";
    }

    public String heldAppointment(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found");
        }

        Booking booking = bookingOpt.get();
        booking.setAppointmentStatus("Held");
        bookingRepository.save(booking);

        return "Appointment marked as held successfully!";
    }

    public String rescheduleAppointment(Long appointmentId, String newBookingTime) {
        Booking appointment = bookingRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found");
        }

        LocalDateTime previousTime = appointment.getBookingTime();
        LocalDateTime newTime = LocalDateTime.parse(newBookingTime);

        AvailableHour availableHour = availableHoursRepository.findByDoctorName(appointment.getDoctorName())
                .stream()
                .filter(hour -> hour.getAvailableTime().equals(newTime))
                .findFirst()
                .orElse(null);

        if (availableHour == null) {
            throw new IllegalArgumentException("Time not available");
        }

        AvailableHour previousAvailableHour = new AvailableHour();
        previousAvailableHour.setDoctorName(appointment.getDoctorName());
        previousAvailableHour.setAvailableTime(previousTime);

        if (availableHoursRepository.findByDoctorNameAndAvailableTime(appointment.getDoctorName(), previousTime) == null) {
            availableHoursRepository.save(previousAvailableHour);
        }

        availableHoursRepository.delete(availableHour);
        appointment.setBookingTime(newTime);
        appointment.setAppointmentStatus("Rescheduled");
        bookingRepository.save(appointment);
        return newBookingTime;
    }
}