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
import comp3350.teachreach.data.hsqldb.TutorHSQLDB;
import comp3350.teachreach.data.interfaces.IAccountPersistence;
import comp3350.teachreach.data.interfaces.ITutorPersistence;
import comp3350.teachreach.logic.DAOs.AccessAccounts;
import comp3350.teachreach.logic.DAOs.AccessTutors;
import comp3350.teachreach.objects.Account;
import comp3350.teachreach.objects.Tutor;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.ITutor;
import comp3350.teachreach.tests.utils.TestUtils;

public
class AccessTutorsIT {
    private AccessTutors accessTutors;
    private AccessAccounts accessAccounts;
    private IAccount testAccount;
    private File tempDB;

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        final IAccountPersistence accountPersistence
                = new AccountHSQLDB(this.tempDB
                .getAbsolutePath()
                .replace(".script", ""));
        final ITutorPersistence tutorPersistence = new TutorHSQLDB(this.tempDB
                .getAbsolutePath()
                .replace(
                        ".script",
                        ""));
        this.accessAccounts = new AccessAccounts(accountPersistence);
        this.testAccount = new Account("testEmail", "password", "testTutor", "pronouns", "major");
        this.accessTutors = new AccessTutors(tutorPersistence);
    }

    @Test
    public void testGetTutors() {
        Map<Integer, ITutor> tutors = accessTutors.getTutors();
        assertEquals(5, tutors.size());
    }

    @Test
    public void testGetTutorByAccountID() {
        ITutor tutor = accessTutors.getTutorByAccountID(5);
        assertEquals(5, tutor.getAccountID());
    }


    @Test
    public void testGetTutorByTutorID() {
        ITutor tutor = accessTutors.getTutorByTutorID(1);
        assertEquals(1, tutor.getTutorID());
    }

    @Test
    public void testInsertTutor() throws DuplicateEmailException {
        IAccount retrievedAccount = accessAccounts.insertAccount(testAccount);
        ITutor tutor = new Tutor(retrievedAccount.getAccountID());
        accessTutors.insertTutor(tutor);
        Map<Integer, ITutor> testTutors = accessTutors.getTutors();
        assertEquals(6, testTutors.size());
    }

    @Test
    public void testUpdateTutor() {
        ITutor tutor = accessTutors.getTutorByTutorID(1);
        tutor.setHourlyRate(30);
        ITutor testTutor = accessTutors.updateTutor(tutor);
        assertEquals(30, testTutor.getHourlyRate(), 0);
    }


    @After
    public void tearDown() {
        this.tempDB.delete();
    }
}
