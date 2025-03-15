package laegebooking.laegebooking.repository;

import laegebooking.laegebooking.model.Booking;
import laegebooking.laegebooking.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPatientId(Long patientId);
    List<Booking> findByDoctorName(String doctorName);
}