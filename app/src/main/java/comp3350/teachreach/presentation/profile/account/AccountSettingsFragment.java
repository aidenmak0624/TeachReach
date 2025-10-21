package comp3350.teachreach.presentation.profile.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.appbar.MaterialToolbar;

import comp3350.teachreach.R;
import comp3350.teachreach.databinding.FragmentAccountSettingsBinding;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.presentation.utils.TRViewModel;

public
class AccountSettingsFragment extends Fragment {
    private FragmentAccountSettingsBinding binding;
    private TRViewModel                    vm;
    private IAccount                       account;
    private boolean                        isTutor;

    public AccountSettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm      = new ViewModelProvider(requireActivity()).get(TRViewModel.class);
        account = vm.getAccount().getValue();
        isTutor = Boolean.TRUE.equals(vm.getIsTutor().getValue());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountSettingsBinding.inflate(inflater, container, false);
        setUpTopBar();
        setUpButtons();
        return binding.getRoot();
    }

    private void setUpTopBar() {
        MaterialToolbar materialToolbar = binding.topAppBar;

        NavController navController = NavHostFragment.findNavController(this);
        View.OnClickListener listener = isTutor ?
                v -> navController.navigate(R.id.actionToTutorProfileSelfViewFragment) :
                v -> navController.navigate(R.id.actionToStudentProfileSelfViewFragment);
        materialToolbar.setNavigationOnClickListener(listener);
    }

    private void setUpButtons() {
        TextView tvEmailField = binding.tvEmailField;
        tvEmailField.setText(account.getAccountEmail());
        Button btnChangeEmail    = binding.btnChangeEmail;
        Button btnChangePassword = binding.btnChangePassword;
        btnChangeEmail.setOnClickListener(v -> NavHostFragment
                .findNavController(this)
                .navigate(R.id.actionToFragmentChangingEmail));
        btnChangePassword.setOnClickListener(v -> NavHostFragment
                .findNavController(this)
                .navigate(R.id.actionToFragmentChangingPassword));
    }
}