package com.RestTime.RestTime.repository;

import com.RestTime.RestTime.model.entity.DemandeConge;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DemandeCongeRepository extends JpaRepository<DemandeConge, Long> {
    List<DemandeConge> findByUser(User user);
    List<DemandeConge> findByStatut(StatutDemande statut);
}