package comp3350.teachreach.tests.unitTests.object;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.threeten.bp.LocalDate;

import comp3350.teachreach.application.TRData;
import comp3350.teachreach.objects.Session;
import comp3350.teachreach.objects.SessionStatus;
import comp3350.teachreach.objects.TimeSlice;
import comp3350.teachreach.objects.interfaces.ISession;
import comp3350.teachreach.objects.interfaces.ITimeSlice;

public class SessionTest {

    private static ISession session;
    private static int sessionID;
    private static int studentID;
    private static int tutorID;
    private static ITimeSlice timeRange;
    private static double sessionCost;
    private static String atLocation;


    @BeforeClass
    public static void setUp() {
        TRData.setDefaultEnums();
        sessionID = -1;
        studentID = 123;
        tutorID = 456;
        LocalDate date = LocalDate.of(2021, 1, 1);
        timeRange = new TimeSlice(date.atTime(10, 0), date.atTime(10, 30));
        sessionCost = 50.0;
        atLocation = "Library";
        session = new Session(sessionID, studentID, tutorID, timeRange, sessionCost, atLocation, SessionStatus.PENDING);
    }

    @Test
    public void testSessionInitialization() {
        assertEquals(sessionID, session.getSessionID());
        assertEquals(studentID, session.getSessionStudentID());
        assertEquals(tutorID, session.getSessionTutorID());
        assertEquals(timeRange, session.getTimeRange());
        assertEquals(sessionCost, session.getSessionCost(), 0.001);
        assertEquals(atLocation, session.getSessionLocation());
        assertEquals(SessionStatus.PENDING, session.getStatus());
    }

    @Test
    public void testSessionStatusChange() {
        session.approve();
        assertEquals(SessionStatus.ACCEPTED, session.getStatus());

        session.reject();
        assertEquals(SessionStatus.REJECTED, session.getStatus());

        session.pend();
        assertEquals(SessionStatus.PENDING, session.getStatus());
    }

    @Test
    public void testSessionID() {
        assertEquals(-1, session.getSessionID());
        session.setSessionID(100);
        assertEquals(100, session.getSessionID());
    }

    @Test
    public void testSessionCost() {
        assertEquals(sessionCost, session.getSessionCost(), 0.001);
        session.setSessionCost(75.0);
        assertEquals(75.0, session.getSessionCost(), 0.001);
    }

    @Test
    public void testSessionStudentID() {
        assertEquals(studentID, session.getSessionStudentID());
        session.setSessionStudentID(999);
        assertEquals(999, session.getSessionStudentID());
    }

    @Test
    public void testSessionTutorID() {
        assertEquals(tutorID, session.getSessionTutorID());
        session.setSessionTutorID(888);
        assertEquals(888, session.getSessionTutorID());
    }

    @Test
    public void testSessionTimeRange() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        ITimeSlice newTimeRange = new TimeSlice(date.atTime(12, 0), date.atTime(12, 30));
        assertEquals(timeRange, session.getTimeRange());
        session.setTime(newTimeRange);
        assertEquals(newTimeRange, session.getTimeRange());
    }

    @Test
    public void testSessionLocation() {
        assertEquals(atLocation, session.getSessionLocation());
        session.setSessionLocation("Home");
        assertEquals("Home", session.getSessionLocation());
    }

}
