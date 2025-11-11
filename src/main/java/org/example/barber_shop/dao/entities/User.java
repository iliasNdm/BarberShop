package org.example.barber_shop.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    // Un user en tant que CLIENT peut avoir plusieurs rendez-vous
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Appointment> appointmentsAsClient;

    public boolean isBarber() {
        return this.role == UserRole.BARBER;
    }

}

