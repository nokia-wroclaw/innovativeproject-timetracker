package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import exceptions.ScheduleDayNotFoundException;
import exceptions.ScheduleNotFoundException;
import models.Privileges;
import models.Request;
import models.Schedule;

import models.Time;
import models.TimeStorage;
import models.User;
import play.libs.Json;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;

import views.html.*;


public class HomeController extends Controller {

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
    public Result settings(){
    	String login=session("Login");
    	return ok(settings.render(login));
    }
    public Result schedule(){
    	String login=session("Login");
    	List<Schedule> schedule2 = models.ScheduleStorage.getSchedule(login);
    	ok(toJson(schedule2));
    	return ok(schedule.render(login));
    }
    public Result privileges(){
    	String login=session("Login");
    	return ok(privileges.render(login));
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
    		JsonNode json=request().body().asJson();
        	models.TimeStorage.track(json);
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

		try {
    		JsonNode json=request().body().asJson();
			List<TimeStorage> result = models.TimeStorage.getData(json);
	    	return ok(toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
			return ok("ERROR");
		}
    }
    /*
     * Action to set schedule for front
     */
    public Result setschedule(){
    	String login=session("Login");
    	try{
    		JsonNode json=request().body().asJson();
    		models.ScheduleStorage.setSchedule(json,login);
        	return ok("OK");
    	}catch(Exception ex){
    		ex.printStackTrace();
    		return ok("ERROR");
    	}

    }
    /*
     * Action to get schedule for extension
     */
    public Result getschedule(){
    	//String login=session("LoginE");
		JsonNode json=request().body().asJson();
    	String login=json.findPath("login").textValue();
    	String day=json.findPath("day").textValue();
		
				try {
					List<Schedule> listSchedule = models.ScheduleStorage.getSchedule(login, day);
		        	return ok(toJson(listSchedule));
				} catch (ScheduleNotFoundException e) {
					System.out.println("ScheduleNotFoundException");
				    ObjectNode result = Json.newObject();
					return ok(result);
				} catch (ScheduleDayNotFoundException e) {
					System.out.println("ScheduleDayNotFoundException");
				    ObjectNode result = Json.newObject();
				    result.put("id", 0);
				    result.put("login", login);
				    result.put("day", day);
				    result.put("end", "00:00:00");
					return ok(result);
				}
    }
    public Result sendPaR() {
		JsonNode json=request().body().asJson();
    	String type=json.findPath("type").textValue();
    	String login=json.findPath("login").textValue();
    	String userlogin=session("Login");
    	if (type.contains("add")){
        	String result=models.RequestStorage.addRequest(userlogin,login);
        	return ok(result);			
    	}else if(type.contains("decline")){
        	String result=models.RequestStorage.deleteRequest(userlogin,login);
        	return ok(result);
    	}else if(type.contains("accept")){
        	String result=models.PrivilegesStorage.addPrivileges(userlogin,login);
        	return ok(result);
    	}else if(type.contains("delete")){
        	String result=models.PrivilegesStorage.deletePrivileges(userlogin,login);
        	return ok(result);
    	}else{
    		return ok("WRONG TYPE OF REQUEST");
    	}

    }
    
    public Result getPaR() {
		JsonNode json=request().body().asJson();
    	String type=json.findPath("type").textValue();
    	String login =session("Login");
    	if (type.contains("privilegesyou")){
        	List<Privileges> result=models.PrivilegesStorage.getYou(login);
        	return ok(toJson(result));		
    	}else if (type.contains("privilegesother")){
        	List<Privileges> result=models.PrivilegesStorage.getOther(login);
        	return ok(toJson(result));		
    	}else  if(type.contains("requestyou")){
        	List<Request> result=models.RequestStorage.getYou(login);
        	return ok(toJson(result));
    	}else  if (type.contains("requestother")){
        	List<Request> result=models.RequestStorage.getOther(login);
        	return ok(toJson(result));		
    	}else {
    		return ok("ERROR TYPE");
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
