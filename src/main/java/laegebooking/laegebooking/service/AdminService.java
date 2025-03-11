package laegebooking.laegebooking.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import laegebooking.laegebooking.dto.AdminDTO;
import laegebooking.laegebooking.dto.DoctorDTO;
import laegebooking.laegebooking.model.Admin;
import laegebooking.laegebooking.model.Doctor;
import laegebooking.laegebooking.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public AdminDTO registerAdmin(AdminDTO adminDTO) {
        String hashedPassword = hashMD5(adminDTO.getPassword());

        Admin admin = new Admin(adminDTO.getFirstName(), adminDTO.getMiddleName(), adminDTO.getLastName(), adminDTO.getPhone(), adminDTO.getEmpId(), adminDTO.getEmail(), hashedPassword);
        admin = adminRepository.save(admin);

        return new AdminDTO(admin.getId(), admin.getFirstName(), admin.getMiddleName(), admin.getLastName(), admin.getPhone(), admin.getEmpId(), admin.getEmail(), null);
    }

    public AdminDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        return new AdminDTO(admin.getId(), admin.getFirstName(), admin.getMiddleName(), admin.getLastName(), admin.getPhone(), admin.getEmpId(), admin.getEmail(), null);
    }

    public Optional<Admin> findByEmpId(String empId) {
        return adminRepository.findByEmpId(empId);
    }

    public boolean isAdmin(String empId) {
        return adminRepository.findByEmpId(empId).isPresent();
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