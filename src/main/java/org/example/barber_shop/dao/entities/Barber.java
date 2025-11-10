package org.example.barber_shop.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString(exclude = "user")
@NoArgsConstructor
@AllArgsConstructor
public class Barber {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private double salary;
    private Date hireDate;

    public Barber(User user, double salary, Date hireDate) {
        this.user = user;
        this.salary = salary;
        this.hireDate = hireDate;
    }
}
