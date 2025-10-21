package comp3350.teachreach.presentation.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import comp3350.teachreach.R;
import comp3350.teachreach.logic.account.AuthenticationHandler;
import comp3350.teachreach.logic.account.InputValidator;
import comp3350.teachreach.logic.exceptions.InvalidCredentialException;
import comp3350.teachreach.logic.exceptions.input.InvalidEmailException;
import comp3350.teachreach.logic.exceptions.input.InvalidPasswordException;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.presentation.home.StudentHomeActivity;
import comp3350.teachreach.presentation.signup.StudentSignUpActivity;

public
class StudentLoginActivity extends AppCompatActivity
{
    private TextInputLayout tilStudentEmail, tilStudentPassword;
    private EditText etStudentEmail, etStudentPassword;
    private AuthenticationHandler authenticationHandler;

    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        tilStudentEmail    = findViewById(R.id.tilStudentLoginEmail);
        tilStudentPassword = findViewById(R.id.tilStudentLoginPassword);
        etStudentEmail     = tilStudentEmail.getEditText();
        etStudentPassword  = tilStudentPassword.getEditText();
        Button          btnLogin  = findViewById(R.id.btnLoginAsStudent);
        Button          btnSignUp = findViewById(R.id.btnSignupAsStudent);
        MaterialToolbar mtTopBar  = findViewById(R.id.topAppBar);

        mtTopBar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        btnLogin.setOnClickListener(v -> login());
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(StudentLoginActivity.this, StudentSignUpActivity.class);
            startActivity(intent);
        });
    }

    private
    void login()
    {
        String email    = etStudentEmail.getText().toString().trim();
        String password = etStudentPassword.getText().toString().trim();

        try {
            tilStudentEmail.setError(null);
            tilStudentPassword.setError(null);
            authenticationHandler = new AuthenticationHandler();
            InputValidator.validateEmail(email);
            InputValidator.validatePassword(password);
            IStudent student = authenticationHandler.authenticateStudent(email, password);
            Intent   intent  = new Intent(StudentLoginActivity.this, StudentHomeActivity.class);
            intent.putExtra("ACCOUNT_ID", student.getAccountID());
            intent.putExtra("STUDENT_ID", student.getStudentID());
            startActivity(intent);
            finish();
        } catch (final InvalidEmailException e) {
            tilStudentEmail.setError(e.getMessage());
        } catch (final InvalidPasswordException e) {
            tilStudentPassword.setError(e.getMessage());
        } catch (final InvalidCredentialException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (final Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Please clear app storage and try again", Toast.LENGTH_LONG).show();
        }
    }
}
