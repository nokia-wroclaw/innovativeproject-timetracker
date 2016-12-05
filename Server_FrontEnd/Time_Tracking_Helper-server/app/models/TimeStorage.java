package models;


import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.avaje.ebean.Model;

public class TimeStorage {
    /*
     * Funkcja zwracajaca dane dotyczace użytkownika nick
     */
	public static List<Time2> getData(Time2 nick) throws Exception {
		// TODO Auto-generated method stub
        //Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
		Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        String name=nick.login;
        System.out.println("BEFORE "+name+ " "+ nick.begin+ " "+ nick.end);
        java.util.Date date1=fromStringToDate(nick.begin);
        java.util.Date date2=fromStringToDate(nick.end);
        /*String begin=fromDateToString(date1);
        String end=fromDateToString(date2);
        System.out.println("AFTER "+name+ " "+ begin+ " "+ end);*/
        List<Time> t= finder.where().and().eq("login", name).findList();
        t=finder.where().and().ge("begin", date1).le("end", date2).findList();
        if(t.isEmpty()){
            /*
             * Ponizsze rekordy dodane, by Patrykowi sie nie nudzilo
             * gdy metoda "track" zostanie zaimplementowana, usunac.
             */
            Time record=new Time("Kruk07",fromStringToDate("12/12/2016@12:12"),fromStringToDate("12/12/2016@14:14"));
            record.save();
            Time record2=new Time("Wmblady",fromStringToDate("12/12/2016@11:54"),fromStringToDate("12/12/2016@15:34"));
            record2.save();
            Time record3=new Time("Kruk07",fromStringToDate("12/12/2016@16:00"),fromStringToDate("12/12/2016@17:54"));
            record3.save();
            Time record4=new Time("Wmblady",fromStringToDate("12/12/2016@16:11"),fromStringToDate("12/12/2016@18:30"));
            record4.save();
            Time record5=new Time("Kruk07",fromStringToDate("13/12/2016@08:05"),fromStringToDate("13/12/2016@15:54"));
            record5.save();
            Time record6=new Time("Wmblady",fromStringToDate("13/12/2016@08:09"),fromStringToDate("13/12/2016@13:54"));
            record6.save();
            Time record7=new Time("Kruk07",fromStringToDate("13/12/2016@16:01"),fromStringToDate("13/12/2016@19:00"));
            record7.save();
            Time record8=new Time("Wmblady",fromStringToDate("13/12/2016@14:00"),fromStringToDate("13/12/2016@18:59"));
            record8.save();     	
        }
        if (t==null){
        	throw new Exception();
        }else{
        	Iterator<Time> iterator = t.iterator();
        	int i=0;
        	List<Time2> t2 = new ArrayList<Time2>();
        	while(iterator.hasNext()){
        		Time krotka = t.get(i);
        		String sbegin=fromDateToString(krotka.begin);
        		String send=fromDateToString(krotka.end);
        		Time2 krotkamodyfikowana= new Time2(krotka.login,sbegin,send);
        		t2.add(krotkamodyfikowana);
        		iterator.next();
        	}
    		return t2;       	
        }

	}
	public static void track(Tracking response) {
		System.out.println("Funkcja zastępcza, by nie wyrzucało błędów");
	
	}
	/*
	 * Funkcja służaca do uaktualniania czasu pracy w bazie danych
	 * response- odpowiedz wyslana przez wtyczke
	 * ODKOMENTOWAC I POPRAWIC-PODOBNIE JAK WYZEJ JEST ZROBIONY GETDATA
	 */
/*	public static void track(Tracking response) {
        String name=response.login;
        String pass=response.password;
        String daterequest=response.date;
        String state= response.state;
        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
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
 /*       if(state.equals("Start")){
        	System.out.println("START");
        	Time record=new Time(name,daterequest,daterequest);
        	record.save();
        	/*
        	 * Dopisac reszte funkcji
        	 * Przeanalizowac mozliwe sytuacje przy stanie "Start"
        	 * To wyżej to prowizorka, by strona mogla jakos cos testowac
        	 */
        	
        			
  /*      }
        if(state.equals("Continue")){
        	/*
        	 * Mozliwe sytuacje tutaj: <15minut- update, else save
        	 * Dopisac
                 */
 /*               List<Time> tc= finder.where().eq("login", name).findList();
        	Time temp=tc.get(tc.size()-1);
                if((Integer.parseInt(temp.end.substring(11,13))==Integer.parseInt(daterequest.substring(11,13)))||
                (Integer.parseInt(temp.end.substring(11,13))==Integer.parseInt(daterequest.substring(11,13))-1)||
                ((temp.end.substring(11,13).equals("23"))&&(daterequest.substring(11,13).equals("00")))){
                        if(Integer.parseInt(temp.end.substring(11,13))==Integer.parseInt(daterequest.substring(11,13))){
                               if(Integer.parseInt(temp.end.substring(14,16))+15>=Integer.parseInt(daterequest.substring(14,16))){
        		               tc.get(tc.size()-1).setEnd(daterequest);
        		               tc.get(tc.size()-1).update();
        	               }
                               else{
                                       Time record=new Time(name,daterequest,daterequest);
                	               record.save();
                               }
                        }
                        else{
                               if(Integer.parseInt(temp.end.substring(14,16))>=45){
                                      if((Integer.parseInt(temp.end.substring(14,16))+15)%60>=Integer.parseInt(daterequest.substring(14,16))){
        		                      tc.get(tc.size()-1).setEnd(daterequest);
        		                      tc.get(tc.size()-1).update();
        	                      }
                                      else{
                                               Time record=new Time(name,daterequest,daterequest);
                	                       record.save();
                                      }
                              }
                              else{
        			  Time record=new Time(name,daterequest,daterequest);
                	          record.save();
                              }
                 
                       } 
                }
                else{  
                       Time record=new Time(name,daterequest,daterequest);
                       record.save();
                }    
        	System.out.println("Continue");
        }
        if(state.equals("End")){
        	/*
        	 * Możliwe sytuacje- update, badz save, jak wyzej
        	 * Czy jeszcze jakies sytuacje sa tutaj mozliwe?
        	 * 
        	 */
 /*               List<Time> te= finder.where().eq("login", name).findList();
        	te.get(te.size()-1).setEnd(daterequest);
    		te.get(te.size()-1).update();
        	System.out.println("End");
        }

	}
*/	
	public static java.util.Date fromStringToDate(String stringDate){
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
	}
	public static String fromDateToString(java.util.Date daterequest){
		
        DateFormat dateFrm = new SimpleDateFormat("dd/MM/yyyy@HH:mm");
		String	datestring= dateFrm.format(daterequest);
		return datestring;
	}

}
