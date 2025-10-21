package comp3350.teachreach.logic.DAOs;

import java.util.List;

import comp3350.teachreach.application.Server;
import comp3350.teachreach.data.interfaces.ITutoredCoursesPersistence;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.interfaces.ICourse;

public class AccessTutoredCourses
{
    private static ITutoredCoursesPersistence TutoredCoursesPersistence;

    public AccessTutoredCourses()
    {
        TutoredCoursesPersistence = Server.getTutorCourseAccess();
    }

    public AccessTutoredCourses(ITutoredCoursesPersistence tutorCourseAccess)
    {
        AccessTutoredCourses.TutoredCoursesPersistence = tutorCourseAccess;
    }

    public List<ICourse> getTutoredCoursesByTutorID(int tutorID)
    {
        try {
            return TutoredCoursesPersistence.getTutorCourseByTutorID(tutorID);
        } catch (final Exception e) {
            throw new DataAccessException(
                    "Failed to get tutor tutored courses by tutor's " + "id",
                    e);
        }
    }

    public boolean storeTutorCourse(int tutorID, String courseCode)
    {
        try {
            return TutoredCoursesPersistence.storeTutorCourse(tutorID,
                                                              courseCode);
        } catch (final Exception e) {
            throw new DataAccessException(
                    "Failed to store tutor tutored course!",
                    e);
        }
    }
}
