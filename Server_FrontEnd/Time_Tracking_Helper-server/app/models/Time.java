package models;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Time {
	@Id
	public Integer id;
	@JsonProperty
	@ManyToOne(cascade = CascadeType.ALL)
	public String login;
	@JsonProperty
	public String begin;
	@JsonProperty
	public String end;
	
	public Time (String login, String begin, String end){
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
    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getBegin() {
        return begin;
    }
    public void setEnd(String end) {
        this.end = end;
    }

    public String getEnd() {
        return end;
    }
}
