package com.todev.tvshows.common;

import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseBuilder {

  private ResponseBuilder() {
    throw new AssertionError("This class shouldn't be instantiated.");
  }

  public static ResponseEntity<?> ok() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  public static <R, T> ResponseEntity<R> ok(final T value, final Function<T, R> mapper) {
    return new ResponseEntity<>(mapper.apply(value), HttpStatus.OK);
  }
}
