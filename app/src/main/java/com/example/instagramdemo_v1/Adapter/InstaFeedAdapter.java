package com.example.instagramdemo_v1.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramdemo_v1.Model.InstaFeedModel;
import com.example.instagramdemo_v1.R;
import com.thbd.tools.Tools;

import java.util.List;


public class InstaFeedAdapter extends RecyclerView.Adapter<InstaFeedAdapter.ViewHolder> {

    List<InstaFeedModel> modelList;

    public InstaFeedAdapter(List<InstaFeedModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insta_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       InstaFeedModel model = modelList.get(position);

        int image = model.getImage();
        String username = model.getUsername();
        String  location = model.getLocation();
        String feed_img = model.getFeed_img();
        String status = model.getStatus();
        String comment_count = model.getComment_count();
        String  date = model.getDate();

        holder.setItems(image, username,location ,feed_img ,status,comment_count,date );

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView userProfile,userProfile2,imageView;
        TextView username_id,location_id,status_id,comment_count_id,date_id;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            userProfile = itemView.findViewById(R.id.userProfile_id);
            userProfile2 = itemView.findViewById(R.id.userProfile2_id);
            username_id = itemView.findViewById(R.id.username_id);
            location_id = itemView.findViewById(R.id.location_id);
            status_id = itemView.findViewById(R.id.status_id);
            comment_count_id = itemView.findViewById(R.id.comment_count_id);
            date_id = itemView.findViewById(R.id.date_id);


        }

        public void setItems(int image, String username, String location, String feed_img,
                             String status, String comment_count, String date) {

//            ImageView userProfile,userProfile2,imageView;
//            TextView username_id,location_id,status_id,comment_count_id,date_id;

            Bitmap myLogo = BitmapFactory.decodeResource(itemView.getContext().getResources(), image);
            Tools.displayImageCircle(itemView.getContext(), userProfile, myLogo );
            Tools.displayImageCircle(itemView.getContext(), userProfile2, myLogo );
            Tools.displayImage(itemView.getContext(), imageView, feed_img );


            username_id.setText(username.trim());
            location_id.setText(location.trim());
            status_id.setText(status.trim());
            date_id.setText(date.trim());
            comment_count_id.setText("View all "+comment_count+" comments");



        }
    }
}
