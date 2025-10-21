package comp3350.teachreach.tests.unitTests.logic.DAOs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import comp3350.teachreach.data.interfaces.ITutorAvailabilityPersistence;
import comp3350.teachreach.logic.DAOs.AccessTutorAvailability;
import comp3350.teachreach.objects.TimeSlice;
import comp3350.teachreach.objects.Tutor;
import comp3350.teachreach.objects.interfaces.ITimeSlice;
import comp3350.teachreach.objects.interfaces.ITutor;

@RunWith(MockitoJUnitRunner.class)
public class AccessTutorAvailabilityTest {

    @Mock
    private ITutorAvailabilityPersistence tutorAvailabilityPersistence;

    @InjectMocks
    private
    AccessTutorAvailability accessTutorAvailability;

    private List<ITimeSlice> returns1 = new ArrayList<>();
    private ITutor tutor1 = new Tutor(1,1);
    private List<ITimeSlice> returns2 = new ArrayList<>();
    private ITutor tutor2 = new Tutor(2,2);
    private LocalDate date10 = LocalDate.of(2100, 12, 10);
    private LocalDate date11 = LocalDate.of(2100, 12, 11);
    private LocalDate date12 = LocalDate.of(2100, 12, 12);
    private LocalDate date13 = LocalDate.of(2100, 12, 13);

    @Before
    public void init() {

        returns1.add(new TimeSlice(LocalDateTime.of(2100, 12, 10, 10, 0), LocalDateTime.of(2100, 12, 10, 18, 0)));
        returns1.add(new TimeSlice(LocalDateTime.of(2100, 12, 11, 10, 0), LocalDateTime.of(2100, 12, 11, 18, 0)));
        returns1.add(new TimeSlice(LocalDateTime.of(2100, 12, 12, 10, 0), LocalDateTime.of(2100, 12, 12, 18, 0)));
        returns1.add(new TimeSlice(LocalDateTime.of(2100, 12, 12, 19, 0), LocalDateTime.of(2100, 12, 13, 21, 0)));
        returns2.add(new TimeSlice(LocalDateTime.of(2100, 12, 10, 9, 0), LocalDateTime.of(2100, 12, 10, 10, 0)));
        returns2.add(new TimeSlice(LocalDateTime.of(2100, 12, 10, 10, 30), LocalDateTime.of(2100, 12, 10, 12, 0)));
        returns2.add(new TimeSlice(LocalDateTime.of(2100, 12, 10, 13, 0), LocalDateTime.of(2100, 12, 10, 15, 0)));
        returns2.add(new TimeSlice(LocalDateTime.of(2100, 12, 10, 18, 15), LocalDateTime.of(2100, 12, 10, 20,  30)));
        returns2.add(new TimeSlice(LocalDateTime.of(2100, 12, 13, 19, 0), LocalDateTime.of(2100, 12, 13, 21, 0)));

        doReturn(returns1).when(tutorAvailabilityPersistence).getAvailability(tutor1);
        doReturn(returns2).when(tutorAvailabilityPersistence).getAvailability(tutor2);
        doReturn(returns1.subList(0,1)).when(tutorAvailabilityPersistence).getAvailabilityOnDay(tutor1, date10);
        doReturn(returns2.subList(1,2)).when(tutorAvailabilityPersistence).getAvailabilityOnDay(tutor1, date11);
        doReturn(returns1.subList(2,4)).when(tutorAvailabilityPersistence).getAvailabilityOnDay(tutor1, date12);
        doReturn(returns2.subList(0,0)).when(tutorAvailabilityPersistence).getAvailabilityOnDay(tutor1, date13);
        doReturn(returns1.subList(0,4)).when(tutorAvailabilityPersistence).getAvailabilityOnDay(tutor2, date10);
        doReturn(returns2.subList(0,0)).when(tutorAvailabilityPersistence).getAvailabilityOnDay(tutor2, date11);
        doReturn(returns2.subList(4,5)).when(tutorAvailabilityPersistence).getAvailabilityOnDay(tutor2, date12);
        doReturn(returns2.subList(4, returns2.size())).when(tutorAvailabilityPersistence).getAvailabilityOnDay(tutor2, date13);

        accessTutorAvailability = new AccessTutorAvailability(tutorAvailabilityPersistence);
        MockitoAnnotations.openMocks(this);

    }
    @Test
    public void getAvailabilityTest() {

        List<ITimeSlice> results = accessTutorAvailability.getAvailability(tutor1);

        assertEquals("Issue with getAvailability results", 4, results.size());

        results = accessTutorAvailability.getAvailability(tutor2);

        assertEquals("Issue with getAvailability results", 5, results.size());
    }

    @Test
    public void getAvailabilityOnDayTest() {

        List<ITimeSlice> results = accessTutorAvailability.getAvailabilityOnDay(tutor1,date10);

        assertEquals("Issues with getAvailabilityOnDay", 1, results.size());

        results = accessTutorAvailability.getAvailabilityOnDay(tutor2,date10);

        assertEquals("Issues with getAvailabilityOnDay", 4, results.size());

        results = accessTutorAvailability.getAvailabilityOnDay(tutor1,date11);

        assertEquals("Issues with getAvailabilityOnDay", 1, results.size());

        results = accessTutorAvailability.getAvailabilityOnDay(tutor2,date11);

        assertEquals("Issues with getAvailabilityOnDay", 0, results.size());

        results = accessTutorAvailability.getAvailabilityOnDay(tutor1,date12);

        assertEquals("Issues with getAvailabilityOnDay", 2, results.size());

        results = accessTutorAvailability.getAvailabilityOnDay(tutor2,date12);

        assertEquals("Issues with getAvailabilityOnDay", 1, results.size());

        results = accessTutorAvailability.getAvailabilityOnDay(tutor1,date13);

        assertEquals("Issues with getAvailabilityOnDay", 0, results.size());

        results = accessTutorAvailability.getAvailabilityOnDay(tutor2,date13);

        assertEquals("Issues with getAvailabilityOnDay", 1, results.size());
    }
}
