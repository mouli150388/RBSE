package com.tutorix.tutorialspoint.models;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarkdetails")
public class SubChapters implements Serializable{
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "lecturename")
    public String txt;
    @ColumnInfo(name = "lecturevideo")
    public String lecture_video_url;
    @ColumnInfo(name = "lectureimage")
    public String lecture_video_thumb;
    @ColumnInfo(name = "lectureduration")
    public String lecture_duration;

    @ColumnInfo(name = "video_srt")
    public String video_srt;
    @ColumnInfo(name = "subjectid")
    public String subjectid;
    @ColumnInfo(name = "classid")
    public String classid;
    @ColumnInfo(name = "userid")
    public String userid;
    @ColumnInfo(name = "sectionid")
    public String section_id;
    @ColumnInfo(name = "lectureid")
    public String lecture_id;

    @ColumnInfo(name = "is_completed")
    public boolean lecture_completed;
    @ColumnInfo(name = "is_bookmarked")
    public boolean lecture_bookmark;
    @ColumnInfo(name = "sync")
    public boolean is_sync=false;
    @ColumnInfo(name = "quizcreateddtm")
    public String createdDtm;

    @ColumnInfo(name = "is_notes")
    public boolean is_notes;

    @ColumnInfo(name="user_lecture_notes")
    public String lecture_notes;

    @Ignore
    public String selectedlecture_id;
    @Ignore
    public boolean is_demo=false;

    @Ignore
    public String lecture_quiz;
    @Ignore
    public String status;
    @Ignore
    public String favourite;
    @Ignore
    public int start_time_secs;


}
