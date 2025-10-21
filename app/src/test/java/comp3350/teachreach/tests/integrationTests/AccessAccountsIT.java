package comp3350.teachreach.tests.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import comp3350.teachreach.data.exceptions.DuplicateEmailException;
import comp3350.teachreach.data.hsqldb.AccountHSQLDB;
import comp3350.teachreach.data.interfaces.IAccountPersistence;
import comp3350.teachreach.logic.DAOs.AccessAccounts;
import comp3350.teachreach.objects.Account;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.tests.utils.TestUtils;

public
class AccessAccountsIT {
    private AccessAccounts accessAccounts;
    private File tempDB;

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        final IAccountPersistence persistence = new AccountHSQLDB(this.tempDB
                .getAbsolutePath()
                .replace(
                        ".script",
                        ""));
        this.accessAccounts = new AccessAccounts(persistence);
    }

    @Test
    public void testGetAccounts() {
        final Map<Integer, IAccount> accounts = accessAccounts.getAccounts();
        assertEquals(7, accounts.size());
    }

    @Test
    public void testGetAccountByEmail() {
        final Optional<IAccount> account = accessAccounts.getAccountByEmail(
                "student1@email.com");
        assertTrue(account.isPresent());
    }

    @Test
    public void testGetAccountByEmailBad() {
        final Optional<IAccount> account = accessAccounts.getAccountByEmail(
                "a@myumanitoba.ca");
        assertFalse(account.isPresent());
    }

    @Test
    public void testStoreAccount() throws DuplicateEmailException {
        final IAccount a = new Account("testAccount@email.com",
                "$2a$12$xeTxmBShbtIWsT/kdxVD8.k2LI",
                "Test Acc",
                "He/Him",
                "Computer Science");
        accessAccounts.insertAccount(a);
        assertEquals(8,
                accessAccounts
                        .getAccounts()
                        .size());
    }

    @Test
    public void testUpdateAccount() {
        final Optional<IAccount> a = accessAccounts.getAccountByEmail(
                "student1@email.com");
        final int oldSize = accessAccounts.getAccounts().size();
        assertTrue(a.isPresent());
        final IAccount u = new Account(a.get().getAccountEmail(),
                "$2a$12$i/QZJZjGQ7leHCtg5Ttx2O3yWfmtkplQYMLg.PXVGNnjF4ld46hJe", "a", "a", "a", a.get().getAccountID());
        accessAccounts.updateAccount(u);
        assertEquals(oldSize, accessAccounts.getAccounts().size());
        assertEquals(
                "$2a$12$i/QZJZjGQ7leHCtg5Ttx2O3yWfmtkplQYMLg.PXVGNnjF4ld46hJe",
                accessAccounts
                        .getAccountByEmail("student1@email.com")
                        .get()
                        .getAccountPassword());
    }


    @After
    public void tearDown() {
        this.tempDB.delete();
    }
}
