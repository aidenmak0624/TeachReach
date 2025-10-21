package comp3350.teachreach.data.interfaces;

import org.threeten.bp.LocalDate;

import java.util.List;

import comp3350.teachreach.objects.interfaces.ITimeSlice;
import comp3350.teachreach.objects.interfaces.ITutor;

public interface ITutorAvailabilityPersistence {
    List<ITimeSlice> getAvailability(ITutor tutor);

    List<ITimeSlice> getAvailabilityOnDay(ITutor tutor, LocalDate date);

    void addAvailability(ITutor tutor, ITimeSlice timeRange);

    void removeAvailability(ITutor tutor, ITimeSlice timeRange);
}
