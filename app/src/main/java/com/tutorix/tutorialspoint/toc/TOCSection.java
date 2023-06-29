package com.tutorix.tutorialspoint.toc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TOCSection implements Serializable {
@SerializedName("section_id")
@Expose
private String sectionId;
@SerializedName("section_name")
@Expose
private String sectionName;
@SerializedName("lecture_count")
@Expose
private Integer lectureCount;
@SerializedName("lecture_duration")
@Expose
private String lectureDuration;
@SerializedName("lectures")
@Expose
private List<TOCLecture> lectures;

public String getSectionId() {
return sectionId;
}

public void setSectionId(String sectionId) {
this.sectionId = sectionId;
}

public String getSectionName() {
return sectionName;
}

public void setSectionName(String sectionName) {
this.sectionName = sectionName;
}

public Integer getLectureCount() {
return lectureCount;
}

public void setLectureCount(Integer lectureCount) {
this.lectureCount = lectureCount;
}

public String getLectureDuration() {
return lectureDuration;
}

public void setLectureDuration(String lectureDuration) {
this.lectureDuration = lectureDuration;
}

public List<TOCLecture> getLectures() {
return lectures;
}

public void setLectures(List<TOCLecture> lectures) {
this.lectures = lectures;
}
}
