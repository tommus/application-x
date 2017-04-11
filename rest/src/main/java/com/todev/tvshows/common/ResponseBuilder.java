package com.todev.tvshows.common;

import java.util.function.Function;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
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

  public static ResponseEntity<?> noContent() {
    return new ResponseEntity<>(NO_CONTENT);
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

  public static ResponseEntity<?> badRequest(final BindingResult bindingResult) {
    return badRequest(bindingResult, BindingResult::getAllErrors);
  }

  public static ResponseEntity<?> badRequest(final Throwable throwable) {
    return badRequest(throwable, Throwable::getMessage);
  }

  private static <R, T> ResponseEntity<R> badRequest(final T value, final Function<T, R> mapper) {
    return new ResponseEntity<>(mapper.apply(value), BAD_REQUEST);
  }

  public static <T> ResponseEntity<T> badRequest() {
    return new ResponseEntity<>(BAD_REQUEST);
  }
}
