package exceptions;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException() {
        System.err.println("Incorrect password");
    }
}
