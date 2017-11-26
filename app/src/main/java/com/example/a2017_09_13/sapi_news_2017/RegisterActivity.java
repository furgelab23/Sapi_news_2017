package com.example.a2017_09_13.sapi_news_2017;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private final static String TAG = "Register: ";
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mPhoneNumber;
    private Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //rakapcsolasok
        mFirstNameField = (EditText) findViewById(R.id.et_fName);
        mLastNameField = (EditText) findViewById(R.id.et_lName);
        mEmailField = (EditText) findViewById(R.id.et_email);
        mPasswordField = (EditText) findViewById(R.id.et_password);
        mPhoneNumber = (EditText) findViewById(R.id.et_phonenumber);
        registerButton = (Button) findViewById(R.id.btn_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    register(mFirstNameField.getText().toString(), mLastNameField.getText().toString(), mEmailField.getText().toString(), mPasswordField.getText().toString(), mPhoneNumber.getText().toString());
                }

                finish();
            }
        });
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
    }

    private void register(final String firstName, final String lastName, final String email, final String password, final String phoneNumber) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                        Toast.makeText(RegisterActivity.this, "Creating new user: weak password",
                                                Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onComplete: weak_password");

                                        Toast toast = Toast.makeText(getApplicationContext(), "Password must be at least 6 characters!", Toast.LENGTH_SHORT);
                                        toast.show();
                                    } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                        Toast.makeText(RegisterActivity.this, "Creating new user: credential is invalid",
                                                Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onComplete: malformed_email");
                                        Toast toast = Toast.makeText(getApplicationContext(), "Not a valid email!", Toast.LENGTH_SHORT);
                                        toast.show();
                                    } catch (FirebaseAuthUserCollisionException existEmail) {
                                        Log.d(TAG, "onComplete: exist_email");
                                        Toast.makeText(RegisterActivity.this, "Creating new user: user collision!",
                                                Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(RegisterActivity.this, "Creating new user: unknown error",
                                                Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onComplete: " + e.getMessage());
                                    }
                                } else if (task.isSuccessful()) {
                                    Log.d(TAG, "completedYAY");
                                    Toast.makeText(RegisterActivity.this, "New user created",
                                            Toast.LENGTH_SHORT).show();


                                    FirebaseUser user = mAuth.getCurrentUser();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("users").child(user.getUid());
                                    myRef.child("firstName").setValue(firstName);
                                    myRef.child("lastName").setValue(lastName);
                                    myRef.child("emailAddress").setValue(email);
                                    myRef.child("phoneNumber").setValue(phoneNumber);

                                } else {
                                    Toast.makeText(getApplicationContext(), "Nothing happened", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        }
                );
    }

    private boolean validateForm() {
        boolean valid = true;

        String firstName = mFirstNameField.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            mFirstNameField.setError("Required");
            valid = false;
        } else {
            mFirstNameField.setError(null);
        }

        String lastName = mLastNameField.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            mLastNameField.setError("Required");
            valid = false;
        } else {
            mLastNameField.setError(null);
        }

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String telefone = mPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(telefone)) {
            mPhoneNumber.setError("Required");
            valid = false;
        } else {
            mPhoneNumber.setError(null);
        }
        return valid;
    }
}
