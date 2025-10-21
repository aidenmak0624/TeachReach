package comp3350.teachreach.tests.unitTests.logic.DAOs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

import comp3350.teachreach.data.interfaces.ITutorPersistence;
import comp3350.teachreach.logic.DAOs.AccessTutors;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.Tutor;
import comp3350.teachreach.objects.interfaces.ITutor;

@RunWith(MockitoJUnitRunner.class)
public class AccessTutorsTest {


    @InjectMocks
    private AccessTutors accessTutors;
    @Mock
    private static ITutorPersistence tutorPersistence;

    @Before
    public void init() {
        Map<Integer, ITutor> returns;
        returns = new HashMap<>();
        returns.put(1, new Tutor(1, 6));
        returns.put(2, new Tutor(2, 7));
        returns.put(3, new Tutor(3, 8));
        returns.put(4, new Tutor(4, 9));
        returns.put(5, new Tutor(5, 10));
        doReturn(returns).when(tutorPersistence).getTutors();
        //when(tutorPersistence.getTutors()).thenReturn(returns);

        accessTutors = new AccessTutors(tutorPersistence);
        MockitoAnnotations.openMocks(this);

    }
    @Test
    public void getTutorsTest() {
        Map<Integer, ITutor> results = accessTutors.getTutors();

        try {
            assertEquals("Incorrect result from getTutors", 5, results.size());
            assertEquals("Incorrect result from getTutors", 6, results.get(1).getAccountID());
            assertEquals("Incorrect result from getTutors", 7, results.get(2).getAccountID());
            assertEquals("Incorrect result from getTutors", 8, results.get(3).getAccountID());
            assertEquals("Incorrect result from getTutors", 9, results.get(4).getAccountID());
            assertEquals("Incorrect result from getTutors", 10, results.get(5).getAccountID());
        } catch(NullPointerException n) {
            fail("Issue with getTutors results");
        }
        assertThrows("Result from getTutors is not an unmodifiable map", UnsupportedOperationException.class,
                () -> results.put(6, new Tutor(6, 11)));

    }

    @Test
    public void getTutorByAccountIDTest() {
        ITutor result = accessTutors.getTutorByAccountID(8);

        assertEquals("Incorrect result from getTutorByAccountID", 3, result.getTutorID());

        assertThrows("DataAccessException expected, but not thrown", DataAccessException.class, () -> accessTutors.getTutorByAccountID(11));
    }

    @Test
    public void getTutorByTutorIDTest() {
        ITutor result = accessTutors.getTutorByTutorID(3);


        assertEquals("Incorrect result from getTutorByAccountID", 8, result.getAccountID());

        assertNull("DataAccessException expected, but not thrown", accessTutors.getTutorByTutorID(6));
    }

}

