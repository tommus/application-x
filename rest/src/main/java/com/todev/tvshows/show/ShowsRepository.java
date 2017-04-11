package com.todev.tvshows.show;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface ShowsRepository extends JpaRepository<Show, UUID> {
}
