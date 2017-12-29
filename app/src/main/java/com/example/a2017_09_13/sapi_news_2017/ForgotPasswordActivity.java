package com.example.a2017_09_13.sapi_news_2017;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailField;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mEmailField = findViewById(R.id.et_email);

        Button btn1 = findViewById(R.id.btn_verirification);
        btn1.setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_verirification:
                String email = mEmailField.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmailField.setError("Required");
                    return;
                }

                Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG)
                        .show();

                mAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ForgotPasswordActivity.this, "Reset email sent!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ForgotPasswordActivity.this, "Failed to send request email", Toast.LENGTH_SHORT).show();

                            }
                        });
                break;

            default:
                break;
        }
    }
}
