package comp3350.teachreach.logic.account;

import comp3350.teachreach.logic.DAOs.AccessAccounts;
import comp3350.teachreach.logic.exceptions.AccountManagerException;
import comp3350.teachreach.logic.exceptions.InvalidCredentialException;
import comp3350.teachreach.logic.exceptions.input.InvalidEmailException;
import comp3350.teachreach.logic.exceptions.input.InvalidPasswordException;
import comp3350.teachreach.logic.interfaces.IAccountManager;
import comp3350.teachreach.logic.interfaces.IAuthenticationHandler;
import comp3350.teachreach.objects.interfaces.IAccount;

public class AccountManager implements IAccountManager {
    private final AccessAccounts accessAccounts;
    private final IAuthenticationHandler authenticationHandler;
    private IAccount theAccount;

    public AccountManager(IAccount theAccount) {
        this.accessAccounts = new AccessAccounts();
        this.authenticationHandler = new AuthenticationHandler();
        this.theAccount = theAccount;
    }

    public AccountManager(AccessAccounts accessAccounts,
                          IAuthenticationHandler authenticationHandler,
                          IAccount theAccount) {
        this.accessAccounts = accessAccounts;
        this.authenticationHandler = authenticationHandler;
        this.theAccount = theAccount;
    }

    @Override
    public IAccountManager updateAccountUsername(String newName)
            throws AccountManagerException {
        try {
            theAccount = accessAccounts.updateAccount(theAccount.setUserName(
                    newName));
            return this;
        } catch (final Exception e) {
            throw new AccountManagerException("Failed to update user name :(",
                    e);
        }
    }

    @Override
    public IAccountManager updateAccountUserPronouns(String pronouns)
            throws AccountManagerException
    {
        try {
            theAccount
                    = accessAccounts.updateAccount(theAccount.setUserPronouns(
                    pronouns));
            return this;
        } catch (final Exception e) {
            throw new AccountManagerException(
                    "Failed to update user pronouns :(",
                    e);
        }
    }

    @Override
    public IAccountManager updateAccountUserMajor(String major)
            throws AccountManagerException
    {
        try {
            theAccount = accessAccounts.updateAccount(theAccount.setUserMajor(
                    major));
            return this;
        } catch (final Exception e) {
            throw new AccountManagerException(
                    "Failed to update user pronouns :(",
                    e);
        }
    }

    @Override
    public IAccountManager updatePassword(String oldPlainPassword,
                                          String newPlainPassword) throws
                                                                   InvalidCredentialException,
                                                                   InvalidPasswordException,
                                                                   AccountManagerException
    {
        try {
            InputValidator.validatePassword(oldPlainPassword);
            InputValidator.validatePassword(newPlainPassword);

            if (newPlainPassword.equals(oldPlainPassword)) {
                throw new InvalidPasswordException(
                        "New password is the same as current password");
            }

            authenticationHandler.authenticateUser(theAccount.getAccountEmail(),
                                                   oldPlainPassword);

            theAccount.setAccountPassword(PasswordManager.encryptPassword(
                    newPlainPassword));
            theAccount = accessAccounts.updateAccount(theAccount);

            return this;
        } catch (InvalidPasswordException e) {
            throw e;
        } catch (InvalidCredentialException e) {
            throw new InvalidCredentialException(
                    "Entered current password is incorrect");
        } catch (Exception e) {
            throw new AccountManagerException("Failed to update password");
        }
    }

    @Override
    public IAccountManager updateEmail(String currentPlainPassword,
                                       String newEmail) throws
                                                        AccountManagerException,
                                                        InvalidCredentialException,
                                                        InvalidPasswordException,
                                                        InvalidEmailException
    {
        try {
            InputValidator.validateEmail(newEmail);
            InputValidator.validatePassword(currentPlainPassword);
            authenticationHandler.authenticateUser(theAccount.getAccountEmail(),
                                                   currentPlainPassword);
            theAccount
                    = accessAccounts.updateAccount(theAccount.setAccountEmail(
                    newEmail));
            return this;
        } catch (InvalidEmailException | InvalidPasswordException e) {
            throw e;
        } catch (InvalidCredentialException e) {
            throw new InvalidCredentialException("Entered password is " +
                                                 "incorrect");
        } catch (Exception e) {
            throw new AccountManagerException("Failed to update account email",
                                              e);
        }
    }
}
