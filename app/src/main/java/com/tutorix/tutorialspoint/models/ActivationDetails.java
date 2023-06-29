package com.tutorix.tutorialspoint.models;

import com.tutorix.tutorialspoint.utility.Constants;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "activation_details")
public class ActivationDetails {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int _id;
    @ColumnInfo(name = "user_id")
    public String user_id;
    @ColumnInfo(name = "classid")
    public String class_id;
    @ColumnInfo(name = Constants.activationType)
    public String activation_type;

    @ColumnInfo(name = Constants.activationStartData)
    public String activation_start_date;

    @ColumnInfo(name = Constants.activationEndDate)
    public String activation_end_date;

    @ColumnInfo(name = Constants.currentDate)
    public String current_date;

    @ColumnInfo(name = Constants.activationKey)
    public String activation_key;

    @ColumnInfo(name = Constants.remaingDays)
    public String days_left;

    @NonNull
    @ColumnInfo(name = Constants.SECURE_DATA_URL)
    public String secure_url;

    @NonNull
    @ColumnInfo(name = Constants.DATA_URL)
    public String data_url;

    @NonNull
    @ColumnInfo(name = "device_id")
    public String device_id;


}
