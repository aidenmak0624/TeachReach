package comp3350.teachreach.presentation.signup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import comp3350.teachreach.R;
import comp3350.teachreach.data.exceptions.DuplicateEmailException;
import comp3350.teachreach.logic.account.AccountCreator;
import comp3350.teachreach.logic.exceptions.InvalidNameException;
import comp3350.teachreach.logic.exceptions.input.InvalidEmailException;
import comp3350.teachreach.logic.exceptions.input.InvalidPasswordException;
import comp3350.teachreach.logic.interfaces.IAccountCreator;
import comp3350.teachreach.objects.interfaces.ITutor;
import comp3350.teachreach.presentation.home.TutorHomeActivity;

public
class TutorSignUpActivity extends AppCompatActivity
{
    private static final int REQUEST_CODE_PICK_FILE = 1;

    private TextInputLayout tilUsername, tilMajor, tilPronouns, tilEmail, tilPassword;

    private EditText etTutorUsername, etTutorPassword, etTutorEmail, etTutorMajor, etPronouns;

    private IAccountCreator accountCreator;

    private
    void createTutorProfile()
    {
        String username = etTutorUsername.getText().toString().trim();
        String password = etTutorPassword.getText().toString().trim();
        String email    = etTutorEmail.getText().toString().trim();
        String major    = etTutorMajor.getText().toString().trim();
        String pronouns = etPronouns.getText().toString();

        try {
            tilEmail.setError(null);
            tilPassword.setError(null);
            tilUsername.setError(null);
            accountCreator = new AccountCreator();
            ITutor newTutor = accountCreator.createTutorAccount(email, password, username, pronouns, major);

            Toast.makeText(TutorSignUpActivity.this, "Tutor Account Created Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TutorSignUpActivity.this, TutorHomeActivity.class);
            intent.putExtra("ACCOUNT_ID", newTutor.getAccountID());
            intent.putExtra("TUTOR_ID", newTutor.getTutorID());

            startActivity(intent);
            finish();
        } catch (final InvalidNameException e) {
            tilUsername.setError(e.getMessage());
        } catch (final InvalidPasswordException e) {
            tilPassword.setError(e.getMessage());
        } catch (final InvalidEmailException | DuplicateEmailException e) {
            tilEmail.setError(e.getMessage());
        } catch (final Exception e) {
            Toast.makeText(TutorSignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private
    void openFilePicker()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to " + "Upload"),
                                   REQUEST_CODE_PICK_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri transcriptUri = data.getData();
            }
        }
    }

    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up);

        tilUsername = findViewById(R.id.tilSignupName);
        tilMajor    = findViewById(R.id.tilSignupMajor);
        tilPronouns = findViewById(R.id.tilSignupPronouns);
        tilEmail    = findViewById(R.id.tilSignupEmail);
        tilPassword = findViewById(R.id.tilSignupPassword);

        etTutorUsername = tilUsername.getEditText();
        etTutorPassword = tilPassword.getEditText();
        etTutorEmail    = tilEmail.getEditText();
        etTutorMajor    = tilMajor.getEditText();
        etPronouns      = tilPronouns.getEditText();

        Button btnUploadTranscript = findViewById(R.id.btnUploadTranscript);
        Button btnCreateProfile    = findViewById(R.id.btnCreateProfile);

        btnUploadTranscript.setOnClickListener(v -> openFilePicker());
        btnCreateProfile.setOnClickListener(v -> createTutorProfile());
    }
}
