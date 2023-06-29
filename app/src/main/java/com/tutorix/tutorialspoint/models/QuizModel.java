package com.tutorix.tutorialspoint.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "quizdetails")
public class QuizModel {

    @ColumnInfo(name = "quizjson")
    public String question;
    @ColumnInfo(name = "totalcorrect")
    public String total_correct;

    @ColumnInfo(name = "quizwrong")
    public String total_wrong;

    @ColumnInfo(name = "subjectid")
    public String subject_id;

    @ColumnInfo(name = "lectureid")
    public String lectur_id;

    @ColumnInfo(name = "sectionid")
    public String section_id;

    @ColumnInfo(name = "userid")
    public String userId;

    @ColumnInfo(name = "classid")
    public String classId;

    @ColumnInfo(name = "quizduration")
    public String QuizDuration;

    @ColumnInfo(name = "attemptedquestions")
    public String attempted_questions;

    @ColumnInfo(name = "quizcreateddtm")
    public String QuizCreatedDtm;

    @ColumnInfo(name = "sync")
    public String sync;

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Integer _id;

    @ColumnInfo(name = "lecture_name")
    public String lecture_name;

    @ColumnInfo(name = "quiz_id")
    public String quiz_id;

    @ColumnInfo(name = "totalquestions")
    public int total;

    @Ignore
    public String option_right;
    @Ignore
    public String explanation;
    @Ignore
    public String option_1;

    @Ignore
    public String question_type;
    @Ignore
    public String option_2;
    @Ignore
    public String option_3;
    @Ignore
    public String option_4;
    @Ignore
    public String option_selected;
    @Ignore
    public String total_marks;


    @ColumnInfo(name = "section_name")
    public String section_name;
    @ColumnInfo(name = "mock_test")
    public String mock_test;



}
