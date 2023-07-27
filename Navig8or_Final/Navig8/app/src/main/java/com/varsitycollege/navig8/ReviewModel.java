package com.varsitycollege.navig8;
//---Code attribution---
//Author:MyOnlineTrainingHub
//Title:How to build Interactive Excel Dashboards that Update with ONE CLICK!
//Date:17/11/2022
//Link:https://www.youtube.com/watch?v=ePKC5ZEqeNY
public class ReviewModel  //Model for submitting reviews to firebase
{
    String uid,  review, timestamp, location,name;
    int ratings;

    public ReviewModel(){}

    public ReviewModel(String review, String timestamp, String location, String name) { //constructor

        this.review = review;
        this.timestamp = timestamp;
        this.location = location;
        this.name = name;

    }
  // Initailizing getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
