package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Time extends Model{
	@Id
	public Integer id;
	@JsonProperty
	@ManyToOne(cascade = CascadeType.ALL)
	public String login;
	@JsonProperty
	public java.util.Date begin;
	@JsonProperty
	public java.util.Date end;
	
	public Time (String login, Date begin, Date end){
		this.login=login;
		this.begin=begin;
		this.end=end;
	}

    public void setLogin(String login) {
        this.login = login;
    }
    public String getLogin() {
        return login;
    }
    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getBegin() {
        return begin;
    }
    public void setEnd(Date end) {
        this.end = end;
    }
    public Date getEnd() {
        return end;
    }
}
