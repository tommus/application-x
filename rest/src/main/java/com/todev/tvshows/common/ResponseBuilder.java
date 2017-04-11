package com.todev.tvshows.common;

import java.util.function.Function;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public final class ResponseBuilder {

  private ResponseBuilder() {
    throw new AssertionError("This class shouldn't be instantiated.");
  }

  public static ResponseEntity<?> ok() {
    return new ResponseEntity<>(OK);
  }

  public static <R, T> ResponseEntity<R> ok(final T value, final Function<T, R> mapper) {
    return new ResponseEntity<>(mapper.apply(value), OK);
  }

  public static ResponseEntity<?> notFound() {
    return new ResponseEntity<>(NOT_FOUND);
  }

  public static ResponseEntity<?> notFound(final Throwable throwable) {
    return notFound(throwable, Throwable::getMessage);
  }

  private static <R, T> ResponseEntity<R> notFound(final T value, final Function<T, R> mapper) {
    return new ResponseEntity<>(mapper.apply(value), NOT_FOUND);
  }
}
