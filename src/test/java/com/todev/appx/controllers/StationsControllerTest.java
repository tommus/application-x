package com.todev.appx.controllers;

import com.google.common.collect.Lists;
import com.todev.appx.controllers.StationsController.DeleteProgramScheduleBody;
import com.todev.appx.controllers.StationsController.ProgramScheduleBody;
import com.todev.appx.controllers.StationsController.UpdateProgramScheduleBody;
import com.todev.appx.models.Program;
import com.todev.appx.models.Show;
import com.todev.appx.models.Station;
import com.todev.appx.repositories.ProgramsRepository;
import com.todev.appx.repositories.ShowsRepository;
import com.todev.appx.repositories.StationsRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */

public class StationsControllerTest extends JsonControllerTest {
    private static final int MIN_RANGE = 600;

    private Random random = new Random(System.currentTimeMillis());
    private List<Show> shows;
    private List<Station> stations;

    @Autowired
    private StationsRepository stationsRepository;

    @Autowired
    private ShowsRepository showsRepository;

    @Autowired
    private ProgramsRepository programsRepository;

    @Before
    public void setup() throws Exception {
        super.setup();

        stationsRepository.deleteAllInBatch();
        showsRepository.deleteAllInBatch();
        programsRepository.deleteAllInBatch();

        shows = new ArrayList<>();
        stations = new ArrayList<>();

        Arrays.asList("TVP1,TVP2,TVP3,Polsat".split(",")).forEach(
            s -> {
                final Station station = stationsRepository.save(new Station(s));
                stations.add(station);
            }
        );

        Arrays.asList("One,Two,Three,Four,Five,Six".split(",")).forEach(
            s -> {
                final Show show = showsRepository.save(new Show(s, "Brief.", 60));
                shows.add(show);
            }
        );

        for (Station station : stations) {
            for (Show show : shows) {
                final int diff = random.nextInt(MIN_RANGE);
                final DateTime time = DateTime.now().plusMinutes(diff);
                final Program program = programsRepository.save(new Program(station, show, time));
                station.addProgram(program);
            }
        }
    }

    /**
     * Tests whether a HTTP 415 status code will be returned if wrong content type is provided.
     *
     * @throws Exception
     */
    @Test
    public void testWrongContentType() throws Exception {
        mockMvc
            .perform(
                get("/stations")
                    .accept(unsupportedContent)
                    .contentType(unsupportedContent))
            .andExpect(status().isUnsupportedMediaType());
    }


