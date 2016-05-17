package com.todev.appx.controllers;

import com.google.common.collect.Lists;
import com.todev.appx.models.Show;
import com.todev.appx.repositories.ShowsRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * Created by Tomasz Dzieniak on 17.05.16.
 */
public class ShowsControllerTest extends JsonControllerTest {
    private List<Show> shows;

    @Autowired
    private ShowsRepository showsRepository;

    @Override
    public void setup() throws Exception {
        super.setup();

        showsRepository.deleteAllInBatch();

        shows = new ArrayList<>();

        Arrays.asList("One,Two,Three,Four,Five,Six".split(",")).forEach(
            s -> {
                final Show show = showsRepository.save(new Show(s, "Brief.", 60));
                shows.add(show);
            }
        );
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
                get("/shows")
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
                get("/shows")
                    .accept(jsonContent)
                    .contentType(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$", hasSize(shows.size())));
    }

    @Test
    public void testAddItem() throws Exception {
        final Show show = new Show("Zero", "Brief.", 60);

        mockMvc
            .perform(
                post("/shows")
                    .accept(jsonContent)
                    .contentType(jsonContent)
                    .content(serialize(show)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name", is(show.getName())))
            .andExpect(jsonPath("$.brief", is(show.getBrief())))
            .andExpect(jsonPath("$.duration", is(show.getDuration())));

        mockMvc
            .perform(
                get("/shows")
                    .accept(jsonContent)
                    .contentType(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$", hasSize(shows.size() + 1)));
    }

    /**
     * Tests whether retrieved list of shows equals expected collection.
     *
     * @throws Exception
     */
    @Test
    public void testValidItemsRetrieved() throws Exception {
        final List<String> names = Lists.transform(shows, input -> input.getName());

        mockMvc
            .perform(
                get("/shows")
                    .accept(jsonContent)
                    .contentType(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$..name", is(names)));
    }

    /**
     * Tests whether show details were returned as expected.
     *
     * @throws Exception
     */
    @Test
    public void testDetailsCorrect() throws Exception {
        for (Show show : shows) {
            mockMvc
                .perform(
                    get(String.format("/shows/%d", show.getId()))
                        .accept(jsonContent)
                        .contentType(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jsonContent))
                .andExpect(jsonPath("$.id", is((int) show.getId())))
                .andExpect(jsonPath("$.name", is(show.getName())))
                .andExpect(jsonPath("$.brief", is(show.getBrief())))
                .andExpect(jsonPath("$.duration", is(show.getDuration())));
        }
    }

    /**
     * Tests whether omitting all of optional parameters results in returning HTTP 400 BAD REQUEST.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateRequiredParams() throws Exception {
        final Show show = shows.get(0);
        show.setName(null);
        show.setBrief(null);
        show.setDuration(0);

        mockMvc
            .perform(
                patch(String.format("/shows/%d", show.getId()))
                    .accept(jsonContent)
                    .contentType(jsonContent)
                    .content(serialize(show)))
            .andExpect(status().isBadRequest());
    }

    /**
     * Tests whether updating an object returns HTTP 200 OK and corresponding item's details.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateExisting() throws Exception {
        final Show show = shows.get(0);
        final int newDuration = show.getDuration() + 10;
        show.setDuration(newDuration);

        mockMvc
            .perform(
                patch(String.format("/shows/%d", show.getId()))
                    .accept(jsonContent)
                    .contentType(jsonContent)
                    .content(serialize(show)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$.id", is((int) show.getId())))
            .andExpect(jsonPath("$.name", is(show.getName())))
            .andExpect(jsonPath("$.brief", is(show.getBrief())))
            .andExpect(jsonPath("$.duration", is(show.getDuration())));
    }

    /**
     * Tests whether deletion of not existing {@link Show} results in returning HTTP 204 NO CONTENT.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteNonExisting() throws Exception {
        mockMvc
            .perform(
                delete(String.format("/shows/%d", -1))
                    .accept(jsonContent)
                    .contentType(jsonContent))
            .andExpect(status().isNoContent());
    }

    /**
     * Tests whether removing a {@link Show} removes item and returns HTTP 204 NO CONTENT.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteExisting() throws Exception {
        final Show show = shows.get(0);

        mockMvc
            .perform(
                delete(String.format("/shows/%d", show.getId()))
                    .accept(jsonContent)
                    .contentType(jsonContent))
            .andExpect(status().isNoContent());

        mockMvc
            .perform(
                get("/shows")
                    .accept(jsonContent)
                    .contentType(jsonContent))
            .andExpect(status().isOk())
            .andExpect(content().contentType(jsonContent))
            .andExpect(jsonPath("$", hasSize(shows.size() - 1)));
    }
}
