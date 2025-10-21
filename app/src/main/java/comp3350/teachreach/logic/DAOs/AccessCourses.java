package comp3350.teachreach.logic.DAOs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import comp3350.teachreach.application.Server;
import comp3350.teachreach.data.interfaces.ICoursePersistence;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.interfaces.ICourse;

public class AccessCourses
{
    private static ICoursePersistence   coursePersistence;
    private static Map<String, ICourse> coursesMap = null;

    public AccessCourses()
    {
        AccessCourses.coursePersistence = Server.getCourseDataAccess();
        AccessCourses.coursesMap        = coursePersistence.getCourses();
    }

    public AccessCourses(ICoursePersistence courseDataAccess)
    {
        AccessCourses.coursePersistence = courseDataAccess;
        AccessCourses.coursesMap        = coursePersistence.getCourses();
    }

    public Map<String, ICourse> getCourses()
    {
        try {
            if (AccessCourses.coursesMap == null) {
                AccessCourses.coursesMap = coursePersistence.getCourses();
            }
            return Collections.unmodifiableMap(coursesMap);
        } catch (final Exception e) {
            throw new DataAccessException("Failed to get all courses!", e);
        }
    }

    public List<ICourse> getCourseList()
    {
        try {
            if (AccessCourses.coursesMap == null) {
                AccessCourses.coursesMap = coursePersistence.getCourses();
            }
            return Collections.unmodifiableList(new ArrayList<>(coursesMap.values()));
        } catch (final Exception e) {
            throw new DataAccessException("Failed to get all courses!", e);
        }
    }

    public ICourse getCourseByCode(String courseCode)
    {
        return coursePersistence
                .getCourseByCourseCode(courseCode)
                .orElseThrow(() -> new DataAccessException("Course not found",
                                                           new NoSuchElementException()));
    }

    public List<ICourse> getCoursesByCode(String courseCode)
    {
        try {
            if (AccessCourses.coursesMap == null) {
                AccessCourses.coursesMap = coursePersistence.getCourses();
            }
            return Collections.unmodifiableList(coursesMap
                                                        .keySet()
                                                        .stream()
                                                        .filter(s -> s.contains(
                                                                courseCode))
                                                        .map(s -> coursesMap.get(
                                                                s))
                                                        .collect(Collectors.toList()));
        } catch (final Exception e) {
            throw new DataAccessException("Failed to get all courses!", e);
        }
    }

    public List<ICourse> getCoursesByName(String courseName)
    {
        try {
            return coursePersistence.getCoursesByName(courseName);
        } catch (final Exception e) {
            throw new DataAccessException("Failed to get courses by name", e);
        }
    }

    public ICourse insertCourse(ICourse newCourse) throws DataAccessException
    {
        try {
            newCourse  = coursePersistence.addCourse(newCourse.getCourseCode(),
                                                     newCourse.getCourseName());
            coursesMap = coursePersistence.getCourses();
            return newCourse;
        } catch (final Exception e) {
            throw new DataAccessException("Course might've already exist!", e);
        }
    }
}
