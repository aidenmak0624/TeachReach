package comp3350.teachreach.logic.interfaces;

import comp3350.teachreach.objects.interfaces.ITutor;

public
interface ISessionCostCalculator
{
    double calculateSessionCost(ITutor t, int sessionDuration);
}
