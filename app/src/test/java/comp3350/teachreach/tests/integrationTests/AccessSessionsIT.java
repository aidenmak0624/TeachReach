package comp3350.teachreach.tests.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.LocalDate;

import java.io.File;
import java.io.IOException;
import java.util.List;

import comp3350.teachreach.application.TRData;
import comp3350.teachreach.data.exceptions.DuplicateEmailException;
import comp3350.teachreach.data.hsqldb.SessionHSQLDB;
import comp3350.teachreach.data.interfaces.ISessionPersistence;
import comp3350.teachreach.logic.DAOs.AccessSessions;
import comp3350.teachreach.logic.exceptions.AccountCreatorException;
import comp3350.teachreach.logic.exceptions.InvalidNameException;
import comp3350.teachreach.logic.exceptions.input.InvalidEmailException;
import comp3350.teachreach.logic.exceptions.input.InvalidPasswordException;
import comp3350.teachreach.objects.Session;
import comp3350.teachreach.objects.SessionStatus;
import comp3350.teachreach.objects.Student;
import comp3350.teachreach.objects.TimeSlice;
import comp3350.teachreach.objects.Tutor;
import comp3350.teachreach.objects.interfaces.ISession;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;
import comp3350.teachreach.tests.utils.TestUtils;

public class AccessSessionsIT {
    private AccessSessions accessSessions;
    private File tempDB;
    private IStudent testStudent;
    private ITutor testTutor;
    private ISession testSession;

    @Before
    public void setUp() throws
            IOException,
            InvalidNameException,
            DuplicateEmailException,
            InvalidPasswordException,
            InvalidEmailException,
            AccountCreatorException {
        TRData.setDefaultEnums();
        this.tempDB = TestUtils.copyDB();
        final ISessionPersistence sessionPersistence
                = new SessionHSQLDB(this.tempDB
                .getAbsolutePath()
                .replace(".script", ""));
        this.accessSessions = new AccessSessions(sessionPersistence);
        this.testStudent = new Student(1, 1);
        this.testTutor = new Tutor(1, 3);

        LocalDate date = LocalDate.of(2023, 12, 21);
        TimeSlice sessionTime = new TimeSlice(date.atTime(10, 30),
                date.atTime(11, 0));
        String sessionLocation = "420 Feltcher Argue";
        this.testSession = new Session(testStudent.getStudentID(), testTutor.getTutorID(), sessionTime, 15.00, sessionLocation);
    }

    @Test
    public void testStoreAccessSession() {
        ISession storedSession
                = accessSessions.storeSession(testSession);
        ISession retrievedSession = accessSessions
                .getSessions(testStudent)
                .get(0);
        assertNotNull(retrievedSession);
        assertEquals(2023, retrievedSession.getTimeRange().getStartYear());
        assertEquals(12, retrievedSession.getTimeRange().getStartMonth());
        assertEquals(21, retrievedSession.getTimeRange().getStartDay());
        assertEquals(10, retrievedSession.getTimeRange().getStartHour());
        assertEquals(30, retrievedSession.getTimeRange().getStartMinute());
        assertEquals(2023, retrievedSession.getTimeRange().getEndYear());
        assertEquals(12, retrievedSession.getTimeRange().getEndMonth());
        assertEquals(21, retrievedSession.getTimeRange().getEndDay());
        assertEquals(11, retrievedSession.getTimeRange().getEndHour());
        assertEquals(0, retrievedSession.getTimeRange().getEndMinute());
        assertEquals(testStudent.getStudentID(),
                retrievedSession.getSessionStudentID());
        assertEquals(testTutor.getTutorID(),
                retrievedSession.getSessionTutorID());
    }

    @Test
    public void testDeleteSession() {

        ISession storedSession
                = accessSessions.storeSession(testSession);
        ISession retrievedSession = accessSessions
                .getSessions(testStudent)
                .get(0);
        assertNotNull(retrievedSession);
        assertTrue(accessSessions.deleteSession(retrievedSession));
        assertFalse(accessSessions.deleteSession(retrievedSession));
        List<ISession> retrievedSessions = accessSessions.getSessions(testStudent);
        assertTrue(retrievedSessions.isEmpty());
    }

