package at.spengergasse.spring_thymeleaf.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByDeviceId(int deviceId);

    List<Reservation> findByDeviceIdAndStartTimeBeforeAndEndTimeAfter(
            int deviceId, LocalDateTime endTime, LocalDateTime startTime);

    List<Reservation> findByPatientIdAndStartTimeBeforeAndEndTimeAfter(
            int patientId, LocalDateTime endTime, LocalDateTime startTime);
}
