package com.tutorix.tutorialspoint.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class Classes {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "class_id")
    public String class_id;
    @ColumnInfo(name = "class_name")
    public String class_name;
    @ColumnInfo(name = "class_status")
    public boolean class_status;
    @ColumnInfo(name = "class_folder")
    public String class_folder;
}
