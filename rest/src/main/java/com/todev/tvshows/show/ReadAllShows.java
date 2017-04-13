package com.todev.tvshows.show;

import java.util.Collection;
import java.util.function.Supplier;

class ReadAllShows {

  static BuilderProvider should() {
    return Builder::new;
  }

  @FunctionalInterface
  interface BuilderProvider {
    Builder readShows();
  }

  @FunctionalInterface
  interface Handler extends Supplier<Collection<Show>> {
    // Empty by design.
  }

  static class Builder {

    Collection<Show> using(final Handler handler) {
      return handler.get();
    }
  }
}
