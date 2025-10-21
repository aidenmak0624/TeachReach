package comp3350.teachreach.presentation.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import comp3350.teachreach.R;
import comp3350.teachreach.data.exceptions.DuplicateEmailException;
import comp3350.teachreach.logic.account.AccountCreator;
import comp3350.teachreach.logic.exceptions.InvalidNameException;
import comp3350.teachreach.logic.exceptions.input.InvalidEmailException;
import comp3350.teachreach.logic.exceptions.input.InvalidPasswordException;
import comp3350.teachreach.logic.interfaces.IAccountCreator;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.presentation.home.StudentHomeActivity;

public
class StudentSignUpActivity extends AppCompatActivity
{
    private TextInputLayout tilUsername, tilMajor, tilPronouns, tilEmail, tilPassword;
    private EditText etUsername, etMajor, etPassword, etEmail, etPronouns;
    private IAccountCreator accountCreator;

    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);

        tilUsername = findViewById(R.id.tilSignupName);
        tilMajor    = findViewById(R.id.tilSignupMajor);
        tilPronouns = findViewById(R.id.tilSignupPronouns);
        tilEmail    = findViewById(R.id.tilSignupEmail);
        tilPassword = findViewById(R.id.tilSignupPassword);

        etUsername = tilUsername.getEditText();
        etPassword = tilPassword.getEditText();
        etEmail    = tilEmail.getEditText();
        etMajor    = tilMajor.getEditText();
        etPronouns = tilPronouns.getEditText();

        Button btnCreateProfile = findViewById(R.id.btnCreateProfile);

        btnCreateProfile.setOnClickListener(v -> createProfile());
    }

    private
    void createProfile()
    {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email    = etEmail.getText().toString().trim();
        String major    = etMajor.getText().toString().trim();
        String pronoun  = etPronouns.getText().toString().trim();

        try {
            tilEmail.setError(null);
            tilPassword.setError(null);
            tilUsername.setError(null);
            accountCreator = new AccountCreator();

            IStudent student = accountCreator.createStudentAccount(email, password, username, pronoun, major);
            Toast.makeText(StudentSignUpActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(StudentSignUpActivity.this, StudentHomeActivity.class);
            intent.putExtra("ACCOUNT_ID", student.getAccountID());
            startActivity(intent);
            finish();
        } catch (final InvalidNameException e) {
            tilUsername.setError(e.getMessage());
        } catch (final InvalidPasswordException e) {
            tilPassword.setError(e.getMessage());
        } catch (final InvalidEmailException | DuplicateEmailException e) {
            tilEmail.setError(e.getMessage());
        } catch (final Exception e) {
            Toast.makeText(StudentSignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
