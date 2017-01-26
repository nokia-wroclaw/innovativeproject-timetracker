package models;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
public class Schedule extends Model implements Comparator<Schedule>, Comparable<Schedule> {

    @Id
    public Integer id;
    @JsonProperty
    public String login;
    @JsonProperty
    public String day;
    @JsonProperty
    public java.sql.Time begin;
    @JsonProperty
    public java.sql.Time end;

    public Schedule(String login, String day, java.sql.Time begin, java.sql.Time end) {
        this.login = login;
        this.day = day;
        this.begin = begin;
        this.end = end;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setBegin(java.sql.Time begin) {
        this.begin = begin;
    }

    public Date getBegin() {
        return begin;
    }

    public void setEnd(java.sql.Time end) {
        this.end = end;
    }

    public Date getEnd() {
        return end;
    }

    public int compare(Schedule s1, Schedule s2) {
        return s1.getBegin().compareTo(s2.getBegin());
    }

    public int compareTo(Schedule s) {
        return (this.getBegin()).compareTo(s.getBegin());
    }
}
