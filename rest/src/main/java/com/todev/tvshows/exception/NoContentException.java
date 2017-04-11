package com.todev.tvshows.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@ResponseStatus(value = NO_CONTENT)
public class NoContentException extends RuntimeException {

  public NoContentException() {
    super();
  }

  public NoContentException(String message) {
    super(message);
  }

  public NoContentException(Throwable cause) {
    super(cause);
  }
}
