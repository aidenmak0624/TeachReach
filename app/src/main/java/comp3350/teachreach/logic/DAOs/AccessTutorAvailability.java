package comp3350.teachreach.logic.DAOs;

import org.threeten.bp.LocalDate;

import java.util.List;

import comp3350.teachreach.application.Server;
import comp3350.teachreach.data.interfaces.ITutorAvailabilityPersistence;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.interfaces.ITimeSlice;
import comp3350.teachreach.objects.interfaces.ITutor;

public class AccessTutorAvailability {
    private static ITutorAvailabilityPersistence TutorAvailabilityPersistence;

    public AccessTutorAvailability() {
        TutorAvailabilityPersistence = Server.getTutorAvailabilityAccess();
    }

    public AccessTutorAvailability(ITutorAvailabilityPersistence tutorAvailabilityAccess) {
        AccessTutorAvailability.TutorAvailabilityPersistence = tutorAvailabilityAccess;
    }

    public List<ITimeSlice> getAvailability(ITutor tutor) {
        try {
            return TutorAvailabilityPersistence.getAvailability(tutor);
        } catch (final Exception e) {
            throw new DataAccessException(
                    "Failed to get tutor's availability ", e);
        }
    }

    public List<ITimeSlice> getAvailabilityOnDay(ITutor tutor, LocalDate date) {
        try {
            return TutorAvailabilityPersistence.getAvailabilityOnDay(tutor, date);
        } catch (final Exception e) {
            throw new DataAccessException(
                    "Failed to get tutor's day availability ", e);
        }
    }

    public void addAvailability(ITutor tutor, ITimeSlice timeSlice) {
        try {

            TutorAvailabilityPersistence.addAvailability(tutor, timeSlice);

        } catch (final Exception e) {
            throw new DataAccessException("Failed to store availability ", e);
        }
    }

    public void removeAvailability(ITutor tutor, ITimeSlice timeSlice) {
        try {
            TutorAvailabilityPersistence.removeAvailability(tutor, timeSlice);
        } catch (final Exception e) {
            throw new DataAccessException("Failed to remove tutor availability ", e);
        }
    }
}
