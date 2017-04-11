package com.todev.tvshows.station;

import java.util.Collection;
import java.util.function.Function;

class ReadAllStations {

  ReadAllStations(final Builder builder) {
    // no-op.
  }

  static BuilderProvider should() {
    return Builder::new;
  }

  @FunctionalInterface
  interface BuilderProvider {
    Builder readStations();
  }

  @FunctionalInterface
  interface Handler extends Function<ReadAllStations, Collection<Station>> {
    // Empty by design.
  }

  static class Builder {

    Collection<Station> using(final Handler handler) {
      return handler.apply(new ReadAllStations(this));
    }
  }
}
