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
import com.fasterxml.jackson.databind.node.ArrayNode;
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
            Time record15 = new Time("Wmblady", fromStringToDate("02/01/2017@08:00"), fromStringToDate("02/01/2017@16:00"), "End");
            record15.save();
            Time record16 = new Time("Kruk07", fromStringToDate("03/01/2017@07:00"), fromStringToDate("03/01/2017@15:00"), "End");
            record16.save();
            Time record5 = new Time("Kruk07", fromStringToDate("06/01/2017@07:00"), fromStringToDate("06/01/2017@15:00"), "End");
            record5.save();
            Time record12 = new Time("Wmblady", fromStringToDate("06/01/2017@14:00"), fromStringToDate("06/01/2017@16:00"), "End");
            record12.save();
            Time record11 = new Time("Wmblady", fromStringToDate("07/01/2017@14:00"), fromStringToDate("07/01/2017@16:00"), "End");
            record11.save();
            Time record10 = new Time("Wmblady", fromStringToDate("08/01/2017@09:00"), fromStringToDate("08/01/2017@14:00"), "End");
            record10.save();
            Time record7 = new Time("Kruk07", fromStringToDate("09/01/2017@10:00"), fromStringToDate("09/01/2017@15:00"), "End");
            record7.save();
            Time record9 = new Time("Wmblady", fromStringToDate("09/01/2017@08:00"), fromStringToDate("09/01/2017@14:30"), "End");
            record9.save();
            Time record8 = new Time("Wmblady", fromStringToDate("09/01/2017@15:00"), fromStringToDate("09/01/2017@17:00"), "End");
            record8.save();
            Time record6 = new Time("Wmblady", fromStringToDate("10/01/2017@11:00"), fromStringToDate("10/01/2017@19:00"), "End");
            record6.save();
            Time record2 = new Time("Wmblady", fromStringToDate("12/01/2017@11:54"), fromStringToDate("12/01/2017@15:34"), "End");
            record2.save();
            Time record = new Time("Kruk07", fromStringToDate("12/01/2017@12:12"), fromStringToDate("12/01/2017@14:14"), "End");
            record.save();
            Time record4 = new Time("Wmblady", fromStringToDate("12/01/2017@16:11"), fromStringToDate("12/01/2017@18:30"), "End");
            record4.save();
            Time record3 = new Time("Kruk07", fromStringToDate("13/01/2017@07:00"), fromStringToDate("13/01/2017@14:00"), "End");
            record3.save();
            Time record13 = new Time("Wmblady", fromStringToDate("13/01/2017@08:00"), fromStringToDate("13/01/2017@16:00"), "End");
            record13.save();
            Time record14 = new Time("Kruk07", fromStringToDate("15/01/2017@022:00"), fromStringToDate("15/01/2017@23:59"), "End");
            record14.save();
        }
        List<Time> t = finder.where().and().eq("login", name).ge("begin", date1).le("end", date2).
                orderBy().asc("begin").findList();

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
            Time record15 = new Time("Wmblady", fromStringToDate("02/01/2017@08:00"), fromStringToDate("02/01/2017@16:00"), "End");
            record15.save();
            Time record16 = new Time("Kruk07", fromStringToDate("03/01/2017@07:00"), fromStringToDate("03/01/2017@15:00"), "End");
            record16.save();
            Time record5 = new Time("Kruk07", fromStringToDate("06/01/2017@07:00"), fromStringToDate("06/01/2017@15:00"), "End");
            record5.save();
            Time record12 = new Time("Wmblady", fromStringToDate("06/01/2017@14:00"), fromStringToDate("06/01/2017@16:00"), "End");
            record12.save();
            Time record11 = new Time("Wmblady", fromStringToDate("07/01/2017@14:00"), fromStringToDate("07/01/2017@16:00"), "End");
            record11.save();
            Time record10 = new Time("Wmblady", fromStringToDate("08/01/2017@09:00"), fromStringToDate("08/01/2017@14:00"), "End");
            record10.save();
            Time record7 = new Time("Kruk07", fromStringToDate("09/01/2017@10:00"), fromStringToDate("09/01/2017@15:00"), "End");
            record7.save();
            Time record9 = new Time("Wmblady", fromStringToDate("09/01/2017@08:00"), fromStringToDate("09/01/2017@14:30"), "End");
            record9.save();
            Time record8 = new Time("Wmblady", fromStringToDate("09/01/2017@15:00"), fromStringToDate("09/01/2017@17:00"), "End");
            record8.save();
            Time record6 = new Time("Wmblady", fromStringToDate("10/01/2017@11:00"), fromStringToDate("10/01/2017@19:00"), "End");
            record6.save();
            Time record2 = new Time("Wmblady", fromStringToDate("12/01/2017@11:54"), fromStringToDate("12/01/2017@15:34"), "End");
            record2.save();
            Time record = new Time("Kruk07", fromStringToDate("12/01/2017@12:12"), fromStringToDate("12/01/2017@14:14"), "End");
            record.save();
            Time record4 = new Time("Wmblady", fromStringToDate("12/01/2017@16:11"), fromStringToDate("12/01/2017@18:30"), "End");
            record4.save();
            Time record3 = new Time("Kruk07", fromStringToDate("13/01/2017@07:00"), fromStringToDate("13/01/2017@14:00"), "End");
            record3.save();
            Time record13 = new Time("Wmblady", fromStringToDate("13/01/2017@08:00"), fromStringToDate("13/01/2017@16:00"), "End");
            record13.save();
            Time record14 = new Time("Kruk07", fromStringToDate("16/01/2017@07:00"), fromStringToDate("16/01/2017@15:00"), "End");
            record14.save();
        }
        List<Time> t = finder.where().and().eq("login", name).ge("begin", date1).le("end", date2).orderBy().asc("begin").findList();

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

    public static void setMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

    public static List<Time> getTimeline(String login, long beginTimestamp, long endTimestamp) {
        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTimeInMillis(beginTimestamp);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        setMidnight(calendar);
        Date beginDate = calendar.getTime();

        calendar.setTimeInMillis(endTimestamp);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        setMidnight(calendar);
        calendar.add(Calendar.DATE, 7);
        Date endDate = calendar.getTime();

        if (finder.all().isEmpty())
            fillDataBaseWithSampleData();
        return finder.where().and().eq("login", login).ge("begin", beginDate).le("end", endDate).orderBy().asc("begin").findList();
    }

    public static ObjectNode getWorkedHours(String login) {
        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        if (finder.all().isEmpty())
            fillDataBaseWithSampleData();

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        setMidnight(calendar);
        Date today = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstMonthDay = calendar.getTime();

        List<Time> todayPeriods = finder.where().and().eq("login", login).ge("begin", today).le("end", now).findList();
        List<Time> monthPeriods = finder.where().and().eq("login", login).ge("begin", firstMonthDay).le("end", now).findList();

        ObjectNode result = Json.newObject();
        result.put("daily", calculateMinutes(todayPeriods));
        result.put("monthly", calculateMinutes(monthPeriods));
        return result;
    }

    public static int calculateMinutes(List<Time> periods) {
        int minutes = 0;
        if (!periods.isEmpty()) {
            int millisecondsInMinute = 1000 * 60;
            for (Time period : periods)
                minutes += (period.getEnd().getTime() - period.getBegin().getTime()) / millisecondsInMinute;
            Time lastPeriod = periods.get(periods.size() - 1);
            if (lastPeriod.getState().equals("Continue")) {
                Date now = new Date();
                int minutesBetween = (int) ((now.getTime() - lastPeriod.getEnd().getTime()) / millisecondsInMinute);
                if (minutesBetween <= 15)
                    minutes += minutesBetween;
            }
        }
        return minutes;
    }

    public static void fillDataBaseWithSampleData() {
        Time record4 = new Time("Kruk07", fromStringToDate("02/01/2017@01:00"), fromStringToDate("02/01/2017@04:00"), "End");
        record4.save();
        Time record16 = new Time("Kruk07", fromStringToDate("03/01/2017@07:00"), fromStringToDate("03/01/2017@15:00"), "End");
        record16.save();
        Time record1 = new Time("Kruk07", fromStringToDate("07/01/2017@05:34"), fromStringToDate("07/01/2017@14:27"), "End");
        record1.save();
        Time record = new Time("Kruk07", fromStringToDate("12/01/2017@12:12"), fromStringToDate("12/01/2017@14:14"), "End");
        record.save();
        Time record3 = new Time("Kruk07", fromStringToDate("13/01/2017@07:00"), fromStringToDate("13/01/2017@14:00"), "End");
        record3.save();
        Time record14 = new Time("Kruk07", fromStringToDate("16/01/2017@07:00"), fromStringToDate("16/01/2017@15:00"), "End");
        record14.save();
        Time record2 = new Time("Kruk07", fromStringToDate("22/01/2017@04:00"), fromStringToDate("22/01/2017@22:00"), "End");
        record2.save();
    }

    public static void addTimeToPeriod(Time lastRecord, Date dateToInsert, String state, String login) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateToInsert);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(lastRecord.getEnd());
        int lastDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (lastDay != currentDay) {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);

            lastRecord.setEnd(calendar.getTime());
            lastRecord.setState("End");
            lastRecord.update();

            calendar.add(Calendar.SECOND, 1);

            Time newRecord = new Time(login, calendar.getTime(), dateToInsert, "Continue");
            newRecord.save();
        } else {
            lastRecord.setEnd(dateToInsert);
            lastRecord.setState(state);
            lastRecord.update();
        }
    }

    /*
     * Funkcja służaca do uaktualniania czasu pracy w bazie danych
     * response- odpowiedz wyslana przez wtyczke
     */
    public static void track(JsonNode json) {
        String login = json.findValue("login").textValue();
        String state = json.findValue("state").textValue();
        long timestamp = json.findValue("date").numberValue().longValue();
        long maxPeriodBetween = 15 * 60 * 1000;
        Date dateToInsert = new Date(timestamp);

        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        Time lastRecord = finder.where().eq("login", login).orderBy().desc("begin").setMaxRows(1).findUnique();
        if (lastRecord != null) {
            if ((timestamp - lastRecord.getEnd().getTime()) < maxPeriodBetween) {
                addTimeToPeriod(lastRecord, dateToInsert, state, login);
            } else {
                lastRecord.setState("End");
                lastRecord.update();
                if (!state.equals("End")) {
                    Time record = new Time(login, dateToInsert, dateToInsert, "Continue");
                    record.save();
                }
            }
        } else {
            Time record = new Time(login, dateToInsert, dateToInsert, "Continue");
            record.save();
        }
    }

    public static void updateDay(String login, JsonNode json) {
        long timestamp = json.findPath("date").numberValue().longValue();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        setMidnight(calendar);

        calendar.add(Calendar.DATE, 1);
        Date end = calendar.getTime();

        calendar.add(Calendar.DATE, -1);
        Date begin = calendar.getTime();

        Model.Finder<Integer, Time> finder = new Model.Finder<>(Time.class);
        List<Time> tc = finder.where().and().eq("login", login).ge("begin", begin).le("end", end).findList();
        tc.forEach((period) -> period.delete());

        ArrayNode node = (ArrayNode) json.findPath("periods");

        for (int i = 0; i < node.size(); i++) {
            String beginTime = node.get(i).findPath("begin").textValue();
            setHour(calendar, beginTime);
            Date beginDate = calendar.getTime();

            String endTime = node.get(i).findPath("end").textValue();
            setHour(calendar, endTime);
            Date endDate = calendar.getTime();

            Time record = new Time(login, beginDate, endDate, "End");
            record.save();
        }
    }

    public static void setHour(Calendar calendar, String hour) {
        int semicolonIndex = hour.indexOf(':');
        int hours = Integer.parseInt(hour.substring(0, semicolonIndex));
        int minutes = Integer.parseInt(hour.substring(semicolonIndex + 1));
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
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