package exceptions;

public class ScheduleNotFoundException extends Exception{
	public ScheduleNotFoundException(){
		System.err.println("SCHEDULE NOT FOUND");
	}
}
