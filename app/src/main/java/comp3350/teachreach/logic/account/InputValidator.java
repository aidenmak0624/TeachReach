package comp3350.teachreach.logic.account;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import comp3350.teachreach.logic.exceptions.InvalidNameException;
import comp3350.teachreach.logic.exceptions.input.InvalidEmailException;
import comp3350.teachreach.logic.exceptions.input.InvalidInputException;
import comp3350.teachreach.logic.exceptions.input.InvalidPasswordException;

public class InputValidator
{
    private static final String  EMAIL_REGEX   = "(?:[a-z0-9!#$%&'*+/=?^_" +
                                                 "`{|}~-]+(?:\\" +
                                                 ".[a-z0-9!#$%&'*+/=?^_" +
                                                 "`{|}~-]+)*|\"" +
                                                 "(?:[\\x01-\\x08\\x0b\\x0c" +
                                                 "\\x0e-\\x1f\\x21\\x23-\\x5b" +
                                                 "\\x5d-\\x7f]|\\\\[\\x01" +
                                                 "-\\x09\\x0b\\x0c\\x0e-\\x7f" +
                                                 "])*\")@(?:(?:[a-z0-9]" +
                                                 "(?:[a-z0-9-]*[a-z0-9])?\\.)" +
                                                 "+[a-z0-9](?:[a-z0-9-]*[a-z0" +
                                                 "-9])?|\\[(?:(?:(2" +
                                                 "(5[0-5]|[0-4][0-9])" +
                                                 "|1[0-9][0-9]|[1-9]?[0-9]))" +
                                                 "\\.){3}(?:(2" +
                                                 "(5[0-5]|[0-4][0-9])" +
                                                 "|1[0-9][0-9]|[1-9]?[0-9])" +
                                                 "|[a-z0-9-]*[a-z0-9]:" +
                                                 "(?:[\\x01-\\x08\\x0b\\x0c" +
                                                 "\\x0e-\\x1f\\x21-\\x5a\\x53" +
                                                 "-\\x7f]|\\\\[\\x01-\\x09" +
                                                 "\\x0b\\x0c\\x0e-\\x7f])+)" +
                                                 "\\])";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static void validateEmail(String email) throws InvalidEmailException
    {
        if (isEmpty(email)) {
            throw new InvalidEmailException("Email cannot be empty");
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidEmailException("Invalid email");
        }
    }

    public static void validatePassword(String password)
            throws InvalidPasswordException
    {
        if (isEmpty(password)) {
            throw new InvalidPasswordException("Password cannot be empty");
        }
    }

    public static void validateName(String name) throws InvalidNameException
    {
        if (isEmpty(name)) {
            throw new InvalidNameException("Name cannot be empty");
        }
    }

    public static void validateInput(String in) throws InvalidInputException
    {
        if (isEmpty(in)) {
            throw new InvalidInputException("Input cannot be empty!");
        }
    }

    private static boolean isEmpty(String input)
    {
        return input == null || !input.matches(".*\\S.*");
    }
}
