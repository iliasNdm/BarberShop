package org.example.barber_shop.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
public class NewstellerSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Temporal(TemporalType.TIMESTAMP)
    private Date subscriptionDate;

    //optionnel si l'utilisateur est inscrit
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    @PrePersist
    public void prePersist() {
        if (subscriptionDate == null) {
            subscriptionDate = new Date();
        }
    }
}
