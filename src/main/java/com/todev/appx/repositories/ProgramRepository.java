package com.todev.appx.repositories;

import com.todev.appx.models.Program;
import com.todev.appx.models.Station;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
public interface ProgramRepository extends JpaRepository<Program, Long> {
    /**
     * Query method that searches for {@link Program} at given credentials.
     *
     * @param stationId searches for {@link Program} at given {@link Station} id.
     * @param startAt   searches for {@link Program} that starts at given time.
     * @return founded {@link Program}.
     */
    Program findFirstByStationIdAndStartAt(long stationId, DateTime startAt);
}
