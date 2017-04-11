package com.todev.tvshows.show;

import com.todev.tvshows.exception.NotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
class ReadShowHandler implements ReadShow.Handler {

  private final ShowsRepository showsRepository;

  @Autowired
  ReadShowHandler(final ShowsRepository showsRepository) {
    this.showsRepository = showsRepository;
  }

  @Override
  public Show apply(ReadShow command) {
    final Show show = this.showsRepository.findOne(command.getId());

    if (show == null) {
      throw new NotFoundException();
    }

    return show;
  }
}
