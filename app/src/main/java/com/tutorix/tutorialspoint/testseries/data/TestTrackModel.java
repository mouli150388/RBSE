package com.tutorix.tutorialspoint.testseries.data;

import com.google.gson.annotations.SerializedName;

public class TestTrackModel {
    @SerializedName("activity_duration")
   public String activity_duration;
    @SerializedName("class_id")
    public String class_id;
    @SerializedName("test_series_id")
    public String test_series_id;
    @SerializedName("test_series_name")
    public String test_series_name;
    @SerializedName("activity_date")
    public String activity_date;
    @SerializedName("total_questions")
    public int total_questions;
    @SerializedName("total_correct")
    public int total_correct;
}
