package comp3350.teachreach.data.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import comp3350.teachreach.objects.interfaces.ICourse;

public
interface ICoursePersistence
{
    Map<String, ICourse> getCourses();

    ICourse addCourse(String courseCode, String courseName);

    Optional<ICourse> getCourseByCourseCode(String courseCode);

    List<ICourse> getCoursesByName(String courseName);
}
