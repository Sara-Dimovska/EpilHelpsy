package com.sas.epilepstop.models;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.UUID;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

@Entity
public class Seizure {
    @Id long id;
    @Uid(7725227109581635276L)
    private String date;
    private String duration;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
