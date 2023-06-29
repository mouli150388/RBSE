package com.tutorix.tutorialspoint.models;

public class QuestionAnsDeatils {
    public String q_id;
    public String lecture_id;
    public String question_type;
    public int question_level;//1easy,2medium,3complecated
    public String option_right;
    public int answer_type;//0Wrong,1 write,-1 unanswered
    public float  result;
}
