package org.example.barber_shop.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // le client qui a pris le rendez-vous (un user avec le role CLIENT)
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @ToString.Exclude
    private User client;

    // Le barber qui va exécuter le rendez-vous
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_id", nullable = false)
    @ToString.Exclude
    private Barber barber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // PENDING, CONFIRMED, CANCELLED, DONE

    private LocalDateTime createdAt;
    private String customerNotes;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = AppointmentStatus.PENDING;
    }
    //private String googleCalendarEventId;
}
