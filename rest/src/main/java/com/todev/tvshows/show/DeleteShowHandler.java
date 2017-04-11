package com.todev.tvshows.show;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
class DeleteShowHandler implements DeleteShow.Handler {

  private final ShowsRepository showsRepository;

  @Autowired
  DeleteShowHandler(final ShowsRepository showsRepository) {
    this.showsRepository = showsRepository;
  }

  @Override
  public void accept(DeleteShow command) {
    this.showsRepository.delete(command.getId());
  }
}
