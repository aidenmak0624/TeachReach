package comp3350.teachreach.objects.interfaces;

public
interface IAccount
{
    String getAccountEmail();

    IAccount setAccountEmail(String accountEmail);

    String getAccountPassword();

    IAccount setAccountPassword(String accountPasswordDigest);

    int getAccountID();

    IAccount setAccountID(int accountID);

    String getUserName();

    IAccount setUserName(String newUserName);

    String getUserPronouns();

    IAccount setUserPronouns(String pronouns);

    String getUserMajor();

    IAccount setUserMajor(String major);

    int getTutorID();

    IAccount setTutorID(int tutorID);

    int getStudentID();

    IAccount setStudentID(int studentID);

    boolean isStudent();

    boolean isTutor();
}
