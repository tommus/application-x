package com.todev.appx.controllers;

import com.google.common.collect.Lists;
import com.todev.appx.models.Program;
import com.todev.appx.models.Station;
import com.todev.appx.repositories.ProgramRepository;
import com.todev.appx.repositories.StationRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */

public class StationControllerTest extends JsonControllerTest {
    private static final int MIN_RANGE = 600;

    private HttpMessageConverter httpMessageConverter;
    private Random random = new Random(System.currentTimeMillis());
    private List<Station> stations;
    private List<Program> programs;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        httpMessageConverter = Arrays.asList(converters).stream().filter(
            c -> c instanceof MappingJackson2HttpMessageConverter).findAny().get();
    }

    @Before
    public void setup() throws Exception {
        super.setup();

        stationRepository.deleteAllInBatch();

        stations = new ArrayList<>();
        programs = new ArrayList<>();

        Arrays.asList("TVP1,TVP2,TVP3,Polsat".split(",")).forEach(
            s -> {
                final Station station = stationRepository.save(new Station(s));
                stations.add(station);
            }
        );

        Arrays.asList("One,Two,Three,Four,Five,Six".split(",")).forEach(
            p -> {
                final int diff = random.nextInt(MIN_RANGE);
                for (Station station : stations) {
                    final Program program = programRepository.save(new Program(station, 60, DateTime.now().plusMinutes(diff), "Brief.", p));
                    station.addProgram(program);
                    programs.add(program);
                }
                programRepository.flush();
                stationRepository.flush();
            }
        );
    }

    /**
     * Tests whether correct number of JSON objects has been retrieved.
     *
     * @throws Exception
     */
    @Test
    public void testValidListSize() throws Exception {
        mockMvc
            .perform(get("/stations.json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$", hasSize(4)));
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
            .perform(get("/stations.json"))
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
                .perform(get(String.format("/stations/%d.json", station.getId())))
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
                .perform(get(String.format("/stations/%d.json", station.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonContent))
                .andExpect(jsonPath("$.id", is((int) station.getId())))
                .andExpect(jsonPath("$.name", is(station.getName())))
                .andExpect(jsonPath("$.schedule", hasSize(station.getPrograms().size())));
        }
    }

    /**
     * Tests whether HTTP 404 NOT FOUND will be returned while trying to delete non-existing program.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteNonExisting() throws Exception {
        final Station station = stations.get(0);

        mockMvc
            .perform(delete(String.format("/stations/%d/schedule/0", station.getId())))
            .andExpect(status().isNotFound());
    }

    /**
     * Tests whether removing a {@link Program} from schedule exists.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteExisting() throws Exception {
        final Station station = stations.get(0);
        final Program program = station.getPrograms().iterator().next();

        mockMvc
            .perform(delete(String.format("/stations/%d/schedule/%d", station.getId(), program.getStartAt().getMillis())))
            .andExpect(status().isNoContent());

        mockMvc
            .perform(get(String.format("/stations/%d.json", station.getId())))
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
        final DateTime date = DateTime.now();

        mockMvc
            .perform(
                post(String.format("/stations/%d/schedule.json", station.getId()))
                    .content(serialize(new Program(station, 60, date, "Brief.", "Program")))
                    .contentType(jsonContent))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$.name", is("Program")))
            .andExpect(jsonPath("$.brief", is("Brief.")))
            .andExpect(jsonPath("$.start_time", is(date.getMillis())))
            .andExpect(jsonPath("$.duration", is(60)))
            .andExpect(jsonPath("$.time_left", is(0)))
            .andExpect(jsonPath("$.time_passed", is(0)));
    }

    /**
     * Tests whether trying to update not existing {@link Program} returns HTTP 404 NOT FOUND.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateProgramNotExistingProgram() throws Exception {
        final Station station = stations.get(0);
        final Program program = station.getPrograms().iterator().next();

        mockMvc
            .perform(put(String.format("/stations/%d/schedule/0.json", station.getId()))
                .content(serialize(program))
                .contentType(jsonContent))
            .andExpect(status().isNotFound());
    }

    /**
     * Tests whether trying to update program for not existing {@link Station} returns HTTP 404 NOT FOUND.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateProgramNotExistingStation() throws Exception {
        final Station station = stations.get(0);
        final Program program = station.getPrograms().iterator().next();

        mockMvc
            .perform(put(String.format("/stations/-1/schedule/%d.json", program.getStartAt().getMillis()))
                .content(serialize(program))
                .contentType(jsonContent))
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
        final long time = program.getStartAt().getMillis();
        final int duration = program.getDuration() + 20;

        mockMvc
            .perform(
                put(String.format("/stations/%d/schedule/%d.json", station.getId(), time))
                    .content(serialize(new Program(
                        station, duration, program.getStartAt(), program.getBrief(), program.getName())))
                    .contentType(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$.name", is(program.getName())))
            .andExpect(jsonPath("$.brief", is(program.getBrief())))
            .andExpect(jsonPath("$.start_time", is(program.getStartAt().getMillis())))
            .andExpect(jsonPath("$.duration", is(duration)))
            .andExpect(jsonPath("$.time_left", is(0)))
            .andExpect(jsonPath("$.time_passed", is(0)));
    }

    /**
     * Serializes given object as JSON-formatted string.
     *
     * @param o an object that should be serialized.
     * @return serialized JSON string.
     * @throws IOException an error that will be raised while trying to serialize malformed object.
     */
    private String serialize(Object o) throws IOException {
        MockHttpOutputMessage mockMessage = new MockHttpOutputMessage();
        httpMessageConverter.write(o, jsonContent, mockMessage);
        return mockMessage.getBodyAsString();
    }
}
