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

import com.example.a2017_09_13.sapi_news_2017.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "Login: ";
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private EditText mEmailField;//emailEditText
    private EditText mPasswordField;//passwordEditText
    private Button mSignInButton;//loginButton
    private Button mRegisterButton;//RegisterButton
    private SignInButton mGoogleButton;
    private GoogleSignInClient googleSignInClient;
    private View.OnClickListener buttonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_login);//erre a activityre ra kapcsolja ezt a viewt hozza rendeli


        buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = view.getId();
                //Hasznalj switchet ilyen helyzetekbe mivel az utasitasok szama novekedhet fejleszteskor
                switch (i) {
                    case R.id.loginButton:
                        signIn();
                        break;
                    case R.id.registerButton:
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.googleButton:
                        googleSignIn();
                        break;
                    default:
                        break;
                }
            }
        };
        /*User user;
        user = new User.Builder()
                .firstname("Csirke")
                .lastname("Csirke 2")
                .emailaddress("anyadpisakja@szopdleannamari.anyad")
                .password("NEMOTOM")
                .telephone("0987654321")
                .bulid();*/
        mEmailField = findViewById(R.id.emailEditText);
        mPasswordField = findViewById(R.id.passwordEditText);
        mSignInButton = findViewById(R.id.loginButton);
        mRegisterButton = findViewById(R.id.registerButton);
        mGoogleButton = findViewById(R.id.googleButton);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Click listeners
        //Mivel ugyanazt az onclick esemenyt tobb view is fogja hasznalni ezert erdemesebb a listenert kulon fieldkent (adattag)
        //vagy kulon statikus osztalyba deklaralni

        mSignInButton.setOnClickListener(buttonListener);
        mRegisterButton.setOnClickListener(buttonListener);
        mGoogleButton.setOnClickListener(buttonListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // mFirebaseInstance.setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();//azt az egy referenciat a singleton osztalyra ,ezzel ferek hozza userhez ...
        mDatabaseReference = mFirebaseInstance.getReference();//ezt kell hasznali ha az adatbazist akarom hasznalni

    }

    //google login
    private void googleSignIn() {
        Intent signinintent = googleSignInClient.getSignInIntent();
        startActivityForResult(signinintent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> taskboci = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = taskboci.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewGoogleUser(user, acct);

                            //userDatabaseReference.child(user.getUid()).child("phonenumber").setValue(user.getPhoneNumber());//telefonszam
                            //Ennel a sornal az tortenik , hogy mikor sikeresen belep a user akkor megjeleniti az esemenyeket
                            onAuthSuccess();

                        } else {
                            Toast.makeText(getApplicationContext(), "Sign In Google Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    ///eddig tart a google login
    private void signIn() {
        //Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();//tolto jelzesu ion vagy mi
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        Log.d(TAG, "signIn:email+password: " + email + "  " + password);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        //hideProgressDialog();
                        if (task.isSuccessful()) {
                            onAuthSuccess();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess() {
        Toast.makeText(getApplicationContext(), "Sign in succesful",
                Toast.LENGTH_SHORT).show();

        // Go to the EventActivity
        startActivity(new Intent(getApplicationContext(), EventActivity.class));//ebbol ebbe
    }


    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    private void writeNewGoogleUser(FirebaseUser user, GoogleSignInAccount acct) {
        DatabaseReference userDatabaseReference = mDatabaseReference.child("users").child(user.getUid());
        userDatabaseReference.child("emailAddress").setValue(acct.getEmail());//emailcim
        userDatabaseReference.child("lastName").setValue(acct.getFamilyName());//vezeteknev
        userDatabaseReference.child("firstName").setValue(acct.getGivenName());//keresztnev

    }
}
