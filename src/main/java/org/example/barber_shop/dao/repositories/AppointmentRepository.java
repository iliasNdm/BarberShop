package org.example.barber_shop.dao.repositories;

import org.example.barber_shop.dao.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByBarber_IdOrderByStartTimeAsc(Long barberId);
}
