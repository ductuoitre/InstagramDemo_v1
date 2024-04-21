package com.example.instagramdemo_v1.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramdemo_v1.Model.StoryModel;
import com.example.instagramdemo_v1.R;
import com.example.instagramdemo_v1.UploadStoryActivity;
import com.thbd.tools.Tools;

import java.util.List;


public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    private static int ADD_STORY_TYPE=0;
    private static int ALL_STORY_TYPE=1;
    List<StoryModel> modelList;

    public StoryAdapter(List<StoryModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ADD_STORY_TYPE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item_add, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StoryModel model = modelList.get(position);

        int viewType =  getItemViewType(position);
        if (viewType == ADD_STORY_TYPE){
            String uid = model.getUid();
            String stories_img= model.getStory_img();
            holder.setAddStory(uid, stories_img);
        }else{
            String uid = model.getUid();
            String sid = model.getSid();
            String name = model.getName();
            String stories_img= model.getStory_img();
            holder.setStory(uid,sid,name,stories_img);
        }


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position ==0) return 0;
        else return 1;


    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView profile_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title =  itemView.findViewById(R.id.title_id);
            profile_img =  itemView.findViewById(R.id.userProfile_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() == 0){
                        Intent intent = new Intent(itemView.getContext(), UploadStoryActivity.class);
                        itemView.getContext().startActivity(intent);
                    }else{

                    }
                }
            });
        }

        public void setAddStory(String uid, String stories_img) {
            title.setText("Add Story");
            Tools.displayImageCircle(itemView.getContext(), profile_img, stories_img );
        }

        public void setStory(String uid, String sid, String name, String stories_img) {
            title.setText(name);
            Tools.displayImageCircle(itemView.getContext(), profile_img, stories_img );

        }
    }

}
