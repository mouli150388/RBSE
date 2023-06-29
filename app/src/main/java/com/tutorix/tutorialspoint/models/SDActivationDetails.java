package com.tutorix.tutorialspoint.models;

import com.tutorix.tutorialspoint.utility.Constants;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sd_activation_details")
public class SDActivationDetails {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int _id;

    @NonNull
    @ColumnInfo(name = "user_id")
    public String user_id;

    @NonNull
    @ColumnInfo(name = "classid")
    public String class_id;

    @NonNull
    @ColumnInfo(name = Constants.activationEndDate)
    public String activation_end_date;

    @NonNull
    @ColumnInfo(name = "act_current_date")
    public String current_date;

    @NonNull
    @ColumnInfo(name = Constants.activationKey)
    public String activation_key;

    @NonNull
    @ColumnInfo(name = "device_id")
    public String device_id;
}
