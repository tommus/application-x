package com.todev.appx.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.todev.appx.models.Program;
import com.todev.appx.models.Show;
import com.todev.appx.models.Station;
import com.todev.appx.repositories.ProgramsRepository;
import com.todev.appx.repositories.ShowsRepository;
import com.todev.appx.repositories.StationsRepository;
import org.apache.log4j.Logger;
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
public class StationsController {
    private static final Logger LOGGER = Logger.getLogger(StationsController.class);
    private static final int RANGE_HRS = 24;
    private final ProgramsRepository programsRepository;
    private final ShowsRepository showsRepository;
    private final StationsRepository stationsRepository;

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
     * Create a new {@link StationsController}.
     *
     * @param stationsRepository a related {@link StationsRepository} repository.
     * @param showsRepository    a related {@link ShowsRepository} repository.
     * @param programsRepository a related {@link ProgramsRepository} repository.
     */
    @Autowired
    public StationsController(StationsRepository stationsRepository, ShowsRepository showsRepository,
                              ProgramsRepository programsRepository) {
        this.stationsRepository = stationsRepository;
        this.showsRepository = showsRepository;
        this.programsRepository = programsRepository;
    }

    /**
     * Retrieves all {@link Station} items.
     *
     * @return a collection of {@link Station} items.
     */
    @JsonView(Station.View.Basic.class)
    @RequestMapping(value = "", method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Station> readStations() {
        return stationsRepository.findAll();
    }

    /**
     * Retrieves a {@link Station} item with given ID.
     *
     * @param id an ID of {@link Station} item that will be retrieved.
     * @return information about retrieval process with corresponding item's details.
     */
    @JsonView(Station.View.Details.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Station getStation(@PathVariable long id) {
        validateStation(id);

        final Station station = stationsRepository.findOne(id);
        final Set<Program> programs = station.getPrograms();

        station.setPrograms(Sets.filter(programs, filterCurrent));

        return station;
    }

    /**
     * Schedules new {@link Program} at given credentials in {@link Station} at given ID.
     *
     * @param id   an ID of {@link Station} in which {@link Program} will be scheduled.
     * @param body details about scheduled {@link Program}.
     * @return information about scheduling process with corresponding item's details.
     */
    @RequestMapping(value = "{id}/schedule", method = RequestMethod.POST,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> scheduleProgram(@PathVariable long id, @RequestBody ProgramScheduleBody body) {
        validateStation(id);

        if (body.getShowId() == 0 || body.getStartAt() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // TODO: Test whether program fits in free gap.

        final Station station = stationsRepository.findOne(id);
        final Show show = showsRepository.findOne(body.getShowId());
        final Program program = programsRepository.save(new Program(station, show, new DateTime(body.getStartAt())));

        return new ResponseEntity<>(program, HttpStatus.CREATED);
    }

    /**
     * Updates {@link Program}'s schedule for {@link Station} at given ID.
     *
     * @param id   an ID of {@link Station} which's {@link Program} should be re-schedule.
     * @param body details about re-scheduled {@link Program}.
     * @return information about re-scheduling process with corresponding item's details.
     */
    @RequestMapping(value = "{id}/schedule", method = RequestMethod.PATCH,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> rescheduleProgram(@PathVariable long id, @RequestBody UpdateProgramScheduleBody body) {
        validateStation(id);

        if (body.getCurrentTime() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (body.getShowId() == 0 && body.getNewTime() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // TODO: Test whether program fits in free gap.

        final Program program = programsRepository.findFirstByStationIdAndStartAt(
            id, new DateTime(body.getCurrentTime()));

        if (program == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (body.getShowId() != 0) {
            final Show show = showsRepository.findOne(body.getShowId());
            program.setShow(show);
        }

        if (body.getNewTime() != 0) {
            final DateTime startAt = new DateTime(body.getNewTime());
            program.setStartAt(startAt);
        }

        programsRepository.flush();

        return new ResponseEntity<>(program, HttpStatus.OK);
    }

    /**
     * Unschedules {@link Program} at given credentials from {@link Station} at given ID.
     *
     * @param id   an ID of {@link Station} which's {@link Program} should be unscheduled.
     * @param body details about unscheduled {@link Program}.
     * @return information about unscheduling process.
     */
    @RequestMapping(value = "{id}/schedule", method = RequestMethod.DELETE,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> unscheduleProgram(@PathVariable long id, @RequestBody DeleteProgramScheduleBody body) {
        validateStation(id);

        if (body.getCurrentTime() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Program program = programsRepository.findFirstByStationIdAndStartAt(
            id, new DateTime(body.getCurrentTime()));

        if (program == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        programsRepository.delete(program.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Method that validates existence of {@link Station} at given ID.
     * If {@link Station} not exists an {@link StationNotFoundException} will be raised.
     *
     * @param id an ID of {@link Station} that will be validated.
     */
    private void validateStation(long id) {
        if (!stationsRepository.exists(id)) {
            throw new StationNotFoundException(id);
        }
    }

    /**
     * An exception that should be raised whenever queried {@link Station} not exist.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class StationNotFoundException extends RuntimeException {
        private static final String DEFAULT_MESSAGE_TEMPLATE =
            "Could not find station with id: %d.";

        StationNotFoundException(long id) {
            super(String.format(DEFAULT_MESSAGE_TEMPLATE, id));
        }
    }

    /**
     * POJO that encapsulates scheduling request body.
     *
     * @see StationsController#scheduleProgram(long, ProgramScheduleBody)
     */
    public static class ProgramScheduleBody {
        @JsonProperty(value = "show_id")
        private long showId;

        @JsonProperty(value = "start_time")
        private long startAt;

        /**
         * Creates new {@link ProgramScheduleBody} item.
         */
        public ProgramScheduleBody() {
            // Intentionally left blank.
        }

        public long getShowId() {
            return showId;
        }

        public void setShowId(long showId) {
            this.showId = showId;
        }

        public long getStartAt() {
            return startAt;
        }

        public void setStartAt(long startAt) {
            this.startAt = startAt;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(
                getShowId(),
                getStartAt()
            );
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj == this) {
                return true;
            }

            if (!(obj instanceof ProgramScheduleBody)) {
                return false;
            }

            final ProgramScheduleBody other = (ProgramScheduleBody) obj;

            return Objects.equal(getShowId(), other.getShowId())
                && Objects.equal(getStartAt(), other.getStartAt());
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(ProgramScheduleBody.class)
                .add("show_id", getShowId())
                .add("start_at", getStartAt())
                .toString();
        }
    }

    /**
     * POJO that encapsulates re-scheduling request body.
     *
     * @see StationsController#rescheduleProgram(long, UpdateProgramScheduleBody)
     */
    public static class UpdateProgramScheduleBody {
        @JsonProperty(value = "current_time")
        private long currentTime;

        @JsonProperty(value = "show_id")
        private long showId;

        @JsonProperty(value = "new_time")
        private long newTime;

        /**
         * Creates new {@link UpdateProgramScheduleBody} item.
         */
        public UpdateProgramScheduleBody() {
            // Intentionally left blank.
        }

        public long getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(long currentTime) {
            this.currentTime = currentTime;
        }

        public long getShowId() {
            return showId;
        }

        public void setShowId(long showId) {
            this.showId = showId;
        }

        public long getNewTime() {
            return newTime;
        }

        public void setNewTime(long newTime) {
            this.newTime = newTime;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(
                getCurrentTime(),
                getShowId(),
                getNewTime()
            );
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj == this) {
                return true;
            }

            if (!(obj instanceof UpdateProgramScheduleBody)) {
                return false;
            }

            final UpdateProgramScheduleBody other = (UpdateProgramScheduleBody) obj;

            return Objects.equal(getCurrentTime(), other.getCurrentTime())
                && Objects.equal(getShowId(), other.getShowId())
                && Objects.equal(getNewTime(), other.getNewTime());
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(UpdateProgramScheduleBody.class)
                .add("current_time", getCurrentTime())
                .add("show_id", getShowId())
                .add("new_time", getNewTime())
                .toString();
        }
    }

    /**
     * POJO that encapsulates unscheduling request body.
     *
     * @see StationsController#unscheduleProgram(long, DeleteProgramScheduleBody)
     */
    public static class DeleteProgramScheduleBody {
        @JsonProperty("current_time")
        private long currentTime;

        /**
         * Creates new {@link DeleteProgramScheduleBody} item.
         */
        public DeleteProgramScheduleBody() {
            // Intentionally left blank.
        }

        public long getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(long currentTime) {
            this.currentTime = currentTime;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(
                getCurrentTime()
            );
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj == this) {
                return true;
            }

            if (!(obj instanceof DeleteProgramScheduleBody)) {
                return false;
            }

            final DeleteProgramScheduleBody other = (DeleteProgramScheduleBody) obj;

            return Objects.equal(getCurrentTime(), other.getCurrentTime());
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(DeleteProgramScheduleBody.class)
                .add("current_time", getCurrentTime())
                .toString();
        }
    }
}
