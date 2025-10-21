package comp3350.teachreach.presentation.profile.tutor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import comp3350.teachreach.logic.availability.TutorAvailabilityManager;
import comp3350.teachreach.logic.exceptions.availability.TutorAvailabilityManagerException;
import comp3350.teachreach.logic.exceptions.input.InvalidInputException;
import comp3350.teachreach.logic.interfaces.ITutorAvailabilityManager;
import comp3350.teachreach.logic.interfaces.ITutorProfileHandler;
import comp3350.teachreach.logic.interfaces.IUserProfileHandler;
import comp3350.teachreach.logic.profile.TutorProfileHandler;
import comp3350.teachreach.logic.profile.UserProfileFetcher;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.ITimeSlice;
import comp3350.teachreach.objects.interfaces.ITutor;
import comp3350.teachreach.presentation.utils.TimeSliceFormatter;

public
class TutorProfileViewModel extends ViewModel
{
    private final LiveData<IUserProfileHandler<ITutor>>
            profileFetcher
            = new MutableLiveData<>(new UserProfileFetcher<>());

    private final LiveData<ITutorProfileHandler> profileHandler = new MutableLiveData<>(new TutorProfileHandler());

    private final LiveData<ITutorAvailabilityManager>
            availabilityManager
            = new MutableLiveData<>(new TutorAvailabilityManager());


    private final MutableLiveData<List<String>> tutoredCoursesCode = new MutableLiveData<>();

    private final MutableLiveData<List<String>> preferredLocations = new MutableLiveData<>();
    private final MutableLiveData<List<String>> availStrList       = new MutableLiveData<>();
    private final MutableLiveData<ITutor>       tutor              = new MutableLiveData<>();
    private final MutableLiveData<IAccount>     tutorAccount       = new MutableLiveData<>();

    public
    LiveData<List<String>> getAvailStrList()
    {
        return availStrList;
    }

    public
    void setAvailStrList(List<String> l)
    {
        availStrList.setValue(l);
    }

    public
    void addAvailability(ITutor t, ITimeSlice ts) throws TutorAvailabilityManagerException
    {
        this.getAvailabilityManager().getValue().addAvailability(t, ts);
        List<String> updated = getAvailStrList().getValue();
        updated.add(TimeSliceFormatter.format(ts));
        availStrList.postValue(updated);
    }

    public
    LiveData<List<String>> getTutoredCoursesCode()
    {
        return this.tutoredCoursesCode;
    }

    public
    void setTutoredCoursesCode(List<String> tutoredCoursesCode)
    {
        this.tutoredCoursesCode.setValue(tutoredCoursesCode);
    }

    public
    void addCourse(ITutor t, String courseCode, String courseName) throws InvalidInputException
    {
        ITutorProfileHandler tph = new TutorProfileHandler();
        tph.addCourse(t, courseCode, courseName);
        this.tutoredCoursesCode.postValue(tph.getCourseCodeList(t));
    }

    public
    void addLocation(ITutor t, String l) throws InvalidInputException
    {
        ITutorProfileHandler tph = new TutorProfileHandler();
        tph.addPreferredLocation(t, l);
        this.preferredLocations.postValue(tph.getPreferredLocations(t));
    }

    public
    LiveData<List<String>> getPreferredLocations()
    {
        return this.preferredLocations;
    }

    public
    void setPreferredLocations(List<String> l)
    {
        this.preferredLocations.setValue(l);
    }

    public
    void postTutor(ITutor t)
    {
        this.tutor.postValue(t);
    }

    public
    LiveData<ITutor> getTutor()
    {
        return tutor;
    }

    public
    void setTutor(ITutor t)
    {
        this.tutor.setValue(t);
    }

    public
    void postTutorAccount(IAccount a)
    {
        this.tutorAccount.postValue(a);
    }

    public
    LiveData<IAccount> getTutorAccount()
    {
        return tutorAccount;
    }

    public
    void setTutorAccount(IAccount a)
    {
        this.tutorAccount.setValue(a);
    }

    public
    LiveData<IUserProfileHandler<ITutor>> getProfileFetcher()
    {
        return profileFetcher;
    }

    public
    LiveData<ITutorAvailabilityManager> getAvailabilityManager()
    {
        return availabilityManager;
    }

    public
    LiveData<ITutorProfileHandler> getProfileHandler()
    {
        return profileHandler;
    }
}
