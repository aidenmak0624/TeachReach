package comp3350.teachreach.tests.unitTests.logic.DAOs;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import comp3350.teachreach.data.hsqldb.PersistenceException;
import comp3350.teachreach.data.interfaces.ITutorLocationPersistence;
import comp3350.teachreach.logic.DAOs.AccessTutorLocations;
import comp3350.teachreach.logic.exceptions.DataAccessException;

@RunWith(MockitoJUnitRunner.class)
public class AccessTutorLocationsTest {

    @Mock
    private ITutorLocationPersistence tutorLocationPersistence;

    @InjectMocks
    private AccessTutorLocations accessTutorLocations;

    @Before
    public void init() {
        List<String> returns = new ArrayList<>();

        returns.add("Library");
        returns.add("Coffee Shop");
        returns.add("House");

        when(tutorLocationPersistence.getTutorLocationByTutorID(1)).thenReturn(returns);
        when(tutorLocationPersistence.getTutorLocationByTutorID(2)).thenThrow(PersistenceException.class);

        accessTutorLocations = new AccessTutorLocations(tutorLocationPersistence);
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void getTutorLocationByTutorIDTest() {

        List<String> results = accessTutorLocations.getTutorLocationByTutorID(1);

        assertEquals("Error in results from getTutorLocationByTutorID", 3, results.size());
        assertEquals("Error in results from getTutorLocationByTutorID", "Coffee Shop", results.get(1));

        assertThrows("Expected DataAccessException not thrown", DataAccessException.class, () -> accessTutorLocations.getTutorLocationByTutorID(2));

     }
}
