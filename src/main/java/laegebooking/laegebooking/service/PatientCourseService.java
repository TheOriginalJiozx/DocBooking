package laegebooking.laegebooking.service;

import laegebooking.laegebooking.model.Patient;
import laegebooking.laegebooking.model.PatientCourse;
import laegebooking.laegebooking.repository.PatientCourseRepository;
import laegebooking.laegebooking.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientCourseService {

    @Autowired
    private PatientCourseRepository patientCourseRepository;
    @Autowired
    private PatientRepository patientRepository;

    public boolean updateCourseStatus(String userEmail, String courseStatus) {
        // Find PatientCourse by email
        Optional<PatientCourse> patientCourseOptional = patientCourseRepository.findByPatientEmail(userEmail);

        if (patientCourseOptional.isPresent()) {
            PatientCourse patientCourse = patientCourseOptional.get();
            patientCourse.setCourseStatus(courseStatus);

            // Find the corresponding patient for the email
            Optional<Patient> patientOptional = patientRepository.findByEmail(userEmail);
            if (patientOptional.isPresent()) {
                Patient patient = patientOptional.get();
                String patientName = patient.getFirstName() + " " +
                        (patient.getMiddleName() != null ? patient.getMiddleName() + " " : "") +
                        patient.getLastName();
                patientCourse.setPatientName(patientName);
            } else {
                // Log if no matching patient found
                System.out.println("Patient not found for email: " + userEmail);
                return false; // Fail if no patient is found
            }

            // Save the updated PatientCourse
            patientCourseRepository.save(patientCourse);
            return true;
        }

        // Log if PatientCourse is not found
        System.out.println("PatientCourse not found for email: " + userEmail);
        return false;
    }
}