    @Test
    public void testUpdateSession() {
        LocalDate sessionDate = LocalDate.of(2023, 12, 21);
        TimeSlice newSessionTime = new TimeSlice(sessionDate.atTime(20, 30),
                sessionDate.atTime(21, 0));
        double newSessionCost = 40.00;
        String newSessionLocation = "469 Feltcher Argue";

        accessSessions.storeSession(testSession);
        ISession retrievedSession = accessSessions
                .getSessions(testStudent)
                .get(0);
        assertNotNull(retrievedSession);
        retrievedSession.setSessionLocation(newSessionLocation);
        retrievedSession.approve();
        retrievedSession.setTime(newSessionTime);
        retrievedSession.setSessionCost(newSessionCost);
        ISession updatedSession = accessSessions.updateSession(retrievedSession);
        assertNotNull(updatedSession);
        assertEquals(2023, updatedSession.getTimeRange().getStartYear());
        assertEquals(12, updatedSession.getTimeRange().getStartMonth());
        assertEquals(21, updatedSession.getTimeRange().getStartDay());
        assertEquals(20, updatedSession.getTimeRange().getStartHour());
        assertEquals(30, updatedSession.getTimeRange().getStartMinute());
        assertEquals(2023, updatedSession.getTimeRange().getEndYear());
        assertEquals(12, updatedSession.getTimeRange().getEndMonth());
        assertEquals(21, updatedSession.getTimeRange().getEndDay());
        assertEquals(21, updatedSession.getTimeRange().getEndHour());
        assertEquals(0, updatedSession.getTimeRange().getEndMinute());
        assertEquals(newSessionLocation, updatedSession.getSessionLocation());
        assertEquals(SessionStatus.ACCEPTED, updatedSession.getStatus());
    }

    @Test
    public void testGetPendingSessions() {
        List<ISession> studentPendingSessionsList
                =
                accessSessions.getPendingSessions(testStudent);
        List<ISession> tutorPendingSessionList
                =
                accessSessions.getPendingSessions(testTutor);
        assertEquals(0, studentPendingSessionsList.size());
        assertEquals(0, tutorPendingSessionList.size());

        accessSessions.storeSession(testSession);
        studentPendingSessionsList
                = accessSessions.getPendingSessions(testStudent);
        tutorPendingSessionList = accessSessions.getPendingSessions(testTutor);
        assertEquals(1, studentPendingSessionsList.size());
        assertEquals(1, tutorPendingSessionList.size());
        assertEquals(SessionStatus.PENDING, studentPendingSessionsList.get(0).getStatus());
        assertEquals(SessionStatus.PENDING, tutorPendingSessionList.get(0).getStatus());
    }

    @Test
    public void testGetAcceptedSessions() {
        List<ISession> studentAcceptedSessionsList
                =
                accessSessions.getAcceptedSessions(testStudent);
        List<ISession> tutorAcceptedSessionList
                =
                accessSessions.getAcceptedSessions(testTutor);
        assertEquals(0, studentAcceptedSessionsList.size());
        assertEquals(0, tutorAcceptedSessionList.size());

        this.testSession.approve();
        accessSessions.storeSession(testSession);
        studentAcceptedSessionsList
                = accessSessions.getAcceptedSessions(testStudent);
        tutorAcceptedSessionList = accessSessions.getAcceptedSessions(testTutor);
        assertEquals(1, studentAcceptedSessionsList.size());
        assertEquals(1, tutorAcceptedSessionList.size());
        assertEquals(SessionStatus.ACCEPTED, studentAcceptedSessionsList.get(0).getStatus());
        assertEquals(SessionStatus.ACCEPTED, tutorAcceptedSessionList.get(0).getStatus());
    }

    @Test
    public void testGetRejectedSessions() {
        List<ISession> studentRejectedSessionsList
                =
                accessSessions.getRejectedSessions(testStudent);
        List<ISession> tutorRejectedSessionList
                =
                accessSessions.getRejectedSessions(testTutor);
        assertEquals(0, studentRejectedSessionsList.size());
        assertEquals(0, tutorRejectedSessionList.size());

        this.testSession.reject();
        accessSessions.storeSession(testSession);
        studentRejectedSessionsList
                = accessSessions.getRejectedSessions(testStudent);
        tutorRejectedSessionList = accessSessions.getRejectedSessions(testTutor);
        assertEquals(1, studentRejectedSessionsList.size());
        assertEquals(1, tutorRejectedSessionList.size());
        assertEquals(SessionStatus.REJECTED, studentRejectedSessionsList.get(0).getStatus());
        assertEquals(SessionStatus.REJECTED, tutorRejectedSessionList.get(0).getStatus());
    }

    @Test
    public void testGetSessions() {
        List<ISession> studentSessionsList
                =
                accessSessions.getSessions(testStudent);
        List<ISession> tutorSessionList = accessSessions.getSessions(
                testTutor);
        assertEquals(0, studentSessionsList.size());
        assertEquals(0, tutorSessionList.size());

        accessSessions.storeSession(testSession);
        studentSessionsList
                =
                accessSessions.getSessions(testStudent);
        tutorSessionList
                =
                accessSessions.getSessions(testTutor);
        assertEquals(1, studentSessionsList.size());
        assertEquals(1, tutorSessionList.size());
    }

    @After
    public void tearDown() {
        this.tempDB.delete();
    }
}
