package comp3350.teachreach.objects.interfaces;

import comp3350.teachreach.objects.Tutor;

public
interface ITutor
{
    int getTutorID();

    Tutor setTutorID(int tutorID);

    int getAccountID();

    void setAccountID(int accountID);

    double getHourlyRate();

    Tutor setHourlyRate(double hourlyRate);

    int getReviewCount();

    Tutor setReviewCount(int reviewCount);

    int getReviewSum();

    Tutor setReviewSum(int reviewSum);
}
