package comp3350.teachreach.logic.exceptions.payment;

public class InvalidCVCException extends PaymentException {
    public InvalidCVCException(String message) {
        super(message);
    }

    public InvalidCVCException(String message, Throwable cause) {
        super(message, cause);
    }
}
