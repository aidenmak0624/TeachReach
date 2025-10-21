package comp3350.teachreach.presentation.profile.tutor;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import comp3350.teachreach.databinding.DialogAddCourseBinding;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.logic.exceptions.input.InvalidInputException;
import comp3350.teachreach.objects.interfaces.ITutor;
import comp3350.teachreach.presentation.utils.TRViewModel;

public class DialogueAddCourse extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentActivity           parentActivity = requireActivity();
        MaterialAlertDialogBuilder builder        = new MaterialAlertDialogBuilder(parentActivity);
        DialogAddCourseBinding binding = DialogAddCourseBinding.inflate(
                parentActivity.getLayoutInflater());
        TRViewModel trViewModel = new ViewModelProvider(parentActivity).get(TRViewModel.class);
        TutorProfileViewModel profileViewModel = new ViewModelProvider(parentActivity).get(
                TutorProfileViewModel.class);
        TextInputLayout tilCourseCode = binding.tilCourseCode;
        EditText        etCourseCode  = tilCourseCode.getEditText();
        TextInputLayout tilCourseName = binding.tilCourseName;
        EditText        etCourseName  = tilCourseName.getEditText();

        return builder.setTitle("Adding New Course").setPositiveButton("Add", (d, w) -> {
            try {
                tilCourseCode.setError(null);
                ITutor tutor = trViewModel.getTutor().getValue();
                assert tutor != null;

                assert etCourseCode != null;
                assert etCourseName != null;

                String courseCode = etCourseCode.getText().toString().trim();
                String courseName = etCourseName.getText().toString().trim();

                profileViewModel.addCourse(tutor, courseCode, courseName);
            } catch (final AssertionError e) {
                Toast.makeText(requireContext(), "Something went wrong :(", Toast.LENGTH_LONG)
                     .show();
            } catch (final InvalidInputException | DataAccessException e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("Cancel", null).setView(binding.getRoot()).create();
    }
}
