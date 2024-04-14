package com.example.instagramdemo_v1.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramdemo_v1.Adapter.InstaFeedAdapter;
import com.example.instagramdemo_v1.Adapter.StoryAdapter;
import com.example.instagramdemo_v1.LoginActivity;
import com.example.instagramdemo_v1.MainScreenActivity;
import com.example.instagramdemo_v1.Model.InstaFeedModel;
import com.example.instagramdemo_v1.Model.StoryModel;
import com.example.instagramdemo_v1.R;
import com.example.instagramdemo_v1.SignUpActivity;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    private static int ADD_STORY_TYPE=0;
    private static int ALL_STORY_TYPE=1;

    StoryAdapter adapter;
    List<StoryModel> modelList =  new ArrayList<>();

    InstaFeedAdapter feedAdapter;
    List<InstaFeedModel> feedModelList =  new ArrayList<>();


    RecyclerView recyclerView, feedRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView =  findViewById(R.id.story_recyclerView_id);
        feedRecyclerView =  findViewById(R.id.recyclerView_id);

        LinearLayout homeBtn= findViewById(R.id.homeBtn);
        LinearLayout searchBtn= findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainScreenActivity.class);
                ImageView imgSearch= findViewById(R.id.imgSearch);
                imgSearch.setImageResource(R.drawable.bottom_btn21);
                startActivity(intent);
                finish();
            }
        });


        modelList.add(new StoryModel(ADD_STORY_TYPE, "1", R.drawable.profile1));
        modelList.add(new StoryModel(ALL_STORY_TYPE, "2","10","Mariya", R.drawable.profile3));
        modelList.add(new StoryModel(ALL_STORY_TYPE, "3","11","Jack", R.drawable.profile2));
        modelList.add(new StoryModel(ALL_STORY_TYPE, "4","13","Alina", R.drawable.profile4));
        modelList.add(new StoryModel(ALL_STORY_TYPE, "2","10","Mariya", R.drawable.profile3));
        modelList.add(new StoryModel(ALL_STORY_TYPE, "3","11","Jack", R.drawable.profile2));
        modelList.add(new StoryModel(ALL_STORY_TYPE, "4","13","Alina", R.drawable.profile4));

        adapter = new StoryAdapter(modelList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        /// setup feed modelList

        feedModelList.add(new InstaFeedModel(R.drawable.profile2,"Jack","USA","https://marketplace.canva.com/EAFH_oMBen8/1/0/900w/canva-gray-and-white-asthetic-friend-instagram-story-C5KpyJG5MHA.jpg", "Hello, have a nice day","3", "10/7/2023"));
        feedModelList.add(new InstaFeedModel(R.drawable.profile4,"Alina","USA","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMdl3HTGdSrPPtE1intiEqAGncJF0-HAyL6VpjWlBNG_wsroaBdglQkhczbEJ6rt5MeCg&usqp=CAU", "Hello, have a nice day","8", "18/7/2023"));
        feedModelList.add(new InstaFeedModel(R.drawable.profile3,"Mariya","USA","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6bQFqMhQmg9hJ-FA5xUUrQidHgQqZC5Nktw&usqp=CAU", "Hello, have a nice day","13", "1/7/2023"));
        feedModelList.add(new InstaFeedModel(R.drawable.profile2,"Jack","USA","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTaYFjYA4n9jA30XMr0YMswgufCFTDoGO2-f0r1gZakb6badmzDJngUab4bCLGFCGTBAnU&usqp=CAU", "Hello, have a nice day","4", "11/7/2023"));
        feedModelList.add(new InstaFeedModel(R.drawable.profile4,"Alina","USA","https://marketplace.canva.com/EAFH_oMBen8/1/0/900w/canva-gray-and-white-asthetic-friend-instagram-story-C5KpyJG5MHA.jpg", "Hello, have a nice day","17", "17/7/2023"));


        feedAdapter = new InstaFeedAdapter(feedModelList);
        feedRecyclerView.setAdapter(feedAdapter);
        feedAdapter.notifyDataSetChanged();


    }
}