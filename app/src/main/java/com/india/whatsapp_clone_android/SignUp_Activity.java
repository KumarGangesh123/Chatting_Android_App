package com.india.whatsapp_clone_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.india.whatsapp_clone_android.databinding.ActivitySignUpBinding;

import Models.SignUpModel;

public class SignUp_Activity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(SignUp_Activity.this);
        progressDialog.setTitle("Creating Whatsapp Account");
        progressDialog.setMessage("We're creating your account");

        getSupportActionBar().hide();

        findViewById(R.id.textView_signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignIn_Activity.class));
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();

        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.editSignInEmail.getText().toString().isEmpty()){
                    binding.editSignInEmail.setError("Enter UserName");
                    return;
                }

                if(binding.editSignUpEmail.getText().toString().isEmpty()){
                    binding.editSignUpEmail.setError("Enter Email");
                    return;
                }

                if(binding.editSignInPassword.getText().toString().isEmpty()){
                    binding.editSignInPassword.setError("Enter Password");
                    return;
                }

                progressDialog.show();

                mAuth.createUserWithEmailAndPassword( binding.editSignUpEmail.getText().toString() , binding.editSignInPassword.getText().toString() ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            SignUpModel singUp = new SignUpModel(binding.editSignInEmail.getText().toString(),binding.editSignUpEmail.getText().toString() , binding.editSignInPassword.getText().toString());

                            String id = task.getResult().getUser().getUid();

                            firebaseDatabase.getReference().child("WhatsUsers").child(id).setValue(singUp);

                            startActivity(new Intent(SignUp_Activity.this,MainActivity.class));
                            finish();

                            Toast.makeText(SignUp_Activity.this, "User created sucessfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SignUp_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


    }
}