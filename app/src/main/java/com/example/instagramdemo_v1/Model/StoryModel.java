package com.example.instagramdemo_v1.Model;

public class StoryModel {
    String uid, sid, name;
    int  type;

    public String getStory_img() {
        return story_img;
    }

    public void setStory_img(String story_img) {
        this.story_img = story_img;
    }

    String story_img;

    public StoryModel(int type,String uid, String sid, String name, String story_img) {
        this.type = type;
        this.uid = uid;
        this.sid = sid;
        this.name = name;
        this.story_img = story_img;

    }

    @Override
    public String toString() {
        return "StoryModel{" +
                "uid='" + uid + '\'' +
                ", sid='" + sid + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", story_img='" + story_img + '\'' +
                '}';
    }

    public StoryModel(int type, String uid, String story_img) {
        this.uid = uid;
        this.type = type;
    }

    public StoryModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
