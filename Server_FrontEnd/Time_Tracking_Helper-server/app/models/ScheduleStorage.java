package models;

import java.util.Iterator;
import java.util.List;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import exceptions.ScheduleDayNotFoundException;
import exceptions.ScheduleNotFoundException;


public class ScheduleStorage {

    public static void setSchedule(JsonNode json, String login) throws Exception {
        Model.Finder<Integer, Schedule> finder = new Model.Finder<>(Schedule.class);
        List<Schedule> t1 = finder.where().eq("login", login).findList();
        t1.forEach((period) -> period.delete());

        DateFormat dateFrm = new SimpleDateFormat("HH:mm");
        List<JsonNode> beginlist = json.findValues("begin");
        List<JsonNode> endlist = json.findValues("end");
        List<JsonNode> daylist = json.findValues("day");
        Iterator<JsonNode> bIterator = beginlist.iterator();
        Iterator<JsonNode> eIterator = endlist.iterator();
        Iterator<JsonNode> dIterator = daylist.iterator();
        int i = 0;
        while (bIterator.hasNext()) {
            System.out.println("Jestem tutaj " + i);
            long ms = dateFrm.parse(beginlist.get(i).textValue()).getTime();
            long ms2 = dateFrm.parse(endlist.get(i).textValue()).getTime();
            String day = daylist.get(i).textValue();
            java.sql.Time begin = new java.sql.Time(ms);
            java.sql.Time end = new java.sql.Time(ms2);
            System.out.println(login + " " + day + " " + begin + " " + end);
            Schedule record = new Schedule(login, day, begin, end);
            record.save();
            bIterator.next();
            eIterator.next();
            dIterator.next();
            i++;
        }

    }

    public static List<Schedule> getSchedule(String login, String day) throws ScheduleNotFoundException, ScheduleDayNotFoundException {
        Model.Finder<Integer, Schedule> finder = new Model.Finder<>(Schedule.class);
        List<Schedule> t1 = finder.where().eq("login", login).findList();
        if (t1.isEmpty()) {
            throw new ScheduleNotFoundException();
        }
        List<Schedule> t = finder.where().and().eq("login", login).eq("day", day).findList();
        if (t.isEmpty()) {
            throw new ScheduleDayNotFoundException();
        }
        return t;
    }

    public static List<Schedule> getSchedule(String login) {
        Model.Finder<Integer, Schedule> finder = new Model.Finder<>(Schedule.class);
        List<Schedule> t = finder.where().eq("login", login).findList();
        return t;
    }

}
