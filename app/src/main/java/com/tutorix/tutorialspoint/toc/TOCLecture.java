package com.tutorix.tutorialspoint.toc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TOCLecture implements Serializable {
@SerializedName("lecture_id")
@Expose
private String lectureId;
@SerializedName("lecture_name")
@Expose
private String lectureName;
@SerializedName("lecture_duration")
@Expose
private String lectureDuration;
@SerializedName("lecture_video_url")
@Expose
private String lectureVideoUrl;
@SerializedName("tp_original_url")
@Expose
private String tpOriginalUrl;
@SerializedName("lecture_demo_flag")
@Expose
private String lectureDemoFlag;

public String getLectureId() {
return lectureId;
}

public void setLectureId(String lectureId) {
this.lectureId = lectureId;
}

public String getLectureName() {
return lectureName;
}

public void setLectureName(String lectureName) {
this.lectureName = lectureName;
}

public String getLectureDuration() {
return lectureDuration;
}

public void setLectureDuration(String lectureDuration) {
this.lectureDuration = lectureDuration;
}

public String getLectureVideoUrl() {
return lectureVideoUrl;
}

public void setLectureVideoUrl(String lectureVideoUrl) {
this.lectureVideoUrl = lectureVideoUrl;
}

public String getTpOriginalUrl() {
return tpOriginalUrl;
}

public void setTpOriginalUrl(String tpOriginalUrl) {
this.tpOriginalUrl = tpOriginalUrl;
}

public String getLectureDemoFlag() {
return lectureDemoFlag;
}

public void setLectureDemoFlag(String lectureDemoFlag) {
this.lectureDemoFlag = lectureDemoFlag;
}
}
