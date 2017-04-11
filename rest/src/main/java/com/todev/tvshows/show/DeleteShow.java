package com.todev.tvshows.show;

import com.todev.tvshows.exception.BadRequestException;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;

class DeleteShow {

  private final UUID id;

  DeleteShow(final Builder builder) {
    this.id = ofNullable(builder.id).orElseThrow(BadRequestException::new);
  }

  static BuilderProvider should() {
    return Builder::new;
  }

  public UUID getId() {
    return id;
  }

  @FunctionalInterface
  interface BuilderProvider {
    Builder deleteShow();
  }

  @FunctionalInterface
  interface Handler extends Consumer<DeleteShow> {
    // Empty by design.
  }

  static class Builder {

    private UUID id;

    void using(final Handler handler) {
      handler.accept(new DeleteShow(this));
    }

    Builder withId(final UUID id) {
      this.id = id;
      return this;
    }
  }
}
