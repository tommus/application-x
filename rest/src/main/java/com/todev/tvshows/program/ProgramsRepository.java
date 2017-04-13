package com.todev.tvshows.program;

import java.util.Collection;
import java.util.UUID;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface ProgramsRepository extends JpaRepository<Program, UUID> {

  // TODO: Make this query filter ongoing programs.

  String FILTER_ONGOING = "SELECT p FROM Program p WHERE ?1 < p.startAt";

  @Query(FILTER_ONGOING)
  Collection<Program> findOngoing(DateTime timestmap);
}
