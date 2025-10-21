package comp3350.teachreach.logic.interfaces;

import comp3350.teachreach.data.exceptions.DuplicateEmailException;
import comp3350.teachreach.logic.exceptions.AccountCreatorException;
import comp3350.teachreach.logic.exceptions.InvalidNameException;
import comp3350.teachreach.logic.exceptions.input.InvalidEmailException;
import comp3350.teachreach.logic.exceptions.input.InvalidPasswordException;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;

public
interface IAccountCreator {
    IStudent createStudentAccount(String email,
                                  String password,
                                  String name,
                                  String pronouns,
                                  String major) throws AccountCreatorException, InvalidNameException, InvalidPasswordException, InvalidEmailException, DuplicateEmailException;

    ITutor createTutorAccount(String email,
                              String password,
                              String name,
                              String pronouns,
                              String major) throws AccountCreatorException, InvalidNameException, InvalidPasswordException, InvalidEmailException, DuplicateEmailException;
}
