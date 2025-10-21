package comp3350.teachreach.logic.account;

import comp3350.teachreach.logic.DAOs.AccessAccounts;
import comp3350.teachreach.logic.DAOs.AccessStudents;
import comp3350.teachreach.logic.DAOs.AccessTutors;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.logic.exceptions.InvalidCredentialException;
import comp3350.teachreach.logic.interfaces.IAuthenticationHandler;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;

public class AuthenticationHandler implements IAuthenticationHandler {
    private final AccessAccounts accountAccess;
    private final AccessTutors tutorAccess;
    private final AccessStudents studentAccess;

    public AuthenticationHandler() {
        this.accountAccess = new AccessAccounts();
        this.tutorAccess = new AccessTutors();
        this.studentAccess = new AccessStudents();
    }

    public AuthenticationHandler(AccessAccounts accountAccess,
                                 AccessTutors tutorAccess,
                                 AccessStudents studentAccess) {
        this.accountAccess = accountAccess;
        this.tutorAccess = tutorAccess;
        this.studentAccess = studentAccess;
    }

    @Override
    public IAccount authenticateUser(String email, String plainPassword) throws InvalidCredentialException {
        for (IAccount account : accountAccess.getAccounts().values()) {
            if (account.getAccountEmail().equals(email)) {
                boolean passwordMatches = PasswordManager.verifyPassword(plainPassword, account.getAccountPassword());
                if (passwordMatches) {
                    return account;
                }
            }
        }
        throw new InvalidCredentialException("Invalid username or password");
    }

    @Override
    public ITutor authenticateTutor(String email, String plainPassword) throws InvalidCredentialException {
        try {
            IAccount account = authenticateUser(email, plainPassword);
            return tutorAccess.getTutorByAccountID(account.getAccountID());
        } catch (DataAccessException e) {
            throw new InvalidCredentialException("Invalid username or password");
        }
    }

    @Override
    public IStudent authenticateStudent(String email, String plainPassword) throws InvalidCredentialException {
        try {
            IAccount account = authenticateUser(email, plainPassword);
            return studentAccess.getStudentByAccountID(account.getAccountID());
        } catch (DataAccessException e) {
            throw new InvalidCredentialException("Invalid username or password");
        }
    }


}
