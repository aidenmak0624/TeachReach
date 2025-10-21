package comp3350.teachreach.tests.unitTests.logic.payment;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import comp3350.teachreach.logic.exceptions.payment.ExpiredCardException;
import comp3350.teachreach.logic.exceptions.payment.InvalidCVCException;
import comp3350.teachreach.logic.exceptions.payment.InvalidCardNumberException;
import comp3350.teachreach.logic.exceptions.payment.InvalidExpiryDateException;
import comp3350.teachreach.logic.exceptions.payment.PaymentException;
import comp3350.teachreach.logic.payment.PaymentValidator;

public class PaymentValidatorTest
{

    @Test
    public void testPaymentValidator()
    {
        try {
            boolean flag = PaymentValidator.validatePaymentInfo(
                    "0000000000000000",
                    "0924",
                    "000"); //Functions on valid input
            assertTrue("Valid Card failed", flag);
            assertThrows("InvalidCardNumberException expected",
                         InvalidCardNumberException.class,
                         () -> {
                             PaymentValidator.validatePaymentInfo(
                                     "000000000000000",
                                     "1299",
                                     "000");
                         }); // Short length of card number
            assertThrows("InvalidCardNumberException expected",
                         InvalidCardNumberException.class,
                         () -> {
                             PaymentValidator.validatePaymentInfo(
                                     "00000000000000000",
                                     "1299",
                                     "000");
                         }); // Long length of card number
            assertThrows("InvalidCardNumberException expected",
                         InvalidCardNumberException.class,
                         () -> {
                             PaymentValidator.validatePaymentInfo(
                                     "000000000000000a",
                                     "1299",
                                     "000");
                         }); //Card Number not numeric.
            assertThrows("InvalidCVCException expected",
                         InvalidCVCException.class,
                         () -> {
                             PaymentValidator.validatePaymentInfo(
                                     "0000000000000000",
                                     "1299",
                                     "00");
                         }); // Short cvc
            assertThrows("InvalidCVCException expected",
                         InvalidCVCException.class,
                         () -> {
                             PaymentValidator.validatePaymentInfo(
                                     "0000000000000000",
                                     "1299",
                                     "0000");
                         }); // Long cvc
            assertThrows("InvalidCVCException expected",
                         InvalidCVCException.class,
                         () -> {
                             PaymentValidator.validatePaymentInfo(
                                     "0000000000000000",
                                     "1299",
                                     "00a");
                         }); // Non-numeric cvc
            assertThrows("InvalidExpiryDateException expected",
                         InvalidExpiryDateException.class,
                         () -> {
                             PaymentValidator.validatePaymentInfo(
                                     "0000000000000000",
                                     "129",
                                     "000");
                         }); // Bad expiry date input
            assertThrows("InvalidExpiryDateException expected",
                         InvalidExpiryDateException.class,
                         () -> {
                             PaymentValidator.validatePaymentInfo(
                                     "0000000000000000",
                                     "2499",
                                     "000");
                         }); // Bad expiry date input
            assertThrows("InvalidExpiryDateException expected",
                         InvalidExpiryDateException.class,
                         () -> {
                             PaymentValidator.validatePaymentInfo(
                                     "0000000000000000",
                                     "199",
                                     "000");
                         }); // Bad expiry date input
            assertThrows("InvalidExpiryDateException expected",
                         InvalidExpiryDateException.class,
                         () -> {
                             PaymentValidator.validatePaymentInfo(
                                     "0000000000000000",
                                     "129a",
                                     "000");
                         }); // Bad expiry date input
            assertThrows("ExpiredCardException expected",
                         ExpiredCardException.class,
                         () -> {
                             PaymentValidator.validatePaymentInfo(
                                     "0000000000000000",
                                     "0224",
                                     "000");
                         }); // Expired Card
        } catch (PaymentException p) {
            assertTrue("Some error from payment validator", false);
        }
    }
}
