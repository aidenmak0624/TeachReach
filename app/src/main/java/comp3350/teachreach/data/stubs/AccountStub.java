package comp3350.teachreach.data.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import comp3350.teachreach.data.interfaces.IAccountPersistence;
import comp3350.teachreach.objects.Account;
import comp3350.teachreach.objects.interfaces.IAccount;

public
class AccountStub implements IAccountPersistence
{

    private static Map<Integer, IAccount> accounts  = null;
    private static int                    currentID = 1;

    public
    AccountStub()
    {
        if (accounts == null) {
            AccountStub.accounts = new HashMap<>();
        }

        this.storeAccount(new Account("guderr@myumanitoba.ca",
                                      "$2a$12$xeTxmBShbtIWsT/kdxVD8.k2LI" +
                                      ".RdOKAHYdRhgiw7Z5YxTd6.beOG",
                                      "Robert Guderian",
                                      "He/Him",
                                      "Computer Science"));
    }

    /**
     * @param newAccount A new account consist of email, password digest,
     *                   name, pronouns, and major
     * @return an IAccount object with a generated integer account identifier
     */
    @Override
    public
    IAccount storeAccount(IAccount newAccount)
    {
        return AccountStub.accounts.put(currentID++, newAccount);
    }

    /**
     * @param existingAccount An existing IAccount object consist of int
     *                        accountID, String email, String password digest,
     *                        String name, String pronouns, and String major
     * @return The updated account object
     * @throws NoSuchElementException if account isn't found
     */
    @Override
    public
    IAccount updateAccount(IAccount existingAccount)
    {
        if (accounts.containsKey(existingAccount.getAccountID())) {
            return accounts.put(existingAccount.getAccountID(),
                                existingAccount);
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * @return Map from Integer accountID -> IAccount object
     */
    @Override
    public
    Map<Integer, IAccount> getAccounts()
    {
        return accounts;
    }
}