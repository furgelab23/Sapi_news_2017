package com.example.a2017_09_13.sapi_news_2017;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2017_09_13.sapi_news_2017.model.User;
import com.example.a2017_09_13.sapi_news_2017.permissions.permissions;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    private TextView tv;
    //facebook
    CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);//erre a activityre ra kapcsolja ezt a viewt hozza rendeli

        //jogosultsag keres
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.permissionRequest(this, permissions.permissions, permissions.PERMISSION_KEY);
        }



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
                    case R.id.tv_forgot:
                        Intent i2 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                        startActivity(i2);
                        break;

                    default:
                        break;
                }
            }
        };
        mEmailField = findViewById(R.id.emailEditText);
        mPasswordField = findViewById(R.id.passwordEditText);
        mSignInButton = findViewById(R.id.loginButton);
        mRegisterButton = findViewById(R.id.registerButton);
        mGoogleButton = findViewById(R.id.googleButton);
        tv = findViewById(R.id.tv_forgot);

        //facebook
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);

        loginWithFB();//facebook belepes

        ///////////////////////////////////////////////////////////////////////////////////////////////////
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
        tv.setOnClickListener(buttonListener);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseInstance = FirebaseDatabase.getInstance();
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
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
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
        userDatabaseReference.child("phoneNumber").setValue(user.getPhoneNumber());//phone

    }



    private void loginWithFB(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Intent i = new Intent(LoginActivity.this,EventActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Sign In success= "+loginResult,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Sign In Failed",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error= "+error,
                        Toast.LENGTH_LONG).show();
            }
        });
    }


}
