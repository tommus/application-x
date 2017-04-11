package com.todev.tvshows.show;

import com.todev.tvshows.exception.BadRequestException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.todev.tvshows.common.ResponseBuilder.badRequest;
import static com.todev.tvshows.common.ResponseBuilder.noContent;
import static com.todev.tvshows.show.DeleteShow.should;

@RestController
@RequestMapping("/shows/{id}")
public class DeleteShowController {

  private final DeleteShow.Handler remover;

  @Autowired
  DeleteShowController(final DeleteShow.Handler remover) {
    this.remover = remover;
  }

  @DeleteMapping
  ResponseEntity<?> remove(@PathVariable UUID id) {
    try {
      should().deleteShow().withId(id).using(this.remover);
    } catch (BadRequestException e) {
      return badRequest(e);
    }
    return noContent();
  }
}
