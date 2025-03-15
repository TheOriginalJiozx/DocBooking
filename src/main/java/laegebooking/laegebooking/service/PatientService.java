package laegebooking.laegebooking.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import laegebooking.laegebooking.dto.PatientDTO;
import laegebooking.laegebooking.model.Patient;
import laegebooking.laegebooking.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public PatientDTO registerPatient(PatientDTO patientDTO) {
        String hashedPassword = hashMD5(patientDTO.getPassword());

        Patient patient = new Patient(patientDTO.getFirstName(), patientDTO.getMiddleName(), patientDTO.getLastName(), patientDTO.getPhone(), patientDTO.getEmail(), hashedPassword);
        patient = patientRepository.save(patient);

        return new PatientDTO(patient.getId(), patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getPhone(), patient.getEmail(), null);
    }

    public boolean isPatient(String userEmail) {
        return patientRepository.findByEmail(userEmail).isPresent();
    }

    public List<String> getAllPatientEmails() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(Patient::getEmail)
                .collect(Collectors.toList());
    }

    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
        return new PatientDTO(patient.getId(), patient.getFirstName(), patient.getMiddleName(), patient.getLastName(), patient.getPhone(), patient.getEmail(), null);
    }

    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
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