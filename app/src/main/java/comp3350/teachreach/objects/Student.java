package comp3350.teachreach.objects;

import comp3350.teachreach.objects.interfaces.IStudent;

public
class Student implements IStudent {
    private int studentID = -1;
    private int accountID = -1;

    public Student(int accountID) {
        this.accountID = accountID;
    }

    public Student(int studentID, int accountID) {
        this(accountID);
        this.studentID = studentID;
    }

    @Override
    public int getStudentID() {
        return studentID;
    }

    @Override
    public Student setStudentID(int studentID) {
        this.studentID = studentID;
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
}