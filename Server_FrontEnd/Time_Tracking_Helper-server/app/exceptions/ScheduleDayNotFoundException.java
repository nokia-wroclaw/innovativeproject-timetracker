package exceptions;

public class ScheduleDayNotFoundException extends Exception{
	public ScheduleDayNotFoundException(){
		System.err.println("DAY NOT FOUND IN SCHEDULE");
	}
}
