package comp3350.teachreach.logic.exceptions.payment;

public class InvalidExpiryDateException extends PaymentException {
    public InvalidExpiryDateException(String message) {
        super(message);
    }

    public InvalidExpiryDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
