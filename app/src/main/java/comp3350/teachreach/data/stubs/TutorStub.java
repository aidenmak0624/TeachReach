package comp3350.teachreach.data.stubs;

import java.util.HashMap;
import java.util.Map;

import comp3350.teachreach.data.interfaces.ITutorPersistence;
import comp3350.teachreach.objects.interfaces.ITutor;

public
class TutorStub implements ITutorPersistence
{
    private static Map<Integer, ITutor> tutors            = null;
    private static int                  currentTutorCount = 1;

    public
    TutorStub()
    {
        if (tutors == null) {
            TutorStub.tutors = new HashMap<>();
        }
    }

    @Override
    public
    ITutor storeTutor(ITutor newTutor)
    {
        ITutor ujTanar = TutorStub.tutors.put(currentTutorCount,
                                              newTutor.setTutorID(
                                                      currentTutorCount));
        currentTutorCount++;
        return ujTanar;
    }

    @Override
    public
    ITutor updateTutor(ITutor newTutor)
    {
        return TutorStub.tutors.put(newTutor.getTutorID(), newTutor);
    }

    @Override
    public
    Map<Integer, ITutor> getTutors()
    {
        return TutorStub.tutors;
    }
}
