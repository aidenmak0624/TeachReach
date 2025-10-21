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
import comp3350.teachreach.databinding.FragmentChangeEmailBinding;
import comp3350.teachreach.logic.account.AccountManager;
import comp3350.teachreach.logic.exceptions.AccountManagerException;
import comp3350.teachreach.logic.exceptions.input.InvalidEmailException;
import comp3350.teachreach.logic.interfaces.IAccountManager;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.presentation.utils.TRViewModel;

public
class FragmentChangingEmail extends Fragment {
    private FragmentChangeEmailBinding binding;
    private TRViewModel                vm;
    private IAccount                   account;

    public FragmentChangingEmail() {
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
        binding = FragmentChangeEmailBinding.inflate(inflater, container, false);
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

        TextInputLayout tvNewEmail = binding.tilNewValue;

        EditText etCurrentPassword = tvCurrentPassword.getEditText();
        EditText etNewEmail        = tvNewEmail.getEditText();

        Button btnApply = binding.fabApply;

        NavController navController = NavHostFragment.findNavController(this);
        btnApply.setOnClickListener(view -> {
            tvNewEmail.setError(null);
            tvCurrentPassword.setError(null);

            IAccountManager accountManager = new AccountManager(account);

            assert etCurrentPassword != null;
            String password = etCurrentPassword.getText().toString().trim();

            assert etNewEmail != null;
            String newEmail = etNewEmail.getText().toString().trim();

            try {
                accountManager.updateEmail(password, newEmail);
                navController.navigate(R.id.actionToAccountSettingsFragment);
            } catch (final InvalidEmailException e) {
                tvNewEmail.setError(e.getMessage());
            } catch (final AccountManagerException e) {
                tvNewEmail.setError(":( Email might've already been taken! )");
            } catch (final Throwable e) {
                tvCurrentPassword.setError(e.getMessage());
            }
        });
    }
}
