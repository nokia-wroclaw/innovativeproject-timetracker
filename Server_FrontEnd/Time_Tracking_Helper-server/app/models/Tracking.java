package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Tracking extends Model{

	@JsonProperty
	public String login;
	@JsonProperty
	public String password;
	@JsonProperty
	public String date;
	@JsonProperty
	public String state;
	
	public Tracking (String login,String password, String date, String state){
		this.login=login;
		this.password=password;
		this.date=date;
		this.state=state;
	}
	
    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
