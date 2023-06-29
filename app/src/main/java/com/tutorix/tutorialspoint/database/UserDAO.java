package com.tutorix.tutorialspoint.database;

import com.tutorix.tutorialspoint.models.UserProfile;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertUser(UserProfile user);

    @Query("select user_id from profile where user_id=:user_id")
    public String getUserId(String user_id);

    @Query("select * from profile where user_id=:user_id")
    public UserProfile getUserProfile(String user_id);

    @Update
    public void updateProfile(UserProfile user);

    @Query("update profile set password=:password where user_id=:user_id")
    public void updatePassword(String user_id,String password);

    @Query("update profile set photo=:img where user_id=:user_id")
    public void updateProfilePhoto(String user_id,String img);

    @Query("delete from profile where user_id=:userid")
    public void deleteUser(String userid);

    @Query("select * from profile where mobile_number=:mobile_no and password=:password")
    UserProfile getUserProfile(String mobile_no, String password);


    @Query("update profile set access_token=:access_token where user_id=:user_id")
    public void updateAccessToken(String user_id,String access_token);
}
