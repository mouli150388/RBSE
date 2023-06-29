package com.tutorix.tutorialspoint.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class Notifications {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;
    @ColumnInfo(name="title")
    public String title;
    @ColumnInfo(name="message")
    public String message;
    @ColumnInfo(name="image")
    public String image;
    @ColumnInfo(name="time")
    public String time;
}
