package models;

import java.util.List;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

public class RequestStorage {


	public static List<Request> getYou(String login) {
		Model.Finder<Integer, Request> finder = new Model.Finder<>(Request.class);
		List<Request> t1 = finder.where().eq("userfrom", login).findList();
		return t1;
	}

	public static List<Request> getOther(String login) {
		Model.Finder<Integer, Request> finder = new Model.Finder<>(Request.class);
		List<Request> t1 = finder.where().eq("userto", login).findList();
		return t1;
	}

	public static String addRequest(String userlogin, String login) {
		if (userlogin.contains(login)){
			return "ENTERED OWN NICK";
		}
		Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
		User t1 = finder.where().eq("login", login).findUnique();
		if(t1==null){
			return "NICK NOT IN DATABASE";
		}
		Model.Finder<Integer, Request> finder2 = new Model.Finder<>(Request.class);
		List<Request> t2 = finder2.where().and().eq("userfrom", userlogin).eq("userto", login).findList();
		if (t2.isEmpty()==false){
			return "REQUEST ALREADY IN DATABASE";	
		}
		Model.Finder<Integer, Privileges> finder3 = new Model.Finder<>(Privileges.class);
		List<Privileges> t3 = finder3.where().and().eq("userfrom", userlogin).eq("userto", login).findList();
		if(t3.isEmpty()==false){
			return "PRIVILEGE ALREADY IN DATABASE";
		}
		Request toadd= new Request(userlogin,login);
		toadd.save();
		return "ADDED";
	}
	
	public static String deleteRequest(String userlogin, String login){
		if (userlogin.contains(login)){
			return "ENTERED OWN NICK";
		}
		Model.Finder<Integer, User> finder = new Model.Finder<>(User.class);
		User t1 = finder.where().eq("login", login).findUnique();
		if(t1==null){
			return "NICK NOT IN DATABASE";
		}
		Model.Finder<Integer, Request> finder2 = new Model.Finder<>(Request.class);
		Request t2= finder2.where().and().eq("userfrom", userlogin).eq("userto", login).findUnique();
		if(t2.delete()){
			return "DELETED";
		}else{
			return "ERROR";
		}
	}

	/*
	 * TODO: metody do obsługi zapytań o pokazanie harmonogramów:
	 * -zablokowac zapytania z tym samym nickiem (np. "Kruk07" "Kruk07")
	 * -zablokowac powtorne wysylanie tych samych zapytan, jeśli zostaly wyslane wczesniej
	 * -zablokowac zapytania, jesli istnieja takie same krotki w priveleges
	 * - zablokowac zapytanie, jesli nie istnieje nick w bazie "User"
	 * 
	 * 
	 * Po zaakceptowaniu zaproszenia usunac krotkę z tabeli Request i dodac ja do tabeli Priveleges
	 * 
	 * obsluga zapytan do frontendu:
	 * "Users that request for adding you"- Select Request po "userto"
	 * "Users that you request for adding"- Select Request po "userfrom"
	 * "Users that added you" - Select Privileges po "userto"
	 * "Users that you added"- Select Privileges po "userfrom"
	 * 
	 * usuwanie- powinno byc zrobione i dla Priveleges i dla Request, w zależności od akcji w froncie
	 */
}
