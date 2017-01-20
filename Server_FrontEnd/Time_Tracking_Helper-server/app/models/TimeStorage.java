package models;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class TimeStorage {


    String name;
    String begin;
    String end;

    public TimeStorage(String name, String begin, String end) {
        this.name = name;
        this.begin = begin;
        this.end = end;
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

    public static List<TimeStorage> getDataSession(String login, JsonNode json) throws Exception {

        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        String name = login;
        java.util.Date date1 = fromStringToDate(json.findValue("begin").textValue());
        java.util.Date date2 = fromStringToDate(json.findValue("end").textValue());
        List<Time> test = finder.all();
        if (test.isEmpty()) {
            Time record15 = new Time("Wmblady", fromStringToDate("02/01/2017@08:00"), fromStringToDate("02/01/2017@16:00"));
            record15.save();
            Time record16 = new Time("Kruk07", fromStringToDate("03/01/2017@07:00"), fromStringToDate("03/01/2017@15:00"));
            record16.save();
            Time record5 = new Time("Kruk07", fromStringToDate("06/01/2017@07:00"), fromStringToDate("06/01/2017@15:00"));
            record5.save();
            Time record12 = new Time("Wmblady", fromStringToDate("06/01/2017@14:00"), fromStringToDate("06/01/2017@16:00"));
            record12.save();
            Time record11 = new Time("Wmblady", fromStringToDate("07/01/2017@14:00"), fromStringToDate("07/01/2017@16:00"));
            record11.save();
            Time record10 = new Time("Wmblady", fromStringToDate("08/01/2017@09:00"), fromStringToDate("08/01/2017@14:00"));
            record10.save();
            Time record7 = new Time("Kruk07", fromStringToDate("09/01/2017@10:00"), fromStringToDate("09/01/2017@15:00"));
            record7.save();
            Time record9 = new Time("Wmblady", fromStringToDate("09/01/2017@08:00"), fromStringToDate("09/01/2017@14:30"));
            record9.save();
            Time record8 = new Time("Wmblady", fromStringToDate("09/01/2017@15:00"), fromStringToDate("09/01/2017@17:00"));
            record8.save();
            Time record6 = new Time("Wmblady", fromStringToDate("10/01/2017@11:00"), fromStringToDate("10/01/2017@19:00"));
            record6.save();
            Time record2 = new Time("Wmblady", fromStringToDate("12/01/2017@11:54"), fromStringToDate("12/01/2017@15:34"));
            record2.save();
            Time record = new Time("Kruk07", fromStringToDate("12/01/2017@12:12"), fromStringToDate("12/01/2017@14:14"));
            record.save();
            Time record4 = new Time("Wmblady", fromStringToDate("12/01/2017@16:11"), fromStringToDate("12/01/2017@18:30"));
            record4.save();
            Time record3 = new Time("Kruk07", fromStringToDate("13/01/2017@07:00"), fromStringToDate("13/01/2017@14:00"));
            record3.save();
            Time record13 = new Time("Wmblady", fromStringToDate("13/01/2017@08:00"), fromStringToDate("13/01/2017@16:00"));
            record13.save();
            Time record14 = new Time("Kruk07", fromStringToDate("16/01/2017@07:00"), fromStringToDate("16/01/2017@15:00"));
            record14.save();
        }
        List<Time> t = finder.where().and().eq("login", name).ge("begin", date1).le("end", date2).findList();

        if (t == null) {
            throw new Exception();
        } else {
            Iterator<Time> iterator = t.iterator();
            int i = 0;
            List<TimeStorage> t2 = new ArrayList<TimeStorage>();
            while (iterator.hasNext()) {
                Time krotka = t.get(i);
                String sbegin = fromDateToString(krotka.begin);
                String send = fromDateToString(krotka.end);
                TimeStorage krotkamodyfikowana = new TimeStorage(krotka.login, sbegin, send);
                t2.add(krotkamodyfikowana);
                iterator.next();
                i++;
            }
            return t2;
        }

    }

    /*
     * Metoda do wyświetlania timeline-u - z wyslanym w jsonie nickiem
     * TODO: weryfikacja, czy użytkownik z sesji ma uprawnienie do przeglądania timeline-u osoby wyslanej w nicku
     * 
     */
    public static List<TimeStorage> getDataLogin(JsonNode json, String userlogin) throws Exception {

        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        String name = json.findValue("login").textValue();
        String range = json.findValue("range").textValue();
        Model.Finder<Integer, Privileges> finder2 = new Model.Finder<>(Privileges.class);

        List<Privileges> t23 = finder2.where().and().eq("userfrom", userlogin).eq("userto", name).findList();
        if (t23 == null) {
            throw new Exception("BRAK UPRAWNIEN");
        }
        System.out.println(name);
        System.out.println(range);
        int days;
        if (range.contains("7")) {
            days = 7;
        } else if (range.contains("31")) {
            days = 31;
        } else {
            throw new Exception("NIEPOPRAWNY FORMAT");
        }
        Calendar calendar = Calendar.getInstance();
        java.util.Date date2 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        java.util.Date date1 = calendar.getTime();
        //java.util.Date date1 = fromStringToDate(json.findValue("begin").textValue());
        //java.util.Date date2 = fromStringToDate(json.findValue("end").textValue());


        List<Time> test = finder.all();
        if (test.isEmpty()) {
            Time record15 = new Time("Wmblady", fromStringToDate("02/01/2017@08:00"), fromStringToDate("02/01/2017@16:00"));
            record15.save();
            Time record16 = new Time("Kruk07", fromStringToDate("03/01/2017@07:00"), fromStringToDate("03/01/2017@15:00"));
            record16.save();
            Time record5 = new Time("Kruk07", fromStringToDate("06/01/2017@07:00"), fromStringToDate("06/01/2017@15:00"));
            record5.save();
            Time record12 = new Time("Wmblady", fromStringToDate("06/01/2017@14:00"), fromStringToDate("06/01/2017@16:00"));
            record12.save();
            Time record11 = new Time("Wmblady", fromStringToDate("07/01/2017@14:00"), fromStringToDate("07/01/2017@16:00"));
            record11.save();
            Time record10 = new Time("Wmblady", fromStringToDate("08/01/2017@09:00"), fromStringToDate("08/01/2017@14:00"));
            record10.save();
            Time record7 = new Time("Kruk07", fromStringToDate("09/01/2017@10:00"), fromStringToDate("09/01/2017@15:00"));
            record7.save();
            Time record9 = new Time("Wmblady", fromStringToDate("09/01/2017@08:00"), fromStringToDate("09/01/2017@14:30"));
            record9.save();
            Time record8 = new Time("Wmblady", fromStringToDate("09/01/2017@15:00"), fromStringToDate("09/01/2017@17:00"));
            record8.save();
            Time record6 = new Time("Wmblady", fromStringToDate("10/01/2017@11:00"), fromStringToDate("10/01/2017@19:00"));
            record6.save();
            Time record2 = new Time("Wmblady", fromStringToDate("12/01/2017@11:54"), fromStringToDate("12/01/2017@15:34"));
            record2.save();
            Time record = new Time("Kruk07", fromStringToDate("12/01/2017@12:12"), fromStringToDate("12/01/2017@14:14"));
            record.save();
            Time record4 = new Time("Wmblady", fromStringToDate("12/01/2017@16:11"), fromStringToDate("12/01/2017@18:30"));
            record4.save();
            Time record3 = new Time("Kruk07", fromStringToDate("13/01/2017@07:00"), fromStringToDate("13/01/2017@14:00"));
            record3.save();
            Time record13 = new Time("Wmblady", fromStringToDate("13/01/2017@08:00"), fromStringToDate("13/01/2017@16:00"));
            record13.save();
            Time record14 = new Time("Kruk07", fromStringToDate("16/01/2017@07:00"), fromStringToDate("16/01/2017@15:00"));
            record14.save();
        }
        List<Time> t = finder.where().and().eq("login", name).ge("begin", date1).le("end", date2).findList();

        if (t == null) {
            throw new Exception();
        } else {
            Iterator<Time> iterator = t.iterator();
            int i = 0;
            List<TimeStorage> t2 = new ArrayList<TimeStorage>();
            while (iterator.hasNext()) {
                Time krotka = t.get(i);
                String sbegin = fromDateToString(krotka.begin);
                String send = fromDateToString(krotka.end);
                TimeStorage krotkamodyfikowana = new TimeStorage(krotka.login, sbegin, send);
                t2.add(krotkamodyfikowana);
                iterator.next();
                i++;
            }
            return t2;
        }

    }

    public static List<Time> getTimeline(String login, long beginTimestamp, long endTimestamp) {
        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        Date beginDate = new Date(beginTimestamp);
        Date endDate = new Date(endTimestamp);
        if (finder.all().isEmpty())
            fillDataBaseWithSampleData();
        List<Time> timeline = finder.where().and().eq("login", login).ge("begin", beginDate).le("end", endDate).findList();
        return timeline;
    }

    public static ObjectNode getWorkedHours(String login) {
        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        if (finder.all().isEmpty())
            fillDataBaseWithSampleData();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date today = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstMonthDay = calendar.getTime();

        //todo periods between + continue?
        List<Time> todayPeriods = finder.where().and().eq("login", login).ge("begin", today).findList();
        List<Time> monthPeriods = finder.where().and().eq("login", login).ge("begin", firstMonthDay).findList();

        ObjectNode result = Json.newObject();
        result.put("daily", calculateMinutes(todayPeriods));
        result.put("monthly", calculateMinutes(monthPeriods));
        return result;
    }

    public static int calculateMinutes(List<Time> periods) {
        int minutes = 0;
        int millisecondsInMinute = 1000 * 60;
        for (Time period: periods)
            minutes += (period.getEnd().getTime() - period.getBegin().getTime()) / millisecondsInMinute;
        return minutes;
    }

    public static void fillDataBaseWithSampleData() {
        Time record16 = new Time("Kruk07", fromStringToDate("03/01/2017@07:00"), fromStringToDate("03/01/2017@15:00"));
        record16.save();
        Time record1 = new Time("Kruk07", fromStringToDate("07/01/2017@05:34"), fromStringToDate("09/01/2017@14:27"));
        record1.save();
        Time record = new Time("Kruk07", fromStringToDate("12/01/2017@12:12"), fromStringToDate("12/01/2017@14:14"));
        record.save();
        Time record3 = new Time("Kruk07", fromStringToDate("13/01/2017@07:00"), fromStringToDate("13/01/2017@14:00"));
        record3.save();
        Time record14 = new Time("Kruk07", fromStringToDate("16/01/2017@07:00"), fromStringToDate("16/01/2017@15:00"));
        record14.save();
        Time record2 = new Time("Kruk07", fromStringToDate("20/01/2017@07:00"), fromStringToDate("20/01/2017@15:00"));
        record2.save();
    }

    /*
     * Funkcja służaca do uaktualniania czasu pracy w bazie danych
     * response- odpowiedz wyslana przez wtyczke
     */
    public static void track(JsonNode json) {
        String name = json.findValue("login").textValue();
        String pass = json.findValue("password").textValue();
        String daterequest = json.findValue("date").textValue();
        String state = json.findValue("state").textValue();
        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);

        if (state.equals("Start")) {
            Time record = new Time(name, fromStringToDate(daterequest), fromStringToDate(daterequest));
            record.save();
        }
        if (state.equals("Continue")) {
            List<Time> tc = finder.where().eq("login", name).findList();
            Time temp = tc.get(tc.size() - 1);
            Date datereq = fromStringToDate(daterequest);
            if ((temp.end.getHours() == datereq.getHours()) || (temp.end.getHours() + 1 == datereq.getHours())
                    || ((temp.end.getHours() == 23) && (datereq.getHours() == 0))) {
                if (temp.end.getHours() == datereq.getHours()) {
                    if (temp.end.getMinutes() + 15 >= datereq.getMinutes()) {
                        tc.get(tc.size() - 1).setEnd(datereq);
                        tc.get(tc.size() - 1).update();
                    } else {
                        Time record = new Time(name, datereq, datereq);
                        record.save();
                    }
                } else {
                    if (temp.end.getMinutes() >= 45) {
                        if ((temp.end.getMinutes() + 15) % 60 >= datereq.getMinutes()) {
                            tc.get(tc.size() - 1).setEnd(datereq);
                            tc.get(tc.size() - 1).update();
                        } else {
                            Time record = new Time(name, datereq, datereq);
                            record.save();
                        }
                    } else {
                        Time record = new Time(name, datereq, datereq);
                        record.save();
                    }
                }
            } else {
                Time record = new Time(name, datereq, datereq);
                record.save();
            }
        }
        if (state.equals("End")) {
            List<Time> te = finder.where().eq("login", name).findList();
            te.get(te.size() - 1).setEnd(fromStringToDate(daterequest));
            te.get(te.size() - 1).update();
        }

    }

    public static java.util.Date fromStringToDate(String stringDate) {
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

    public static String fromDateToString(java.util.Date daterequest) {
        DateFormat dateFrm = new SimpleDateFormat("dd/MM/yyyy@HH:mm");
        String datestring = dateFrm.format(daterequest);
        return datestring;
    }

}

