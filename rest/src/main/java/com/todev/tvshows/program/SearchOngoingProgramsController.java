package com.todev.tvshows.program;

import java.util.Collection;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.todev.tvshows.common.ResponseBuilder.ok;
import static com.todev.tvshows.program.SearchOngoingPrograms.should;
import static java.util.Collections.unmodifiableCollection;
import static java.util.function.UnaryOperator.identity;

@RestController
@RequestMapping(value = "/programs")
class SearchOngoingProgramsController {

  private final SearchOngoingPrograms.Handler reader;

  @Autowired
  SearchOngoingProgramsController(final SearchOngoingPrograms.Handler reader) {
    this.reader = reader;
  }

  @GetMapping
  ResponseEntity<?> programs() {
    final Collection<Program> programs =
        should().searchOngoingPrograms().atTimestamp(DateTime.now().minusDays(1)).using(this.reader);
    return ok(unmodifiableCollection(programs), identity());
  }
}
