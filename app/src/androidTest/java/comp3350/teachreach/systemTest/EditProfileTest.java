package comp3350.teachreach.systemTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
public class EditProfileTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);



    @Test
    public void testSuccessfulProfileEdit() {
        // Navigate to the edit profile page
        onView(ViewMatchers.withId(R.id.btnStudentLogin)).perform(click());
        onView(withId(R.id.etStudentLoginEmail)).perform(typeText("student1@email.com"), closeSoftKeyboard());
        onView(withId(R.id.etStudentLoginPassword)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.btnLoginAsStudent)).perform(click());
        onView(withId(R.id.NavBarProfile)).perform(click());

        onView(withId(R.id.fabEditProfile)).perform(click());

        // Check if the edit text fields are displayed
        onView(withId(R.id.etUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.etMajor)).check(matches(isDisplayed()));
        onView(withId(R.id.etPronouns)).check(matches(isDisplayed()));

        // Perform text replacement to simulate editing
        onView(withId(R.id.etUsername)).perform(replaceText("New Name"), closeSoftKeyboard());
        onView(withId(R.id.etMajor)).perform(replaceText("New Major"), closeSoftKeyboard());
        onView(withId(R.id.etPronouns)).perform(replaceText("New Pronouns"), closeSoftKeyboard());

        onView(withId(R.id.fabApply)).perform(click());


        // Verify changes were applied by checking the displayed text
        onView(withId(R.id.tvNameField)).check(matches(withText("New Name")));
        onView(withId(R.id.tvMajorField)).check(matches(withText("New Major")));
        onView(withId(R.id.tvPronounsField)).check(matches(withText("New Pronouns")));
    }

}
