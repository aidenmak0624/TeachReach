package comp3350.teachreach.logic.DAOs;

import java.util.List;

import comp3350.teachreach.application.Server;
import comp3350.teachreach.data.interfaces.ITutorLocationPersistence;
import comp3350.teachreach.logic.exceptions.DataAccessException;

public class AccessTutorLocations {
    private static ITutorLocationPersistence TutorLocationPersistence;

    public AccessTutorLocations() {
        TutorLocationPersistence = Server.getTutorLocationAccess();
    }

    public AccessTutorLocations(ITutorLocationPersistence tutorLocationAccess) {
        AccessTutorLocations.TutorLocationPersistence = tutorLocationAccess;
    }

    public List<String> getTutorLocationByTutorID(int tutorID) {
        try {
            return TutorLocationPersistence.getTutorLocationByTutorID(tutorID);
        } catch (final Exception e) {
            throw new DataAccessException(
                    "Failed to get tutor location by tutor's " + "id", e);
        }
    }

    public boolean storeTutorLocation(int tutorID, String location) {
    try{
        return TutorLocationPersistence.storeTutorLocation(tutorID, location);
    }
    catch (final Exception e) {
        throw new DataAccessException("Failed to store tutor location!", e);
    }

    }
}
