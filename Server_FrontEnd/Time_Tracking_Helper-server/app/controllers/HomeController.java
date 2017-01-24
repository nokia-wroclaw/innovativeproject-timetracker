package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import exceptions.IncorrectPasswordException;
import exceptions.ScheduleDayNotFoundException;
import exceptions.ScheduleNotFoundException;
import models.Privileges;
import models.Request;
import models.Schedule;
import models.SendedObject;
import services.ExcelGenerator;

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
        String login = session("Login");
        if (login != null) {
            return ok(index.render("Hello World!"));
        } else {
            return redirect(routes.HomeController.login());
        }
    }

    public Result login() {
        String login2 = session("Login");
        return ok(login.render("Hello " + login2));
    }

    public Result register() {
        return ok(register.render(""));

    }

    public Result timeline() {
        String login = session("Login");
        if (login != null) {
            return ok(timeline.render(login));
        } else {
            return redirect(routes.HomeController.login());
        }

    }

    public Result settings() {
        String login = session("Login");
        if (login != null) {
            return ok(settings.render(login));
        } else {
            return redirect(routes.HomeController.login());
        }
    }

    public Result schedule() {
        String login = session("Login");
        if (login != null) {
            List<Schedule> schedule2 = models.ScheduleStorage.getSchedule(login);
            ok(toJson(schedule2));
            return ok(schedule.render(login));
        } else {
            return redirect(routes.HomeController.login());
        }
    }

    public Result privileges() {
        String login = session("Login");
        if (login != null) {
            return ok(privileges.render(login));
        } else {
            return redirect(routes.HomeController.login());
        }
    }

    /*
     * Method adding User to database
     * TODO- Do it better;
     * @return Page with error
     */
    public Result addUserController() {
        try {
            User user = Json.fromJson(request().body().asJson(), User.class);
            models.UserStorage.addUser(user);
            return ok("ADDED");
        } catch (Exception e) {
            return ok("ERROR- Login already exist in database");
        }
    }

    public Result index2() {
        if (session("Login") != null) {
            return redirect(routes.HomeController.index());
        } else {
            return redirect(routes.HomeController.login());
        }
    }

    /*
     * Action logging in User to system
     * @return Page with data
     */
    public Result loginController() {
        User response;
        try {
            User user = Json.fromJson(request().body().asJson(), User.class);
            response = models.UserStorage.loginUser(user);
            session("Login", response.login);
            return ok("User " + response.login + " logged in, name: " + response.name + " surname: " + response.surname);
        } catch (Exception e) {
            return ok("ERROR- Login not found");
        }
    }

    /*
     * Action logging out User to system
     * @return Page with login
     */
    public Result logoutController() {
        User response;
        try {
            User user = Json.fromJson(request().body().asJson(), User.class);
            response = models.UserStorage.logoutUser(user);
            session().clear();
            return ok("User " + response.login + " logged out");
        } catch (Exception e) {
            return ok("ERROR- Login not found");
        }
    }

    /*
     * Action logging in User to system from Extension
     */
    public Result loginExtensionController() {
        User response;
        try {
            User user = Json.fromJson(request().body().asJson(), User.class);
            response = models.UserStorage.loginUser(user);
            session("LoginE", response.login);
            return ok("User " + response.login + " logged in, name: " + response.name + " surname: " + response.surname);
        } catch (Exception e) {
            return ok("ERROR- Login not found");
        }
    }

    /*
     * Action logging out User to system from Extension
     */
    public Result logoutExtensionController() {
        User response;
        try {
            User user = Json.fromJson(request().body().asJson(), User.class);
            response = models.UserStorage.logoutUser(user);
            session().clear();
            return ok("User " + response.login + " logged out");
        } catch (Exception e) {
            return ok("ERROR- Login not found");
        }
    }

    /*
     * Action enabling tracking work from extension
     */
    public Result tracking() {
        try {
            JsonNode json = request().body().asJson();
            models.TimeStorage.track(json);
            return ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ok("ERROR- Not added to database");
        }

    }

    /*
     * Action sending user's Timeline to frontend and to main site
     */
    public Result sendUserTimelineData() {

        try {
            JsonNode json = request().body().asJson();
            String login = session("Login");
            List<TimeStorage> result = models.TimeStorage.getDataSession(login, json);
            return ok(toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            return ok("ERROR");
        }
    }


    public Result sendOtherTimelineData() {

        try {
            JsonNode json = request().body().asJson();
            String userlogin = session("Login");
            List<TimeStorage> result = models.TimeStorage.getDataLogin(json, userlogin);
            return ok(toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            return ok("ERROR");
        }
    }

    /*
     * Action to set schedule for front
     */
    public Result setschedule() {
        String login = session("Login");
        try {
            JsonNode json = request().body().asJson();
            models.ScheduleStorage.setSchedule(json, login);
            return ok("OK");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ok("ERROR");
        }

    }

    /*
     * Action to get schedule for extension
     */

    public Result getschedule() {
        //String login=session("LoginE");
        JsonNode json = request().body().asJson();
        String login = json.findPath("login").textValue();
        String day = json.findPath("day").textValue();

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
        JsonNode json = request().body().asJson();
        String type = json.findPath("type").textValue();
        String login = json.findPath("login").textValue();
        String userlogin = session("Login");
        if (type.contains("add")) {
            //użytkownik A wysyla zaproszenie do użytkownika B
            String result = models.RequestStorage.addRequest(userlogin, login);
            return ok(result);
        } else if (type.contains("decline")) {
            //użytkownik A cofa zaproszenie do użytkownika B
            String result = models.RequestStorage.deleteRequest(userlogin, login);
            return ok(result);
        } else if (type.contains("acceptrequest")) {
            //użytkownik B akceptuje zaproszenie użytkownika A
            String result = models.PrivilegesStorage.addPrivileges(userlogin, login);
            return ok(result);
        } else if (type.contains("decrequest")) {
            //użytkownik B odrzuca zaproszenie użytkownika A
            String result = models.RequestStorage.declinePrivileges(userlogin, login);
            return ok(result);
        } else if (type.contains("deletefrom")) {
            //użytkownik B usuwa z dostępu użytkownika A
            String result = models.PrivilegesStorage.deleteFromPrivileges(userlogin, login);
            return ok(result);
        } else if (type.contains("deleteto")) {
            //użytkownik A usuwa z dostępu użytkownika B
            String result = models.PrivilegesStorage.deleteToPrivileges(userlogin, login);
            return ok(result);
        } else {
            return ok("WRONG TYPE OF REQUEST");
        }

    }

    public Result getPaR() {
        JsonNode json = request().body().asJson();
        String type = json.findPath("type").textValue();
        String login = session("Login");
        if (type.contains("privilegesyou")) {
            List<SendedObject> result = models.PrivilegesStorage.getYou(login);
            return ok(toJson(result));
        } else if (type.contains("privilegesother")) {
            List<SendedObject> result = models.PrivilegesStorage.getOther(login);
            return ok(toJson(result));
        } else if (type.contains("requestyou")) {
            List<SendedObject> result = models.RequestStorage.getYou(login);
            return ok(toJson(result));
        } else if (type.contains("requestother")) {
            List<SendedObject> result = models.RequestStorage.getOther(login);
            return ok(toJson(result));
        } else {
            return ok("ERROR TYPE");
        }
    }

    public Result setEstimatedHours() {
        //number- liczba estimated hours
        //login- login użytkownika, któremu zmieniamy estimated hours
        String userlogin = session("Login");
        try {
            JsonNode json = request().body().asJson();
            String number = json.findPath("number").textValue();
            String login = json.findPath("login").textValue();
            String result = models.PrivilegesStorage.setEstimated(userlogin, login, number);
            return ok(result);
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public Result generateExcel() {
        JsonNode json = request().body().asJson();
        String login = session("Login");
        long begin = json.findPath("begin").numberValue().longValue();
        long end = json.findPath("end").numberValue().longValue();
        List<Schedule> weeklySchedule = models.ScheduleStorage.getSchedule(login);
        List<Time> timeline = models.TimeStorage.getTimeline(login, begin, end);
        ExcelGenerator generator = new ExcelGenerator(weeklySchedule, timeline, begin, end);
        generator.generateExcel();
        System.out.println("Wygenerowano excela, zwracam plik...");
        return ok(new java.io.File("app/NewExcelFile.xlsx"));
    }

    public Result sendWorkedHours() {
        String login = request().body().asJson().findPath("login").textValue();
        ObjectNode result = models.TimeStorage.getWorkedHours(login);
        return ok(result);
    }

    public Result updateTimelineDay() {
        models.TimeStorage.updateDay(session("Login"), request().body().asJson());
        return ok();
    }

    public Result sendFullSchedule() {
        List<Schedule> weeklySchedule = models.ScheduleStorage.getSchedule(session("Login"));
        System.out.println(toJson(weeklySchedule));
        return ok(toJson(weeklySchedule));
    }

    public Result changeUserInfo() {
        try {
            models.UserStorage.changeUserInfo(session("Login"), request().body().asJson());
        } catch (IncorrectPasswordException e) {
            return badRequest();
        }
        return ok();
    }
}