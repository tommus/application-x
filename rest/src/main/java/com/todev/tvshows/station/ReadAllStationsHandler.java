package com.todev.tvshows.station;

import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
class ReadAllStationsHandler implements ReadAllStations.Handler {

  private final StationsRepository stationsRepository;

  @Autowired
  ReadAllStationsHandler(final StationsRepository stationsRepository) {
    this.stationsRepository = stationsRepository;
  }

  @Override
  public Collection<Station> apply(ReadAllStations command) {
    return this.stationsRepository.findAll();
  }
}
