package comp3350.teachreach.logic.profile;

import java.util.List;
import java.util.stream.Collectors;

import comp3350.teachreach.logic.DAOs.AccessCourses;
import comp3350.teachreach.logic.DAOs.AccessTutorLocations;
import comp3350.teachreach.logic.DAOs.AccessTutoredCourses;
import comp3350.teachreach.logic.DAOs.AccessTutors;
import comp3350.teachreach.logic.account.InputValidator;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.logic.exceptions.input.InvalidInputException;
import comp3350.teachreach.logic.interfaces.ITutorProfileHandler;
import comp3350.teachreach.objects.Course;
import comp3350.teachreach.objects.interfaces.ICourse;
import comp3350.teachreach.objects.interfaces.ITutor;

public
class TutorProfileHandler implements ITutorProfileHandler
{
    private final AccessTutors         accessTutors;
    private final AccessTutoredCourses accessTutoredCourses;
    private final AccessTutorLocations accessTutorLocations;
    private final AccessCourses accessCourses;

    public
    TutorProfileHandler()
    {
        this.accessTutors = new AccessTutors();
        this.accessTutoredCourses = new AccessTutoredCourses();
        this.accessCourses = new AccessCourses();
        this.accessTutorLocations = new AccessTutorLocations();
    }

    public TutorProfileHandler(AccessTutors accessTutors, AccessTutoredCourses accessTutoredCourses,
                               AccessCourses accessCourses, AccessTutorLocations accessTutorLocations) {
        this.accessTutors = accessTutors;
        this.accessTutoredCourses = accessTutoredCourses;
        this.accessCourses = accessCourses;
        this.accessTutorLocations = accessTutorLocations;
    }

    @Override
    public
    ITutorProfileHandler setHourlyRate(ITutor t, double hourlyRate)
    {
        t.setHourlyRate(hourlyRate);
        return this;
    }

    @Override
    public
    double getAvgReview(ITutor t)
    {
        return t.getReviewCount() > 0 ? ((double) t.getReviewSum() / (double) t.getReviewCount()) : 0;
    }

    @Override
    public
    List<ICourse> getCourses(ITutor t)
    {
        return accessTutoredCourses.getTutoredCoursesByTutorID(t.getTutorID());
    }

    @Override
    public
    List<String> getCourseCodeList(ITutor t)
    {
        return accessTutoredCourses
                .getTutoredCoursesByTutorID(t.getTutorID())
                .stream()
                .map(ICourse::getCourseCode)
                .collect(Collectors.toList());
    }

    @Override
    public
    List<String> getCourseDescriptionList(ITutor t)
    {
        return accessTutoredCourses
                .getTutoredCoursesByTutorID(t.getTutorID())
                .stream()
                .map(ICourse::getCourseName)
                .collect(Collectors.toList());
    }

    @Override
    public
    List<String> getPreferredLocations(ITutor t)
    {
        return accessTutorLocations.getTutorLocationByTutorID(t.getTutorID());
    }

    @Override
    public
    ITutorProfileHandler addReview(ITutor t, int score)
    {
        return this;
    }

    @Override
    public
    ITutorProfileHandler addCourse(ITutor t, String courseCode, String courseName) throws InvalidInputException
    {
        InputValidator.validateInput(courseCode);
        InputValidator.validateInput(courseName);
        try {
            accessCourses.insertCourse(new Course(courseCode, courseName));
        } catch (final DataAccessException ignored) {
        } finally {
            accessTutoredCourses.storeTutorCourse(t.getTutorID(), courseCode);
        }
        return this;
    }

    @Override
    public
    ITutorProfileHandler removeCourse(ITutor t, String courseCode)
    {
        return this;
    }

    @Override
    public
    ITutorProfileHandler addPreferredLocation(ITutor t, String preferredLocation) throws InvalidInputException
    {
        InputValidator.validateInput(preferredLocation);

        accessTutorLocations.storeTutorLocation(t.getTutorID(), preferredLocation);

        return this;
    }

    @Override
    public
    ITutor updateTutorProfile(ITutor t)
    {
        t = accessTutors.updateTutor(t);
        return t;
    }
}
