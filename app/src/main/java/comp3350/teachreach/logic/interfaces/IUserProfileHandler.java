package comp3350.teachreach.logic.interfaces;

import comp3350.teachreach.objects.interfaces.IAccount;

public
interface IUserProfileHandler<T>
{
    String getUserEmail(T user);

    String getUserName(T user);

    String getUserPronouns(T user);

    String getUserMajor(T user);

    IAccount getUserAccount(T user);
}
