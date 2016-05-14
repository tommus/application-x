package com.todev.appx.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.todev.appx.views.Views;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
@Entity
public class Station {
    @GeneratedValue
    @Id
    @JsonView(Views.BasicStation.class)
    private long id;

    @JsonView(Views.BasicStation.class)
    private String name;

    @JsonProperty(value = "schedule")
    @JsonView(Views.DetailStation.class)
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

//    public void setId(long id) {
//        this.id = id;
//    }

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
        return Objects.hashCode(
            getId(),
            getName()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Station)) {
            return false;
        }

        final Station other = (Station) obj;

        return Objects.equal(getId(), other.getId())
            && Objects.equal(getName(), other.getName());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Station.class)
            .add("id", getId())
            .add("name", getName())
            .toString();
    }
}
