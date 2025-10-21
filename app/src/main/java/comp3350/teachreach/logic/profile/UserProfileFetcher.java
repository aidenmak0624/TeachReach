package comp3350.teachreach.logic.profile;


import java.util.NoSuchElementException;
import java.util.Optional;

import comp3350.teachreach.logic.DAOs.AccessAccounts;
import comp3350.teachreach.logic.interfaces.IUserProfileHandler;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;

public
class UserProfileFetcher<T> implements IUserProfileHandler<T>
{
    private AccessAccounts accessAccounts;

    public
    UserProfileFetcher()
    {
        accessAccounts = new AccessAccounts();
    }

    public
    UserProfileFetcher(AccessAccounts accessAccounts)
    {
        this.accessAccounts = accessAccounts;
    }

    private
    String getProperty(T user, java.util.function.Function<IAccount, String> mapper)
    {
        return findAccount(user).map(mapper).orElseThrow(() -> new NoSuchElementException("Account not found!"));
    }

    private
    Optional<IAccount> findAccount(T user)
    {
        if (user instanceof IStudent) {
            return accessAccounts.getAccountByAccountID(((IStudent) user).getAccountID());
        }
        if (user instanceof ITutor) {
            return accessAccounts.getAccountByAccountID(((ITutor) user).getAccountID());
        }
        return Optional.empty();
    }

    @Override
    public
    String getUserEmail(T user)
    {
        return getProperty(user, IAccount::getAccountEmail);
    }

    @Override
    public
    String getUserName(T user)
    {
        return getProperty(user, IAccount::getUserName);
    }

    @Override
    public
    String getUserPronouns(T user)
    {
        return getProperty(user, IAccount::getUserPronouns);
    }

    @Override
    public
    String getUserMajor(T user)
    {
        return getProperty(user, IAccount::getUserMajor);
    }

    @Override
    public
    IAccount getUserAccount(T user)
    {
        return findAccount(user).orElseThrow(() -> new NoSuchElementException("Account not found!"));
    }
}
