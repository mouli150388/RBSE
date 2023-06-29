package com.tutorix.tutorialspoint.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "classes")
public class ClassModel {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "class_id")
    public String class_id;

    @ColumnInfo(name = "class_name")
    @NonNull
    public String class_name;

    @NonNull
    @ColumnInfo(name = "class_status")
    public boolean class_status;

    @NonNull
    @ColumnInfo(name = "class_folder")
    public String class_folder;
}
