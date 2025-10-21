package comp3350.teachreach.systemTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.teachreach.R;
import comp3350.teachreach.presentation.MainActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AccountLoginAndAccountManagementTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    //system test for login
    @Test
    public void testSuccessfulLogin() {
        onView(ViewMatchers.withId(R.id.btnStudentLogin)).perform(click());
        onView(withId(R.id.etStudentLoginEmail)).perform(typeText("student1@email.com"), closeSoftKeyboard());
        onView(withId(R.id.etStudentLoginPassword)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.btnLoginAsStudent)).perform(click());
        onView(withId(R.id.navigationMenu)).check(matches(isDisplayed()));
    }

    // System test for account creation
    @Test
    public void testSuccessfulAccountCreation() {

        onView(withId(R.id.btnStudentLogin)).perform(click());
        onView(withId(R.id.btnSignupAsStudent)).perform(click());

        // actions for account creation details
        onView(withId(R.id.etUsername)).perform(typeText("TestUser"), closeSoftKeyboard());
        onView(withId(R.id.etMajor)).perform(typeText("Computer Science"), closeSoftKeyboard());
        onView(withId(R.id.etPronouns)).perform(typeText("They/Them"), closeSoftKeyboard());
        onView(withId(R.id.etEmail)).perform(typeText("testuser@email.com"), closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("TestPassword123"), closeSoftKeyboard());

        // Click the create profile button
        onView(withId(R.id.btnCreateProfile)).perform(click());

        // Verify successful navigation post-account creation
        onView(withId(R.id.navigationMenu)).check(matches(isDisplayed()));
    }
}
