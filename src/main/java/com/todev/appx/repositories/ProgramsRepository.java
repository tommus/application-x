package com.todev.appx.repositories;

import com.todev.appx.models.Program;
import com.todev.appx.models.Station;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProgramsRepository extends JpaRepository<Program, Long> {
  String FILTER_ONGOING =
      "SELECT p FROM Program p WHERE ?1 BETWEEN p.startAt AND DATEADD(minute, p.show.duration, p.startAt)";

  /**
   * Query method that searches for {@link Program} at given credentials.
   *
   * @param stationId searches for {@link Program} at given {@link Station} id.
   * @param startAt searches for {@link Program} that starts at given time.
   * @return founded {@link Program}.
   */
  Program findFirstByStationIdAndStartAt(long stationId, DateTime startAt);

  /**
   * Query method that searches for ongoing {@link Program}s related to given {@literal time}.
   *
   * @param time a point in time related to which ongoing programs should be retrieved.
   * @return a collection of ongoing {@link Program}s.
   */
  @Query(FILTER_ONGOING)
  List<Program> findByOngoing(DateTime time);
}
