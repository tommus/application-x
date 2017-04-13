package com.todev.tvshows.program;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface ProgramsRepository extends JpaRepository<Program, UUID> {

  // TODO: Make this query filter ongoing programs.

  String FILTER_ONGOING = "SELECT p FROM Program p WHERE p.startAt >= :timestamp";

  @Query(value = FILTER_ONGOING)
  Collection<Program> findOngoing(@Param("timestamp") LocalDateTime timestamp);
}
