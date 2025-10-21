package comp3350.teachreach.presentation.home;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.navigation.NavigationBarView;

import comp3350.teachreach.R;
import comp3350.teachreach.application.Server;
import comp3350.teachreach.databinding.ActivityNavigationStudentBinding;
import comp3350.teachreach.logic.DAOs.AccessStudents;
import comp3350.teachreach.presentation.utils.TRViewModel;

public class StudentHomeActivity extends AppCompatActivity {
    private static final int  BACK_DELAY = 2000;
    private              long backPressedTime;

    private OnBackPressedCallback            onBackPressedCallback;
    private ActivityNavigationStudentBinding binding;

    private NavigationBarView navigationMenu;
    private NavController     navController;
    private TRViewModel       vm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm             = new ViewModelProvider(this).get(TRViewModel.class);
        navigationMenu = (NavigationBarView) binding.navigationMenu;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(
                R.id.nav_host_fragment_student);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        int accountId = getIntent().getIntExtra("ACCOUNT_ID", -1);
        vm.setAccount(Server.getAccountDataAccess().getAccounts().get(accountId));
        AccessStudents accessStudents = new AccessStudents();
        vm.setStudent(accessStudents.getStudentByAccountID(accountId));
        assert vm.getStudent() != null;
        setUpNavigationMenu();
        setUpBackButtonHandler();
    }

    private void setUpNavigationMenu() {
        navigationMenu.setSelectedItemId(R.id.NavBarSearch);
        navigationMenu.setOnItemSelectedListener(i -> {
            int itemId = i.getItemId();
            if (itemId == R.id.NavBarSessions) navController.navigate(R.id.sessionFragment);
            else if (itemId == R.id.NavBarSearch) navController.navigate(R.id.searchFragment);
            else if (itemId == R.id.NavBarProfile) navController.navigate(
                    R.id.studentProfileSelfViewFragment);
            else if (itemId == R.id.NavBarChats) navController.navigate(R.id.actionToGroupFragment);
            return true;
        });
        navController.addOnDestinationChangedListener((controller, dest, bundle) -> {
            if (dest.getId() == R.id.studentProfileSelfViewFragment) changeNavigationMenu(
                    NavDest.profile);
            if (dest.getId() == R.id.searchFragment) changeNavigationMenu(NavDest.search);
            if (dest.getId() == R.id.sessionFragment) changeNavigationMenu(NavDest.sessions);
            if (dest.getId() == R.id.groupFragment) changeNavigationMenu(NavDest.chat);
        });
    }

    private void changeNavigationMenu(NavDest n) {
        switch (n) {
            case sessions -> navigationMenu.getMenu()
                                           .findItem(R.id.NavBarSessions)
                                           .setChecked(true);
            case search -> navigationMenu.getMenu().findItem(R.id.NavBarSearch).setChecked(true);
            case profile -> navigationMenu.getMenu().findItem(R.id.NavBarProfile).setChecked(true);
            case chat -> navigationMenu.getMenu().findItem(R.id.NavBarChats).setChecked(true);
        }
    }

    private void setUpBackButtonHandler() {
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backIsPressed();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void backIsPressed() {
        if (backPressedTime + BACK_DELAY > System.currentTimeMillis()) finishAffinity();
        else Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        backPressedTime = System.currentTimeMillis();
    }

    private enum NavDest {
        sessions,
        search,
        profile,
        chat
    }
}