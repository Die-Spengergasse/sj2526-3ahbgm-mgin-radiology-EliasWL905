package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.Patient;
import at.spengergasse.spring_thymeleaf.entities.PatientRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/list")
    public String patients(Model model) {
        try {
            model.addAttribute("patients", patientRepository.findAll());
            return "patlist";
        } catch (DataAccessException e) {
            model.addAttribute("error", "Datenbankfehler: Die Verbindung zur Datenbank konnte nicht hergestellt werden. Bitte prüfen Sie, ob MySQL läuft.");
            model.addAttribute("patients", java.util.List.of());
            return "patlist";
        }
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("Patient", new Patient());
        return "add_patient";
    }

    @PostMapping("/add")
    public String savePatient(@ModelAttribute Patient patient, Model model) {

        // Geburtsdatum in der Zukunft
        if (patient.getBirth() != null && patient.getBirth().isAfter(LocalDate.now())) {
            model.addAttribute("error", "Fehler: Das Geburtsdatum darf nicht in der Zukunft liegen.");
            model.addAttribute("Patient", patient);
            return "add_patient";
        }

        // Sozialversicherungsnummer: genau 10 Ziffern
        String svnr = patient.getSvnr();
        if (svnr == null || !svnr.matches("\\d{10}")) {
            model.addAttribute("error", "Fehler: Die Sozialversicherungsnummer muss genau 10 Ziffern enthalten (z.B. 1234010190).");
            model.addAttribute("Patient", patient);
            return "add_patient";
        }

        try {
            patientRepository.save(patient);
            return "redirect:/patient/list";
        } catch (DataAccessException e) {
            model.addAttribute("error", "Fehler: Die Verbindung zur Datenbank konnte nicht hergestellt werden.");
            model.addAttribute("Patient", patient);
            return "add_patient";
        }
    }
}
