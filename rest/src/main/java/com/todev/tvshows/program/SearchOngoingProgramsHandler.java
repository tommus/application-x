package com.todev.tvshows.program;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class SearchOngoingProgramsHandler implements SearchOngoingPrograms.Handler {

  private final ProgramsRepository programsRepository;

  @Autowired
  SearchOngoingProgramsHandler(final ProgramsRepository programsRepository) {
    this.programsRepository = programsRepository;
  }

  @Override
  public Collection<Program> apply(SearchOngoingPrograms command) {
    return this.programsRepository.findOngoing(command.getTimestamp());
  }
}
