package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final PatientRepository patientRepository;
    private final DeviceRepository deviceRepository;

    public ReservationController(ReservationRepository reservationRepository,
                                 PatientRepository patientRepository,
                                 DeviceRepository deviceRepository) {
        this.reservationRepository = reservationRepository;
        this.patientRepository = patientRepository;
        this.deviceRepository = deviceRepository;
    }

    private void addFormData(Model model, Reservation reservation) {
        model.addAttribute("reservation", reservation);
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        try {
            addFormData(model, new Reservation());
            return "add_reservation";
        } catch (DataAccessException e) {
            model.addAttribute("error", "Datenbankfehler: Die Verbindung zur Datenbank konnte nicht hergestellt werden. Bitte prüfen Sie, ob MySQL läuft.");
            model.addAttribute("reservation", new Reservation());
            return "add_reservation";
        }
    }

    @PostMapping("/add")
    public String saveReservation(@ModelAttribute Reservation reservation,
                                  @RequestParam int patientId,
                                  @RequestParam int deviceId,
                                  Model model) {
        try {
            Patient patient = patientRepository.findById(patientId).orElse(null);
            Device device = deviceRepository.findById(deviceId).orElse(null);

            reservation.setPatient(patient);
            reservation.setDevice(device);

            LocalDateTime startTime = reservation.getStartTime();
            LocalDateTime endTime = reservation.getEndTime();

            // reservation in the past
            if (startTime != null && startTime.isBefore(LocalDateTime.now())) {
                model.addAttribute("error", "Fehler: Der Startzeitpunkt darf nicht in der Vergangenheit liegen.");
                addFormData(model, reservation);
                return "add_reservation";
            }

            // overlapping reservation for device
            if (device != null && startTime != null && endTime != null) {
                List<Reservation> deviceConflicts = reservationRepository
                        .findByDeviceIdAndStartTimeBeforeAndEndTimeAfter(deviceId, endTime, startTime);
                if (!deviceConflicts.isEmpty()) {
                    model.addAttribute("error", "Fehler: Das Gerät \"" + device.getName() + "\" ist in diesem Zeitraum bereits belegt.");
                    addFormData(model, reservation);
                    return "add_reservation";
                }
            }

            // overlapping reservation for patient
            if (patient != null && startTime != null && endTime != null) {
                List<Reservation> patientConflicts = reservationRepository
                        .findByPatientIdAndStartTimeBeforeAndEndTimeAfter(patientId, endTime, startTime);
                if (!patientConflicts.isEmpty()) {
                    model.addAttribute("error", "Fehler: Der Patient \"" + patient.getFirstname() + " " + patient.getLastname() + "\" hat in diesem Zeitraum bereits einen anderen Termin.");
                    addFormData(model, reservation);
                    return "add_reservation";
                }
            }

            reservationRepository.save(reservation);
            return "redirect:/device/list";

        } catch (DataAccessException e) {
            model.addAttribute("error", "Datenbankfehler: Die Verbindung zur Datenbank konnte nicht hergestellt werden. Bitte prüfen Sie, ob MySQL läuft.");
            addFormData(model, reservation);
            return "add_reservation";
        }
    }

    @GetMapping("/list/{deviceId}")
    public String reservations(@PathVariable int deviceId, Model model) {
        try {
            model.addAttribute("reservations", reservationRepository.findByDeviceId(deviceId));
            model.addAttribute("device", deviceRepository.findById(deviceId).orElse(null));
            return "reservation_list";
        } catch (DataAccessException e) {
            model.addAttribute("error", "Datenbankfehler: Die Verbindung zur Datenbank konnte nicht hergestellt werden. Bitte prüfen Sie, ob MySQL läuft.");
            model.addAttribute("reservations", List.of());
            return "reservation_list";
        }
    }
}
