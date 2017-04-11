package com.todev.tvshows.show;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReadShowHandler implements ReadShow.Handler {

  private final ShowsRepository showsRepository;

  @Autowired
  ReadShowHandler(final ShowsRepository showsRepository) {
    this.showsRepository = showsRepository;
  }

  @Override
  public Show apply(ReadShow command) {
    return this.showsRepository.findOne(command.getId());
  }
}
