package comp3350.teachreach.tests.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import comp3350.teachreach.data.hsqldb.TutoredCoursesHSQLDB;
import comp3350.teachreach.data.interfaces.ITutoredCoursesPersistence;
import comp3350.teachreach.logic.DAOs.AccessTutoredCourses;
import comp3350.teachreach.objects.interfaces.ICourse;
import comp3350.teachreach.tests.utils.TestUtils;

public class AccessTutoredCoursesIT {
    private AccessTutoredCourses accessTutoredCourses;
    private File tempDB;

    @Before
    public
    void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        final ITutoredCoursesPersistence persistence = new TutoredCoursesHSQLDB(this.tempDB
                .getAbsolutePath()
                .replace(
                        ".script",
                        ""));
        this.accessTutoredCourses = new AccessTutoredCourses(persistence);
    }

    @Test
    public void testGetTutorCourseByTID() {
        List<ICourse> tutoredCourses = accessTutoredCourses.getTutoredCoursesByTutorID(2);
        assertEquals(1, tutoredCourses.size());
    }

    @Test
    public void testStoreTutorCourse() {
        boolean result = accessTutoredCourses.storeTutorCourse(2, "COMP 3350");
        assertTrue(result);
        List<ICourse> tutoredCourses = accessTutoredCourses.getTutoredCoursesByTutorID(1);
        assertEquals(2, tutoredCourses.size());
        assertEquals("COMP 3350", tutoredCourses.get(1).getCourseCode());
    }

    @After
    public void tearDown() {
        this.tempDB.delete();
    }
}
