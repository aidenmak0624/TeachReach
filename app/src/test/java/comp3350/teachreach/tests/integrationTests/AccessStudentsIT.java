package comp3350.teachreach.tests.integrationTests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import comp3350.teachreach.data.exceptions.DuplicateEmailException;
import comp3350.teachreach.data.hsqldb.AccountHSQLDB;
import comp3350.teachreach.data.hsqldb.StudentHSQLDB;
import comp3350.teachreach.data.interfaces.IAccountPersistence;
import comp3350.teachreach.data.interfaces.IStudentPersistence;
import comp3350.teachreach.logic.DAOs.AccessAccounts;
import comp3350.teachreach.logic.DAOs.AccessStudents;
import comp3350.teachreach.logic.exceptions.AccountCreatorException;
import comp3350.teachreach.logic.exceptions.InvalidNameException;
import comp3350.teachreach.logic.exceptions.input.InvalidEmailException;
import comp3350.teachreach.logic.exceptions.input.InvalidPasswordException;
import comp3350.teachreach.objects.Account;
import comp3350.teachreach.objects.Student;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.tests.utils.TestUtils;

public class AccessStudentsIT {
    private IStudent testStudent;
    private IAccount testAccount;
    private AccessStudents accessStudents;
    private AccessAccounts accessAccounts;
    private File tempDB;

    @Before
    public void setUp() throws
            IOException,
            InvalidNameException,
            DuplicateEmailException,
            InvalidPasswordException,
            InvalidEmailException,
            AccountCreatorException {
        this.tempDB = TestUtils.copyDB();
        final IAccountPersistence accountPersistence
                = new AccountHSQLDB(this.tempDB
                .getAbsolutePath()
                .replace(".script", ""));
        final IStudentPersistence studentPersistence
                = new StudentHSQLDB(this.tempDB
                .getAbsolutePath()
                .replace(".script", ""));
        this.accessAccounts = new AccessAccounts(accountPersistence);
        this.accessStudents = new AccessStudents(studentPersistence);
        this.testAccount = new Account("testEmail", "password", "testStudent", "pronouns", "major");
        this.testStudent = new Student(1, 1);
    }

    @Test
    public void testGetStudentByStudentID() {
        IStudent testS
                =
                accessStudents.getStudentByStudentID(testStudent.getStudentID());
        assertEquals(testStudent.getStudentID(), testS.getStudentID());
    }

    @Test
    public void testGetStudents() {
        Map<Integer, IStudent> testStudents;
        testStudents = accessStudents.getStudents();
        assertEquals(2, testStudents.size());
    }

    @Test
    public void testInsertStudent() throws DuplicateEmailException {
        IAccount retrievedAccount = accessAccounts.insertAccount(testAccount);
        IStudent testStudent2 = new Student(retrievedAccount.getAccountID());
        accessStudents.insertStudent(testStudent2);
        Map<Integer, IStudent> testStudents = accessStudents.getStudents();
        assertEquals(3, testStudents.size());
    }

    @After
    public void tearDown() {
        this.tempDB.delete();
    }
}
