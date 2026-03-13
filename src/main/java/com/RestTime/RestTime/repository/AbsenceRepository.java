package com.RestTime.RestTime.repository;

import com.RestTime.RestTime.model.entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
}