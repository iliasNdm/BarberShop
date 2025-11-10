package org.example.barber_shop.dao.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewstellerSubscription {
    @Id
    private Long id;
    private String name;
    private Date subscriptionDate;
}
