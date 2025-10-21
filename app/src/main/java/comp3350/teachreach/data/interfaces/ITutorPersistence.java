package comp3350.teachreach.data.interfaces;

import java.util.Map;

import comp3350.teachreach.objects.interfaces.ITutor;

public
interface ITutorPersistence
{
    ITutor storeTutor(ITutor newTutor);

    ITutor updateTutor(ITutor newTutor);

    Map<Integer, ITutor> getTutors();
}
