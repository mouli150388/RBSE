package com.tutorix.tutorialspoint.testseries.data;

import java.util.List;

public class TestQuestions {

    public int status;//0-Not YET VISITED
    //1-VISITED BUT NOT ANSWERED
    // 2 ANSWERED
    // 3 QUESTION NOT ANSWERED MARK FOR REVIEW
    // 4 QUESTION ANSWERED MARK FOR REVIEW
    public String question;
    public int question_id;
    public int option_right;
    public String explanation;
    public String answer="";
    public String my_answer="";
    public int option_selected=-1;
    public int subject_id;
    public String question_type="O";
    public List<QuestionOption> options;

}


