package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.avaje.ebean.Model;

public class PrivilegesStorage {

	public static List<SendedObject> getYou(String login) {
		Model.Finder<Integer, Privileges> finder = new Model.Finder<>(Privileges.class);
		
		List<Privileges> t1 = finder.where().eq("userfrom", login).findList();

		Iterator<Privileges> iterator = t1.iterator();
		List<SendedObject> finallist = new ArrayList<SendedObject>();
		int id=0;
		while (iterator.hasNext()){
			
			Model.Finder<Integer, User> finder2 = new Model.Finder<>(User.class);
			Privileges item = t1.get(id);
			User usernameandsurname = finder2.where().eq("login", item.userto).findUnique();

			SendedObject obj=
					new SendedObject(id,usernameandsurname.name,usernameandsurname.surname,item.userfrom,item.userto,item.estimatedHours);
			finallist.add(obj);
			iterator.next();
			id++;
		}
		return finallist;
	}

	public static List<SendedObject> getOther(String login) {
		Model.Finder<Integer, Privileges> finder = new Model.Finder<>(Privileges.class);
		List<Privileges> t1 = finder.where().eq("userto", login).findList();
		
		Iterator<Privileges> iterator = t1.iterator();
		List<SendedObject> finallist = new ArrayList<SendedObject>();
		int id=0;
		while (iterator.hasNext()){
			
			Model.Finder<Integer, User> finder2 = new Model.Finder<>(User.class);
			Privileges item = t1.get(id);
			User usernameandsurname = finder2.where().eq("login",item.userfrom).findUnique();

			SendedObject obj=
					new SendedObject(id,usernameandsurname.name,usernameandsurname.surname,item.userfrom,item.userto,item.estimatedHours);
			finallist.add(obj);
			iterator.next();
			id++;
		}
		return finallist;
	}
	
	
	public static String addPrivileges(String userlogin, String login) {

		Model.Finder<Integer, Request> finder = new Model.Finder<>(Request.class);
		Request t2= finder.where().and().eq("userfrom", login).eq("userto", userlogin).findUnique();

		t2.delete();
		Privileges toadd=new Privileges(login,userlogin);
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

