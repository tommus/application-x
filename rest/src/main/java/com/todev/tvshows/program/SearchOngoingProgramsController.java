package com.todev.tvshows.program;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.todev.tvshows.common.ResponseBuilder.ok;
import static com.todev.tvshows.program.SearchOngoingPrograms.should;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Optional.ofNullable;
import static java.util.function.UnaryOperator.identity;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.TIME;

@RestController
@RequestMapping(value = "/programs")
class SearchOngoingProgramsController {

  private final SearchOngoingPrograms.Handler reader;

  @Autowired
  SearchOngoingProgramsController(final SearchOngoingPrograms.Handler reader) {
    this.reader = reader;
  }

  @GetMapping
  ResponseEntity<?> programs(
      final @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DATE) LocalDate date,
      final @RequestParam(value = "time", required = false) @DateTimeFormat(iso = TIME) LocalTime time) {

    final LocalDate localDate = ofNullable(date).orElse(LocalDate.now());
    final LocalTime localTime = ofNullable(time).orElse(LocalTime.MIDNIGHT);
    final LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

    final Collection<Program> programs = should().searchOngoingPrograms().at(localDateTime).using(this.reader);
    return ok(unmodifiableCollection(programs), identity());
  }
}
