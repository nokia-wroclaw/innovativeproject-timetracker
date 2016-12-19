package models;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

public class TimeStorage {

	
	String name;
	String begin;
	String end;
	
	public TimeStorage(String name,String begin,String end){
		this.name=name;
		this.begin=begin;
		this.end=end;
	}
    public void setName(String login) {
        this.name = login;
    }
    public String getName() {
        return name;
    }
    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getBegin() {
        return begin;
    }
    public void setEnd(String end) {
        this.end = end;
    }
    public String getEnd() {
        return end;
    }
    
    /*
     * Funkcja zwracajaca dane dotyczace użytkownika nick
     */
    
	public static List<TimeStorage> getData(JsonNode json) throws Exception {

		Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        String name=json.findValue("login").textValue();
        java.util.Date date1=fromStringToDate(json.findValue("begin").textValue());
        java.util.Date date2=fromStringToDate(json.findValue("end").textValue());
        List<Time> test=finder.all();
        if(test.isEmpty()){
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
            Time record9=new Time("Wmblady",fromStringToDate("14/12/2016@08:00"),fromStringToDate("14/12/2016@16:00"));
            record9.save();  
            Time record10=new Time("Wmblady",fromStringToDate("15/12/2016@09:00"),fromStringToDate("15/12/2016@17:30"));
            record10.save();
            Time record11=new Time("Wmblady",fromStringToDate("16/12/2016@07:30"),fromStringToDate("16/12/2016@13:30"));
            record11.save();  
            Time record12=new Time("Wmblady",fromStringToDate("16/12/2016@14:00"),fromStringToDate("16/12/2016@16:00"));
            record12.save();  
        }        
        List<Time> t= finder.where().and().eq("login", name).ge("begin", date1).le("end", date2).findList();

        if (t==null){
        	throw new Exception();
        }else{
        	Iterator<Time> iterator = t.iterator();
        	int i=0;
        	List<TimeStorage> t2 = new ArrayList<TimeStorage>();
        	while(iterator.hasNext()){
        		Time krotka = t.get(i);
        		String sbegin=fromDateToString(krotka.begin);
        		String send=fromDateToString(krotka.end);
        		TimeStorage krotkamodyfikowana= new TimeStorage(krotka.login,sbegin,send);
        		t2.add(krotkamodyfikowana);
        		iterator.next();
        		i++;
        	}
    		return t2;       	
        }

	}
	/*
	 * Funkcja służaca do uaktualniania czasu pracy w bazie danych
	 * response- odpowiedz wyslana przez wtyczke
	 */
	public static void track(JsonNode json) {
        String name=json.findValue("login").textValue();
        String pass=json.findValue("password").textValue();
        String daterequest=json.findValue("date").textValue();
        String state= json.findValue("state").textValue();
        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
 
       if(state.equals("Start")){
        	Time record=new Time(name,fromStringToDate(daterequest),fromStringToDate(daterequest));
        	record.save();
       }
        if(state.equals("Continue")){
                List<Time> tc= finder.where().eq("login", name).findList();
        	    Time temp=tc.get(tc.size()-1);
        	    Date datereq=fromStringToDate(daterequest);
        	    if((temp.end.getHours()==datereq.getHours())||(temp.end.getHours()+1==datereq.getHours())
        	      ||((temp.end.getHours()==23)&&(datereq.getHours()==0)))
        	    {
        	    	if(temp.end.getHours()==datereq.getHours())
        	    	{
        	    		if(temp.end.getMinutes()+15>=datereq.getMinutes())
        	    		{
        	    			tc.get(tc.size()-1).setEnd(datereq);
        	        		tc.get(tc.size()-1).update();
        	    		}
        	    		else
        	    		{
        	    			Time record=new Time(name,datereq,datereq);
        	            	record.save();
        	    		}
        	    	}
        	    	else
        	    	{
        	    		if(temp.end.getMinutes()>=45)
        	    		{
        	    			if((temp.end.getMinutes()+15)%60>=datereq.getMinutes())
        	    			{
        	    				tc.get(tc.size()-1).setEnd(datereq);
            	        		tc.get(tc.size()-1).update();
        	    			}
        	    			else
        	    			{
        	    				Time record=new Time(name,datereq,datereq);
            	            	record.save();
        	    			}
        	    		}
        	    		else
        	    		{
        	    			Time record=new Time(name,datereq,datereq);
        	            	record.save();
        	    		}
        	    	}
        	    }
        	    else
        	    {
        	    	Time record=new Time(name,datereq,datereq);
	            	record.save();
        	    }	 
        }
        if(state.equals("End")){
            List<Time> te= finder.where().eq("login", name).findList();
        	te.get(te.size()-1).setEnd(fromStringToDate(daterequest));
    		te.get(te.size()-1).update();
        }

	}	
	public static java.util.Date fromStringToDate(String stringDate){
        DateFormat dateFrm = new SimpleDateFormat("dd/MM/yyyy@HH:mm");
        
        java.util.Date daterequest = null;
		try {
			daterequest = dateFrm.parse(stringDate);
		} catch (ParseException e) {
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

