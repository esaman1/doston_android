package com.doston.shortvideo.UserDiscover_Classes;

public class Doston_Discover_user_Model {
    String fb_id;
    String username;
    String first_name;
    String last_name;
    String profile_pic;
    String videoCount;
    String followersCount;

    public Doston_Discover_user_Model() {
    }
//test
    public Doston_Discover_user_Model(String fb_id, String username, String first_name, String last_name, String profile_pic, String videoCount, String followersCount) {
        this.fb_id = fb_id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_pic = profile_pic;
        this.videoCount = videoCount;
        this.followersCount = followersCount;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }
}
