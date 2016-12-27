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
		Model.Finder<Integer, Privileges> finder3 = new Model.Finder<>(Privileges.class);
		List<Privileges> t3 = finder3.where().and().eq("userfrom", userlogin).eq("userto", login).findList();
		if(t3.isEmpty()==false){
			return "PRIVILEGE ALREADY IN DATABASE";
		}
		Model.Finder<Integer, Request> finder = new Model.Finder<>(Request.class);
		Request t2= finder.where().and().eq("userfrom", userlogin).eq("userto", login).findUnique();

		t2.delete();
		Privileges toadd=new Privileges(userlogin,login);
		toadd.save();
		return "OK";

	}
	
	public static String deletePrivileges(String userlogin, String login){
		Model.Finder<Integer, Privileges> finder = new Model.Finder<>(Privileges.class);
		Privileges t2= finder.where().and().eq("userfrom", userlogin).eq("userto", login).findUnique();
		t2.delete();
		return "DELETED";

	}

}
