package comp3350.teachreach.data.stubs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import comp3350.teachreach.data.interfaces.ICoursePersistence;
import comp3350.teachreach.objects.Course;
import comp3350.teachreach.objects.interfaces.ICourse;

public
class CourseStub implements ICoursePersistence
{
    private static Map<String, ICourse> courses = null;

    public
    CourseStub()
    {
        if (courses == null) {
            CourseStub.courses = new HashMap<>();
        }
        CourseStub.courses.put("COMP 2080",
                               new Course("COMP 2080",
                                          "Analysis of Algorithms"));
        CourseStub.courses.put("COMP 1010",
                               new Course("COMP 1010",
                                          "Introduction to Computer Science"));
        CourseStub.courses.put("COMP 1012",
                               new Course("COMP 1012",
                                          "Introduction to Computer Science " +
                                          "for Engineers"));
        CourseStub.courses.put("COMP 2150",
                               new Course("COMP 2150", "Object Orientation"));
        CourseStub.courses.put("COMP 3380",
                               new Course("COMP 3380",
                                          "Databases Concepts and Usage"));
    }

    @Override
    public
    Map<String, ICourse> getCourses()
    {
        return CourseStub.courses;
    }

    @Override
    public
    ICourse addCourse(String courseCode, String courseName)
    {
        return CourseStub.courses.putIfAbsent(courseCode,
                                              new Course(courseCode,
                                                         courseName));
    }

    @Override
    public
    Optional<ICourse> getCourseByCourseCode(String courseCode)
    {
        return Optional.ofNullable(CourseStub.courses.get(courseCode));
    }

    @Override
    public
    List<ICourse> getCoursesByName(String courseName)
    {
        return courses
                .entrySet()
                .parallelStream()
                .filter(e -> e.getValue().getCourseName().contains(courseName))
                .map(e -> (ICourse) new Course(e.getKey(),
                                               e.getValue().getCourseName()))
                .collect(Collectors.toList());
    }
}
