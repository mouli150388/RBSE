package com.tutorix.tutorialspoint.models;
public class VocationCourses {

public String id;
public String toc;
public String name;
public int img;

public VocationCourses(String id, String name, int img,String toc) {
this.toc = toc;
this.id = id;
this.name = name;
this.img = img;
}
}
