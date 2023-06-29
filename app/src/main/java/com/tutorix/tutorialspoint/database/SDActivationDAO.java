package com.tutorix.tutorialspoint.database;

import com.tutorix.tutorialspoint.models.SDActivationDetails;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SDActivationDAO {
    @Insert
    public long inserActivation(SDActivationDetails activationDetails);

    @Query("select * from sd_activation_details where user_id=:user_id and classid=:classid order by _id DESC limit 1")
    public SDActivationDetails getActivationDetails(String user_id, String classid);

    @Query("update sd_activation_details set activation_end_date=:activation_end_date ,  activation_key=:activation_key ,  act_current_date=:current_date ,  device_id=:device_id where classId=:classid and user_id=:user_id")
    public void updateActivationDetails(String activation_end_date,   String activation_key, String current_date, String classid, String user_id,String device_id);

    @Query("delete from sd_activation_details where user_id= :userid and classid= :classid")
    public void deleteActivationDetails(String userid, String classid);

    @Query("select * from sd_activation_details where user_id=:user_id and classid=:classid ")
    public List<SDActivationDetails> getActivationDetailsList(String user_id, String classid);

    @Query("select * from sd_activation_details where user_id=:user_id  order by _id DESC limit 1")
    SDActivationDetails getActivationDetails(String user_id);

    @Query("update sd_activation_details set activation_end_date=:activation_end_date , act_current_date=:current_date where classId=:classid and user_id=:user_id")
    public void updateActivationDate(String activation_end_date,String current_date,String classid,String user_id);


}
