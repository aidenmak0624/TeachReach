package comp3350.teachreach.presentation.session;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.Callables;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comp3350.teachreach.application.Server;
import comp3350.teachreach.logic.availability.TutorAvailabilityManager;
import comp3350.teachreach.logic.interfaces.ISessionHandler;
import comp3350.teachreach.logic.interfaces.ITutorAvailabilityManager;
import comp3350.teachreach.logic.session.SessionHandler;
import comp3350.teachreach.objects.interfaces.ISession;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;

public
class SessionViewModel extends ViewModel
{
    private final ExecutorService          threadPool = Executors.newCachedThreadPool();
    private final ListeningExecutorService service    = MoreExecutors.listeningDecorator(threadPool);


    private final ITutorAvailabilityManager availabilityManager = new TutorAvailabilityManager();

    private final LiveData<ISessionHandler> sessionHandler = new MutableLiveData<>(new SessionHandler(
            availabilityManager));


    private final MutableLiveData<ConcurrentMap<Integer, IStudent>>
            studentMap
            = new MutableLiveData<>(new ConcurrentHashMap<>(Server.getStudentDataAccess().getStudents()));

    private final MutableLiveData<ConcurrentMap<Integer, ITutor>>
                                                  tutorMap
                                                                      = new MutableLiveData<>(new ConcurrentHashMap<>(
            Server.getTutorDataAccess().getTutors()));
    private final MutableLiveData<List<ISession>> sessionsBeingViewed = new MutableLiveData<>();
    private final MutableLiveData<String>         err                 = new MutableLiveData<>(null);

    public
    LiveData<ISessionHandler> getSessionHandler()
    {
        return sessionHandler;
    }

    public
    void setSessionsStudent(@NonNull SessionType type, @NonNull IStudent s)
    {
        ListenableFuture<List<ISession>> futureList = Futures.submitAsync(Callables.asAsyncCallable(() -> {
            switch (type) {
                case pending -> {
                    return sessionHandler.getValue().getPendingSessions(s);
                }
                case accepted -> {
                    return sessionHandler.getValue().getAcceptedSessions(s);
                }
                case rejected -> {
                    return sessionHandler.getValue().getRejectedSessions(s);
                }
                default -> {
                    return sessionHandler.getValue().getSessions(s);
                }
            }
        }, service), service);
        waitForUpdate(futureList);
    }

    public
    void setSessionsTutor(@NonNull SessionType type, @NonNull ITutor t)
    {
        ListenableFuture<List<ISession>> futureList = Futures.submitAsync(Callables.asAsyncCallable(() -> {
            switch (type) {
                case pending -> {
                    return sessionHandler.getValue().getPendingSessions(t);
                }
                case accepted -> {
                    return sessionHandler.getValue().getAcceptedSessions(t);
                }
                case rejected -> {
                    return sessionHandler.getValue().getRejectedSessions(t);
                }
                default -> {
                    return sessionHandler.getValue().getSessions(t);
                }
            }
        }, service), service);
        waitForUpdate(futureList);
    }

    public
    LiveData<ConcurrentMap<Integer, IStudent>> getStudents()
    {
        return studentMap;
    }

    public
    LiveData<ConcurrentMap<Integer, ITutor>> getTutors()
    {
        return tutorMap;
    }

    public
    LiveData<List<ISession>> getSessionsBeingViewed()
    {
        return sessionsBeingViewed;
    }

    private
    void waitForUpdate(@NonNull ListenableFuture<List<ISession>> future)
    {
        Futures.addCallback(future, new FutureCallback<>()
        {
            @Override
            public
            void onSuccess(List<ISession> result)
            {
                sessionsBeingViewed.postValue(result);
            }

            @Override
            public
            void onFailure(@NonNull Throwable t)
            {
                err.postValue(t.getMessage());
            }
        }, service);
    }

    @Override
    protected
    void onCleared()
    {
        super.onCleared();
        threadPool.shutdown();
        service.shutdown();
    }

    public
    enum SessionType
    {
        pending, accepted, rejected, others
    }
}
