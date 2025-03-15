package laegebooking.laegebooking.repository;

import laegebooking.laegebooking.model.AvailableHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailableHoursRepository extends JpaRepository<AvailableHour, Long> {
    List<AvailableHour> findByDoctorName(String doctorName);
    AvailableHour findByDoctorNameAndAvailableTime(String doctorName, LocalDateTime availableTime);
}