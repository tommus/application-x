package com.todev.appx.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.todev.appx.models.Program;
import com.todev.appx.repositories.ProgramsRepository;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
@RestController
@RequestMapping("programs")
public class ProgramsController {
    private final ProgramsRepository programsRepository;

    /**
     * Create a new {@link ProgramsController}.
     *
     * @param programsRepository a related {@link ProgramsRepository} repository.
     */
    @Autowired
    public ProgramsController(ProgramsRepository programsRepository) {
        this.programsRepository = programsRepository;
    }

    /**
     * Retrieves {@link Program}s ongoing at given time.
     *
     * @param time a point in time that should be used to retrieve ongoing programs.
     * @return a collection of ongoing {@link Program}s.
     */
    @JsonView(Program.View.Details.class)
    @RequestMapping(value = "", method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Program> readPrograms(
        @RequestParam(value = "time") long time) {
        final List<Program> programs = programsRepository.findAll();
        filterOngoing(programs, new DateTime(time));
        return programs;
    }

    /**
     * Filters a collection of {@link Program}s. Leaves only those are ongoing at given time.
     *
     * @param programs a collection of programs that should be filtered.
     * @param time     a point in time that should be used to filter programs.
     */
    private void filterOngoing(List<Program> programs, DateTime time) {
        final Iterator<Program> iterator = programs.iterator();

        while (iterator.hasNext()) {
            final Program program = iterator.next();
            final int duration = program.getDuration();
            final DateTime startsAt = program.getStartAt();
            final DateTime endsAt = startsAt.plusMinutes(duration);

            if (time.getMillis() < startsAt.getMillis() || time.getMillis() >= endsAt.getMillis()) {
                iterator.remove();
            } else {
                final long sinceStart = Minutes.minutesBetween(startsAt, time).getMinutes();
                final long tillEnd = Minutes.minutesBetween(time, endsAt).getMinutes();
                program.setSinceStart(sinceStart);
                program.setTillEnd(tillEnd);
            }
        }
    }
}
