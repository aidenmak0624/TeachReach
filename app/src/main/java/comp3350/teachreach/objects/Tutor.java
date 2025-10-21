package comp3350.teachreach.objects;

import comp3350.teachreach.objects.interfaces.ITutor;

public
class Tutor implements ITutor {
    private int tutorID = -1;
    private int accountID = -1;
    private double hourlyRate = 0;
    private int reviewSum = 0;
    private int reviewCount = 0;

    public Tutor(int accountID) {
        this.accountID = accountID;
    }

    public Tutor(int tutorID, int accountID) {
        this.accountID = accountID;
        this.tutorID = tutorID;
    }

    public Tutor(int tutorID,
                 int accountID,
                 double hourlyRate,
                 int reviewSum,
                 int reviewCount) {
        this(tutorID, accountID);
        this.hourlyRate = hourlyRate;
        this.reviewSum = reviewSum;
        this.reviewCount = reviewCount;
    }

    @Override
    public int getTutorID() {
        return tutorID;
    }

    @Override
    public Tutor setTutorID(int tutorID) {
        this.tutorID = tutorID;
        return this;
    }

    @Override
    public int getAccountID() {
        return accountID;
    }

    @Override
    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    @Override
    public double getHourlyRate() {
        return hourlyRate;
    }

    @Override
    public Tutor setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
        return this;
    }

    @Override
    public int getReviewCount() {
        return reviewCount;
    }

    @Override
    public Tutor setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
        return this;
    }

    @Override
    public int getReviewSum() {
        return reviewSum;
    }

    @Override
    public Tutor setReviewSum(int reviewSum) {
        this.reviewSum = reviewSum;
        return this;
    }
}
