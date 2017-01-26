package exceptions;

public class EmailExistsInDatabaseException extends Exception {
    public EmailExistsInDatabaseException() {
        System.err.println("Email already exists in database");
    }
}
