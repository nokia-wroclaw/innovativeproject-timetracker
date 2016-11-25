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
    
	/*public static java.util.Date fromStringToDate(String stringDate){
        DateFormat dateFrm = new SimpleDateFormat("dd/MM/yyyy@HH:mm");
        
        java.util.Date daterequest = null;
		try {
			daterequest = dateFrm.parse(stringDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("COS Z DATA");
			e.printStackTrace();
		}
		System.out.println(stringDate+ " "+ daterequest);
		return daterequest;
	}*/
}
