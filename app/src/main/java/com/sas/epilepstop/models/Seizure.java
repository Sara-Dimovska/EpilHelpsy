package com.sas.epilepstop.models;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

public class Seizure {
    private Integer Id;
    private DateTime date;
    private String duration;

    public Seizure() {
        this.date = new DateTime();
        this.duration = "b";
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
