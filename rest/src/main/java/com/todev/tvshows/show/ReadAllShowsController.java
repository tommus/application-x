package com.todev.tvshows.show;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.todev.tvshows.common.ResponseBuilder.ok;
import static com.todev.tvshows.show.ReadAllShows.should;
import static java.util.function.Function.identity;

@RestController
class ReadAllShowsController {

  private final ReadAllShows.Handler reader;

  @Autowired
  ReadAllShowsController(final ReadAllShows.Handler reader) {
    this.reader = reader;
  }

  @RequestMapping(value = "shows")
  public ResponseEntity<?> shows() {
    final Collection<Show> shows = should().readShows().using(this.reader);
    return ok(shows, identity());
  }
}
