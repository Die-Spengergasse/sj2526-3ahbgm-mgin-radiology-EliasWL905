package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationRepository reservationRepository;
    private final PatientRepository patientRepository;
    private final DeviceRepository deviceRepository;

    public ReservationController(ReservationRepository reservationRepository, PatientRepository patientRepository, DeviceRepository deviceRepository) {
        this.reservationRepository = reservationRepository;
        this.patientRepository = patientRepository;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        return "add_reservation";
    }

    @PostMapping("/add")
    public String saveReservation(@ModelAttribute Reservation reservation, 
                                  @RequestParam int patientId, 
                                  @RequestParam int deviceId) {

        Patient patient = patientRepository.findById(patientId).orElse(null);
        Device device = deviceRepository.findById(deviceId).orElse(null);
        
        reservation.setPatient(patient);
        reservation.setDevice(device);
        
        reservationRepository.save(reservation);
        return "redirect:/device/list";
    }

    @GetMapping("/list/{deviceId}")
    public String reservations(@PathVariable int deviceId, Model model) {
        model.addAttribute("reservations", reservationRepository.findByDeviceId(deviceId));
        model.addAttribute("device", deviceRepository.findById(deviceId).orElse(null));
        return "reservation_list";
    }
}
