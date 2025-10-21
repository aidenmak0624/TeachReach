package comp3350.teachreach.logic.account;

import comp3350.teachreach.data.exceptions.DuplicateEmailException;
import comp3350.teachreach.logic.DAOs.AccessAccounts;
import comp3350.teachreach.logic.DAOs.AccessStudents;
import comp3350.teachreach.logic.DAOs.AccessTutors;
import comp3350.teachreach.logic.exceptions.AccountCreatorException;
import comp3350.teachreach.logic.exceptions.InvalidNameException;
import comp3350.teachreach.logic.exceptions.input.InvalidEmailException;
import comp3350.teachreach.logic.exceptions.input.InvalidPasswordException;
import comp3350.teachreach.logic.interfaces.IAccountCreator;
import comp3350.teachreach.objects.Account;
import comp3350.teachreach.objects.Student;
import comp3350.teachreach.objects.Tutor;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;

public class AccountCreator implements IAccountCreator
{
    private static final Object         lock           = new Object();
    private static       AccessAccounts accessAccounts = null;
    private static AccessStudents accessStudents = null;
    private static AccessTutors accessTutors = null;

    public AccountCreator() {
        accessAccounts = new AccessAccounts();
        accessStudents = new AccessStudents();
        accessTutors = new AccessTutors();
    }

    public AccountCreator(AccessAccounts accessAccounts,
                          AccessStudents accessStudents,
                          AccessTutors accessTutors) {
        AccountCreator.accessAccounts = accessAccounts;
        AccountCreator.accessStudents = accessStudents;
        AccountCreator.accessTutors = accessTutors;
    }

    private static IAccount createAccount(String email,
                                          String password,
                                          String name,
                                          String pronouns,
                                          String major) throws
                                                        AccountCreatorException,
                                                        InvalidEmailException,
                                                        InvalidPasswordException,
                                                        InvalidNameException,
                                                        DuplicateEmailException
    {
        try {
            InputValidator.validateEmail(email);
            InputValidator.validatePassword(password);
            InputValidator.validateName(name);

            IAccount newAccount = new Account(email,
                                              PasswordManager.encryptPassword(
                                                      password),
                                              name,
                                              pronouns,
                                              major);
            newAccount = accessAccounts.insertAccount(newAccount);
            return newAccount;
        } catch (DuplicateEmailException e) {
            throw new DuplicateEmailException("Email already in use");
        } catch (InvalidEmailException |
                 InvalidPasswordException |
                 InvalidNameException e) {
            throw e;
        } catch (final Exception e) {
            throw new AccountCreatorException("Failed to created new account",
                                              e);
        }
    }

    @Override
    public IStudent createStudentAccount(String email,
                                         String password,
                                         String name,
                                         String pronouns,
                                         String major) throws
                                                       AccountCreatorException,
                                                       InvalidNameException,
                                                       InvalidPasswordException,
                                                       InvalidEmailException,
                                                       DuplicateEmailException
    {
        try {
            IStudent newStudent;
            synchronized (lock) {
                IAccount newAccount = createAccount(email,
                                                    password,
                                                    name,
                                                    pronouns,
                                                    major);
                newStudent = new Student(newAccount.getAccountID());
                newStudent = accessStudents.insertStudent(newStudent);
            }
            return newStudent;
        } catch (InvalidEmailException |
                 InvalidPasswordException |
                 InvalidNameException |
                 DuplicateEmailException e) {
            throw e;
        } catch (final Exception e) {
            throw new AccountCreatorException("Failed to make new student :(", e);
        }
    }

    @Override
    public ITutor createTutorAccount(String email,
                                     String password,
                                     String name,
                                     String pronouns,
                                     String major) throws
                                                   AccountCreatorException,
                                                   InvalidNameException,
                                                   InvalidPasswordException,
                                                   InvalidEmailException,
                                                   DuplicateEmailException
    {
        try {
            ITutor newTutor;
            synchronized (lock) {
                IAccount newAccount = createAccount(email,
                                                    password,
                                                    name,
                                                    pronouns,
                                                    major);
                newTutor = new Tutor(newAccount.getAccountID());
                newTutor = accessTutors.insertTutor(newTutor);
            }
            return newTutor;
        } catch (InvalidEmailException |
                 InvalidPasswordException |
                 InvalidNameException |
                 DuplicateEmailException e) {
            throw e;
        } catch (final Exception e) {
            throw new AccountCreatorException("Failed to make new tutor :(", e);
        }
    }
}

