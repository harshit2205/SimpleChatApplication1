package com.example.user.simplechatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Toolbar mtoolbar;
    EditText memail, mpassword;
    Button mlogIn;
    ProgressDialog mprogressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mtoolbar = findViewById(R.id.log_in_activity_header);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Log in tab");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        memail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password);
        mlogIn = findViewById(R.id.log_in);
        mprogressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        mlogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = memail.getText().toString();
                String password = mpassword.getText().toString();
                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    mprogressDialog.setTitle("Logging In");
                    mprogressDialog.setMessage("Please wait while we log you to your account");
                    mprogressDialog.setCanceledOnTouchOutside(false);
                    mprogressDialog.show();
                    logInUser(email, password);
                }
            }
        });
    }

    private void logInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            mprogressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            mprogressDialog.hide();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
