package com.todev.appx.repositories;

import com.todev.appx.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationsRepository extends JpaRepository<Station, Long> {
  // Use default CRUD methods.
}
