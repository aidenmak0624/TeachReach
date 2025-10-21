package comp3350.teachreach;




import androidx.test.filters.LargeTest;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;


import comp3350.teachreach.systemTest.AccountLoginAndAccountManagementTest;
import comp3350.teachreach.systemTest.EditProfileTest;
import comp3350.teachreach.systemTest.SearchAndSortTest;

@LargeTest
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountLoginAndAccountManagementTest.class,
        EditProfileTest.class,
        SearchAndSortTest.class

})

public class AllAcceptanceTest {
}
