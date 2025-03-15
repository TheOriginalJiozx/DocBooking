package laegebooking.laegebooking.repository;

import laegebooking.laegebooking.model.PatientCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientCourseRepository extends JpaRepository<PatientCourse, Long> {
    Optional<PatientCourse> findByPatientEmail(String patientEmail);
}