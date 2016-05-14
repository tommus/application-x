package com.todev.appx.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.todev.appx.serializers.DateTimeDeserializer;
import com.todev.appx.views.Views;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
@Entity
public class Program {
    @GeneratedValue
    @Id
    @JsonIgnore
    private long id;

    @JsonView({Views.BasicProgram.class, Views.DetailStation.class})
    private String name;

    @JsonView({Views.BasicProgram.class, Views.DetailStation.class})
    private String brief;

    @JsonDeserialize(using = DateTimeDeserializer.class)
    @JsonProperty(value = "start_time")
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonView({Views.ScheduleProgram.class, Views.DetailStation.class})
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startAt;

    @JsonView(Views.Invisible.class)
    private int duration;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.DETACH)
    private Station station;

    @JsonProperty(value = "time_passed")
    @JsonView(Views.DetailProgram.class)
    @Transient
    private long sinceStart;

    @JsonProperty(value = "time_left")
    @JsonView(Views.DetailProgram.class)
    @Transient
    private long tillEnd;

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

    public DateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(DateTime startAt) {
        this.startAt = startAt;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public long getSinceStart() {
        return sinceStart;
    }

    public void setSinceStart(long sinceStart) {
        this.sinceStart = sinceStart;
    }

    public long getTillEnd() {
        return tillEnd;
    }

    public void setTillEnd(long tillEnd) {
        this.tillEnd = tillEnd;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
            getId(),
            getName(),
            getBrief(),
            getStartAt(),
            getDuration(),
            getSinceStart(),
            getTillEnd()
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

        if (!(obj instanceof Program)) {
            return false;
        }

        final Program other = (Program) obj;

        return Objects.equal(getId(), other.getId())
            && Objects.equal(getName(), other.getName())
            && Objects.equal(getBrief(), other.getBrief())
            && Objects.equal(getStartAt(), other.getStartAt())
            && Objects.equal(getDuration(), other.getDuration())
            && Objects.equal(getStation(), other.getStation())
            && Objects.equal(getSinceStart(), other.getSinceStart())
            && Objects.equal(getTillEnd(), other.getTillEnd());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Program.class)
            .add("id", getId())
            .add("name", getName())
            .add("brief", getBrief())
            .add("start_at", getStartAt().toString())
            .add("duration", getDuration())
            .add("station", getStation().getName())
            .add("time_passed", getSinceStart())
            .add("time_left", getTillEnd())
            .toString();
    }
}
