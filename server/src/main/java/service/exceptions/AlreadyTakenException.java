package service.exceptions;


// Error 403 Already Taken
public class AlreadyTakenException extends Exception {
    public AlreadyTakenException(String message) {
        super(message);
    }
}
