package com.todev.tvshows.program;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "PROGRAM")
class Program {

  @Id
  @Column(name = "ID")
  private UUID id;

  @Column(name = "START_AT")
  private LocalDateTime startAt;

  public Program() {
    super();
  }

  Program(Builder builder) {
    this.id = ofNullable(builder.id).orElseGet(UUID::randomUUID);
    this.startAt = ofNullable(builder.startAt).orElseThrow(IllegalArgumentException::new);
  }

  static BuilderProvider should() {
    return Builder::new;
  }

  public UUID getId() {
    return id;
  }

  public LocalDateTime getStartAt() {
    return startAt;
  }

  @FunctionalInterface
  interface BuilderProvider {
    Builder prepareProgram();
  }

  static class Builder implements Supplier<Program> {

    private UUID id;

    private LocalDateTime startAt;

    Builder withId(final UUID id) {
      this.id = id;
      return this;
    }

    Builder startingAt(final LocalDateTime startAt) {
      this.startAt = startAt;
      return this;
    }

    @Override
    public Program get() {
      return new Program(this);
    }
  }
}
