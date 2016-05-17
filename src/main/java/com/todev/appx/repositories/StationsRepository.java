package com.todev.appx.repositories;

import com.todev.appx.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
public interface StationsRepository extends JpaRepository<Station, Long> {
    // Use default CRUD methods.
}
