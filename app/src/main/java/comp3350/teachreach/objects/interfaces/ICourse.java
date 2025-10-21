package comp3350.teachreach.objects.interfaces;

public
interface ICourse
{
    String getCourseCode();

    ICourse setCourseCode(String courseCode);

    String getCourseName();

    ICourse setCourseName(String courseName);

    boolean equals(ICourse otherCourse);

    String toString();
}
