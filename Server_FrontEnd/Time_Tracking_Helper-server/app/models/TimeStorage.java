package models;


import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import com.avaje.ebean.Model;

public class TimeStorage {
    /*
     * Funkcja zwracajaca dane dotyczace użytkownika nick
     */
	public static List<Time> getData(Time nick) throws Exception {
		// TODO Auto-generated method stub
        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        String name=nick.login;
        List<Time> t= finder.where().eq("login", name).findList();
        if(t.isEmpty()){
            /*
             * Ponizsze rekordy dodane, by Patrykowi sie nie nudzilo
             * gdy metoda "track" zostanie zaimplementowana, usunac.
             */
            Time record=new Time("Kruk07","12/12/2016@12:12","12/12/2016@14:14");
            record.save();
            Time record2=new Time("Wmblady","12/12/2016@11:54","12/12/2016@15:34");
            record2.save();
            Time record3=new Time("Kruk07","12/12/2016@16:00","12/12/2016@17:54");
            record3.save();
            Time record4=new Time("Wmblady","12/12/2016@16:11","12/12/2016@18:30");
            record4.save();
            Time record5=new Time("Kruk07","13/12/2016@08:05","13/12/2016@15:54");
            record5.save();
            Time record6=new Time("Wmblady","13/12/2016@08:09","13/12/2016@13:54");
            record6.save();
            Time record7=new Time("Kruk07","13/12/2016@16:01","13/12/2016@19:00");
            record7.save();
            Time record8=new Time("Wmblady","13/12/2016@14:00","13/12/2016@18:59");
            record8.save();     	
        }
        if (t==null){
        	throw new Exception();
        }else{
    		return t;       	
        }

	}
	/*
	 * Funkcja służaca do uaktualniania czasu pracy w bazie danych
	 * response- odpowiedz wyslana przez wtyczke
	 * 
	 */
	public static void track(Tracking response) {
        String name=response.login;
        String pass=response.password;
        String daterequest=response.date;
        String state= response.state;
        /*
         * Niżej skomentowane linijki służyły do konwersji ze String na Date
         * Jest problem z porownywaniem stringów "begin" i "end" z bazy danych (trzeba wybrac krotke z najmłodszą datą "end")
         * Wymyśl cos odpowiedniego
         */
        //java.util.Date daterequest= fromStringToDate(response.date);
        //System.out.println(name+" "+ pass+" "+response.date+" "+daterequest+" "+state);
        //Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        //List<Time> time=finder.where().eq("login",name).findList();
        //Comparator<? super Time> c = null;
		//time.sort(c);
        if(state.equals("Start")){
        	System.out.println("START");
        	Time record=new Time(name,daterequest,daterequest);
        	record.save();
        	/*
        	 * Dopisac reszte funkcji
        	 * Przeanalizowac mozliwe sytuacje przy stanie "Start"
        	 * To wyżej to prowizorka, by strona mogla jakos cos testowac
        	 */
        	
        			
        }
        if(state.equals("Continue")){
        	/*
        	 * Mozliwe sytuacje tutaj: <15minut- update, else save
        	 * Dopisac
        	 */
        	System.out.println("Continue");
        }
        if(state.equals("End")){
        	/*
        	 * Możliwe sytuacje- update, badz save, jak wyzej
        	 * Czy jeszcze jakies sytuacje sa tutaj mozliwe?
        	 * 
        	 */
        	System.out.println("End");
        }

	}
	
	/*public static java.util.Date fromStringToDate(String stringDate){
        DateFormat dateFrm = new SimpleDateFormat("dd/MM/yyyy@HH:mm");
        
        java.util.Date daterequest = null;
		try {
			daterequest = dateFrm.parse(stringDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("COS Z DATA");
			e.printStackTrace();
		}
		return daterequest;
	}*/

}
