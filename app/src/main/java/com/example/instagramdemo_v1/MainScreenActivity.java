package com.example.instagramdemo_v1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramdemo_v1.Adapter.UserAdapter;
import com.example.instagramdemo_v1.ContentProvider.ReadAllContact;
import com.example.instagramdemo_v1.Home.HomeActivity;
import com.example.instagramdemo_v1.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainScreenActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_WRITE_CONTACTS = 101;
    private ImageView outmain;
    private FirebaseAuth auth;
    private RecyclerView mainUserRecyclerView;
    private UserAdapter adapter;
    private FirebaseDatabase database;
    private ArrayList<User> usersArrayList;
    private EditText txt_search;
    ReadAllContact cp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        outmain = findViewById(R.id.outmain);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = database.getReference().child("user");

        usersArrayList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    usersArrayList.add(user);
                }
                sortUsersByUsername();
                adapter.notifyDataSetChanged();
//                getLastMessages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserAdapter(MainScreenActivity.this, usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(MainScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish this activity to prevent returning back to it
        }
        showContact();
        search();
        outmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainScreenActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });


    }

    private void search() {
        txt_search = findViewById(R.id.editTextText);

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filteredUserSearch(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filteredUserSearch(String searchText) {
        ArrayList<User> filteredUsers = new ArrayList<>();
        for (User user : usersArrayList) {
            if (user.getUserName().toLowerCase().contains(searchText)) {
                filteredUsers.add(user);
            }
        }
        adapter.setUser(filteredUsers);
    }
    private void showContact() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else {
            cp = new ReadAllContact(this);
            ArrayList<User> newContacts = cp.getAllContact();
            if (newContacts != null && !newContacts.isEmpty()) {
                usersArrayList.addAll(newContacts);
                adapter.notifyDataSetChanged(); // Thông báo cho Adapter rằng dữ liệu đã thay đổi
            } else {
                // Hiển thị thông báo lỗi
                Toast.makeText(MainScreenActivity.this, "Không thể đọc danh bạ", Toast.LENGTH_SHORT).show();
            }
        }
    }
//    private void getLastMessages() {
//        DatabaseReference chatReference = database.getReference().child("chats");
//        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
//                    for (DataSnapshot userChatSnapshot : chatSnapshot.getChildren()) {
//                        String lastMessage = ""; // Initialize last message
//                        long latestTimeStamp = 0; // Initialize timestamp of the latest message
//                        // Loop through each message in the chat
//                        for (DataSnapshot messageSnapshot : userChatSnapshot.getChildren()) {
//                            // Get message details
//                            String message = messageSnapshot.child("message").getValue(String.class);
//                            long timeStamp = messageSnapshot.child("timeStamp").getValue(Long.class);
//                            // Check if this message is the latest one
//                            if (timeStamp > latestTimeStamp) {
//                                lastMessage = message;
//                                latestTimeStamp = timeStamp;
//                            }
//                        }
//                        // Get sender and receiver UIDs
//                        String senderUid = userChatSnapshot.child("senderid").getValue(String.class);
////                        String receiverUid = userChatSnapshot.child("receiverUid").getValue(String.class);
//                        // Find the corresponding user and update lastMessage
//                        for (User user : usersArrayList) {
//                            if (user.getUserId().equals(senderUid) ) {
//                                user.setLastMessage(lastMessage);
//                                break;
//                            }
//                        }
//                    }
//                }
//                adapter.notifyDataSetChanged(); // Notify RecyclerView adapter after updating lastMessage
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
//    }
private void sortUsersByUsername() {
    Collections.sort(usersArrayList, new Comparator<User>() {
        @Override
        public int compare(User user1, User user2) {
            return user1.getUserName().compareToIgnoreCase(user2.getUserName());
        }
    });
}


}
