package com.todev.tvshows.program;

import com.todev.tvshows.exception.BadRequestException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

class SearchOngoingPrograms {

  private final LocalDateTime timestamp;

  SearchOngoingPrograms(final Builder builder) {
    this.timestamp = ofNullable(builder.timestamp).orElseThrow(BadRequestException::new);
  }

  static BuilderProvider should() {
    return Builder::new;
  }

  LocalDateTime getTimestamp() {
    return timestamp;
  }

  @FunctionalInterface
  interface BuilderProvider {
    Builder searchOngoingPrograms();
  }

  @FunctionalInterface
  interface Handler extends Function<SearchOngoingPrograms, Collection<Program>> {
    // Empty by design.
  }

  static class Builder {

    private LocalDateTime timestamp;

    Collection<Program> using(final Handler handler) {
      return handler.apply(new SearchOngoingPrograms(this));
    }

    Builder at(final LocalDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
    }
  }
}
