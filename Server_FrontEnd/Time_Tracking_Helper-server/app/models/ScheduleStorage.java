package models;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import exceptions.ScheduleDayNotFoundException;
import exceptions.ScheduleNotFoundException;


public class ScheduleStorage {

    public static void setSchedule(JsonNode json, String login) throws Exception {
        Model.Finder<Integer, Schedule> finder = new Model.Finder<>(Schedule.class);
        List<Schedule> t1 = finder.where().eq("login", login).findList();
        t1.forEach((period) -> period.delete());

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        ArrayNode node = (ArrayNode) json.findPath("schedule");
        List<Schedule> periodsInDay = new LinkedList<Schedule>();
        String previousPeriodDay = "monday";

        for (int i = 0; i < node.size(); i++) {
            long begin = dateFormat.parse(node.get(i).findPath("begin").textValue()).getTime();
            long end = dateFormat.parse(node.get(i).findPath("end").textValue()).getTime();
            String day = node.get(i).findPath("day").textValue();

            Time beginTime = new Time(begin);
            Time endTime = new Time(end);

            if (!day.equals(previousPeriodDay)) {
                previousPeriodDay = day;
                List<Schedule> mergedPeriods = mergeOverlappingPeriods(periodsInDay);
                mergedPeriods.forEach((period) -> period.save());
            }
            periodsInDay.add(new Schedule(login, day, beginTime, endTime));
        }
        List<Schedule> mergedPeriods = mergeOverlappingPeriods(periodsInDay);
        mergedPeriods.forEach((period) -> period.save());
    }

    public static List<Schedule> mergeOverlappingPeriods(List<Schedule> periods) {
        List<Schedule> mergedPeriods = new LinkedList<Schedule>();
        if (!periods.isEmpty()) {
            Collections.sort(periods);
            Schedule previousPeriod = periods.get(0);
            mergedPeriods.add(previousPeriod);
            periods.remove(previousPeriod);
            while (!periods.isEmpty()) {
                Schedule period = periods.get(0);
                if (period.getBegin().compareTo(previousPeriod.getEnd()) > 0) {
                    previousPeriod = period;
                    mergedPeriods.add(period);
                    periods.remove(period);
                } else {
                    if (period.getEnd().compareTo(previousPeriod.getEnd()) <= 0) {
                        periods.remove(period);
                    } else {
                        previousPeriod.setEnd((Time) period.getEnd());
                        periods.remove(period);
                    }
                }
            }
        }
        return mergedPeriods;
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
