package comp3350.teachreach.presentation.booking;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

import comp3350.teachreach.R;
import comp3350.teachreach.databinding.FragmentPaymentBinding;
import comp3350.teachreach.databinding.FragmentPlaceHolderBinding;
import comp3350.teachreach.logic.availability.TutorAvailabilityManager;
import comp3350.teachreach.logic.exceptions.payment.ExpiredCardException;
import comp3350.teachreach.logic.exceptions.payment.InvalidCVCException;
import comp3350.teachreach.logic.exceptions.payment.InvalidCardNumberException;
import comp3350.teachreach.logic.exceptions.payment.InvalidExpiryDateException;
import comp3350.teachreach.logic.exceptions.payment.PaymentException;
import comp3350.teachreach.logic.interfaces.ISessionHandler;
import comp3350.teachreach.logic.interfaces.ITutorAvailabilityManager;
import comp3350.teachreach.logic.payment.PaymentValidator;
import comp3350.teachreach.logic.session.SessionHandler;
import comp3350.teachreach.objects.Session;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITimeSlice;
import comp3350.teachreach.objects.interfaces.ITutor;

public class PaymentFragment extends Fragment {
    private FragmentPaymentBinding binding;
    private Double                 grandTotal;
    private TextInputLayout        tilCardNumber, tilExpDate, tilCVC;
    private BookingViewModel bookingViewModel;
    private EditText         cardNumber, expDate, cVc;

    public PaymentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpButtons();
        setUpTextFields();
    }

    private void setUpTextFields() {
        grandTotal = bookingViewModel.getSessionPrice().getValue();
        TextView tvGrandTotal = binding.tvTotalField;
        tvGrandTotal.setText(String.format(Locale.getDefault(), "$%.2f", grandTotal));

        tilCardNumber = binding.tilCardNumber;
        tilExpDate    = binding.tilExpDate;
        tilCVC        = binding.tilCVC;

        cardNumber = tilCardNumber.getEditText();
        expDate    = tilExpDate.getEditText();
        cVc        = tilCVC.getEditText();
    }

    private void setUpButtons() {
        Button        cancelButton  = binding.cancelButton;
        Button        confirmButton = binding.confirmButton;
        NavController navController = NavHostFragment.findNavController(this);
        cancelButton.setOnClickListener(v -> navController.navigateUp());
        confirmButton.setOnClickListener(v -> confirmBooking());
    }

    private void confirmBooking() {
        String strCardNumber = cardNumber.getText().toString().trim();
        String strExpDate    = expDate.getText().toString().trim();
        String strCVC        = cVc.getText().toString().trim();
        tilCardNumber.setError(null);
        tilExpDate.setError(null);
        tilCVC.setError(null);
        try {
            PaymentValidator.validatePaymentInfo(strCardNumber, strExpDate, strCVC);
            ITutorAvailabilityManager availabilityManager = new TutorAvailabilityManager();
            ISessionHandler           sessionHandler      = new SessionHandler(availabilityManager);
            IStudent student = bookingViewModel.getStudent()
                                               .getValue();
            ITutor     tutor       = bookingViewModel.getTutor().getValue();
            ITimeSlice sessionTime = bookingViewModel.getSessionTime().getValue();
            String     location    = bookingViewModel.getSessionLocation().getValue();
            sessionHandler.bookSession(new Session(student.getStudentID(),
                                                   tutor.getTutorID(),
                                                   sessionTime,
                                                   grandTotal,
                                                   location));
            AlertDialog doneDialog = makeDoneDialog("Congratulations!",
                                                    "Booking request has been" +
                                                            " sent to your tutor!",
                                                    "View My Sessions",
                                                    (dialog, which) -> {
                                                        NavHostFragment
                                                                .findNavController(
                                                                        requireParentFragment().requireParentFragment())
                                                                .navigate(R.id.sessionFragment);
                                                    },
                                                    "Done",
                                                    (dialog, which) -> dialog.dismiss());
            doneDialog.setOnDismissListener(dialog -> NavHostFragment
                    .findNavController(this)
                    .navigate(R.id.actionToTutorProfileViewFragment));
            doneDialog.show();
        } catch (InvalidCardNumberException e) {
            tilCardNumber.setError(e.getMessage());
        } catch (InvalidCVCException e) {
            tilCVC.setError(e.getMessage());
        } catch (InvalidExpiryDateException | ExpiredCardException e) {
            tilExpDate.setError(e.getMessage());
        } catch (PaymentException unknownErr) {
            Toast.makeText(requireContext(), "Issue with payment info, please review",
                           Toast.LENGTH_SHORT).show();
        } catch (final Throwable e) {
            AlertDialog errorDialog = makeDoneDialog("Something Bad Happened!",
                                                     "Booking request " + "mightn't be sent to " +
                                                             "tutor :(", "Go Back to Tutor Profile",
                                                     (dialog, which) -> {
                                                         NavHostFragment.findNavController(this)
                                                                        .navigate(
                                                                                R.id.actionToTutorProfileViewFragment);
                                                     }, "Dismiss",
                                                     (dialog, which) -> dialog.cancel());
            errorDialog.setOnCancelListener(dialog -> {
                SlidingPaneLayout slidingPaneLayout = requireActivity().requireViewById(
                        R.id.searchFragment);
                slidingPaneLayout.closePane();
                NavHostFragment.findNavController(this).navigate(R.id.actionToPlaceHolderFragment);
            });
            errorDialog.show();
            Toast.makeText(requireContext(), "Tutor mightn't be available for booking",
                           Toast.LENGTH_SHORT).show();
        }
    }

    private AlertDialog makeDoneDialog(String title, String message, CharSequence posMsg,
                                       DialogInterface.OnClickListener posListener,
                                       CharSequence negMsg,
                                       DialogInterface.OnClickListener negListener) {
        return new MaterialAlertDialogBuilder(requireContext()).setTitle(title)
                                                               .setMessage(message)
                                                               .setView(
                                                                       FragmentPlaceHolderBinding.inflate(
                                                                                                         this.getLayoutInflater())
                                                                                                 .getRoot())
                                                               .setPositiveButton(posMsg,
                                                                                  posListener)
                                                               .setNegativeButton(negMsg,
                                                                                  negListener)
                                                               .create();
    }
}
