package comp3350.teachreach.data.interfaces;

import java.util.Map;

import comp3350.teachreach.data.exceptions.DuplicateEmailException;
import comp3350.teachreach.objects.interfaces.IAccount;

public
interface IAccountPersistence
{
    IAccount storeAccount(IAccount newAccount) throws DuplicateEmailException;

    IAccount updateAccount(IAccount existingAccount);

    Map<Integer, IAccount> getAccounts();
}
