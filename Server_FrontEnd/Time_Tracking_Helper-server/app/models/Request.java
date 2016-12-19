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
public class Request extends Model{
	@Id
	public Integer id;
	@JsonProperty
	public String userfrom;
	@JsonProperty
	public String userto;

	public Request (String userfrom, String userto){
		this.userfrom=userfrom;
		this.userto=userto;
	}


    public void setuserfrom(String userfrom) {
        this.userfrom = userfrom;
    }
    public String getuserfrom() {
        return userfrom;
    }
    public void setuserto(String userto) {
        this.userto = userto;
    }
    public String getuserto() {
        return userto;
    }
}
