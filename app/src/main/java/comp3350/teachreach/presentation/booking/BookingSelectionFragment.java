package comp3350.teachreach.presentation.booking;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import comp3350.teachreach.R;
import comp3350.teachreach.databinding.FragmentBookingSelectionBinding;
import comp3350.teachreach.objects.interfaces.ITimeSlice;
import comp3350.teachreach.presentation.utils.GridSpacingItemDecoration;

public
class BookingSelectionFragment extends Fragment {
    private BookingViewModel bookingViewModel;

    private FragmentBookingSelectionBinding binding;
    private LocalDate                       date;
    private ITimeSlice                      selectedSlot;
    private String                          selectedLocation;
    private boolean                         isLarge, isLandscape;

    public BookingSelectionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
        date             = bookingViewModel.getBookingDate().getValue();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookingSelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Configuration config = getResources().getConfiguration();
        isLarge     = config.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE);
        isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE;
        setUpTopBar();
        setUpTimeSlots();
        setUpButtons();
        setUpLocationField();
    }

    private void setUpLocationField() {
        List<String> tutorLocations = bookingViewModel.getTutorLocations().getValue();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireActivity(),
                                                               android.R.layout.simple_list_item_1,
                                                               tutorLocations == null ?
                                                                       new ArrayList<>() :
                                                                       tutorLocations);

        AutoCompleteTextView locations = binding.locationField;
        locations.setAdapter(arrayAdapter);
        locations.setThreshold(1);
        locations.setLongClickable(true);
        locations.setOnLongClickListener(v -> {
            locations.setText("");
            selectedLocation = null;
            return true;
        });
        locations.setOnItemClickListener((p, v, pos, id) -> {
            locations.clearFocus();
            selectedLocation = tutorLocations.get(pos);
        });
    }

    private void setUpButtons() {
        Button positive = binding.confirmButton;
        Button negative = binding.cancelButton;

        NavController navController = NavHostFragment.findNavController(this);

        positive.setOnClickListener(v -> continueBooking(navController));
        negative.setOnClickListener(v -> navController.navigateUp());
    }

    private void setUpTimeSlots() {
        RecyclerView recyclerView = binding.timeSlotRecyclerView;

        TimeSlotRecyclerAdapter adapter = new TimeSlotRecyclerAdapter(
                bookingViewModel.getTimeSlots().getValue(),
                this::setSelectedSlot);

        int spanCount = isLarge || isLandscape ? 4 : 2;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(),
                                                                         spanCount);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);

        recyclerView.addItemDecoration(
                new GridSpacingItemDecoration(spanCount, spacingInPixels, true, 0));
    }

    private void setUpTopBar() {
        MaterialToolbar materialToolbar = binding.topAppBar;
        materialToolbar.setTitle(
                "Booking for " + date.format(DateTimeFormatter.ofPattern("eee, d MMM, yyyy")));
        materialToolbar.setNavigationOnClickListener(
                view -> NavHostFragment.findNavController(this).navigateUp());
    }

    void setSelectedSlot(ITimeSlice ts) {
        selectedSlot = ts;
    }

    void continueBooking(NavController navController) {
        if (selectedSlot == null) {
            Toast.makeText(requireContext(), "Must select a time slot!", Toast.LENGTH_SHORT).show();
            return;
        }
        bookingViewModel.setSessionTime(selectedSlot);
        bookingViewModel.setSessionLocation(selectedLocation);
        selectedSlot = null;
        navController.navigate(R.id.actionToReviewBookingFragment);
    }
}
