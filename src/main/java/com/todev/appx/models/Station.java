package com.todev.appx.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Station {
  @GeneratedValue
  @Id
  @JsonView(View.Basic.class)
  private long id;

  @JsonView(View.Basic.class)
  private String name;

  @JsonProperty(value = "schedule")
  @JsonView(View.Details.class)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE)
  private Set<Program> programs = new HashSet<>();

  public Station() {
    // Default constructor for Hibernate.
  }

  public Station(String name) {
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Program> getPrograms() {
    return programs;
  }

  public void setPrograms(Set<Program> programs) {
    this.programs = programs;
  }

  public void addProgram(Program program) {
    programs.add(program);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Station)) {
      final Station other = (Station) obj;

      return Objects.equals(getId(), other.getId()) && Objects.equals(getName(), other.getName());
    }

    return false;
  }

  @Override
  public String toString() {
    return "Station {id = " + Objects.toString(getId()) + ", name = " + Objects.toString(getName()) + "}";
  }

  /**
   * Class allow to define which fields will be present in queried response.
   */
  public static class View {
    public static class Basic {
    }

    public static class Details extends Basic {
    }
  }
}
