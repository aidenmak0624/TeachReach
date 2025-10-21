package comp3350.teachreach.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.teachreach.tests.unitTests.logic.DAOs.AccessAccountsTest;
import comp3350.teachreach.tests.unitTests.logic.DAOs.AccessCoursesTest;
import comp3350.teachreach.tests.unitTests.logic.DAOs.AccessSessionsTest;
import comp3350.teachreach.tests.unitTests.logic.DAOs.AccessStudentsTest;
import comp3350.teachreach.tests.unitTests.logic.DAOs.AccessTutorAvailabilityTest;
import comp3350.teachreach.tests.unitTests.logic.DAOs.AccessTutorCoursesTest;
import comp3350.teachreach.tests.unitTests.logic.DAOs.AccessTutorLocationsTest;
import comp3350.teachreach.tests.unitTests.logic.DAOs.AccessTutorsTest;
import comp3350.teachreach.tests.unitTests.logic.account.AccountCreatorTest;
import comp3350.teachreach.tests.unitTests.logic.account.AccountManagerTest;
import comp3350.teachreach.tests.unitTests.logic.account.AuthenticationHandlerTest;
import comp3350.teachreach.tests.unitTests.logic.account.InputValidatorTest;
import comp3350.teachreach.tests.unitTests.logic.account.PasswordManagerTest;
import comp3350.teachreach.tests.unitTests.logic.availability.TimeSlotGeneratorTest;
import comp3350.teachreach.tests.unitTests.logic.availability.TutorAvailabilityManagerTest;
import comp3350.teachreach.tests.unitTests.logic.payment.PaymentValidatorTest;
import comp3350.teachreach.tests.unitTests.logic.profile.TutorProfileHandlerTest;
import comp3350.teachreach.tests.unitTests.logic.session.SessionCostCalculatorTest;
import comp3350.teachreach.tests.unitTests.logic.session.SessionHandlerTest;
import comp3350.teachreach.tests.unitTests.object.SessionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountCreatorTest.class,
        AccountManagerTest.class,
        AuthenticationHandlerTest.class,
        InputValidatorTest.class,
        PasswordManagerTest.class,
        TimeSlotGeneratorTest.class,
        TutorAvailabilityManagerTest.class,
        AccessAccountsTest.class,
        AccessCoursesTest.class,
        AccessSessionsTest.class,
        AccessStudentsTest.class,
        AccessTutorAvailabilityTest.class,
        AccessTutorCoursesTest.class,
        AccessTutorLocationsTest.class,
        AccessTutorsTest.class,
        PaymentValidatorTest.class,
        TutorProfileHandlerTest.class,
        SessionCostCalculatorTest.class,
        SessionHandlerTest.class,
        SessionTest.class})

public
class AllUnitTests
{

}
