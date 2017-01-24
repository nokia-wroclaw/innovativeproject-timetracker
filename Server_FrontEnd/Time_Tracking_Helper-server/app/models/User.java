package models;

import play.db.ebean.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.mindrot.jbcrypt.BCrypt;

import play.data.validation.Constraints.Required;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Klasa tworzaca jedna z tabel baz danych
 * Poszczegolne pola publiczne to sa nazwy kolumn tabeli
 * Setterow i Getterow nie tlumacze
 * 
 */
@Entity
public class User extends Model {
    @Id
    public Integer id;
    @JsonProperty
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public String login;
    @JsonProperty
    public String password;
    @JsonProperty
    public String name;
    @JsonProperty
    public String surname;
    @JsonProperty
    public String email;

    public User(String Login, String Password, String Name, String Surname, String Email) {
        this.login = Login;
        this.password = Password;
        this.name = Name;
        this.surname = Surname;
        this.email = Email;
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
