package com.todev.tvshows.show;

import com.todev.tvshows.exception.NotFoundException;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

class ReadShow {

  private final UUID id;

  ReadShow(final Builder builder) {
    this.id = ofNullable(builder.id).orElseThrow(NotFoundException::new);
  }

  static BuilderProvider should() {
    return Builder::new;
  }

  public UUID getId() {
    return id;
  }

  @FunctionalInterface
  interface BuilderProvider {
    Builder readShow();
  }

  @FunctionalInterface
  interface Handler extends Function<ReadShow, Show> {
    // Empty by design.
  }

  static class Builder {

    private UUID id;

    Show using(final Handler handler) {
      return handler.apply(new ReadShow(this));
    }

    Builder withId(final UUID id) {
      this.id = id;
      return this;
    }
  }
}
