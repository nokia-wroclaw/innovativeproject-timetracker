package models;

import com.avaje.ebean.Model;

import play.data.Form;
import play.data.FormFactory;

import javax.inject.Inject;

import org.mindrot.jbcrypt.BCrypt;

import exceptions.IncorrectPasswordException;
import models.User;

import com.fasterxml.jackson.databind.JsonNode;

public class UserStorage {


    /*
  *Metoda sluzaca do weryfikacji hasel i loginow
  *przed dodaniem użytkownika do tabeli Users
  * @return: przekierowanie na strone glowna
  */
    public static String addUser(User user) throws Exception {
        Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
        String log = user.login;

        User userdatabase = finder.where().eq("login", log).findUnique();
        if (userdatabase != null) {
            throw new Exception();
        } else {
            String passun = user.password;
            user.password = BCrypt.hashpw(passun, BCrypt.gensalt());
            user.save();
            return "OK";
        }
    }

    /*
     *Metoda sluzaca do logowania,
     *na podstawie loginu i hasła
     * @return: przekierowanie na strone glowna, gdy nic nie znajdzie, lub gdy znajdzie przekierowuje na strone z danymi usera
     */
    public static User loginUser(User user) throws Exception {
        Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
        String login = user.login;
        String pass = user.password;
        User userdatabase = finder.where().eq("login", login).findUnique();
        if (userdatabase != null && BCrypt.checkpw(pass, userdatabase.password)) {
            return userdatabase;
        } else {
            return null;
        }
    }
     
     /*
      * wylogowanie uzytkownika
      * @return potwierdzenie lub przekierowanie na strone glowna, jesli jest cos zle
      */

    public static User logoutUser(User user) throws Exception {
        Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
        String login = user.login;
        User userdatabase = finder.where().eq("login", login).findUnique();
        if (userdatabase == null) {
            throw new Exception();
        } else {
            user.update();
            return userdatabase;
        }
    }

    public static void changeUserInfo(String login, JsonNode json) throws IncorrectPasswordException {
        Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
        User user = finder.where().eq("login", login).findUnique();
        String newEmail = json.findPath("newemail").textValue();
        if (newEmail != null) {
            User userWithNewEmail = finder.where().eq("email", newEmail).findUnique();
            if (userWithNewEmail != null)
                throw new EmailExistsInDatabaseException();
            user.setEmail(newEmail);
        }
        String newSurname = json.findPath("newsurname").textValue();
        if (newSurname != null)
            user.setSurname(newSurname);
        String oldPassword = json.findPath("oldpwd").textValue();
        if (oldPassword != null) {
            if (BCrypt.checkpw(oldPassword, user.getPassword())) {
                String newPassword = json.findPath("newpwd").textValue();
                user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            } else {
                throw new IncorrectPasswordException();
            }
        }
        user.update();
    }
}
