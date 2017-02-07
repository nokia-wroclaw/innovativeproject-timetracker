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
public class Privileges extends Model{
	@Id
	public Integer id;
	@JsonProperty
	public String userfrom;
	@JsonProperty
	public String userto;
	@JsonProperty
	public Integer estimatedHours;

	public Privileges (String userfrom, String userto){
		this.userfrom=userfrom;
		this.userto=userto;
		this.estimatedHours=0;
	}


    public void setuserfrom(String userfrom) {
        this.userfrom = userfrom;
    }
    public String getUserfrom() {
        return userfrom;
    }
    public void setuserto(String userto) {
        this.userto = userto;
    }
    public String getUserto() {
        return userto;
    }
    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }
    public int getEstimatedHours() {
        return estimatedHours;
    }
}
