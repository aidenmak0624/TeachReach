package comp3350.teachreach.logic.interfaces;

import comp3350.teachreach.logic.exceptions.InvalidCredentialException;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;

public
interface IAuthenticationHandler {
    IAccount authenticateUser(String email, String plainPassword) throws InvalidCredentialException;

    ITutor authenticateTutor(String email, String plainPassword) throws InvalidCredentialException;

    IStudent authenticateStudent(String email, String plainPassword) throws InvalidCredentialException;
}
