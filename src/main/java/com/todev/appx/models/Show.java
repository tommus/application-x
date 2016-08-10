package com.todev.appx.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Show {
  @GeneratedValue
  @Id
  @JsonView(View.Basic.class)
  private long id;

  private String name;
  private String brief;
  private int duration;

  @JsonIgnore
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "show", cascade = CascadeType.REMOVE)
  private Set<Program> programs = new HashSet<>();

  public Show() {
    // Default constructor for Hibernate.
  }

  public Show(String name, String brief, int duration) {
    this.name = name;
    this.brief = brief;
    this.duration = duration;
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

  public String getBrief() {
    return brief;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public Set<Program> getPrograms() {
    return programs;
  }

  public void setPrograms(Set<Program> programs) {
    this.programs = programs;
  }

  public void addProgram(Program program) {
    this.programs.add(program);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getBrief(), getDuration());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (obj instanceof Show) {
      final Show other = (Show) obj;

      return Objects.equals(getId(), other.getId()) && Objects.equals(getName(), other.getName()) && Objects.equals(
          getBrief(), other.getBrief()) && Objects.equals(getDuration(), other.getDuration());
    }

    return false;
  }

  @Override
  public String toString() {
    return "Show {id = "
        + Objects.toString(getId())
        + ", name = "
        + Objects.toString(getName())
        + ", brief = "
        + Objects.toString(getBrief())
        + ", duration = "
        + Objects.toString(getDuration())
        + "}";
  }

  /**
   * Class allow to define which fields will be present in queried response.
   */
  public static class View {
    public static class Basic {
    }
  }
}
