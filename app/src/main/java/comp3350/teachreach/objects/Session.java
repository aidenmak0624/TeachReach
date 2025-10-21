package comp3350.teachreach.objects;

import comp3350.teachreach.objects.interfaces.ISession;
import comp3350.teachreach.objects.interfaces.ITimeSlice;


public class Session implements ISession {
    private int sessionID;
    private int studentID;
    private int tutorID;
    private ITimeSlice timeRange;
    private double sessionCost;
    private int status;
    private String atLocation;

    public Session(int studentID,
                   int tutorID,
                   ITimeSlice timeRange,
                   double sessionCost,
                   String atLocation) {
        this.sessionID = -1;
        this.studentID = studentID;
        this.tutorID = tutorID;
        this.timeRange = timeRange;
        this.sessionCost = sessionCost;
        this.atLocation = atLocation;
        this.status = SessionStatus.PENDING;
    }

    public Session(int sessionID,
                   int studentID,
                   int tutorID,
                   ITimeSlice timeRange,
                   double sessionCost,
                   String atLocation,
                   int status) {
        this.sessionID = sessionID;
        this.studentID = studentID;
        this.tutorID = tutorID;
        this.timeRange = timeRange;
        this.sessionCost = sessionCost;
        this.atLocation = atLocation;
        this.status = status;
    }

    @Override
    public int getSessionStudentID() {
        return this.studentID;
    }

    @Override
    public Session setSessionStudentID(int studentID) {
        this.studentID = studentID;
        return this;
    }

    @Override
    public int getSessionTutorID() {
        return tutorID;
    }

    @Override
    public void setSessionTutorID(int tutorID) {
        this.tutorID = tutorID;
    }

    @Override
    public int getSessionID() {
        return this.sessionID;
    }

    @Override
    public Session setSessionID(int sessionID) {
        this.sessionID = sessionID;
        return this;
    }

    @Override
    public ITimeSlice getTimeRange() {
        return this.timeRange;
    }

    @Override
    public Session setTime(ITimeSlice time) {
        timeRange = time;
        return this;
    }

    @Override
    public String getSessionLocation() {
        return this.atLocation;
    }

    @Override
    public Session setSessionLocation(String location) {
        this.atLocation = location;
        return this;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public Session approve() {
        this.status = SessionStatus.ACCEPTED;
        return this;
    }

    @Override
    public Session reject() {
        this.status = SessionStatus.REJECTED;
        return this;
    }

    @Override
    public Session pend() {
        this.status = SessionStatus.PENDING;
        return this;
    }

    @Override
    public double getSessionCost() {
        return sessionCost;
    }

    @Override
    public void setSessionCost(double sessionCost) {
        this.sessionCost = sessionCost;
    }
}
