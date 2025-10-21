package comp3350.teachreach.logic.DAOs;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import comp3350.teachreach.application.Server;
import comp3350.teachreach.data.interfaces.ISessionPersistence;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.SessionStatus;
import comp3350.teachreach.objects.interfaces.ISession;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;

public
class AccessSessions {
    private static final Predicate<ISession> isAccepted = s -> s.getStatus() == SessionStatus.ACCEPTED;
    private static final Predicate<ISession> isRejected = s -> s.getStatus() == SessionStatus.REJECTED;
    private static final Predicate<ISession> isPending = s -> s.getStatus() == SessionStatus.PENDING;
    private static ISessionPersistence sessionPersistence;
    private static Map<Integer, ISession> sessions = null;

    public AccessSessions() {
        AccessSessions.sessionPersistence = Server.getSessionDataAccess();
        AccessSessions.sessions = sessionPersistence.getSessions();
    }

    public AccessSessions(ISessionPersistence sessionDataAccess) {
        AccessSessions.sessionPersistence = sessionDataAccess;
        AccessSessions.sessions = sessionDataAccess.getSessions();
    }

    public boolean deleteSession(ISession session) {
        try {
            boolean result
                    = sessionPersistence.deleteSession(session);
            if (result) {
                sessions = sessionPersistence.getSessions();
            }
            return result;
        } catch (final Exception e) {
            throw new DataAccessException("Failed to delete session", e);
        }
    }

    public ISession storeSession(ISession session) {
        try {
            ISession newSession = sessionPersistence.storeSession(session);
            sessions = sessionPersistence.getSessions();
            return newSession;
        } catch (final Exception e) {
            throw new DataAccessException("Failed to store session!", e);
        }
    }

    public ISession updateSession(ISession session) {
        try {
            session = sessionPersistence.updateSession(session);
            sessions = sessionPersistence.getSessions();
            return session;
        } catch (final Exception e) {
            throw new DataAccessException("Failed to update session!", e);
        }
    }

    private List<ISession> getSessions(Object person, Predicate<ISession> filterPredicate, String errorMessage) {
        try {
            int id;
            if (person instanceof ITutor) {
                id = ((ITutor) person).getTutorID();
            } else if (person instanceof IStudent) {
                id = ((IStudent) person).getStudentID();
            } else {
                throw new IllegalArgumentException("Unsupported person type");
            }

            Predicate<ISession> idMatch = s -> {
                if (person instanceof ITutor) {
                    return s.getSessionTutorID() == id;
                } else {
                    return s.getSessionStudentID() == id;
                }
            };

            return AccessSessions.sessions.values()
                    .stream()
                    .filter(filterPredicate.and(idMatch))
                    .collect(Collectors.toList());
        } catch (final Exception e) {
            throw new DataAccessException(errorMessage, e);
        }
    }

    public List<ISession> getSessions(ITutor tutor) {
        return getSessions(tutor, s -> true, "Failed to get sessions by tutor");
    }

    public List<ISession> getSessions(IStudent student) {
        return getSessions(student, s -> true, "Failed to get sessions by student");
    }

    public List<ISession> getPendingSessions(ITutor tutor) {
        return getSessions(tutor, isPending, "Failed to get pending sessions by tutor");
    }

    public List<ISession> getPendingSessions(IStudent student) {
        return getSessions(student, isPending, "Failed to get pending sessions by student");
    }

    public List<ISession> getAcceptedSessions(ITutor tutor) {
        return getSessions(tutor, isAccepted, "Failed to get accepted sessions by tutor");
    }

    public List<ISession> getAcceptedSessions(IStudent student) {
        return getSessions(student, isAccepted, "Failed to get accepted sessions by student");
    }

    public List<ISession> getRejectedSessions(ITutor tutor) {
        return getSessions(tutor, isRejected, "Failed to get rejected sessions by tutor");
    }

    public List<ISession> getRejectedSessions(IStudent student) {
        return getSessions(student, isRejected, "Failed to get rejected sessions by student");
    }


}
