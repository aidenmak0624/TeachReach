package comp3350.teachreach.presentation.profile.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import comp3350.teachreach.R;
import comp3350.teachreach.databinding.FragmentChangePasswordBinding;
import comp3350.teachreach.logic.account.AccountManager;
import comp3350.teachreach.logic.interfaces.IAccountManager;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.presentation.utils.TRViewModel;

public
class FragmentChangingPassword extends Fragment {
    private FragmentChangePasswordBinding binding;
    private TRViewModel                   vm;
    private IAccount                      account;

    public FragmentChangingPassword() {
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
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        setUpTopBar();
        setUpTextFields();
        return binding.getRoot();
    }

    private void setUpTopBar() {
        MaterialToolbar materialToolbar = binding.topAppBar;
        NavController   navController   = NavHostFragment.findNavController(this);
        materialToolbar.setNavigationOnClickListener(
                v -> navController.navigate(R.id.actionToAccountSettingsFragment));
    }

    private void setUpTextFields() {
        TextInputLayout tvCurrentPassword = binding.tilCurrentPassword;

        TextInputLayout tvNewPassword = binding.tilNewValue;

        EditText etCurrentPassword = tvCurrentPassword.getEditText();
        EditText etNewPassword     = tvNewPassword.getEditText();

        Button btnApply = binding.fabApply;

        NavController navController = NavHostFragment.findNavController(this);
        btnApply.setOnClickListener(view -> {
            tvNewPassword.setError(null);
            tvCurrentPassword.setError(null);

            IAccountManager accountManager = new AccountManager(account);

            assert etCurrentPassword != null;
            String password = etCurrentPassword.getText().toString().trim();

            assert etNewPassword != null;
            String newPassword = etNewPassword.getText().toString().trim();

            try {
                accountManager.updatePassword(password, newPassword);
                navController.navigate(R.id.actionToAccountSettingsFragment);
            } catch (final Throwable e) {
                String msg = ":( Invalid passwords )";
                tvCurrentPassword.setError(msg);
                tvNewPassword.setError(msg);
            }
        });
    }
}
