package comp3350.teachreach.tests.unitTests.logic.DAOs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import comp3350.teachreach.data.interfaces.IStudentPersistence;
import comp3350.teachreach.logic.DAOs.AccessStudents;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.Student;
import comp3350.teachreach.objects.interfaces.IStudent;

@RunWith(MockitoJUnitRunner.class)
public class AccessStudentsTest {


    @InjectMocks
    private AccessStudents accessStudents;
    @Mock
    private IStudentPersistence studentPersistence;

    private Map<Integer, IStudent> returns = new HashMap<>();

    @Before
    public void init() {
        returns.put(1, new Student(1, 6));
        returns.put(2, new Student(2, 7));
        returns.put(3, new Student(3, 8));
        returns.put(4, new Student(4, 9));
        returns.put(5, new Student(5, 10));

        doReturn(returns).when(studentPersistence).getStudents();


        accessStudents = new AccessStudents(studentPersistence);
        MockitoAnnotations.openMocks(this);

    }
    @Test
    public void getStudentsTest() {
        Map<Integer, IStudent> results = accessStudents.getStudents();

        try {
            assertEquals("Incorrect result from getStudents", 5, results.size());
            assertEquals("Incorrect result from getStudents", 6, results.get(1).getAccountID());
            assertEquals("Incorrect result from getStudents", 7, results.get(2).getAccountID());
            assertEquals("Incorrect result from getStudents", 8, results.get(3).getAccountID());
            assertEquals("Incorrect result from getStudents", 9, results.get(4).getAccountID());
            assertEquals("Incorrect result from getStudents", 10, results.get(5).getAccountID());
        } catch(NullPointerException n) {
            fail("Error in results from get getStudents");
        }
        assertThrows("Result from getStudents is not an unmodifiable map", UnsupportedOperationException.class,
                () -> results.put(6, new Student(6, 11)));
    }

    @Test
    public void getStudentByAccountID() {
        IStudent resultStudent = accessStudents.getStudentByAccountID(8);

        assertEquals("Incorrect result from getStudentByAccountID", 3, resultStudent.getStudentID());

        assertThrows("DataAccessException expected, but not thrown", DataAccessException.class, () -> accessStudents.getStudentByAccountID(11));
    }

    @Test
    public void getStudentByStudentID() {
        IStudent resultStudent = accessStudents.getStudentByStudentID(3);

        assertEquals("Incorrect result from getStudentByAccountID", 8, resultStudent.getAccountID());

        assertThrows("DataAccessException expected, but not thrown", DataAccessException.class, () -> accessStudents.getStudentByStudentID(11));
    }
}
