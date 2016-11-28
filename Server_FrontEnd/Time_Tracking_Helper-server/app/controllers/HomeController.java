package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Model;

import models.Time;
import models.Tracking;
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
    	String login2= session("Login");
        return ok(login.render("Hello "+ login2));
    }
    
    public Result register() {
        return ok(register.render(""));
        
    }
    public Result timeline(){
    	String login=session("Login");
    	return ok(timeline.render(login));
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
    	if(session("Login")!=null){
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
	    	return ok("User "+response.login +" logged in, name: "+ response.name+" surname: "+ response.surname);
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
    /*
     * Action logging in User to system from Extension
     */
    public Result loginExtensionController(){
    	User response;
		try {
            User user = Json.fromJson(request().body().asJson(), User.class);
			response = models.UserStorage.loginUser(user);
			session("LoginE",response.login);
	    	return ok("User "+response.login +" logged in, name: "+ response.name+" surname: "+ response.surname);
		} catch (Exception e) {
			return ok("ERROR- Login not found");
		}
    }
    /*
     * Action logging out User to system from Extension
     */
    public Result logoutExtensionController(){
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
    /*
     * Action enabling tracking work from extension
     */
    public Result tracking() {
    	try{
    		//System.out.println("JESTEM1");
        	Tracking response = Json.fromJson(request().body().asJson(), Tracking.class);
    		//System.out.println("JESTEM2");
        	models.TimeStorage.track(response);
            return ok("OK");	
    	}catch (Exception e){
    		e.printStackTrace();
    		return ok("ERROR- Not added to database");
    	}

    }
    /*
     * Action sending Timeline to frontend
     */
    public Result sendData() {
    	List<Time> response;
		try {
            Time nick = Json.fromJson(request().body().asJson(), Time.class);
			response = models.TimeStorage.getData(nick);
	    	return ok(toJson(response));
		} catch (Exception e) {
			return ok("ERROR");
		}
    }
    
    public Result getUsers() {
        Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
        List<User> users = finder.all();
        return ok(toJson(users));
    }
    
    public Result getUserinfo() {
        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        List<Time> users = finder.all();
        return ok(toJson(users));
    }

}
