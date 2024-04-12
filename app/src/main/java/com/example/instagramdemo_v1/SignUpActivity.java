package com.example.instagramdemo_v1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagramdemo_v1.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText txtUsernameSign, txtEmailSign, txtPasswordSign, txtPasswordConfirmSign;
    Button btnSignUpConfirm, btnBackToLogin;
    ImageView imgAvt;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    Uri avtUri;
    String avturi;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        txtUsernameSign = findViewById(R.id.txtUsernameSign);
        txtEmailSign = findViewById(R.id.txtEmailSign);
        txtPasswordSign = findViewById(R.id.txtPasswordSign);
        txtPasswordConfirmSign = findViewById(R.id.txtPasswordConfirmSign);
        btnSignUpConfirm = findViewById(R.id.btnSignUpConfirm);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        imgAvt = findViewById(R.id.imgAvt);
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"), 10);

            }
        });
        btnSignUpConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtUsernameSign.getText().toString();
                String email = txtEmailSign.getText().toString();
                String password = txtPasswordSign.getText().toString();
                String passwordcf = txtPasswordConfirmSign.getText().toString();
                String status = "Hey I'm using this application";
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordcf)) {
                    Toast.makeText(SignUpActivity.this, "Please enter valid information", Toast.LENGTH_LONG).show();
                } else if (!email.matches(emailPattern)) {
                    Toast.makeText(SignUpActivity.this, "Please enter email in correct format", Toast.LENGTH_LONG).show();
                } else if(password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "More than 6 characters in Password", Toast.LENGTH_LONG).show();
                } else if(!password.equals(passwordcf)) {
                    Toast.makeText(SignUpActivity.this, "Password confirm is not correct", Toast.LENGTH_LONG).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = firebaseDatabase.getReference().child("user").child(id);
                                StorageReference storageReference = firebaseStorage.getReference().child("upload").child(id);
                                if(avtUri != null) {
                                    storageReference.putFile(avtUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                          if(task.isSuccessful()) {
                                              storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                  @Override
                                                  public void onSuccess(Uri uri) {
                                                        avturi = uri.toString();
                                                        User user = new User(avturi, name, email, password, id, "", status );
                                                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<Void> task) {
                                                              if(task.isSuccessful()) {
                                                                  Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                                  startActivity(intent);
                                                                  Toast.makeText(SignUpActivity.this, "Sign Up Successful !!!", Toast.LENGTH_LONG).show();
                                                                  finish();
                                                              }
                                                          }
                                                      });
                                                  }
                                              });
                                          }
                                        }
                                    });
                                } else {
                                    String status = "Hey I'm using this application";
                                    avturi = "https://firebasestorage.googleapis.com/v0/b/insdemo-f2f43.appspot.com/o/picture.jpg?alt=media&token=413560fe-7b61-49f8-8d1d-fd38d179f11f";
                                    User user = new User(avturi, name, email, password, id, "", status);
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                                Toast.makeText(SignUpActivity.this, "Sign Up Successful !!!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10) {
            if(data != null) {
                avtUri = data.getData();
                imgAvt.setImageURI(avtUri);
            }
        }
    }
}