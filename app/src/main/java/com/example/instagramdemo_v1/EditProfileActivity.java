package com.example.instagramdemo_v1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    TextInputEditText txtEmailChange, txtUsernameChange;
    ImageView imgChange;
    String email, name, avt;
    Uri avtUri;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firebaseStorage = FirebaseStorage.getInstance();
        txtEmailChange = findViewById(R.id.txtEmailChange);
        txtUsernameChange = findViewById(R.id.txtUsernameChange);
        imgChange = findViewById(R.id.imgChange);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            name = b.getString("name");
            email = b.getString("email");
            avt = b.getString("photoUrl");
        }

        txtEmailChange.setText(email);
        txtUsernameChange.setText(name);

        if (avt != null) {
            Glide.with(EditProfileActivity.this)
                    .load(avt)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imgChange);
        }
        imgChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), 10);
            }
        });
    }

    private void updateData(String name, String photoUrl) {
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("userName", name);
        userData.put("avt", photoUrl);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
            userRef.updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void btnCancelEdit_onClick(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            avtUri = data.getData();
            imgChange.setImageURI(avtUri);
        }

    }


    public void btnSaveEdit_onClick(View view) {
        String nName = txtUsernameChange.getText().toString();
        String nEmail = txtEmailChange.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference storageReference = firebaseStorage.getReference().child("upload").child(user.getUid());
        if (avtUri != null) {
            storageReference.putFile(avtUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                // Get the download URL from the task snapshot
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        avt = uri.toString();
                                        // Update user's profile with the new photo URL
                                        updateData(nName, avt);
                                        // Proceed with finishing the activity
                                        Intent resultIntent = new Intent();
                                        setResult(Activity.RESULT_OK, resultIntent);
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // If no new image is selected, just update the user's profile with the new name
            updateData(nName, avt);
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

}
