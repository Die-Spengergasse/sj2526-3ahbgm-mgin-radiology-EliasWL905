package at.spengergasse.spring_thymeleaf.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//Also leere Interfaces – Spring Data JPA implementiert findAll(), save(), findById(), delete() usw.
// vollautomatisch zur Laufzeit, ohne dass man eine einzige Zeile SQL oder Implementierungscode schreiben muss.
// Man erbt diese Methoden einfach durch das Erweitern von JpaRepository.
public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
