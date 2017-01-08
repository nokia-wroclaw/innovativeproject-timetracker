package models;

import java.util.List;

import com.avaje.ebean.Model;

public class PrivilegesStorage {

	public static List<Privileges> getYou(String login) {
		Model.Finder<Integer, Privileges> finder = new Model.Finder<>(Privileges.class);
		
		List<Privileges> t1 = finder.where().eq("userfrom", login).findList();
		return t1;
	}

	public static List<Privileges> getOther(String login) {
		Model.Finder<Integer, Privileges> finder = new Model.Finder<>(Privileges.class);
		List<Privileges> t1 = finder.where().eq("userto", login).findList();
		return t1;
	}
	
	
	public static String addPrivileges(String userlogin, String login) {

		Model.Finder<Integer, Request> finder = new Model.Finder<>(Request.class);
		Request t2= finder.where().and().eq("userfrom", login).eq("userto", userlogin).findUnique();

		t2.delete();
		Privileges toadd=new Privileges(userlogin,login);
		toadd.save();
		return "OK";

	}
	
	public static String deleteFromPrivileges(String userlogin, String login){
		Model.Finder<Integer, Privileges> finder = new Model.Finder<>(Privileges.class);
		Privileges t2= finder.where().and().eq("userfrom", userlogin).eq("userto", login).findUnique();
		t2.delete();
		return "DELETED";

	}

	public static String deleteToPrivileges(String userlogin, String login) {
		Model.Finder<Integer, Privileges> finder = new Model.Finder<>(Privileges.class);
		Privileges t2= finder.where().and().eq("userfrom", login).eq("userto", userlogin).findUnique();
		t2.delete();
		return "DELETED";
	}

	public static String setEstimated(String userlogin, String login, String number) {
		
		Model.Finder<Integer, Privileges> finder = new Model.Finder<>(Privileges.class);
		Privileges t1 = finder.where().and().eq("userfrom", login).eq("userto", userlogin).findUnique();
		if (t1==null){
			return "ERROR";
		}
		t1.setEstimatedHours(Integer.parseInt(number));
		t1.update();
		
		
		return "OK";
	}



}
