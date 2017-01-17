package models;

import com.avaje.ebean.Model;

import play.data.Form;
import play.data.FormFactory;
import javax.inject.Inject;

import org.mindrot.jbcrypt.BCrypt;

import models.User;




public class UserStorage{

    
	   /*
     *Metoda sluzaca do weryfikacji hasel i loginow
     *przed dodaniem użytkownika do tabeli Users
     * @return: przekierowanie na strone glowna
     */
     public static String addUser(User user) throws Exception{
         Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
         String log=user.login;

         User userdatabase=finder.where().eq("login",log).findUnique();
         if(userdatabase!=null){
           throw new Exception();
         }else{
             String passun=user.password; 
             user.password= BCrypt.hashpw(passun, BCrypt.gensalt());
             user.save();
             return "OK";
         }
    }
    /*
     *Metoda sluzaca do logowania,
     *na podstawie loginu i hasła
     * @return: przekierowanie na strone glowna, gdy nic nie znajdzie, lub gdy znajdzie przekierowuje na strone z danymi usera
     */
     public static User loginUser(User user) throws Exception{
         Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
         String login=user.login;
         String pass=user.password;
         User userdatabase=finder.where().eq("login",login).findUnique();
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
     
     public static User logoutUser(User user) throws Exception{
         Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
         String login=user.login;
         User userdatabase=finder.where().eq("login",login).findUnique();
         if(userdatabase==null){
             throw new Exception();	 
         }else{
             user.update();   
             return userdatabase;
         }
   }
     
}
