package com.example.instagramdemo_v1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagramdemo_v1.Home.HomeActivity;
import com.example.instagramdemo_v1.Model.InstaFeedModel;
import com.example.instagramdemo_v1.Model.StoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

public class AddFeedActivity extends AppCompatActivity {
        ImageView uploadImage;
        ImageView saveButton;
        EditText textpost, uploadDesc, uploadLang;
        String imageURL;
        Uri uri;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_feed);
            textpost= findViewById(R.id.textpost);
            uploadImage = findViewById(R.id.uploadImage);
            saveButton = findViewById(R.id.saveButton);
            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK){
                                Intent data = result.getData();
                                uri = data.getData();
                                uploadImage.setImageURI(uri);
                            } else {
                                Toast.makeText(com.example.instagramdemo_v1.AddFeedActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
            uploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent photoPicker = new Intent(Intent.ACTION_PICK);
                    photoPicker.setType("image/*");
                    activityResultLauncher.launch(photoPicker);
                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveData();

                    Intent intent = new Intent(AddFeedActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            });
        }
        public void saveData(){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Feeds")
                    .child(uri.getLastPathSegment());
            AlertDialog.Builder builder = new AlertDialog.Builder(com.example.instagramdemo_v1.AddFeedActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progess_layout);
            AlertDialog dialog = builder.create();
            dialog.show();
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageURL = urlImage.toString();
                    uploadData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }
        public void uploadData(){

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userId = user.getUid();
            String userName= user.getEmail();
            Toast.makeText(this, userName.toString(), Toast.LENGTH_SHORT).show();
            String sId= UUID.randomUUID().toString();
            int image= R.drawable.ic_launcher_foreground;
            String status= textpost.getText().toString();
            String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            InstaFeedModel feedModel = new InstaFeedModel(R.drawable.profile2, userName, "USA", imageURL, status, "3", currentDate);
            FirebaseDatabase.getInstance().getReference("Feeds").child(currentDate)
                    .setValue(feedModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(com.example.instagramdemo_v1.AddFeedActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(com.example.instagramdemo_v1.AddFeedActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

