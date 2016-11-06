package models;

import play.db.ebean.*;
import javax.persistence.*;
import play.data.validation.Constraints.Required;
import com.avaje.ebean.Model;
/*
 * Klasa tworzaca jedna z tabel baz danych
 * Poszczegolne pola publiczne to sa nazwy kolumn tabeli
 * Setterow i Getterow nie tlumacze
 * 
 */
@Entity
public class Users extends Model{
	@Id
	public Integer id;
	@Required
	public String login;
	@Required
	public String password;
	@Required
	public String name;
	@Required
	public String surname;
	@Required
	public String email;
	
	public Users(){}
	
    public Users(String Login,String Password,String Name,String Surname, String Email) {
        this.login=Login;
        this.password=Password;
        this.name=Name;
        this.surname=Surname;
        this.email=Email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
