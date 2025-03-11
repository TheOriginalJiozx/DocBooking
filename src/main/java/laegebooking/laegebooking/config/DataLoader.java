package laegebooking.laegebooking.config;

import laegebooking.laegebooking.model.Admin;
import laegebooking.laegebooking.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(AdminRepository adminRepository) {
        return args -> {
            if (adminRepository.count() == 0) {
                Random random = new Random();
                String empId = "EMP" + random.nextInt(1000, 9999);
                String firstName = "FirstName" + random.nextInt(1, 100);
                String middleName = "MiddleName" + random.nextInt(1, 100);
                String lastName = "LastName" + random.nextInt(1, 100);
                String phone = "1234" + random.nextInt(100000, 999999);
                String email = firstName.toLowerCase() + "@example.com";
                String password = "password" + random.nextInt(1000, 9999);
                String hashedPassword = hashMD5(password);

                Admin admin = new Admin(empId, firstName, middleName, lastName, phone, email, hashedPassword);
                adminRepository.save(admin);

                System.out.println("Generated Password (Before Hashing): " + password);

                System.out.println("Generated Admin Details:");
                System.out.println("EmpId: " + empId);
                System.out.println("First Name: " + firstName);
                System.out.println("Middle Name: " + middleName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Phone: " + phone);
                System.out.println("Email: " + email);
                System.out.println("Password: " + password);
            }
        };
    }

    private String hashMD5(String input) {
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