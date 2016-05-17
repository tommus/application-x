package com.todev.appx.controllers;

import com.todev.appx.models.Show;
import com.todev.appx.repositories.ShowsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Tomasz Dzieniak on 16.05.16.
 */
@RestController
@RequestMapping("shows")
public class ShowsController {
    private final ShowsRepository showsRepository;

    /**
     * Create a new {@link ShowsController}.
     *
     * @param showsRepository a related {@link ShowsRepository} repository.
     */
    @Autowired
    public ShowsController(ShowsRepository showsRepository) {
        this.showsRepository = showsRepository;
    }

    /**
     * Creates a {@link Show} item.
     *
     * @param body contains item's specification.
     * @return detailed information about creation process with corresponding item's details.
     */
    @RequestMapping(value = "", method = RequestMethod.POST,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createShow(@RequestBody Show body) {
        final Show show = showsRepository.save(body);
        return new ResponseEntity<>(show, HttpStatus.CREATED);
    }

    /**
     * Retrieves all {@link Show} items.
     *
     * @return a collection of {@link Show} items.
     */
    @RequestMapping(value = "", method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Show> readShows() {
        return showsRepository.findAll();
    }

    /**
     * Retrieves a {@link Show} item with given ID.
     *
     * @param id an ID of {@link Show} item that will be retrieved.
     * @return information about retrieval process with corresponding item's details.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> readShow(@PathVariable long id) {
        final Show show = showsRepository.findOne(id);

        if (show == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(show, HttpStatus.OK);
    }

    /**
     * Updates a {@link Show} item at given ID.
     *
     * @param id   an ID of {@link Show} item that will be updated.
     * @param body contains item's specification.
     * @return detailed information about update process with corresponding item's details.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> updateShow(@PathVariable long id, @RequestBody Show body) {
        final Show show = showsRepository.findOne(id);

        if (show == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (body.getName() == null && body.getBrief() == null && body.getDuration() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (body.getName() != null) {
            show.setName(body.getName());
        }

        if (body.getBrief() != null) {
            show.setBrief(body.getBrief());
        }

        if (body.getDuration() != 0) {
            show.setDuration(body.getDuration());
        }

        return new ResponseEntity<>(show, HttpStatus.OK);
    }

    /**
     * Deletes a {@link Show} item at given ID.
     *
     * @param id an ID of {@link Show} item that will be deleted.
     * @return detailed information about delete process.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> deleteShow(@PathVariable long id) {
        final Show show = showsRepository.findOne(id);

        if (show != null) {
            showsRepository.delete(id);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
