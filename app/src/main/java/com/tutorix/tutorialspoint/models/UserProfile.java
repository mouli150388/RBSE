package com.tutorix.tutorialspoint.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "profile")
public class UserProfile {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    public String user_id;
    @ColumnInfo(name = "access_token")
    public String accessToken;
    @ColumnInfo(name = "deviceId")
    public String deviceId;
    @ColumnInfo(name = "user_name")
    public String user_name;
    @ColumnInfo(name = "password")
    public String password;
    @ColumnInfo(name = "full_name")
    public String full_name;

    @ColumnInfo(name = "email_id")
    public String email_id;

    @ColumnInfo(name = "mobile_number")
    public String mobile_number;
    @ColumnInfo(name = "mobile_country_code")
    public String mobile_country_code;
    @ColumnInfo(name = "photo")
    public String photo;

    @ColumnInfo(name = "user_type")
    public String user_type;

    @ColumnInfo(name = "gender")
    public String gender;

    @ColumnInfo(name = "date_of_birth")
    public String date_of_birth;

    @ColumnInfo(name = "father_name")
    public String father_name;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "city")
    public String city;

    @ColumnInfo(name = "state")
    public String state;

    @ColumnInfo(name = "school_name")
    public String school_name;

    @ColumnInfo(name = "roll_number")
    public String roll_number;

    @ColumnInfo(name = "section_name")
    public String section_name;

    @ColumnInfo(name = "postal_code")
    public String postal_code;

    @ColumnInfo(name = "country_code")
    public String country_code;

    @ColumnInfo(name = "school_address")
    public String school_address;

    @ColumnInfo(name = "school_city")
    public String school_city;

    @ColumnInfo(name = "school_state")
    public String school_state;

    @ColumnInfo(name = "school_postal_code")
    public String school_postal_code;

    @ColumnInfo(name = "school_country_code")
    public String school_country_code;




}
