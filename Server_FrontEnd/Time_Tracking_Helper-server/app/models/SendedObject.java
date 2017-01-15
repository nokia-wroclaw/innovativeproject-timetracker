package models;

/*
 * Klasa do wysyłania danych o uprawnienia
 * TODO: Usunąc ją i połaczyc innym sposobem dwie listy (jak będzie czas... )
 */

public class SendedObject{
	int id;
	String name;
	String surname;
	String userfrom;
	String userto;
	int hours;
	SendedObject(int id,String name, String surname,String userfrom,String userto, int hours){
		this.id=id;
		this.name=name;
		this.surname=surname;
		this.userfrom=userfrom;
		this.userto=userto;
		this.hours=hours;
	}
	SendedObject(int id,String name, String surname,String userfrom,String userto){
		this.id=id;
		this.name=name;
		this.surname=surname;
		this.userfrom=userfrom;
		this.userto=userto;
	}
	   public void setId(int id) {
	        this.id = id;
	    }

	    public int getId() {
	        return id;
	    }
	   public void setName(String name) {
	        this.name = name;
	    }

	    public String getName() {
	        return name;
	    }
	    public void setSurname(String surname) {
	        this.surname = surname;
	    }

	    public String getSurname() {
	        return surname;
	    }
    public void setuserfrom(String userfrom) {
        this.userfrom = userfrom;
    }
    public String getUserfrom() {
        return userfrom;
    }
    public void setuserto(String userto) {
        this.userto = userto;
    }
    public String getUserto() {
        return userto;
    }
    public void setHours(int hours) {
        this.hours = hours;
    }
    public int getEstimatedHours() {
        return hours;
    }
}