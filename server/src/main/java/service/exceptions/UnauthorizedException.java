package service.exceptions;

// Error 401 Unauthorized
public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super(message);
    }
}