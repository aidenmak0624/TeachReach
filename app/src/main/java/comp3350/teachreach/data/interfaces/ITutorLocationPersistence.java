package comp3350.teachreach.data.interfaces;

import java.util.List;

public interface ITutorLocationPersistence {
    List<String> getTutorLocationByTutorID(int tutorID);

    boolean storeTutorLocation(int tutorID, String location);
}
