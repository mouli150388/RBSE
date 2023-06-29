package com.tutorix.tutorialspoint.testseries.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestSeriesPhysics {
    @SerializedName("max_optional_attempt")
    public int max_optional_attempt;

    @SerializedName("total_marks")
    public int total_marks;
    @SerializedName("total_questions")
    public int total_questions;
    public int total_correct;
    public int total_wrong;
    public int total_gain_marks;
    /*public int correct_answers;
    public int wrong_answers;*/
    public int attempted_questions;
    @SerializedName("questions")
    public List<TestQuestions> questions;
}
