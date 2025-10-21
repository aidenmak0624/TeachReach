package comp3350.teachreach.presentation.profile.tutor;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

import comp3350.teachreach.R;
import comp3350.teachreach.application.Server;
import comp3350.teachreach.databinding.FragmentTutorProfileSelfViewBinding;
import comp3350.teachreach.logic.exceptions.availability.TutorAvailabilityManagerException;
import comp3350.teachreach.logic.interfaces.ISessionHandler;
import comp3350.teachreach.logic.interfaces.ITutorProfileHandler;
import comp3350.teachreach.objects.TimeSlice;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.ISession;
import comp3350.teachreach.objects.interfaces.ITimeSlice;
import comp3350.teachreach.objects.interfaces.ITutor;
import comp3350.teachreach.presentation.session.SessionViewModel;
import comp3350.teachreach.presentation.utils.TimeSliceFormatter;

public
class TutorProfileSelfViewFragment extends Fragment
{
    private static ITutorProfileHandler profileHandler;
    private static ISessionHandler      sessionHandler;
    private static List<String>         prefLocations;
    private static List<String>         tutoredCourses;
    private static List<String>         availStrList;

    private TutorProfileViewModel               profileViewModel;
    private SessionViewModel                    sessionViewModel;
    private FragmentTutorProfileSelfViewBinding binding;
    private IAccount                            account;
    private ITutor                              tutor;

    private LocalDate     selectedDate;
    private LocalDateTime selectedStartDateTime, selectedEndDateTime;
    private LocalTime selectedStartTime, selectedEndTime;

    private boolean isLarge, isLandscape;

    public
    TutorProfileSelfViewFragment()
    {
    }

    private
    void fillUpProfileDetails()
    {
        TextView tvName        = binding.tvNameField;
        TextView tvPronouns    = binding.tvPronounsField;
        TextView tvMajor       = binding.tvMajorField;
        TextView tvPrice       = binding.tvRatingField;
        TextView tvReviews     = binding.tvReviewsField;
        TextView tvMoneyEarned = binding.tvMoneyEarnedField;

        tvName.setText(account.getUserName());
        tvPronouns.setText(account.getUserPronouns());
        tvMajor.setText(account.getUserMajor());
        tvPrice.setText(String.format(Locale.getDefault(), "$%.2f/h", tutor.getHourlyRate()));
        tvReviews.setText(String.format(Locale.getDefault(),
                                        "%.1f ⭐(%d)",
                                        profileHandler.getAvgReview(tutor),
                                        tutor.getReviewCount()));
        tvMoneyEarned.setText(String.format(Locale.getDefault(),
                                            "$%.02f",
                                            sessionHandler
                                                    .getAcceptedSessions(tutor)
                                                    .stream()
                                                    .mapToDouble(ISession::getSessionCost)
                                                    .sum()));
    }

    private
    void setUpEditProfileButton()
    {
        Button btnEditProfile = binding.fabEditProfile;
        btnEditProfile.setOnClickListener(v -> NavHostFragment
                .findNavController(this)
                .navigate(R.id.actionToEditTutorProfileFragment));
    }

    private
    void setUpTopBarMenu()
    {
        MaterialToolbar mtTopBar = binding.topAppBar;
        mtTopBar.setOnMenuItemClickListener(i -> {
            int itemId = i.getItemId();
            if (itemId == R.id.tbAccountSettings) {
                NavHostFragment.findNavController(this).navigate(R.id.actionToAccountSettingsFragment);
            }
            return true;
        });
    }

