package com.todev.tvshows.show;

import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
class ReadAllShowsHandler implements ReadAllShows.Handler {

  private final ShowsRepository showsRepository;

  @Autowired
  ReadAllShowsHandler(final ShowsRepository showsRepository) {
    this.showsRepository = showsRepository;
  }

  @Override
  public Collection<Show> get() {
    return this.showsRepository.findAll();
  }
}
