package comp3350.teachreach.logic.interfaces;

import java.util.List;

import comp3350.teachreach.logic.exceptions.input.InvalidInputException;
import comp3350.teachreach.objects.interfaces.ICourse;
import comp3350.teachreach.objects.interfaces.ITutor;

public
interface ITutorProfileHandler
{

    ITutorProfileHandler setHourlyRate(ITutor t, double hourlyRate);

    double getAvgReview(ITutor t);

    List<ICourse> getCourses(ITutor t);

    List<String> getCourseCodeList(ITutor t);

    List<String> getCourseDescriptionList(ITutor t);

    List<String> getPreferredLocations(ITutor t);

    ITutorProfileHandler addReview(ITutor t, int score);

    ITutorProfileHandler addCourse(ITutor t, String courseCode, String courseName) throws InvalidInputException;

    ITutorProfileHandler removeCourse(ITutor t, String courseCode);

    ITutorProfileHandler addPreferredLocation(ITutor t, String preferredLocation) throws InvalidInputException;

    ITutor updateTutorProfile(ITutor t);
}
