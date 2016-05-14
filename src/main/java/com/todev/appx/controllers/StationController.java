package com.todev.appx.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.todev.appx.models.Program;
import com.todev.appx.models.Station;
import com.todev.appx.repositories.ProgramRepository;
import com.todev.appx.repositories.StationRepository;
import com.todev.appx.views.Views;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
@RestController
@RequestMapping("stations")
public class StationController {
    private static final int RANGE_HRS = 24;
    private final ProgramRepository programRepository;
    private final StationRepository stationRepository;

    /**
     * Predicate that allows to filter for ongoing programs at specific time interval.
     */
    private final Predicate<Program> filterCurrent = new Predicate<Program>() {
        private final DateTime start = DateTime.now();
        private final DateTime end = DateTime.now().plusHours(RANGE_HRS);
        private final Interval range = new Interval(start, end);

        @Override
        public boolean apply(Program input) {
            final int duration = input.getDuration();
            final DateTime programStart = input.getStartAt();
            final DateTime programEnd = input.getStartAt().plusMinutes(duration);
            return range.contains(programStart) || range.contains(programEnd);
        }
    };

    /**
     * Create a new StationController.
     *
     * @param stationRepository a related {@link StationRepository} repository.
     * @param programRepository a related {@link ProgramRepository} repository.
     */
    @Autowired
    public StationController(StationRepository stationRepository, ProgramRepository programRepository) {
        this.stationRepository = stationRepository;
        this.programRepository = programRepository;
    }

    /**
     * Handler method for a /stations endpoint.
     *
     * @return a collection of available TV stations.
     */
    @JsonView(Views.BasicStation.class)
    @RequestMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Station> getStations() {
        return stationRepository.findAll();
    }

    /**
     * Handler method for a /stations{id} endpoint.
     *
     * @param id an ID of {@link Station} which details should be retrieved.
     * @return an instance of a {@link Station} object.
     */
    @JsonView(Views.DetailStation.class)
    @RequestMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Station getStation(@PathVariable long id) {
        validateStation(id);

        final Station station = stationRepository.findOne(id);
        final Set<Program> programs = station.getPrograms();
        station.setPrograms(Sets.filter(programs, filterCurrent));
        return station;
    }

    /**
     * Handler method for a /{id}/schedule endpoint.
     *
     * @param id      an ID of {@link Station} to which schedule program should be added.
     * @param program detailed information about {@link Program} that should be added.
     * @return information about addition process with optional corresponding {@link Program} details.
     */
    @RequestMapping(value = "/{id}/schedule",
        method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> addProgram(@PathVariable long id, @RequestBody Program program) {
        validateStation(id);

        final Station station = stationRepository.findOne(id);
        final Program saved = programRepository.save(program);

        program.setStation(station);
        station.addProgram(program);

        stationRepository.flush();
        programRepository.flush();

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Handler method that allows to remove {@link Program} from {@link Station}'s schedule.
     *
     * @param stationId an ID of {@link Station} which {@link Program} should be removed from schedule.
     * @param startTime starting time of {@link Program} which should be removed from schedule.
     * @return information about removing process.
     */
    @RequestMapping(value = "/{station_id}/schedule/{start_time}",
        method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> deleteProgram(
        @PathVariable(value = "station_id") long stationId, @PathVariable(value = "start_time") long startTime) {
        validateStation(stationId);
        validateProgram(stationId, startTime);

        final Program program = programRepository.findFirstByStationIdAndStartAt(stationId, new DateTime(startTime));
        programRepository.delete(program.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Handler method that allows to update {@link Program} details.
     *
     * @param stationId an ID of {@link Station} which {@link Program} should be updated.
     * @param startTime starting time of {@link Program} which should be updated.
     * @param program   updated information about {@link Program}.
     * @return information about updating process.
     */
    @RequestMapping(value = "{station_id}/schedule/{start_time}",
        method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> updateProgram(
        @PathVariable(value = "station_id") long stationId, @PathVariable(value = "start_time") long startTime,
        @RequestBody Program program) {
        validateStation(stationId);
        validateProgram(stationId, startTime);

        final Station station = stationRepository.findOne(stationId);
        final Program saved = programRepository.findFirstByStationIdAndStartAt(stationId, new DateTime(startTime));

        saved.setName(program.getName());
        saved.setBrief(program.getBrief());
        saved.setStartAt(program.getStartAt());
        saved.setDuration(program.getDuration());
        saved.setStation(station);

        programRepository.flush();

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    /**
     * Method that validates existence of {@link Station} at given ID.
     * If {@link Station} not exists an {@link StationNotFoundException} will be raised.
     *
     * @param id an ID of {@link Station} that will be validated.
     */
    private void validateStation(long id) {
        if (!stationRepository.exists(id)) {
            throw new StationNotFoundException(id);
        }
    }

    /**
     * Method that validates existence of {@link Program} at given credentials.
     * If {@link Program} not exists an {@link ProgramNotFoundException} will be raised.
     *
     * @param stationId an ID of {@link Station} that will be validated.
     * @param startAt   starting time of {@link Program} that will be validated.
     */
    private void validateProgram(long stationId, long startAt) {
        if (programRepository.findFirstByStationIdAndStartAt(stationId, new DateTime(startAt)) == null) {
            throw new ProgramNotFoundException(stationId, startAt);
        }
    }

    /**
     * An exception that should be raised whenever {@link Program} not exist.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class ProgramNotFoundException extends RuntimeException {
        private static final String DEFAULT_MESSAGE_TEMPLATE =
            "Could not find program in station of ID: %d which starts at: %d.";

        ProgramNotFoundException(long stationId, long startAt) {
            super(String.format(DEFAULT_MESSAGE_TEMPLATE, stationId, startAt));
        }
    }

    /**
     * An exception thath should be raised whenever {@link Station} not exist.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class StationNotFoundException extends RuntimeException {
        private static final String DEFAULT_MESSAGE_TEMPLATE =
            "Could not find station with id: %d.";

        StationNotFoundException(long id) {
            super(String.format(DEFAULT_MESSAGE_TEMPLATE, id));
        }
    }
}
