package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Time extends Model {

    @Id
    public Integer id;
    @JsonProperty
    @ManyToOne(cascade = CascadeType.ALL)
    public String login;
    @JsonProperty
    public java.util.Date begin;
    @JsonProperty
    public java.util.Date end;
    @JsonProperty
    public String state;

    public Time(String login, Date begin, Date end, String state) {
        this.login = login;
        this.begin = begin;
        this.end = end;
        this.state = state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return this.login;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getBegin() {
        return this.begin;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getEnd() {
        return this.end;
    }
}