    /**
     * Tests whether correct number of JSON objects has been retrieved.
     *
     * @throws Exception
     */
    @Test
    public void testValidListSize() throws Exception {
        mockMvc
            .perform(
                get("/stations")
                    .accept(jsonContent)
                    .contentType(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$", hasSize(stations.size())));
    }

    /**
     * Tests whether retrieved list of stations equals expected collection.
     *
     * @throws Exception
     */
    @Test
    public void testValidItemsRetrieved() throws Exception {
        final List<String> names = Lists.transform(stations, input -> input.getName());

        mockMvc
            .perform(
                get("/stations")
                    .accept(jsonContent)
                    .contentType(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$..name", is(names)));
    }

    /**
     * Tests whether view's details are available.
     *
     * @throws Exception
     */
    @Test
    public void testViewFields() throws Exception {
        for (Station station : stations) {
            mockMvc
                .perform(
                    get(String.format("/stations/%d", station.getId()))
                        .accept(jsonContent)
                        .contentType(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonContent))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.schedule").exists())
                .andExpect(jsonPath("$.schedule[*].name").exists())
                .andExpect(jsonPath("$.schedule[*].brief").exists())
                .andExpect(jsonPath("$.schedule[*].start_time").exists())
                .andExpect(jsonPath("$.schedule[*].id").doesNotExist())
                .andExpect(jsonPath("$.schedule[*].time_passed").doesNotExist())
                .andExpect(jsonPath("$.schedule[*].time_left").doesNotExist())
                .andExpect(jsonPath("$.schedule[*].duration").doesNotExist());
        }
    }

    /**
     * Tests whether program details were returned as expected.
     *
     * @throws Exception
     */
    @Test
    public void testDetailsCorrect() throws Exception {
        for (Station station : stations) {
            mockMvc
                .perform(
                    get(String.format("/stations/%d", station.getId()))
                        .accept(jsonContent)
                        .contentType(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonContent))
                .andExpect(jsonPath("$.id", is((int) station.getId())))
                .andExpect(jsonPath("$.name", is(station.getName())))
                .andExpect(jsonPath("$.schedule", hasSize(station.getPrograms().size())));
        }
    }

    /**
     * Tests whether deletion of not existing program results returning HTTP 404 NOT FOUND.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteNonExisting() throws Exception {
        final Station station = stations.get(0);
        final DeleteProgramScheduleBody body = new DeleteProgramScheduleBody();
        body.setCurrentTime(10);

        mockMvc
            .perform(
                delete(String.format("/stations/%d/schedule", station.getId()))
                    .accept(jsonContent)
                    .contentType(jsonContent)
                    .content(serialize(body)))
            .andExpect(status().isNotFound());
    }

    /**
     * Tests whether removing a {@link Program} from schedule removes item and returns HTTP 204 NO CONTENT.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteExisting() throws Exception {
        final Station station = stations.get(0);
        final Program program = station.getPrograms().iterator().next();
        final DeleteProgramScheduleBody body = new DeleteProgramScheduleBody();
        body.setCurrentTime(program.getStartAt().getMillis());

        mockMvc
            .perform(
                delete(String.format("/stations/%d/schedule", station.getId()))
                    .accept(jsonContent)
                    .contentType(jsonContent)
                    .content(serialize(body)))
            .andExpect(status().isNoContent());

        mockMvc
            .perform(
                get(String.format("/stations/%d", station.getId()))
                    .accept(jsonContent)
                    .contentType(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$.schedule", hasSize(station.getPrograms().size() - 1)));
    }

    /**
     * Tests whether adding a {@link Program} returns HTTP 201 CREATED alongside with created object details.
     *
     * @throws Exception
     */
    @Test
    public void testAddItem() throws Exception {
        final Station station = stations.get(0);
        final Show show = shows.get(0);
        final DateTime date = DateTime.now();
        final ProgramScheduleBody body = new ProgramScheduleBody();
        body.setShowId(show.getId());
        body.setStartAt(date.getMillis());

        mockMvc
            .perform(
                post(String.format("/stations/%d/schedule", station.getId()))
                    .accept(jsonContent)
                    .contentType(jsonContent)
                    .content(serialize(body)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$.name", is(show.getName())))
            .andExpect(jsonPath("$.brief", is(show.getBrief())))
            .andExpect(jsonPath("$.start_time", is(date.getMillis())))
            .andExpect(jsonPath("$.duration", is(show.getDuration())))
            .andExpect(jsonPath("$.time_left", is(0)))
            .andExpect(jsonPath("$.time_passed", is(0)));
    }

    /**
     * Tests whether ommiting one of required parameters results with returning HTTP 400 BAD REQUEST.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateProgramRequiredParams() throws Exception {
        final Station station = stations.get(0);
        final UpdateProgramScheduleBody body = new UpdateProgramScheduleBody();

        mockMvc
            .perform(
                patch(String.format("/stations/%d/schedule", station.getId()))
                    .accept(jsonContent)
                    .contentType(jsonContent)
                    .content(serialize(body)))
            .andExpect(status().isBadRequest());
    }

    /**
     * Tests whether trying to update not existing {@link Program} returns HTTP 404 NOT FOUND.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateProgramNotExistingProgram() throws Exception {
        final Station station = stations.get(0);
        final DateTime date = DateTime.now();
        final UpdateProgramScheduleBody body = new UpdateProgramScheduleBody();
        body.setCurrentTime(10);
        body.setNewTime(date.getMillis());

        mockMvc
            .perform(
                patch(String.format("/stations/%d/schedule", station.getId()))
                    .accept(jsonContent)
                    .contentType(jsonContent)
                    .content(serialize(body)))
            .andExpect(status().isNotFound());
    }

    /**
     * Tests whether trying to update program for not existing {@link Station} returns HTTP 404 NOT FOUND.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateProgramNotExistingStation() throws Exception {
        final UpdateProgramScheduleBody body = new UpdateProgramScheduleBody();

        mockMvc
            .perform(
                patch(String.format("/stations/%d/schedule", -1))
                    .accept(jsonContent)
                    .contentType(jsonContent)
                    .content(serialize(body)))
            .andExpect(status().isNotFound());
    }

    /**
     * Tests whether updating a {@link Program} returns HTTP 200 OK alongside with updated object details.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateItem() throws Exception {
        final Station station = stations.get(0);
        final Program program = station.getPrograms().iterator().next();
        final DateTime time = program.getStartAt();
        final DateTime newTime = time.plusMinutes(30);
        final UpdateProgramScheduleBody body = new UpdateProgramScheduleBody();
        body.setCurrentTime(time.getMillis());
        body.setNewTime(newTime.getMillis());

        mockMvc
            .perform(
                patch(String.format("/stations/%d/schedule", station.getId()))
                    .accept(jsonContent)
                    .contentType(jsonContent)
                    .content(serialize(body)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$.name", is(program.getName())))
            .andExpect(jsonPath("$.brief", is(program.getBrief())))
            .andExpect(jsonPath("$.start_time", is(newTime.getMillis())))
            .andExpect(jsonPath("$.duration", is(program.getDuration())))
            .andExpect(jsonPath("$.time_left", is(0)))
            .andExpect(jsonPath("$.time_passed", is(0)));
    }
}
