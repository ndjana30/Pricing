package com.gestion.spa.repositories;

import com.gestion.spa.models.Facturation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturationRepository extends JpaRepository<Facturation,Long> {
}
