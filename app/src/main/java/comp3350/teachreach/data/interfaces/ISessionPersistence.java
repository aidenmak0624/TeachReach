package comp3350.teachreach.data.interfaces;

import java.util.Map;

import comp3350.teachreach.objects.interfaces.ISession;

public
interface ISessionPersistence {
    ISession storeSession(ISession newSession);

    boolean deleteSession(ISession session);

    ISession updateSession(ISession session);

    Map<Integer, ISession> getSessions();
}
