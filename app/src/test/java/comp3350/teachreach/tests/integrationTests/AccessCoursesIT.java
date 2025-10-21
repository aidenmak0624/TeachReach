package comp3350.teachreach.tests.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import comp3350.teachreach.data.hsqldb.CourseHSQLDB;
import comp3350.teachreach.data.interfaces.ICoursePersistence;
import comp3350.teachreach.logic.DAOs.AccessCourses;
import comp3350.teachreach.objects.Course;
import comp3350.teachreach.objects.interfaces.ICourse;
import comp3350.teachreach.tests.utils.TestUtils;

public
class AccessCoursesIT
{
    private AccessCourses accessCourses;
    private File          tempDB;

    @Before
    public
    void setUp() throws IOException
    {
        this.tempDB = TestUtils.copyDB();
        final ICoursePersistence persistence = new CourseHSQLDB(this.tempDB
                                                                        .getAbsolutePath()
                                                                        .replace(
                                                                                ".script",
                                                                                ""));
        this.accessCourses = new AccessCourses(persistence);
    }

    @Test
    public
    void testGetCourses()
    {
        final Map<String, ICourse> courses = accessCourses.getCourses();
        assertEquals(5, courses.size());
    }

    @Test
    public
    void testGetCourseByCode()
    {
        ICourse course = new Course("COMP 3030", "AUTOMATA");
        accessCourses.insertCourse(course);
        course = accessCourses.getCourseByCode("COMP 3030");
        assertNotNull(course);
        assertEquals(course.getCourseCode(), "COMP 3030");
    }

    @Test
    public
    void testGetCourseByName()
    {
        ICourse course = new Course("COMP 3030", "AUTOMATA");
        accessCourses.insertCourse(course);
        final List<ICourse> courses
                = accessCourses.getCoursesByName("AUTOMATA");
        assertNotNull(courses);
        assertEquals(1, courses.size());
    }

    @Test
    public
    void testInsertCourse()
    {
        final ICourse course = new Course("COMP 5555", "GOD OF COMPUTER");
        accessCourses.insertCourse(course);
        assertEquals(6, accessCourses.getCourses().size());
    }

    @After
    public
    void tearDown()
    {
        this.tempDB.delete();
    }
}
