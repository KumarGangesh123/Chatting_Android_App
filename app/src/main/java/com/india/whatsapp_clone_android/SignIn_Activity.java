package com.india.whatsapp_clone_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.india.whatsapp_clone_android.databinding.ActivitySignInBinding;

public class SignIn_Activity extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivitySignInBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(SignIn_Activity.this);
        progressDialog.setTitle("Logging in Whatsapp account");
        progressDialog.setMessage("Please wait, while we are log in your account");

        findViewById(R.id.textView_signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp_Activity.class));
                finish();
            }
        });

        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.editSignInEmail.getText().toString().isEmpty()){
                    binding.editSignInEmail.setError("Enter Email");
                    return;
                }

                if(binding.editSignInPassword.getText().toString().isEmpty()){
                    binding.editSignInPassword.setError("Enter Password");
                    return;
                }

                progressDialog.show();

                auth.signInWithEmailAndPassword(binding.editSignInEmail.getText().toString(), binding.editSignInPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(SignIn_Activity.this, "User found", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignIn_Activity.this,MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(SignIn_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });




            }
        });

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(SignIn_Activity.this,MainActivity.class));
            finish();
        }

    }
}