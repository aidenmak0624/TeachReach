package comp3350.teachreach.data.interfaces;

import java.util.List;

import comp3350.teachreach.objects.interfaces.ICourse;

public interface ITutoredCoursesPersistence
{
    List<ICourse> getTutorCourseByTutorID(int tutorID);

    boolean storeTutorCourse(int tutorID, String courseCode);
}
