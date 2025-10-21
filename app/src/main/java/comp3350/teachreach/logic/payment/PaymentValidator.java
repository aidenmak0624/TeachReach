package comp3350.teachreach.logic.payment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import comp3350.teachreach.logic.exceptions.payment.ExpiredCardException;
import comp3350.teachreach.logic.exceptions.payment.InvalidCVCException;
import comp3350.teachreach.logic.exceptions.payment.InvalidCardNumberException;
import comp3350.teachreach.logic.exceptions.payment.InvalidExpiryDateException;
import comp3350.teachreach.logic.exceptions.payment.PaymentException;

public class PaymentValidator
{
    public static boolean validatePaymentInfo(String cardNum,
                                              String expiryDate,
                                              String cvc)
            throws PaymentException
    {
        boolean numValid    = true;
        boolean cvcValid    = true;
        boolean expiryValid = true;
        boolean notExpired  = true;
        try {
            Long.parseLong(cardNum);
        } catch (NumberFormatException nfe) {
            numValid = false;
        }
        numValid &= cardNum.length() == 16;

        try {
            Integer.parseInt(cvc);
        } catch (NumberFormatException nfe) {
            cvcValid = false;
        }
        cvcValid &= cvc.length() == 3;

        expiryValid &= expiryDate.length() == 4;

        try {
            LocalDate expDate = LocalDate.parse("01" + expiryDate,
                                                DateTimeFormatter.ofPattern(
                                                        "ddMMyy"));
            notExpired = !expDate.isBefore(LocalDate.now());
        } catch (Exception e) {
            expiryValid = false;
        }

        if (!numValid) {
            throw (new InvalidCardNumberException("Invalid Card Number"));
        } else if (!cvcValid) {
            throw (new InvalidCVCException("Invalid CVC Number"));
        } else if (!expiryValid) {
            throw (new InvalidExpiryDateException("Invalid Expiry Date"));
        } else if (!notExpired) {
            throw (new ExpiredCardException("Card Is Expired"));
        }

        return numValid && cvcValid && expiryValid && notExpired;
    }
}
