package comp3350.teachreach.logic.interfaces;

import comp3350.teachreach.logic.exceptions.AccountManagerException;
import comp3350.teachreach.logic.exceptions.InvalidCredentialException;
import comp3350.teachreach.logic.exceptions.input.InvalidEmailException;
import comp3350.teachreach.logic.exceptions.input.InvalidPasswordException;

public
interface IAccountManager
{
    IAccountManager updateAccountUsername(String newName)
            throws AccountManagerException;

    IAccountManager updateAccountUserPronouns(String pronouns)
            throws AccountManagerException;

    IAccountManager updateAccountUserMajor(String major)
            throws AccountManagerException;

    IAccountManager updatePassword(String oldPlainPasswordFromUser,
                                   String newPlainPasswordFromUser)
            throws InvalidCredentialException, InvalidPasswordException, AccountManagerException;

    IAccountManager updateEmail(String password, String newEmail)
            throws AccountManagerException, InvalidCredentialException, InvalidPasswordException, InvalidEmailException;
}
