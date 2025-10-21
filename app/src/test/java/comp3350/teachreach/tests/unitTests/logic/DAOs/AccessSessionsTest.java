package comp3350.teachreach.tests.unitTests.logic.DAOs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.threeten.bp.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp3350.teachreach.application.TRData;
import comp3350.teachreach.data.interfaces.ISessionPersistence;
import comp3350.teachreach.logic.DAOs.AccessSessions;
import comp3350.teachreach.objects.Session;
import comp3350.teachreach.objects.SessionStatus;
import comp3350.teachreach.objects.Student;
import comp3350.teachreach.objects.TimeSlice;
import comp3350.teachreach.objects.Tutor;
import comp3350.teachreach.objects.interfaces.ISession;

@RunWith(MockitoJUnitRunner.class)
public class AccessSessionsTest {

    @Mock
    private ISessionPersistence sessionPersistence;

    @InjectMocks
    private AccessSessions accessSessions;


    @Before
    public void init() {
        TRData.setDefaultEnums();

        Map<Integer, ISession> returns = new HashMap<>();
        returns.put(1, new Session(1, 1,1, new TimeSlice(LocalDateTime.of(2100, 12, 1, 10, 0 ), LocalDateTime.of(2100, 12, 1, 11, 0)), 15, "Coffee Shop", SessionStatus.PENDING));
        returns.put(2, new Session(2, 2,1, new TimeSlice(LocalDateTime.of(2100, 12, 1, 12, 0 ), LocalDateTime.of(2100, 12, 1, 13, 0)), 15, "Coffee Shop", SessionStatus.PENDING));
        returns.put(3, new Session(3, 3,1, new TimeSlice(LocalDateTime.of(2100, 12, 2, 10, 0 ), LocalDateTime.of(2100, 12, 2, 13, 0)), 15, "Library", SessionStatus.ACCEPTED));
        returns.put(4, new Session(4, 4,1, new TimeSlice(LocalDateTime.of(2100, 12, 2, 12, 0 ), LocalDateTime.of(2100, 12, 2, 11, 0)), 15, "Library", SessionStatus.ACCEPTED));
        returns.put(5, new Session(5, 2,1, new TimeSlice(LocalDateTime.of(2100, 12, 3, 12, 0 ), LocalDateTime.of(2100, 12, 1, 13, 0)), 15, "Library", SessionStatus.REJECTED));
        returns.put(6, new Session(6, 6,1, new TimeSlice(LocalDateTime.of(2100, 12, 3, 10, 0 ), LocalDateTime.of(2100, 12, 1, 11, 0)), 15, "Library", SessionStatus.REJECTED));
        returns.put(7, new Session(7, 7,2, new TimeSlice(LocalDateTime.of(2100, 12, 1, 12, 0 ), LocalDateTime.of(2100, 12, 1, 13, 0)), 15, "Coffee Shop", SessionStatus.PENDING));
        returns.put(8, new Session(8, 2,2, new TimeSlice(LocalDateTime.of(2100, 12, 1, 10, 0 ), LocalDateTime.of(2100, 12, 1, 11, 0)), 15, "Coffee Shop", SessionStatus.PENDING));
        returns.put(9, new Session(9, 9,2, new TimeSlice(LocalDateTime.of(2100, 12, 2, 12, 0 ), LocalDateTime.of(2100, 12, 2, 13, 0)), 15, "Library", SessionStatus.ACCEPTED));
        returns.put(10, new Session(10, 10,2, new TimeSlice(LocalDateTime.of(2100, 12, 2, 10, 0 ), LocalDateTime.of(2100, 12, 2, 11, 0)), 15, "Library", SessionStatus.ACCEPTED));
        returns.put(11, new Session(11, 11,2, new TimeSlice(LocalDateTime.of(2100, 12, 3, 12, 0 ), LocalDateTime.of(2100, 12, 1, 13, 0)), 15, "Coffee Shop", SessionStatus.REJECTED));
        returns.put(12, new Session(12, 12,2, new TimeSlice(LocalDateTime.of(2100, 12, 3, 10, 0 ), LocalDateTime.of(2100, 12, 1, 11, 0)), 15, "Coffee Shop", SessionStatus.REJECTED));
        doReturn(returns).when(sessionPersistence).getSessions();

        accessSessions = new AccessSessions(sessionPersistence);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getSessionsTest() {
        List<ISession> result = accessSessions.getSessions(new Tutor(1, 1));

        assertEquals("Issue with getSessions for tutor", 6, result.size());

        result = accessSessions.getSessions(new Tutor(2, 2));

        assertEquals("Issue with getSessions for tutor", 6, result.size());

        result = accessSessions.getSessions(new Student(2, 3));

        assertEquals("Issue with getSessions for student", 3, result.size());

    }

    @Test
    public void getPendingSessionsTest() {

        List<ISession> result = accessSessions.getPendingSessions(new Tutor(1, 1));

        assertEquals("Issue with getPendingSessions for tutor", 2, result.size());

        result = accessSessions.getPendingSessions(new Tutor(2, 2));

        assertEquals("Issue with getPendingSessions for tutor", 2, result.size());

        result = accessSessions.getPendingSessions(new Student(2, 3));

        assertEquals("Issue with getPendingSessions for student", 2, result.size());
    }


    @Test
    public void getAcceptedSessionsTest() {

        List<ISession> result = accessSessions.getAcceptedSessions(new Tutor(1, 1));

        assertEquals("Issue with getAcceptedSessions for tutor", 2, result.size());

        result = accessSessions.getAcceptedSessions(new Tutor(2, 2));

        assertEquals("Issue with getAcceptedSessions for tutor", 2, result.size());

        result = accessSessions.getAcceptedSessions(new Student(2, 3));

        assertEquals("Issue with getAcceptedSessions for Student", 0, result.size());

    }

    @Test
    public void getRejectedSessionsTest() {

        List<ISession> result = accessSessions.getRejectedSessions(new Tutor(1, 1));

        assertEquals("Issue with getRejectedSessions for tutor", 2, result.size());

        result = accessSessions.getRejectedSessions(new Tutor(2, 2));

        assertEquals("Issue with getRejectedSessions for tutor", 2, result.size());

        result = accessSessions.getRejectedSessions(new Student(2, 3));

        assertEquals("Issue with getRejectedSessions for student", 1, result.size());

    }
}
