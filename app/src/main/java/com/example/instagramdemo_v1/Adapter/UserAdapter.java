package com.example.instagramdemo_v1.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramdemo_v1.ChatWin;
import com.example.instagramdemo_v1.MainScreenActivity;
import com.example.instagramdemo_v1.Model.User;
import com.example.instagramdemo_v1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        FirebaseUser userData = FirebaseAuth.getInstance().getCurrentUser();
        User users = userArrayList.get(position);
        holder.username.setText(users.getUserName());
//        holder.lastMessage.setText(users.getLastMessage());
        Picasso.get().load(users.getAvt()).into(holder.userimg);
        if (users.getUserId().equals(userData.getUid())) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = 0; // Thiết lập chiều cao của item view là 0
            holder.itemView.setLayoutParams(params);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(users.getUserId());

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // ID tồn tại trong Firebase, mở ChatWin
                            Intent i = new Intent(MainScreen, ChatWin.class);
                            i.putExtra("nameeee", users.getUserName());
                            i.putExtra("reciverImg", users.getAvt());
                            i.putExtra("uid", users.getUserId());
                            MainScreen.startActivity(i);
                        } else {
                            Intent smsintent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto: " + users.getEmail()));
                            if(ActivityCompat.checkSelfPermission(MainScreen, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((Activity) MainScreen, new String[] {Manifest.permission.SEND_SMS}, 1);
                                return;
                            }
                            MainScreen.startActivity(smsintent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
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
        TextView lastMessage;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.avtProfile);
            username = itemView.findViewById(R.id.username);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            camera=itemView.findViewById(R.id.camera);

        }
    }

}
