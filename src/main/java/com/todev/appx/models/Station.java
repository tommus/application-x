package com.todev.appx.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.todev.appx.views.Views;

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
    private long id;

    @OneToMany(mappedBy = "station")
    private Set<Program> programs = new HashSet<>();

    private String name;

    public Station() {
        // Default constructor for Hibernate.
    }

    public Station(String name) {
        this.name = name;
    }

    @JsonView(Views.BasicStation.class)
    public long getId() {
        return id;
    }

//    public void setId(long id) {
//        this.id = id;
//    }

    @JsonView(Views.BasicStation.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty(value = "schedule")
    @JsonView(Views.DetailStation.class)
    public Set<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(Set<Program> programs) {
        this.programs = programs;
    }
}
