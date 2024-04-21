package com.example.instagramdemo_v1.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramdemo_v1.Adapter.InstaFeedAdapter;
import com.example.instagramdemo_v1.Adapter.StoryAdapter;
import com.example.instagramdemo_v1.AddFeedActivity;
import com.example.instagramdemo_v1.LoginActivity;
import com.example.instagramdemo_v1.MainScreenActivity;
import com.example.instagramdemo_v1.Model.InstaFeedModel;
import com.example.instagramdemo_v1.Model.StoryModel;
import com.example.instagramdemo_v1.R;
import com.example.instagramdemo_v1.SignUpActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import omari.hamza.storyview.StoryView;


public class HomeActivity extends AppCompatActivity {
    StoryAdapter adapter;
    private static int ADD_STORY_TYPE=0;
    private static int ALL_STORY_TYPE=1;

    List<StoryModel> modelList =  new ArrayList<>();

    InstaFeedAdapter feedAdapter;
    List<InstaFeedModel> feedModelList =  new ArrayList<>();


    RecyclerView recyclerView, feedRecyclerView;

        DatabaseReference databaseReference;
        ValueEventListener eventListener;
        SearchView searchView;
        ImageView imgChat;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            recyclerView = findViewById(R.id.story_recyclerView_id);
            feedRecyclerView = findViewById(R.id.recyclerView_id);
            imgChat= findViewById(R.id.btnChat);
            LinearLayout homeBtn = findViewById(R.id.homeBtn);
            LinearLayout addBtn = findViewById(R.id.addBtn);
            LinearLayout searchBtn = findViewById(R.id.searchBtn);
            imgChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, MainScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, AddFeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progess_layout);
            AlertDialog dialog = builder.create();
            dialog.show();
            modelList = new ArrayList<>();
            databaseReference = FirebaseDatabase.getInstance().getReference("Story");
            dialog.show();

            adapter = new StoryAdapter(modelList);
            recyclerView.setAdapter(adapter);

            databaseReference = FirebaseDatabase.getInstance().getReference("Story");
            eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    modelList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        StoryModel storyModel = itemSnapshot.getValue(StoryModel.class);
                        modelList.add(storyModel);
                        Log.d("DataSnapshot", "Key: " + itemSnapshot.getKey() + ", Value: " + storyModel.toString());
                    }

                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    dialog.dismiss();
                }
            });




        /// setup feed modelList

        feedModelList.add(new InstaFeedModel(R.drawable.profile2,"Jack","USA","https://marketplace.canva.com/EAFH_oMBen8/1/0/900w/canva-gray-and-white-asthetic-friend-instagram-story-C5KpyJG5MHA.jpg", "Hello, have a nice day","3", "10/7/2023"));
        feedModelList.add(new InstaFeedModel(R.drawable.profile4,"Alina","USA","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMdl3HTGdSrPPtE1intiEqAGncJF0-HAyL6VpjWlBNG_wsroaBdglQkhczbEJ6rt5MeCg&usqp=CAU", "Hello, have a nice day","8", "18/7/2023"));
        feedModelList.add(new InstaFeedModel(R.drawable.profile3,"Mariya","USA","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6bQFqMhQmg9hJ-FA5xUUrQidHgQqZC5Nktw&usqp=CAU", "Hello, have a nice day","13", "1/7/2023"));
        feedModelList.add(new InstaFeedModel(R.drawable.profile2,"Jack","USA","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTaYFjYA4n9jA30XMr0YMswgufCFTDoGO2-f0r1gZakb6badmzDJngUab4bCLGFCGTBAnU&usqp=CAU", "Hello, have a nice day","4", "11/7/2023"));
        feedModelList.add(new InstaFeedModel(R.drawable.profile4,"Alina","USA","https://marketplace.canva.com/EAFH_oMBen8/1/0/900w/canva-gray-and-white-asthetic-friend-instagram-story-C5KpyJG5MHA.jpg", "Hello, have a nice day","17", "17/7/2023"));

        feedModelList= new ArrayList<>();
        feedAdapter = new InstaFeedAdapter(feedModelList);
        feedRecyclerView.setAdapter(feedAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Feeds");
            eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    feedModelList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        InstaFeedModel instaFeedModel = itemSnapshot.getValue(InstaFeedModel.class);
                        feedModelList.add(instaFeedModel);
                        Log.d("DataSnapshot", "Key: " + itemSnapshot.getKey() + ", Value: " + instaFeedModel.toString());
                    }

                    feedAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    dialog.dismiss();
                }
            });


    }
}