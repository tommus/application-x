package com.todev.tvshows.show;

import com.todev.tvshows.exception.BadRequestException;
import com.todev.tvshows.exception.NotFoundException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.todev.tvshows.common.ResponseBuilder.badRequest;
import static com.todev.tvshows.common.ResponseBuilder.notFound;
import static com.todev.tvshows.show.ReadShow.should;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/shows/{id}")
class ReadShowController {

  private final ReadShow.Handler reader;

  @Autowired
  ReadShowController(final ReadShow.Handler reader) {
    this.reader = reader;
  }

  @GetMapping
  ResponseEntity<?> show(@PathVariable UUID id) {
    Show show;

    try {
      show = should().readShow().withId(id).using(this.reader);
    } catch (BadRequestException e) {
      return badRequest(e);
    } catch (NotFoundException e) {
      return notFound(e);
    }

    return ok(show);
  }
}
