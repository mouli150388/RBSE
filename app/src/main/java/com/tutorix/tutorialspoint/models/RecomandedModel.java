package com.tutorix.tutorialspoint.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "mocktest_recommended_videos")
public class RecomandedModel {

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

    @ColumnInfo(name = "lecture_id")
    public String lecture_id;

    @ColumnInfo(name = "lecture_title")
    public String lecture_title;

    @ColumnInfo(name = "mocktest_type")
    public String mocktest_type;

    @ColumnInfo(name = "created_dtm")
    public String created_dtm;

    @ColumnInfo(name = "sync")
    public String sync;


    @Ignore
    public String lecture_count;
    @Ignore
    public int total;
}
