package com.tutorix.tutorialspoint.toc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TOCSectionModel {
@SerializedName("course_name")
@Expose
private String courseName;
@SerializedName("course_image")
@Expose
private String courseImage;

@SerializedName("sections")
@Expose
private List<TOCSection> TOCSections;

public String getCourseName() {
return courseName;
}

public void setCourseName(String courseName) {
this.courseName = courseName;
}

public String getCourseImage() {
return courseImage;
}

public void setCourseImage(String courseImage) {
this.courseImage = courseImage;
}



public List<TOCSection> getTOCSections() {
return TOCSections;
}

public void setTOCSections(List<TOCSection> TOCSections) {
this.TOCSections = TOCSections;
}

}
