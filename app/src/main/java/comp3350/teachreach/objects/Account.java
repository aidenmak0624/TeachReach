package comp3350.teachreach.objects;

import comp3350.teachreach.objects.interfaces.IAccount;

public
class Account implements IAccount
{
    private int    accountID = -1;
    private int    tutorID   = -1;
    private int    studentID = -1;
    private String accountEmail;
    private String accountPassword;
    private String userName;
    private String userPronouns;
    private String userMajor;

    public
    Account(String accountEmail,
            String accountPassword,
            String userName,
            String userPronouns,
            String userMajor)
    {
        this.accountEmail    = accountEmail;
        this.accountPassword = accountPassword;
        this.userName        = userName;
        this.userPronouns    = userPronouns;
        this.userMajor       = userMajor;
    }

    public
    Account(String accountEmail,
            String accountPassword,
            String userName,
            String userPronouns,
            String userMajor,
            int accountID)
    {
        this(accountEmail, accountPassword, userName, userPronouns, userMajor);
        this.accountID = accountID;
    }

    @Override
    public
    String getAccountEmail()
    {
        return this.accountEmail;
    }

    @Override
    public
    Account setAccountEmail(String accountEmail)
    {
        this.accountEmail = accountEmail;
        return this;
    }

    @Override
    public
    String getAccountPassword()
    {
        return this.accountPassword;
    }

    @Override
    public
    Account setAccountPassword(String accountPassword)
    {
        this.accountPassword = accountPassword;
        return this;
    }

    @Override
    public
    int getAccountID()
    {
        return this.accountID;
    }

    @Override
    public
    Account setAccountID(int accountID)
    {
        this.accountID = accountID;
        return this;
    }

    @Override
    public
    String getUserName()
    {
        return this.userName;
    }

    @Override
    public
    Account setUserName(String newUserName)
    {
        this.userName = newUserName;
        return this;
    }

    @Override
    public
    String getUserPronouns()
    {
        return this.userPronouns;
    }

    @Override
    public
    Account setUserPronouns(String pronouns)
    {
        this.userPronouns = pronouns;
        return this;
    }

    @Override
    public
    String getUserMajor()
    {
        return this.userMajor;
    }

    @Override
    public
    Account setUserMajor(String major)
    {
        this.userMajor = major;
        return this;
    }

    @Override
    public
    int getTutorID()
    {
        return tutorID;
    }

    @Override
    public
    Account setTutorID(int tutorID)
    {
        this.tutorID = tutorID;
        return this;
    }

    @Override
    public
    int getStudentID()
    {
        return this.studentID;
    }

    @Override
    public
    Account setStudentID(int studentID)
    {
        this.studentID = studentID;
        return this;
    }

    @Override
    public
    boolean isStudent()
    {
        return this.studentID != -1;
    }

    @Override
    public
    boolean isTutor()
    {
        return this.tutorID != -1;
    }
}
