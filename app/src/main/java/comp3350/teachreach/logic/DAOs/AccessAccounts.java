package comp3350.teachreach.logic.DAOs;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import comp3350.teachreach.application.Server;
import comp3350.teachreach.data.exceptions.DuplicateEmailException;
import comp3350.teachreach.data.interfaces.IAccountPersistence;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.interfaces.IAccount;

public class AccessAccounts
{
    private static IAccountPersistence    accountPersistence;
    private static Map<Integer, IAccount> accounts = null;

    public AccessAccounts()
    {
        accountPersistence = Server.getAccountDataAccess();
        accounts           = accountPersistence.getAccounts();
    }

    public AccessAccounts(final IAccountPersistence accountPersistence)
    {
        AccessAccounts.accountPersistence = accountPersistence;
        accounts                          = accountPersistence.getAccounts();
    }

    public Map<Integer, IAccount> getAccounts()
    {
        try {
            if (accounts == null) {
                AccessAccounts.accounts = accountPersistence.getAccounts();
            }
            return Collections.unmodifiableMap(accounts);
        } catch (final Exception e) {
            throw new DataAccessException("Fail to get all accounts!", e);
        }
    }

    public Optional<IAccount> getAccountByEmail(String email)
    {
        try {
            if (accounts == null) {
                AccessAccounts.accounts = accountPersistence.getAccounts();
            }
            return accounts
                    .values()
                    .stream()
                    .filter(a -> a.getAccountEmail().equals(email))
                    .findFirst();
        } catch (final Exception e) {
            throw new DataAccessException("Failed to get account by email", e);
        }
    }

    public Optional<IAccount> getAccountByAccountID(int accountID) {
        if (accounts == null) {
            accounts = accountPersistence.getAccounts();
        }
        return Optional.ofNullable(accounts.get(accountID));
    }

    public IAccount insertAccount(IAccount newAccount)
            throws DuplicateEmailException
    {
        try {
            newAccount = accountPersistence.storeAccount(newAccount);
            accounts   = accountPersistence.getAccounts();
            return newAccount;
        } catch (DuplicateEmailException e) {
            throw e;
        } catch (final Exception e) {
            throw new DataAccessException("Error storing new Account", e);
        }
    }

    public IAccount updateAccount(IAccount existingAccount)
    {
        try {
            existingAccount = accountPersistence.updateAccount(existingAccount);
            accounts        = accountPersistence.getAccounts();
            return existingAccount;
        } catch (final Exception e) {
            throw new DataAccessException("Error updating account!", e);
        }
    }
}
