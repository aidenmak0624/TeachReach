package comp3350.teachreach.tests.unitTests.logic.DAOs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp3350.teachreach.data.interfaces.ICoursePersistence;
import comp3350.teachreach.logic.DAOs.AccessCourses;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.Course;
import comp3350.teachreach.objects.interfaces.ICourse;

@RunWith(MockitoJUnitRunner.class)
public class AccessCoursesTest
{

    @Mock
    private ICoursePersistence coursePersistence;

    @InjectMocks
    private AccessCourses accessCourses;


    @Before
    public void init() {
        Map<String, ICourse> returns = new HashMap<>();
        returns.put("COMP 2080",
                new Course("COMP 2080", "Analysis of Algorithms"));
        returns.put("COMP 1010",
                new Course("COMP 1010",
                        "Introduction to Computer Science"));
        returns.put("COMP 1012",
                new Course("COMP 1012",
                        "Introduction to Computer Science " +
                                "for Engineers"));
        returns.put("COMP 2150", new Course("COMP 2150", "Object Orientation"));
        returns.put("COMP 3380",
                new Course("COMP 3380", "Databases Concepts and Usage"));
        doReturn(returns).when(coursePersistence).getCourses();

        List<ICourse> results = new ArrayList<>();
        results.add(new Course("COMP 2150", "Object Orientation"));
        when(coursePersistence.getCoursesByName("Object Orientation")).thenReturn(results);

        accessCourses = new AccessCourses(coursePersistence);
        MockitoAnnotations.openMocks(this);


    }
    @Test
    public void getCoursesTest()
    {


        Map<String, ICourse> result = accessCourses.getCourses();

        try {
            assertEquals(5, result.size());
            assertEquals("Issue with getCourses return contents",
                    "Analysis of Algorithms",
                    result.get("COMP 2080").getCourseName());
            assertEquals("Issue with getCourses return contents",
                    "Introduction to Computer Science",
                    result.get("COMP 1010").getCourseName());
            assertEquals("Issue with getCourses return contents",
                    "Introduction to Computer Science for Engineers",
                    result.get("COMP 1012").getCourseName());
            assertEquals("Issue with getCourses return contents",
                    "Object Orientation",
                    result.get("COMP 2150").getCourseName());
            assertEquals("Issue with getCourses return contents",
                    "Databases Concepts and Usage",
                    result.get("COMP 3380").getCourseName());
        } catch(NullPointerException n) {
            fail("Issues with results from getCourses");
        }
        assertThrows("Result from getAccounts is not an unmodifiable map",
                     UnsupportedOperationException.class,
                     () -> result.put("COMP 4620",
                                      new Course("COMP 4620",
                                                 "Professional Practice in " +
                                                 "Computer Science")));
    }

    @Test
    public void getCoursesByCodeTest()
    {

        List<ICourse> result = accessCourses.getCoursesByCode("COMP 1012");

        assertEquals("Issue with getCourseByCode result",
                "Introduction to Computer Science for Engineers",
                     result.get(0).getCourseName());
        assertThrows("DataAccessException expected from getCourseByCode",
                     DataAccessException.class,
                     () -> accessCourses.getCourseByCode("HelloWorld"));
    }

    @Test
    public void getCoursesByNameTest()
    {

        List<ICourse> result = accessCourses.getCoursesByName(
                "Object Orientation");

        assertEquals("Issue with getCoursesByName result",
                "COMP 2150",
                     result.get(0).getCourseCode());

        assertEquals("Unexpected result from getCoursesByName", 0,
                     accessCourses.getCoursesByName("HelloWorld").size());
    }
}
