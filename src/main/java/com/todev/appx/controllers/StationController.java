package com.todev.appx.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.todev.appx.models.Program;
import com.todev.appx.models.Station;
import com.todev.appx.repositories.StationRepository;
import com.todev.appx.views.Views;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
@RestController
@RequestMapping("stations")
public class StationController {
    private static final int RANGE_HRS = 24;
    private final StationRepository stationRepository;
    private final Predicate<Program> filterCurrent = new Predicate<Program>() {
        private final DateTime start = DateTime.now();
        private final DateTime end = DateTime.now().plusHours(RANGE_HRS);
        private final Interval range = new Interval(start, end);

        @Override
        public boolean apply(Program input) {
            return range.contains(input.getStartAt());
        }
    };

    /**
     * Create a new StationController.
     * @param stationRepository a related {@link StationRepository} repository.
     */
    @Autowired
    public StationController(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    /**
     * Handler method for a /stations endpoint.
     * @return a collection of available TV stations.
     */
    @JsonView(Views.BasicStation.class)
    @RequestMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Station> getStationsJson() {
        return stationRepository.findAll();
    }

    /**
     * Handler method for a /stations{id} endpoint.
     * @param id an ID of {@link Station} which details should be retrieved.
     * @return an instance of a {@link Station} object.
     */
    @JsonView(Views.DetailStation.class)
    @RequestMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Station getStationJson(@PathVariable long id) {
        final Station station = stationRepository.findById(id);
        final Set<Program> programs = station.getPrograms();
        station.setPrograms(Sets.filter(programs, filterCurrent));
        return station;
    }
}
