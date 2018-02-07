package com.example.user.simplechatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText musername, mpassword, memail;
    Button mSubmit;
    Toolbar mtoolbar;
    ProgressDialog mprogressdialog;
    DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        musername = findViewById(R.id.username);
        memail = findViewById(R.id.email_address);
        mpassword = findViewById(R.id.password);
        mSubmit = findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        mtoolbar = findViewById(R.id.registration_page_header);
        mprogressdialog = new ProgressDialog(this);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = musername.getText().toString();
                String email = memail.getText().toString();
                String password = mpassword.getText().toString();

                if(!TextUtils.isEmpty(username) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    mprogressdialog.setTitle("Registering User");
                    mprogressdialog.setMessage("Please wait while we create your account");
                    mprogressdialog.show();
                    mprogressdialog.setCanceledOnTouchOutside(false);
                    register_user(username, email, password);
                }
            }
        });
    }

    public void register_user(final String displayName, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = firebaseUser.getUid();
                            mdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name",displayName);
                            userMap.put("status","hii there! I am using simple chat app");
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");
                             mdatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful()){
                                         mprogressdialog.dismiss();
                                         Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                         startActivity(intent);
                                         finish();
                                     }
                                 }
                             });



                        } else {
                            mprogressdialog.hide();
                            Toast.makeText(RegisterActivity.this, "Couldn't create your account please try again with different credentials",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


}
