package comp3350.teachreach.presentation.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

import comp3350.teachreach.R;
import comp3350.teachreach.databinding.FragmentReviewBookingBinding;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.ITimeSlice;
import comp3350.teachreach.objects.interfaces.ITutor;

public
class ReviewBookingFragment extends Fragment {
    private FragmentReviewBookingBinding binding;
    private BookingViewModel             bookingViewModel;

    public ReviewBookingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewBookingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillUpBookingDetails();
        setUpButtons();
    }

    private void setUpButtons() {
        Button        cancelButton  = binding.cancelButton;
        Button        confirmButton = binding.confirmButton;
        NavController navController = NavHostFragment.findNavController(this);
        cancelButton.setOnClickListener(v -> navController.navigateUp());
        confirmButton.setOnClickListener(v -> navController.navigate(R.id.actionToPaymentFragment));
    }

    private void fillUpBookingDetails() {
        TextView tutorName = binding.tvNameField;
        TextView sDate     = binding.tvSessionDateField;
        TextView sTime     = binding.tvSessionTimeField;
        TextView sDuration = binding.tvSessionDurationField;
        TextView sPrice    = binding.tvSessionPriceField;
        TextView sLocation = binding.tvSessionLocationField;

        ITimeSlice sessionTime = bookingViewModel.getSessionTime().getValue();
        IAccount   account     = bookingViewModel.getTutorAccount().getValue();
        ITutor     tutor       = bookingViewModel.getTutor().getValue();
        String     location    = bookingViewModel.getSessionLocation().getValue();
        if (location == null) {
            location = "TBD";
            bookingViewModel.setSessionLocation(location);
        }
        double price = (double) sessionTime.getDuration().toMinutes() / 60 * tutor.getHourlyRate();
        bookingViewModel.setSessionPrice(price);
        assert account != null;
        tutorName.setText(account.getUserName());
        sDate.setText(sessionTime.getStartTime()
                                 .format(DateTimeFormatter.ofPattern("eee, d MMMM, yyyy")));
        sTime.setText(sessionTime.getStartTime().format(DateTimeFormatter.ofPattern("h:mm a")));
        sDuration.setText(String.format(Locale.getDefault(), "%d minutes",
                                        sessionTime.getDuration().toMinutes()));
        sPrice.setText(String.format(Locale.getDefault(), "$%.2f", price));
        sLocation.setText(location);
    }
}
