package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Model;


import models.User;
import play.libs.Json;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;

import views.html.*;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {



    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Hello World!"));
        
    }
    
    public Result login() {
    	String login= session("Login");
        return ok(index.render("Hello "+ login));
    }
    /*
     * Method adding User to database
     * TODO- Do it better;
     * @return Page with error
     */
    public Result addUserController(){
    	try {
            User user = Json.fromJson(request().body().asJson(), User.class);
			models.UserStorage.addUser(user);
	    	return ok("ADDED");	
	    } catch (Exception e) {
	    	return ok("ERROR- Login already exist in database");
		}
    }
    
    public Result index2(){
    	if(session("Login")==null){
    		return redirect(routes.HomeController.index());
    	}else{
    		return redirect(routes.HomeController.login());
    	}
    }
    /*
     * Action logging in User to system
     * TODO- Do it better;
     * @return Page with data
     */
    public Result loginController(){
    	User response;
		try {
            User user = Json.fromJson(request().body().asJson(), User.class);
			response = models.UserStorage.loginUser(user);
			session("Login",response.login);
	    	return ok("User "+response.login +" logged in");
		} catch (Exception e) {
			return ok("ERROR- Login not found");
		}
    }
    /*
     * Action logging out User to system
     * TODO- Do it better;
     * @return Page with login
     */
    public Result logoutController(){
    	User response;
		try {
            User user = Json.fromJson(request().body().asJson(), User.class);
			response = models.UserStorage.logoutUser(user);
			session().clear();
	    	return ok("User "+response.login+" logged out");
		} catch (Exception e) {
			return ok("ERROR- Login not found");
		}
    }
    public Result getUsers() {
        Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
        List<User> users = finder.all();
        return ok(toJson(users));
    }

}
