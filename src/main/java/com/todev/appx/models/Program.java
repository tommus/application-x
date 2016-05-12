package com.todev.appx.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.todev.appx.serializers.DateTimeSerializer;
import com.todev.appx.views.Views;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
@Entity
public class Program {
    @GeneratedValue
    @Id
    @JsonIgnore
    private long id;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startAt;

    @JsonIgnore
    @ManyToOne
    private Station station;

    @Transient
    private long sinceStart;

    @Transient
    private long tillEnd;

    private String name;
    private String brief;
    private int duration;

    public Program() {
        // Default constructor for Hibernate.
    }

    public Program(Station station, int duration, DateTime startAt, String brief, String name) {
        this.station = station;
        this.duration = duration;
        this.startAt = startAt;
        this.brief = brief;
        this.name = name;
    }

    public long getId() {
        return id;
    }

//    public void setId(long id) {
//        this.id = id;
//    }

    @JsonView({Views.BasicProgram.class, Views.DetailStation.class})
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonView({Views.BasicProgram.class, Views.DetailStation.class})
    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    @JsonProperty(value = "start_time")
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonView({Views.ScheduleProgram.class, Views.DetailStation.class})
    public DateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(DateTime startAt) {
        this.startAt = startAt;
    }

    @JsonIgnore
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @JsonIgnore
    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @JsonProperty(value = "time_passed")
    @JsonView(Views.DetailProgram.class)
    public long getSinceStart() {
        return sinceStart;
    }

    public void setSinceStart(long sinceStart) {
        this.sinceStart = sinceStart;
    }

    @JsonProperty(value = "time_left")
    @JsonView(Views.DetailProgram.class)
    public long getTillEnd() {
        return tillEnd;
    }

    public void setTillEnd(long tillEnd) {
        this.tillEnd = tillEnd;
    }
}
