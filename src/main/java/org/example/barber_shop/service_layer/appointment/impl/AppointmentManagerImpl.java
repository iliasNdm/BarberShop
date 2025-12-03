package org.example.barber_shop.service_layer.appointment.impl;
import org.example.barber_shop.dao.entities.*;
import org.example.barber_shop.dao.repositories.*;
import org.example.barber_shop.dto.appointment.AppointmentCreationRequest;
import org.example.barber_shop.dto.service.ServiceResponse;
import org.example.barber_shop.exception.ResourceNotFoundException;
import org.example.barber_shop.dto.appointment.AppointmentResponse;
import org.example.barber_shop.service_layer.appointment.AppointmentManager;
import org.example.barber_shop.service_layer.barber.BarberManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class AppointmentManagerImpl implements AppointmentManager {
    private final AppointmentRepository appointmentRepository;
    private final BarberManager barberManager;
    private final UserRepository userRepository = null;
    private final ServiceRepository serviceRepository = null;
    private final AppointmentServiceRepository appointmentServiceRepository = null;
    private final BarberRepository barberRepository;


    public AppointmentManagerImpl(AppointmentRepository appointmentRepository, BarberManager barberManager, BarberRepository barberRepository) {
        this.appointmentRepository = appointmentRepository;
        this.barberManager = barberManager;
        this.barberRepository = barberRepository;
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
    public AppointmentResponse bookAppointment(AppointmentCreationRequest request,
                                               Long userId) {

        // 1) Récupérer le client (User)
        User client = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found."));

        // 2) Récupérer le barber
        Barber barber = barberRepository.findById(request.getBarberId())
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found."));

        // 3) Récupérer les services choisis
        List<Service> services = serviceRepository.findAllById(request.getServiceIds());
        if (services.isEmpty()) {
            throw new ResourceNotFoundException("No valid services provided.");
        }

        // 4) Calculer la durée totale + endTime
        int totalDuration = services.stream()
                .mapToInt(Service::getDurationMinutes)
                .sum();

        LocalDateTime startTime = request.getDateTime();
        LocalDateTime endTime = startTime.plusMinutes(totalDuration);

        // 5) TODO: vérifier les conflits de créneau pour ce barber

        // 6) Créer l'Appointment
        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setBarber(barber);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setCreatedAt(LocalDateTime.now());

        Appointment savedAppointment = appointmentRepository.save(appointment);

        // 7) Créer les liens dans la table de jonction AppointmentService
        List<AppointmentService> appointmentServices = services.stream()
                .map(service -> {
                    AppointmentService join = new AppointmentService();
                    join.setAppointment(savedAppointment);
                    join.setService(service);
                    return join;
                })
                .collect(Collectors.toList());

        appointmentServiceRepository.saveAll(appointmentServices);
        savedAppointment.setServices(appointmentServices); // si relation bidirectionnelle

        // 8) Réutiliser ton mapper existant
        return getAppointmentDetails(savedAppointment.getId());
    }


    @Override
    @Transactional
    public  Boolean cancelAppointment(Long appointmentId) {}

}
