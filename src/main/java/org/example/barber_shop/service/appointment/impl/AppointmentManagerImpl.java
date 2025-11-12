package org.example.barber_shop.service.appointment.impl;
import org.example.barber_shop.dao.entities.AppointmentService;
import org.example.barber_shop.dao.repositories.AppointmentServiceRepository;
import org.example.barber_shop.dto.appointment.AppointmentCreationRequest;
import org.example.barber_shop.dto.service.ServiceResponse;
import org.example.barber_shop.exception.ResourceNotFoundException;
import org.springframework.boot.context.config.*;
import org.example.barber_shop.dao.entities.Appointment;
import org.example.barber_shop.dao.repositories.AppointmentRepository;
import org.example.barber_shop.dto.appointment.AppointmentResponse;
import org.example.barber_shop.service.appointment.AppointmentManager;
import org.example.barber_shop.service.barber.BarberManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentManagerImpl implements AppointmentManager {
    private final AppointmentRepository appointmentRepository;
    private final BarberManager barberManager;

    public AppointmentManagerImpl(AppointmentRepository appointmentRepository, BarberManager barberManager) {
        this.appointmentRepository = appointmentRepository;
        this.barberManager = barberManager;
    }

    @Override
    @Transactional
    public AppointmentResponse getAppointmentDetails(Long appointmentId) {

        Appointment appointmentEntity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found."));
        String barberName = appointmentEntity.getBarber().getUser().getFirstName();

        List<ServiceResponse> serviceResponses = appointmentEntity.getServices().stream()
                // Map AppointmentService (Join Entity) to Service (Core Entity)
                .map(AppointmentService::getService)

                .map(serviceEntity -> new ServiceResponse(
                        serviceEntity.getId(),
                        serviceEntity.getName(),
                        serviceEntity.getDurationMinutes(),
                        serviceEntity.getPrice()
                ))

                // Collect results into a List
                .collect(Collectors.toList());
        return new AppointmentResponse(
                appointmentEntity.getId(),
                appointmentEntity.getStartTime(),
                appointmentEntity.getStatus(),
                barberName,
                serviceResponses
        );
    }

    @Override
    @Transactional
    public AppointmentResponse bookAppointment(AppointmentCreationRequest appointmentCreationRequest,
                                               Long userId) {
        // keep going from here
    }
}
