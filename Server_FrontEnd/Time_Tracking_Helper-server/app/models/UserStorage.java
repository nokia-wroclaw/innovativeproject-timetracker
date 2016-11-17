package models;

import com.avaje.ebean.Model;

import play.data.Form;
import play.data.FormFactory;
import javax.inject.Inject;
import models.User;



public class UserStorage{

	
    @Inject
    private static FormFactory formFactory;
    
	   /*
     *Metoda sluzaca do weryfikacji hasel i loginow
     *przed dodaniem użytkownika do tabeli Users
     * @return: przekierowanie na strone glowna
     */
     public static String addUser(User user) throws Exception{
    	 System.out.println("JESTEM TUTAJ");
         Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
         String log=user.login;
         
         User equal=finder.where().eq("login",log).findUnique();
         //System.out.println(equal.login+" "+equal.name+ " "+ equal.surname);
         if(equal!=null){
           System.out.println("Login already exist !");
           throw new Exception();
         }else{
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
         User equal=finder.where().eq("login",login).eq("password",pass).findUnique();
         if(equal==null){
             throw new Exception();	 
         }else{
             return equal;
         }
     	}        
     
     /*
      * wylogowanie uzytkownika
      * @return potwierdzenie lub przekierowanie na strone glowna, jesli jest cos zle
      */
     
     public static User logoutUser(User user) throws Exception{
         Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
         String login=user.login;
         //String pass=user.password;
         //User equal=finder.where().eq("login",log).eq("password",pass).findUnique();
         User equal=finder.where().eq("login",login).findUnique();
         if(equal==null){
             throw new Exception();	 
         }else{

             System.out.println("User name: "+equal.name+" User surname: "+equal.surname);
             user.update();   
             return equal;
         }
   }
     
}