package com.tutorix.tutorialspoint.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mocktest_stats")
public class MockTestModelTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int _id;

    @ColumnInfo(name = "user_id")
    public String user_id;

    @ColumnInfo(name = "class_id")
    public String class_id;

    @ColumnInfo(name = "subject_id")
    public String subject_id;

    @ColumnInfo(name = "section_id")
    public String section_id;

    @ColumnInfo(name = "mocktest_type")
    public String mocktest_type;

    @ColumnInfo(name = "total_attempts")
    public int total_attempts;

    @ColumnInfo(name = "total_marks")
    public int total_marks;

    @ColumnInfo(name = "total_questions")
    public int total_questions;

    @ColumnInfo(name = "low_marks")
    public int low_marks;

    @ColumnInfo(name = "high_marks")
    public int high_marks;

    @ColumnInfo(name = "created_dtm")
    public String created_dtm;

    @ColumnInfo(name = "sync")
    public String sync;
}
