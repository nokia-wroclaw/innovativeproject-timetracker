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
     * Akcja sluzaca do dodania uzytkownika do tabeli Users
     * @return: przekierowanie na strone glowna
     * TODO: weryfikacja danych (wykrycie bledow, zdublowanie loginow itp.)
     */
    public Result addUser() {
        Form<Users> userForm = formFactory.form(Users.class);
        
        Users user = userForm.bindFromRequest().get();
        user.save();
        //return ok("Product: " + product.name); // DEBUG
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
