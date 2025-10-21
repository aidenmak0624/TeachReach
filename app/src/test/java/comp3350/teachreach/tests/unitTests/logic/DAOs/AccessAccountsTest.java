package comp3350.teachreach.tests.unitTests.logic.DAOs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import comp3350.teachreach.data.interfaces.IAccountPersistence;
import comp3350.teachreach.logic.DAOs.AccessAccounts;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.Account;
import comp3350.teachreach.objects.interfaces.IAccount;

@RunWith(MockitoJUnitRunner.class)
public class AccessAccountsTest {
    @Mock
    private IAccountPersistence accountPersistence;

    @InjectMocks
    private AccessAccounts accessAccounts;

    @Before
    public void init() {
        Map<Integer, IAccount> accounts = new HashMap<>();
        accounts.put(1, new Account("guderr@myumanitoba.ca",
                "$2a$12$xeTxmBShbtIWsT/kdxVD8.k2LI" +
                        ".RdOKAHYdRhgiw7Z5YxTd6.beOG",
                "Robert Guderian",
                "He/Him",
                "Computer Science", 1));
        accounts.put(2, new Account("mcmill5@myumanitoba.ca",
                "$2a$12$xeTxmBShbtIWsT/kdxVD8.k2LI" +
                        "lVkQHj5sVFgrTQF4QpJWVbo9CBie",
                "Camryn Mcmillan",
                "She/Her",
                "Computer Science", 2));
        accounts.put(3, new Account("huang1@myumanitoba.ca",
                "$2a$12$xeTxmBShbtIWsT/kdxVD8.k2LI" +
                        "$2a$12$r9yuopZw8rOLVK",
                "Justin Huang",
                "He/Him",
                "Computer Science", 3));
        doReturn(accounts).when(accountPersistence).getAccounts();

        accessAccounts = new AccessAccounts(accountPersistence);
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void getAccountsTest() {

        Map<Integer, IAccount> result = accessAccounts.getAccounts();

        try {
            assertEquals(3, result.size());
            assertEquals("Issue with getAccounts return contents",  "Robert Guderian", result.get(1).getUserName());
            assertEquals("Issue with getAccounts return contents", "Camryn Mcmillan", result.get(2).getUserName());
            assertEquals("Issue with getAccounts return contents", "Justin Huang", result.get(3).getUserName());
        } catch(NullPointerException n) {
            fail("Issues with results from getAccounts");
        }
        assertThrows("Result from getAccounts is not an unmodifiable map", UnsupportedOperationException.class,
                () -> result.put(4, new Account("sharma7@myumanitoba.ca",
                        "$2a$12$xeTxmBShbtIWsT/kdxVD8.k2LI" +
                                "$2a$12$r9yuopZw8rOLVK",
                        "Ashna Sharma",
                        "He/Him",
                        "Computer Science", 4)));

    }

    @Test
    public void getAccountByEmailTest() {
        Optional<IAccount> result = accessAccounts.getAccountByEmail("mcmill5@myumanitoba.ca");

        assertTrue("Did not pull account correctly from getAccountByEmail", result.isPresent());
        assertEquals("Incorrect result for getAccountByEmail", 2, result.get().getAccountID());


    }

    @Test
    public void getAccountByAccountIDTest() {
        Optional<IAccount> resultAccount =  accessAccounts.getAccountByAccountID(3);
        IAccount result = resultAccount.orElseThrow(() -> new DataAccessException(("Issues with results from getAccountByAccountID")));

        assertEquals("Incorrect result from getAccountByAccountID", "Justin Huang", result.getUserName());
    }
}
