package com.todev.tvshows.program;

import com.todev.tvshows.exception.BadRequestException;
import java.util.Collection;
import java.util.function.Function;
import org.joda.time.DateTime;

import static java.util.Optional.ofNullable;

class SearchOngoingPrograms {

  private final DateTime timestamp;

  SearchOngoingPrograms(final Builder builder) {
    this.timestamp = ofNullable(builder.timestamp).orElseThrow(BadRequestException::new);
  }

  static BuilderProvider should() {
    return Builder::new;
  }

  public DateTime getTimestamp() {
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

    private DateTime timestamp;

    Collection<Program> using(final Handler handler) {
      return handler.apply(new SearchOngoingPrograms(this));
    }

    Builder atTimestamp(final DateTime timestamp) {
      this.timestamp = timestamp;
      return this;
    }
  }
}
