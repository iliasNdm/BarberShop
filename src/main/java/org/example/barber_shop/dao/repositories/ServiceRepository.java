package org.example.barber_shop.dao.repositories;

import org.example.barber_shop.dao.entities.Barber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Barber,Long> {
}
