package comp3350.teachreach.logic.exceptions.payment;

public class ExpiredCardException extends PaymentException {
    public ExpiredCardException(String message) {
        super(message);
    }

    public ExpiredCardException(String message, Throwable cause) {
        super(message, cause);
    }
}
