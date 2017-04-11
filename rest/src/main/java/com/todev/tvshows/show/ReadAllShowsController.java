package com.todev.tvshows.show;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.todev.tvshows.common.ResponseBuilder.ok;
import static com.todev.tvshows.show.ReadAllShows.should;
import static java.util.function.UnaryOperator.identity;

@RestController
@RequestMapping(value = "/shows")
class ReadAllShowsController {

  private final ReadAllShows.Handler reader;

  @Autowired
  ReadAllShowsController(final ReadAllShows.Handler reader) {
    this.reader = reader;
  }

  @GetMapping
  public ResponseEntity<?> shows() {
    final Collection<Show> shows = should().readShows().using(this.reader);
    return ok(shows, identity());
  }
}
