package com.todev.appx.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.todev.appx.models.Program;
import com.todev.appx.repositories.ProgramsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = { "programs" }, description = "Operations related to programs")
@RestController
@RequestMapping("programs")
public class ProgramsController {

  @Autowired
  ProgramsRepository programsRepository;

  /**
   * Retrieves {@link Program}s ongoing at given time.
   *
   * @param time a point in time that should be used to retrieve ongoing programs.
   * @return a collection of ongoing {@link Program}s.
   */
  @ApiOperation(value = "Finds ongoing programs")
  @JsonView(Program.View.Details.class)
  @RequestMapping(value = "", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE
  })
  public List<Program> readPrograms(@RequestParam(value = "time") long time) {

    final DateTime select = new DateTime(time);
    final List<Program> programs = programsRepository.findByOngoing(select);
    updateTimes(programs, select);
    return programs;
  }

  /**
   * Refreshes {@literal sinceStart} and {@literal tillEnd} fields for all {@link Program}s in given collection.
   *
   * @param ongoing a collection of {@link Program}s which fields will be updated.
   * @param time a point in time related to which fields should be recalculated.
   */
  private void updateTimes(List<Program> ongoing, DateTime time) {
    ongoing.forEach(p -> {
      final int duration = p.getDuration();
      final DateTime startAt = p.getStartAt();
      final DateTime endAt = p.getStartAt().plusMinutes(duration);
      final long sinceStart = Minutes.minutesBetween(startAt, time).getMinutes();
      final long tillEnd = Minutes.minutesBetween(time, endAt).getMinutes();
      p.setSinceStart(sinceStart);
      p.setTillEnd(tillEnd);
    });
  }
}
