package org.example.barber_shop.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AppointmentCreationRequest {
    @NotNull(message = "Select a barber")
    Long barberId;

    @Future(message = "Select a valid time")
    LocalDateTime dateTime;
    Long serviceId;
}
