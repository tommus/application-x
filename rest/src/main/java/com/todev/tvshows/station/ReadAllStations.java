package com.todev.tvshows.station;

import java.util.Collection;
import java.util.function.Supplier;

class ReadAllStations {

  static BuilderProvider should() {
    return Builder::new;
  }

  @FunctionalInterface
  interface BuilderProvider {
    Builder readStations();
  }

  @FunctionalInterface
  interface Handler extends Supplier<Collection<Station>> {
    // Empty by design.
  }

  static class Builder {

    Collection<Station> using(final Handler handler) {
      return handler.get();
    }
  }
}
