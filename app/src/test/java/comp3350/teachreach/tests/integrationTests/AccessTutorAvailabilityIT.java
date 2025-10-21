package comp3350.teachreach.tests.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.LocalDate;

import java.io.File;
import java.io.IOException;
import java.util.List;

import comp3350.teachreach.data.hsqldb.TutorAvailabilityHSQLDB;
import comp3350.teachreach.data.interfaces.ITutorAvailabilityPersistence;
import comp3350.teachreach.logic.DAOs.AccessTutorAvailability;
import comp3350.teachreach.objects.TimeSlice;
import comp3350.teachreach.objects.Tutor;
import comp3350.teachreach.objects.interfaces.ITimeSlice;
import comp3350.teachreach.objects.interfaces.ITutor;
import comp3350.teachreach.tests.utils.TestUtils;

public class AccessTutorAvailabilityIT {
    private AccessTutorAvailability accessTutorAvailability;
    private LocalDate checkAgainstStartDate;
    private ITimeSlice checkAgainstTimeRange;
    private ITutor testTutor;
    private File tempDB;


    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        final ITutorAvailabilityPersistence persistence = new TutorAvailabilityHSQLDB(this.tempDB
                .getAbsolutePath()
                .replace(
                        ".script",
                        ""));
        this.accessTutorAvailability = new AccessTutorAvailability(persistence);
        this.testTutor = new Tutor(1, 3);
        this.checkAgainstStartDate = LocalDate.of(2024, 4, 20);
        LocalDate checkAgainstEndDate = LocalDate.of(2024, 4, 20);
        this.checkAgainstTimeRange = new TimeSlice(checkAgainstStartDate.atTime(8, 51), checkAgainstEndDate.atTime(16, 5));
    }

    @Test
    public void testGetAvailability() {
        assertEquals(1, accessTutorAvailability.getAvailability(testTutor).size());
        assertTrue(checkAgainstTimeRange.equals(accessTutorAvailability.getAvailability(testTutor).get(0)));
    }

    @Test
    public void testGetAvailabilityOnDay() {
        List<ITimeSlice> availabilities = accessTutorAvailability.getAvailabilityOnDay(testTutor, checkAgainstStartDate);
        assertEquals(1, availabilities.size());
        assertTrue(checkAgainstTimeRange.equals(availabilities.get(0)));
    }

    @Test
    public void testAddAvailability() {
        LocalDate date = LocalDate.of(2024, 5, 1);
        ITimeSlice newAvailability = new TimeSlice(date.atTime(9, 0), date.atTime(10, 0));
        accessTutorAvailability.addAvailability(testTutor, newAvailability);
        List<ITimeSlice> availabilities = accessTutorAvailability.getAvailability(testTutor);
        assertEquals(2, availabilities.size());
        assertEquals(newAvailability, availabilities.get(1));
    }

    @Test
    public void testRemoveAvailability() {
        LocalDate date = LocalDate.of(2024, 5, 1);
        ITimeSlice newAvailability = new TimeSlice(date.atTime(9, 0), date.atTime(10, 0));
        accessTutorAvailability.addAvailability(testTutor, newAvailability);
        List<ITimeSlice> availabilities = accessTutorAvailability.getAvailability(testTutor);
        assertEquals(2, availabilities.size());
        accessTutorAvailability.removeAvailability(testTutor, newAvailability);
        assertEquals(1, accessTutorAvailability.getAvailability(testTutor).size());
    }

    @After
    public void tearDown() {
        this.tempDB.delete();
    }
}
