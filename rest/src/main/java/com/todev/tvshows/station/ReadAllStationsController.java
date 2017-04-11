package com.todev.tvshows.station;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.todev.tvshows.common.ResponseBuilder.ok;
import static com.todev.tvshows.station.ReadAllStations.should;
import static java.util.Collections.unmodifiableCollection;
import static java.util.function.UnaryOperator.identity;

@RestController
@RequestMapping(value = "/stations")
class ReadAllStationsController {

  private final ReadAllStations.Handler reader;

  @Autowired
  ReadAllStationsController(final ReadAllStations.Handler reader) {
    this.reader = reader;
  }

  @GetMapping
  ResponseEntity<?> stations() {
    final Collection<Station> stations = should().readStations().using(this.reader);
    return ok(unmodifiableCollection(stations), identity());
  }
}
