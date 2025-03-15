package laegebooking.laegebooking.controller;

import laegebooking.laegebooking.model.AvailableHour;
import laegebooking.laegebooking.service.AvailableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AvailableController {

    @Autowired
    private AvailableService availableService;

    @GetMapping("/api/available-hours")
    public List<AvailableHour> getAvailableHours(@RequestParam String doctorName) {
        return availableService.getAvailableHoursByDoctor(doctorName);
    }
}