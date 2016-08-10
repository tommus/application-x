package com.todev.appx.repositories;

import com.todev.appx.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowsRepository extends JpaRepository<Show, Long> {
  // Use default CRUD methods.
}
