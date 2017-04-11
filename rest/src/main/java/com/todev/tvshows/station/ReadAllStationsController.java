package com.todev.tvshows.station;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.todev.tvshows.common.ResponseBuilder.ok;
import static com.todev.tvshows.station.ReadAllStations.should;
import static java.util.function.Function.identity;

@RestController
class ReadAllStationsController {

  private final ReadAllStations.Handler reader;

  @Autowired
  ReadAllStationsController(final ReadAllStations.Handler reader) {
    this.reader = reader;
  }

  @RequestMapping(value = "stations")
  public ResponseEntity<?> stations() {
    final Collection<Station> stations = should().readStations().using(this.reader);
    return ok(stations, identity());
  }
}
