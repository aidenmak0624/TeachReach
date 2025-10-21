package comp3350.teachreach.presentation.profile.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Locale;

import comp3350.teachreach.R;
import comp3350.teachreach.databinding.FragmentStudentProfileSelfViewBinding;
import comp3350.teachreach.logic.availability.TutorAvailabilityManager;
import comp3350.teachreach.logic.session.SessionHandler;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.ISession;
import comp3350.teachreach.presentation.utils.TRViewModel;

public class StudentProfileSelfViewFragment extends Fragment {
    private FragmentStudentProfileSelfViewBinding binding;

    private TRViewModel vm;

    private TextView tvName, tvMajor, tvPronouns, tvMoneySpent;
    private Button btnEditProfile;

    private IAccount account;

    public StudentProfileSelfViewFragment() {
    }

    private void fillUpProfileDetails() {
        tvName       = binding.tvNameField;
        tvPronouns   = binding.tvPronounsField;
        tvMajor      = binding.tvMajorField;
        tvMoneySpent = binding.tvMoneySpentField;

        tvName.setText(account.getUserName());
        tvPronouns.setText(account.getUserPronouns());
        tvMajor.setText(account.getUserMajor());
        try {
            double moneySpent = new SessionHandler(
                    new TutorAvailabilityManager()).getAcceptedSessions(vm.getStudent().getValue())
                                                   .stream()
                                                   .mapToDouble(ISession::getSessionCost)
                                                   .sum();
            tvMoneySpent.setText(String.format(Locale.getDefault(), "$%.2f", moneySpent));
        } catch (final Throwable e) {
            Toast.makeText(requireContext(), "Failed to fetch total money spent!",
                           Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpEditProfileButton() {
        btnEditProfile = binding.fabEditProfile;
        btnEditProfile.setOnClickListener(view -> NavHostFragment.findNavController(this)
                                                                 .navigate(
                                                                         R.id.actionToEditStudentProfileFragment));
    }

    private void setUpTopBarMenu() {
        MaterialToolbar mtTopBar = binding.topAppBar;
        mtTopBar.setOnMenuItemClickListener(i -> {
            int itemId = i.getItemId();
            if (itemId == R.id.tbAccountSettings) {
                NavHostFragment.findNavController(this)
                               .navigate(R.id.actionToAccountSettingsFragment);
            }
            return true;
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm      = new ViewModelProvider(requireActivity()).get(TRViewModel.class);
        account = vm.getAccount().getValue();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentProfileSelfViewBinding.inflate(inflater, container, false);
        fillUpProfileDetails();
        setUpEditProfileButton();
        setUpTopBarMenu();
        return binding.getRoot();
    }
}