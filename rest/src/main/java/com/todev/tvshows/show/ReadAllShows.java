package com.todev.tvshows.show;

import java.util.Collection;
import java.util.function.Function;

class ReadAllShows {

  ReadAllShows(final Builder builder) {
    // no-op.
  }

  static BuilderProvider should() {
    return Builder::new;
  }

  @FunctionalInterface
  interface BuilderProvider {
    Builder readShows();
  }

  @FunctionalInterface
  interface Handler extends Function<ReadAllShows, Collection<Show>> {
    // Empty by design.
  }

  static class Builder {

    Collection<Show> using(final Handler handler) {
      return handler.apply(new ReadAllShows(this));
    }
  }
}
