package com.todev.tvshows.show;

import java.util.UUID;
import java.util.function.Supplier;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "SHOW")
class Show {

  @Id
  @Column(name = "ID")
  private UUID id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "BRIEF")
  private String brief;

  @Column(name = "DURATION")
  private Integer duration;

  Show() {
    super();
  }

  Show(final Builder builder) {
    this.id = ofNullable(builder.id).orElseGet(UUID::randomUUID);
    this.name = ofNullable(builder.name).orElseThrow(IllegalArgumentException::new);
    this.brief = ofNullable(builder.brief).orElse("");
    this.duration = ofNullable(builder.duration).orElse(0);
  }

  static BuilderProvider should() {
    return Builder::new;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getBrief() {
    return brief;
  }

  public Integer getDuration() {
    return duration;
  }

  @FunctionalInterface
  interface BuilderProvider {
    Builder prepareShow();
  }

  static class Builder implements Supplier<Show> {

    private UUID id;

    private String name;

    private String brief;

    private Integer duration;

    Builder withId(final UUID id) {
      this.id = id;
      return this;
    }

    Builder named(final String name) {
      this.name = name;
      return this;
    }

    Builder briefed(final String brief) {
      this.brief = brief;
      return this;
    }

    Builder lasting(final Integer duration) {
      this.duration = duration;
      return this;
    }

    @Override
    public Show get() {
      return new Show(this);
    }
  }
}
