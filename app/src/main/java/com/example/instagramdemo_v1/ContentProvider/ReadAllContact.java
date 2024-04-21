package com.example.instagramdemo_v1.ContentProvider;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.example.instagramdemo_v1.Model.User;

import java.util.ArrayList;

public class ReadAllContact {
    private final Activity activity;

    public ReadAllContact(Activity activity) {
        this.activity = activity;
    }
    public ArrayList<User> getAllContact() {
        ArrayList<User> listContact = new ArrayList<>();
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
        };
        Cursor cursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , projection,null,null,null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String number = cursor.getString(2);
                String thumbnailUri = "https://firebasestorage.googleapis.com/v0/b/insdemo-f2f43.appspot.com/o/Android.jpg?alt=media&token=4f4300bf-776a-447e-bfaf-e0c866ba181f";
                if (thumbnailUri == null) {
                    thumbnailUri = "https://firebasestorage.googleapis.com/v0/b/insdemo-f2f43.appspot.com/o/Android.jpg?alt=media&token=4f4300bf-776a-447e-bfaf-e0c866ba181f";

                }

                User c = new User(thumbnailUri, name, number, "password", id + " ", "lass message", number);
                listContact.add(c);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return listContact;
    }
}
