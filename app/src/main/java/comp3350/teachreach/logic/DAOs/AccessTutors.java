package comp3350.teachreach.logic.DAOs;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

import comp3350.teachreach.application.Server;
import comp3350.teachreach.data.interfaces.ITutorPersistence;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.interfaces.ITutor;

public class AccessTutors
{
    private static ITutorPersistence    tutorPersistence;
    private static Map<Integer, ITutor> tutors = null;

    public AccessTutors()
    {
        tutorPersistence = Server.getTutorDataAccess();
        tutors           = tutorPersistence.getTutors();
    }

    public AccessTutors(final ITutorPersistence tutorPersistence)
    {
        AccessTutors.tutorPersistence = tutorPersistence;
        AccessTutors.tutors           = tutorPersistence.getTutors();
    }

    public Map<Integer, ITutor> getTutors()
    {
        if (tutors == null) {
            tutors = tutorPersistence.getTutors();
        }
        return Collections.unmodifiableMap(tutors);
    }

    public ITutor getTutorByAccountID(int accountID) throws DataAccessException
    {
        if (tutors == null) {
            tutors = tutorPersistence.getTutors();
        }
        return tutors
                .values()
                .stream()
                .filter(t -> t.getAccountID() == accountID)
                .findFirst()
                .orElseThrow(() -> new DataAccessException(
                        "Failed to get tutor by accountID",
                        new NoSuchElementException()));
    }

    public ITutor getTutorByTutorID(int tutorID)
    {
        if (tutors == null) {
            tutors = tutorPersistence.getTutors();
        }
        return tutors.get(tutorID);
    }

    public ITutor insertTutor(ITutor newTutor)
    {
        try {
            newTutor = tutorPersistence.storeTutor(newTutor);
            tutors   = tutorPersistence.getTutors();
            return newTutor;
        } catch (final Exception e) {
            throw new DataAccessException("Failed to insert new tutor", e);
        }
    }

    public ITutor updateTutor(ITutor existingTutor)
    {
        try {
            existingTutor = tutorPersistence.updateTutor(existingTutor);
            tutors        = tutorPersistence.getTutors();
            return existingTutor;
        } catch (final Exception e) {
            throw new DataAccessException("Failed to update existing tutor", e);
        }
    }
}
