package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Model;


import models.Users;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;

import views.html.*;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

	
    @Inject
    private FormFactory formFactory;
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Hello World!"));
        
    }
    /*
     * Action generates ErrorPage
     * TODO- Do it better;
     * @return Page with error
     */
    public Result genError(){
    	return ok("ERROR- Login or password already exist in database");
    }
    /*
     *Metoda sluzaca do weryfikacji hasel i loginow
     *przed dodaniem użytkownika do tabeli Users
     * @return: przekierowanie na strone glowna
     */
     public Result addUser() {
         Form<Users> userForm = formFactory.form(Users.class);
         Users user = userForm.bindFromRequest().get();
         Model.Finder<Integer, Users> finder = new Model.Finder<>(Users.class);
         List<Users> users = finder.all();
         String log=user.login;
         String pass=user.password;
         if(users.isEmpty())
         {
           user.save();
         }
         else
         {
           int samelogin=0;
           int samepass=0;
           int i=0;
           while((i<users.size())&&(samelogin==0)&&(samepass==0))
           {
              if(users.get(i).login.equals(log))
              {
                samelogin=1;
              }
              if(users.get(i).password.equals(pass))
              {
                samepass=1;
              }
              i++;
           }
           if((samelogin==1)||(samepass==1))
           {
             System.out.println("Login or password alreay exist !");
             return redirect(routes.HomeController.genError());
           }
           else
           {
             user.save();
           }
        }
        return redirect(routes.HomeController.index());
    }
    /*
     *Metoda sluzaca do podawania danych o uzytkowniku,
     *na podstawie loginu i hasła
     * @return: przekierowanie na strone glowna
     */
     public Result aboutUser(){
         Form<Users> userForm = formFactory.form(Users.class);
         Users user = userForm.bindFromRequest().get();
         Model.Finder<Integer, Users> finder = new Model.Finder<>(Users.class);
         List<Users> users = finder.all();
         String log=user.login;
         String pass=user.password;
           int same=0;
           int i=0;
           while((i<users.size())&&(same==0))
           {
              if((users.get(i).login.equals(log))&&(users.get(i).password.equals(pass)))
              {
                System.out.println("User name: "+users.get(i).name+"User surname: "+users.get(i).surname);
                same=1;
              }
              i++;        
           }
         return redirect(routes.HomeController.index());
   }              
    /*
     * Metoda sluzaca do wypisania wszystkich danych zawartych w tabeli
     * @return: zawartosc tabeli Users- wszystkie krotki
     * TODO: modyfikacja metody- metoda po zalogowaniu do systemu ma zwrocic wszystkie informacje n.t. uzytkownika
     */
    public Result getUsers() {
        Model.Finder<Integer, Users> finder = new Model.Finder<>(Users.class);
        List<Users> users = finder.all();
        return ok(toJson(users));
    }
}
