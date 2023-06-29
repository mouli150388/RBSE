package com.tutorix.tutorialspoint.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "trackingdetails")
public class TrackModel {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "activitytype")
    public String activity_type;

    @ColumnInfo(name = "activityduration")
    public String activity_duration;

    @ColumnInfo(name = "lecture_name")
    public String lecture_name="lecture_name";

    @ColumnInfo(name = "trackcreateddtm")
    public String activity_date;

    @ColumnInfo(name = "subjectid")
    public String subject_id;

    @ColumnInfo(name = "quizid")
    public String quiz_id;

    @ColumnInfo(name = "sectionid")
    public String section_id;

    @ColumnInfo(name = "lectureid")
    public String lecture_id;

    @ColumnInfo(name = "classid")
    public String class_id;

    @ColumnInfo(name = "user_id")
    public String user_id;

    @Ignore
    public String datetime;

    @ColumnInfo(name = "duration_insec")
    public long duration_insec;

    @ColumnInfo(name = "sync")
    public boolean is_sync;

    @ColumnInfo(name = "videoduratrion")
    public String videoduratrion;

    @ColumnInfo(name = "section_name")
    public String section_name;




}
