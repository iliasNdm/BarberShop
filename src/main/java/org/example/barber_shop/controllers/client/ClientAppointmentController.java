package org.example.barber_shop.controllers.client;

import jakarta.validation.Valid;
import org.example.barber_shop.dao.entities.User;
import org.example.barber_shop.dao.repositories.UserRepository;
import org.example.barber_shop.dto.appointment.AppointmentCreationRequest;
import org.example.barber_shop.dto.appointment.AppointmentResponse;
import org.example.barber_shop.dto.appointment.AppointmentUpdateRequest;
import org.example.barber_shop.exception.ResourceNotFoundException;
import org.example.barber_shop.service_layer.appointment.AppointmentManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/client/appointments")
public class ClientAppointmentController {

    private final AppointmentManager appointmentManager;
    private final UserRepository userRepository;

    public ClientAppointmentController(AppointmentManager appointmentManager,
                                       UserRepository userRepository) {
        this.appointmentManager = appointmentManager;
        this.userRepository = userRepository;
    }


    @PostMapping
    public ResponseEntity<?> bookAppointment(
            @Valid @RequestBody AppointmentCreationRequest request,
            Authentication authentication
    ) {
        try {

            String email = authentication.getName();

            User client = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found."));

            Long userId = client.getId();

            AppointmentResponse response = appointmentManager.bookAppointment(request, userId);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException ex) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "NOT_FOUND",
                            "message", ex.getMessage()
                    ));

        } catch (IllegalStateException ex) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "BAD_REQUEST",
                            "message", ex.getMessage()
                    ));

        } catch (Exception ex) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "INTERNAL_ERROR",
                            "message", ex.getMessage()
                    ));
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentManager.getAppointmentDetails(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        Boolean cancelled = appointmentManager.cancelAppointment(id);
        return cancelled ? ResponseEntity.noContent().build()
                : ResponseEntity.badRequest().build();
    }


    @PatchMapping("/{id}/time")
    public ResponseEntity<AppointmentResponse> updateTime(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentUpdateRequest request
    ) {
        LocalDateTime newDateTime = request.getNewDateTime();
        AppointmentResponse response =
                appointmentManager.updateAppointmentTime(id, newDateTime);
        return ResponseEntity.ok(response);
    }
}
