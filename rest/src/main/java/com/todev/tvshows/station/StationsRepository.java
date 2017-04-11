package com.todev.tvshows.station;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface StationsRepository extends JpaRepository<Station, UUID> {
}
