package com.RestTime.RestTime.repository;

import com.RestTime.RestTime.model.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByNom(String nom);
}