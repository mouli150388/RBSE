package com.tutorix.tutorialspoint.models;


import java.io.Serializable;
import java.util.ArrayList;

public class Chapters implements Serializable {
    public String txt;
    public String section_id;
    public String lecture_count;
    public String question_count;
    public String total_duration;
    public String section_image;
    public String subjectid;
    public String classid;
    public String userid;
    public String section_status;
    public String subject_name;
    public String avg;
    public String attrmpted_count;
    public String calculated_time;
    public boolean availableAllVideos=true;


    public ArrayList<SubChapters> subchapters;


}
