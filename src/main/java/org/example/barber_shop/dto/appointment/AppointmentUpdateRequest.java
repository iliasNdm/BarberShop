package org.example.barber_shop.dto.appointment;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.barber_shop.dao.entities.AppointmentStatus;

import java.time.LocalDateTime;
@Data
public class AppointmentUpdateRequest {

    @NotNull(message = "Appointment ID is required for update.")
    Long appointmentId;
    @Future(message = "New time must be in the future.")
    LocalDateTime dateTime;
    AppointmentStatus status; // allow client to cancel his appointment
}
