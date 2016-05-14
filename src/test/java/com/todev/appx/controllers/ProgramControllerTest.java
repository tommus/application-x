package com.todev.appx.controllers;

import com.todev.appx.models.Program;
import com.todev.appx.repositories.ProgramRepository;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
public class ProgramControllerTest extends JsonControllerTest {
    private static final int HRS_BUFFER = 5;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime evening = DateTime.now().plusHours(HRS_BUFFER);

    @Autowired
    private ProgramRepository programRepository;

    @Before
    public void setup() throws Exception {
        super.setup();

        programRepository.deleteAllInBatch();
        programRepository.save(new Program(null, 60, evening, "Brief description.", "Program name"));
    }

    /**
     * Tests whether an empty JSON will be returned if no ongoing programs will be found.
     *
     * @throws Exception
     */
    @Test
    public void testNoOngoingPrograms() throws Exception {
        final DateTime test = evening.minusMinutes(5);

        mockMvc
            .perform(get("/programs.json").param("time", String.valueOf(test.getMillis())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * Tests whether an endpoint returns program details when it fits 24H time range.
     *
     * @throws Exception
     */
    @Test
    public void testProgramInRange() throws Exception {
        final DateTime test = evening.plusMinutes(5);

        mockMvc
            .perform(get("/programs.json").param("time", String.valueOf(test.getMillis())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$", hasSize(1)));
    }

    /**
     * Tests whether correct fields are available / hidden for specific view.
     *
     * @throws Exception
     */
    @Test
    public void testViewFields() throws Exception {
        final DateTime test = evening.plusMinutes(5);

        mockMvc
            .perform(get("/programs.json").param("time", String.valueOf(test.getMillis())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").doesNotExist())
            .andExpect(jsonPath("$[0].name").exists())
            .andExpect(jsonPath("$[0].brief").exists())
            .andExpect(jsonPath("$[0].start_at").doesNotExist())
            .andExpect(jsonPath("$[0].duration").doesNotExist())
            .andExpect(jsonPath("$[0].time_passed").exists())
            .andExpect(jsonPath("$[0].time_left").exists());
    }

    /**
     * Tests whether `time_passed` is correctly calculated while returning program's detailed information.
     *
     * @throws Exception
     */
    @Test
    public void testValidTimePassedCalculation() throws Exception {
        for (int i = 0; i < 60; ++i) {
            final DateTime test = evening.plusMinutes(i);

            mockMvc
                .perform(get("/programs.json").param("time", String.valueOf(test.getMillis())))
                .andExpect(jsonPath("$[0].time_passed", is(i)));
        }
    }

    /**
     * Tests whether `time_left` is correctly calculated while returning program's detailed information.
     *
     * @throws Exception
     */
    @Test
    public void testValidTimeLeftCalculation() throws Exception {
        for (int i = 0; i < 60; ++i) {
            final DateTime test = evening.plusMinutes(i);

            mockMvc
                .perform(get("/programs.json").param("time", String.valueOf(test.getMillis())))
                .andExpect(jsonPath("$[0].time_left", is(60 - i)));
        }
    }

    /**
     * Tests whether an 4xx error will be returned while trying to retrieve an response without required `time`
     * parameter.
     *
     * @throws Exception
     */
    @Test
    public void testMissingTimeParam() throws Exception {
        mockMvc
            .perform(get("/programs.json"))
            .andExpect(status().is4xxClientError());
    }
}
