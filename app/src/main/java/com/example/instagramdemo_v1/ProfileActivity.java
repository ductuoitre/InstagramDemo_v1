package com.example.instagramdemo_v1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramdemo_v1.Adapter.InstaFeedAdapter;
import com.example.instagramdemo_v1.Home.HomeActivity;
import com.example.instagramdemo_v1.Model.InstaFeedModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    TextView txtNameProfile, txtEmailProfile;
    ImageView avtProfile;
    RecyclerView myRecyclerView;
    InstaFeedAdapter feedAdapter;
    List<InstaFeedModel> feedModelList = new ArrayList<>();
    String name, email, photoUrl;
    Button btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtEmailProfile = findViewById(R.id.txtEmailProfile);
        txtNameProfile = findViewById(R.id.txtNameProfile);
        avtProfile = findViewById(R.id.avtProfile);
        myRecyclerView = findViewById(R.id.myRecyclerView);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("name", name);
                b.putString("email", email);
                b.putString("photoUrl", photoUrl);
                intent.putExtras(b);
//        Toast.makeText(ProfileActivity.this, name + "\n" + email + "\n" + photoUrl, Toast.LENGTH_LONG).show();
                startActivityForResult(intent, 1);
            }
        });
        loadData();
    }

    public void outprofile_onClick(View view) {
        Intent backtoHome = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(backtoHome);
    }

    public void logout_onClick(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
        dialog.setTitle("Xác nhận");
        dialog.setMessage("Bạn có muốn đăng xuất ?");
        dialog.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        dialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.create();
        dialog.show();
    }

    public void loadData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lấy dữ liệu từ DataSnapshot và thực hiện các thao tác cần thiết
                    name = snapshot.child("userName").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                    photoUrl = snapshot.child("avt").getValue(String.class);

                    // Hiển thị thông tin lấy được lên giao diện người dùng
                    txtNameProfile.setText(name);
                    txtEmailProfile.setText(email);

                    // Tải ảnh đại diện nếu có
                    if (photoUrl != null) {
                        Glide.with(ProfileActivity.this).load(photoUrl).error(R.drawable.ic_launcher_foreground).into(avtProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình lấy dữ liệu
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadData();
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }

    }
}