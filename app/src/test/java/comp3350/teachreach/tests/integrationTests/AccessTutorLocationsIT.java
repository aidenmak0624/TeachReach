package comp3350.teachreach.tests.integrationTests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import comp3350.teachreach.data.hsqldb.TutorLocationHSQLDB;
import comp3350.teachreach.data.interfaces.ITutorLocationPersistence;
import comp3350.teachreach.logic.DAOs.AccessTutorLocations;
import comp3350.teachreach.tests.utils.TestUtils;

public class AccessTutorLocationsIT {
    private AccessTutorLocations accessTutorLocations;
    private File tempDB;

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        final ITutorLocationPersistence persistence = new TutorLocationHSQLDB(this.tempDB
                .getAbsolutePath()
                .replace(
                        ".script",
                        ""));
        this.accessTutorLocations = new AccessTutorLocations(persistence);
    }

    @Test
    public void testGetTutorLocationByTutorID() {
        List<String> locations = accessTutorLocations.getTutorLocationByTutorID(1);
        assertEquals(1, locations.size());
    }

    @Test
    public void testStoreTutorLocation() {
        accessTutorLocations.storeTutorLocation(1, "Test Location");
        List<String> locations = accessTutorLocations.getTutorLocationByTutorID(1);
        assertEquals(2, locations.size());
        assertEquals("Test Location", locations.get(1));
    }

    @After
    public void tearDown() {
        this.tempDB.delete();
    }
}
