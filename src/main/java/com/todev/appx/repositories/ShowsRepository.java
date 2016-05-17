package com.todev.appx.repositories;

import com.todev.appx.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Tomasz Dzieniak on 14.05.16.
 */
public interface ShowsRepository extends JpaRepository<Show, Long> {
    // Use default CRUD methods.
}
