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

  @Column(name = "ID")
  @Id
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

  public static class Builder implements Supplier<Show> {

    private UUID id;

    private String name;

    private String brief;

    private Integer duration;

    Builder() {
      super();
    }

    public Builder withId(final UUID id) {
      this.id = id;
      return this;
    }

    public Builder named(final String name) {
      this.name = name;
      return this;
    }

    public Builder briefed(final String brief) {
      this.brief = brief;
      return this;
    }

    public Builder lasting(final Integer duration) {
      this.duration = duration;
      return this;
    }

    @Override
    public Show get() {
      return new Show(this);
    }
  }
}
