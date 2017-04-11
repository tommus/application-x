package com.todev.tvshows.show;

import com.todev.tvshows.exception.NotFoundException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.todev.tvshows.common.ResponseBuilder.notFound;
import static com.todev.tvshows.common.ResponseBuilder.ok;
import static com.todev.tvshows.show.ReadShow.should;
import static java.util.function.UnaryOperator.identity;

@RestController
@RequestMapping(value = "/shows/{id}")
public class ReadShowController {

  private final ReadShow.Handler reader;

  @Autowired
  ReadShowController(final ReadShow.Handler reader) {
    this.reader = reader;
  }

  @GetMapping
  public ResponseEntity<?> show(@PathVariable UUID id) {
    try {
      final Show show = should().readShow().withId(id).using(this.reader);
      return ok(show, identity());
    } catch (NotFoundException e) {
      return notFound(e);
    }
  }
}
