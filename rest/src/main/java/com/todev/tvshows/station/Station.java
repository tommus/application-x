package com.todev.tvshows.station;

import java.util.UUID;
import java.util.function.Supplier;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "STATION")
class Station {

  @Column(name = "ID")
  @Id
  private UUID id;

  @Column(name = "NAME")
  private String name;

  Station() {
    super();
  }

  Station(final Builder builder) {
    this.id = ofNullable(builder.id).orElseGet(UUID::randomUUID);
    this.name = ofNullable(builder.name).orElseThrow(IllegalArgumentException::new);
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

  @FunctionalInterface
  interface BuilderProvider {
    Builder prepareStation();
  }

  static class Builder implements Supplier<Station> {

    private UUID id;

    private String name;

    Builder() {
      super();
    }

    Builder withId(final UUID id) {
      this.id = id;
      return this;
    }

    Builder named(final String name) {
      this.name = name;
      return this;
    }

    @Override
    public Station get() {
      return new Station(this);
    }
  }
}
