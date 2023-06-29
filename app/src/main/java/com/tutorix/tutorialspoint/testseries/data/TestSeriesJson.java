package com.tutorix.tutorialspoint.testseries.data;

import com.google.gson.annotations.SerializedName;

public class TestSeriesJson {
    public String name;
    public int total_questions;
    public String total_marks;
    public int max_questions_attempt;

    @SerializedName("biology")
    public  TestSeriesPhysics biology;
    @SerializedName("chemistry")
    public  TestSeriesPhysics chemistry;
    @SerializedName("physics")
    public TestSeriesPhysics physics;
    @SerializedName("mathematics")
    public TestSeriesPhysics mathematics;
}
