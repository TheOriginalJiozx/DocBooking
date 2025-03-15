package laegebooking.laegebooking.service;

import laegebooking.laegebooking.model.AvailableHour;
import laegebooking.laegebooking.repository.AvailableHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailableService {

    @Autowired
    private AvailableHoursRepository availableHoursRepository;

    public List<AvailableHour> getAvailableHoursByDoctor(String doctorName) {
        return availableHoursRepository.findByDoctorName(doctorName);
    }
}