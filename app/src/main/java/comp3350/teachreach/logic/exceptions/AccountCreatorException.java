package comp3350.teachreach.logic.exceptions;

public
class AccountCreatorException extends Exception
{
    public
    AccountCreatorException(String message)
    {
        super(message);
    }

    public
    AccountCreatorException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
