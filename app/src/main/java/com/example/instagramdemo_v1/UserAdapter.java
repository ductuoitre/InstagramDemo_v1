package com.example.instagramdemo_v1;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramdemo_v1.Model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {

    Context MainScreen;

   ArrayList<User> userArrayList;

    public UserAdapter(MainScreenActivity mainScreenActivity, ArrayList<User> usersArrayList) {
        this.MainScreen=mainScreenActivity;
        this.userArrayList=usersArrayList;
    }


    @NonNull
    @Override
    public UserAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(MainScreen).inflate(R.layout.user_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.Viewholder holder, int position) {

        User users = userArrayList.get(position);
        holder.username.setText(users.getUserName());
        holder.userstatus.setText(users.getStatus());
        Picasso.get().load(users.getAvt()).into(holder.userimg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(MainScreen, ChatWin.class);
                    intent.putExtra("nameeee",users.getUserName());
                    intent.putExtra("reciverImg",users.getAvt());
                    intent.putExtra("uid",users.getUserId());
                    MainScreen.startActivity(intent);
                } catch ( Exception E){
                    Log.d("MainScreenActivity", E.getMessage());
                }


            }

//        holder.camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openCamera();
//            }
        });



    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        MainScreen.startActivity(intent);
    }

    @Override
    public int getItemCount() {
       return  userArrayList.size();
    }

    public void setUser(ArrayList<User> filteruser) {
             userArrayList=filteruser;

        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        CircleImageView userimg,camera;
        TextView username;
        TextView userstatus;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.avt);
            username = itemView.findViewById(R.id.username);
            userstatus = itemView.findViewById(R.id.userstatus);
            camera=itemView.findViewById(R.id.camera);

        }
    }
}
