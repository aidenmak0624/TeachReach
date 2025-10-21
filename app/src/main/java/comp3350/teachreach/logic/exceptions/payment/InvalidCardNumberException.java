package comp3350.teachreach.logic.exceptions.payment;
public class InvalidCardNumberException extends PaymentException {

        public InvalidCardNumberException(String message) {
            super(message);
        }

        public InvalidCardNumberException(String message, Throwable cause) {
            super(message, cause);
        }
}