    private
    void setUpTutoredCourses()
    {
        Button            btnAddCourse = binding.btnAddCourse;
        DialogueAddCourse addCourse    = new DialogueAddCourse();
        btnAddCourse.setOnClickListener(v -> addCourse.show(getChildFragmentManager(), "Add Course"));
        RecyclerView               recycler      = binding.rvTutoredCourses;
        int                        spanCount     = isLarge || isLandscape ? 6 : 2;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), spanCount);
        recycler.setLayoutManager(layoutManager);
        profileViewModel.setTutoredCoursesCode(profileHandler.getCourseCodeList(tutor));
        tutoredCourses = profileViewModel.getTutoredCoursesCode().getValue();
        StringRecyclerAdapter recyclerAdapter = new StringRecyclerAdapter(tutoredCourses);
        recycler.setAdapter(recyclerAdapter);
        profileViewModel.getTutoredCoursesCode().observe(getViewLifecycleOwner(), recyclerAdapter::updateData);
    }

    private
    void setUpPreferredLocations()
    {
        DialogueAddLocation addLocation    = new DialogueAddLocation();
        Button              btnAddLocation = binding.btnAddLocation;
        btnAddLocation.setOnClickListener(v -> addLocation.show(getChildFragmentManager(), "Add Location"));
        RecyclerView               recycler      = binding.rvPreferredLocations;
        int                        spanCount     = isLarge || isLandscape ? 6 : 2;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), spanCount);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(new StringRecyclerAdapter(new ArrayList<>()));
        profileViewModel.setPreferredLocations(profileHandler.getPreferredLocations(tutor));
        prefLocations = profileViewModel.getPreferredLocations().getValue();
        StringRecyclerAdapter recyclerAdapter = new StringRecyclerAdapter(prefLocations);
        recycler.setAdapter(recyclerAdapter);
        profileViewModel.getPreferredLocations().observe(getViewLifecycleOwner(), recyclerAdapter::updateData);
    }

    private
    void setUpAvailability()
    {
        Button btnAddAvail = binding.btnAddAvailability;
        btnAddAvail.setOnClickListener(v -> getDatePicker().show(getChildFragmentManager(), "Add Availability"));
        RecyclerView               recycler      = binding.rvAvailability;
        int                        spanCount     = isLarge || isLandscape ? 2 : 1;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), spanCount);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(new StringRecyclerAdapter(new ArrayList<>()));

        profileViewModel.setAvailStrList(Server
                                                 .getTutorAvailabilityAccess()
                                                 .getAvailability(tutor)
                                                 .stream()
                                                 .map(TimeSliceFormatter::format)
                                                 .collect(Collectors.toList()));
        availStrList = profileViewModel.getAvailStrList().getValue();
        StringRecyclerAdapter recyclerAdapter = new StringRecyclerAdapter(availStrList);
        recycler.setAdapter(recyclerAdapter);
        profileViewModel.getAvailStrList().observe(getViewLifecycleOwner(), recyclerAdapter::updateData);
    }

    private
    MaterialDatePicker<Long> getDatePicker()
    {
        long     today    = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(today);

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder
                .datePicker()
                .setCalendarConstraints(new CalendarConstraints.Builder()
                                                .setValidator(DateValidatorPointForward.now())
                                                .setStart(calendar.getTimeInMillis())
                                                .build())
                .build();

        MaterialTimePicker startTimePicker = new MaterialTimePicker.Builder().setTitleText("Select start time").build();

        MaterialTimePicker endTimePicker = new MaterialTimePicker.Builder().setTitleText("Select end time").build();

        datePicker.addOnCancelListener(dialog -> resetDateTime());
        startTimePicker.addOnCancelListener(dialog -> resetDateTime());
        endTimePicker.addOnCancelListener(dialog -> resetDateTime());

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate = Instant.ofEpochMilli(selection).atZone(ZoneOffset.UTC).toLocalDate();
            startTimePicker.show(getChildFragmentManager(), "StartTimePicker");
        });

        startTimePicker.addOnPositiveButtonClickListener(v -> {
            selectedStartTime     = LocalTime.of(startTimePicker.getHour(), startTimePicker.getMinute());
            selectedStartDateTime = LocalDateTime.of(selectedDate, selectedStartTime);
            endTimePicker.show(getChildFragmentManager(), "EndTimePicker");
        });

        endTimePicker.addOnPositiveButtonClickListener(v -> {
            selectedEndTime     = LocalTime.of(endTimePicker.getHour(), endTimePicker.getMinute());
            selectedEndDateTime = LocalDateTime.of(selectedDate, selectedEndTime);
            if (selectedEndDateTime.isBefore(selectedStartDateTime)) {
                Toast.makeText(requireContext(), "End time cannot be set before start", Toast.LENGTH_SHORT).show();
            }
            ITimeSlice timeSlice = new TimeSlice(selectedStartDateTime, selectedEndDateTime);
            try {
                profileViewModel.getAvailabilityManager().getValue().addAvailability(tutor, timeSlice);
                Toast
                        .makeText(requireContext(),
                                  "Added availability: " + TimeSliceFormatter.format(timeSlice),
                                  Toast.LENGTH_SHORT)
                        .show();
            } catch (final TutorAvailabilityManagerException e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (final Throwable e) {
                Toast.makeText(requireContext(), "Failed to set availability", Toast.LENGTH_SHORT).show();
            }
        });
        return datePicker;
    }

    private
    void resetDateTime()
    {
        selectedDate          = null;
        selectedStartTime     = null;
        selectedEndTime       = null;
        selectedStartDateTime = null;
        selectedEndDateTime   = null;
    }

    @Override
    public
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sessionViewModel = new ViewModelProvider(requireActivity()).get(SessionViewModel.class);
        profileViewModel = new ViewModelProvider(requireActivity()).get(TutorProfileViewModel.class);
        profileHandler   = profileViewModel.getProfileHandler().getValue();
        sessionHandler   = sessionViewModel.getSessionHandler().getValue();
    }

    @Override
    public
    View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentTutorProfileSelfViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public
    void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        tutor   = profileViewModel.getTutor().getValue();
        account = profileViewModel.getTutorAccount().getValue();
        Configuration config = getResources().getConfiguration();
        isLarge     = config.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE);
        isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE;
        fillUpProfileDetails();
        setUpEditProfileButton();
        setUpTopBarMenu();
        setUpTutoredCourses();
        setUpPreferredLocations();
        setUpAvailability();
    }
}