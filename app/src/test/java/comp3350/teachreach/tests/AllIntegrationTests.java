package comp3350.teachreach.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.teachreach.tests.integrationTests.AccessAccountsIT;
import comp3350.teachreach.tests.integrationTests.AccessCoursesIT;
import comp3350.teachreach.tests.integrationTests.AccessMessagesIT;
import comp3350.teachreach.tests.integrationTests.AccessSessionsIT;
import comp3350.teachreach.tests.integrationTests.AccessStudentsIT;
import comp3350.teachreach.tests.integrationTests.AccessTutorAvailabilityIT;
import comp3350.teachreach.tests.integrationTests.AccessTutorLocationsIT;
import comp3350.teachreach.tests.integrationTests.AccessTutoredCoursesIT;
import comp3350.teachreach.tests.integrationTests.AccessTutorsIT;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccessAccountsIT.class,
        AccessCoursesIT.class,
        AccessMessagesIT.class,
        AccessSessionsIT.class,
        AccessStudentsIT.class,
        AccessTutorAvailabilityIT.class,
        AccessTutoredCoursesIT.class,
        AccessTutorLocationsIT.class,
        AccessTutorsIT.class,})


public
class AllIntegrationTests
{

}
