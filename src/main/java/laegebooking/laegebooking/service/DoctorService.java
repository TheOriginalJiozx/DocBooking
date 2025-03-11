package laegebooking.laegebooking.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import laegebooking.laegebooking.dto.DoctorDTO;
import laegebooking.laegebooking.model.Doctor;
import laegebooking.laegebooking.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public DoctorDTO registerDoctor(DoctorDTO doctorDTO) {
        if (doctorDTO.getEmpId() == null || doctorDTO.getEmpId().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty");
        }

        String hashedPassword = hashMD5(doctorDTO.getPassword());

        Doctor doctor = new Doctor(doctorDTO.getFirstName(), doctorDTO.getMiddleName(), doctorDTO.getLastName(), doctorDTO.getPhone(), doctorDTO.getEmpId(), doctorDTO.getEmail(), hashedPassword);
        doctor = doctorRepository.save(doctor);

        return new DoctorDTO(doctor.getId(), doctor.getFirstName(), doctor.getMiddleName(), doctor.getLastName(), doctor.getPhone(), doctor.getEmpId(), doctor.getEmail(), null);
    }

    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        return new DoctorDTO(doctor.getId(), doctor.getFirstName(), doctor.getMiddleName(), doctor.getLastName(), doctor.getPhone(), doctor.getEmpId(), doctor.getEmail(), null);
    }

    public Optional<Doctor> findByEmpId(String empId) {
        return doctorRepository.findByEmpId(empId);
    }

    public String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }
}