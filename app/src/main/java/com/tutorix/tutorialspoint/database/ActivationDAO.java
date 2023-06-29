package com.tutorix.tutorialspoint.database;

import com.tutorix.tutorialspoint.models.ActivationDetails;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ActivationDAO {
    @Insert
    public long inserActivation(ActivationDetails activationDetails);

    @Query("select * from activation_details where user_id=:user_id and classid=:classid order by _id DESC limit 1")
    public ActivationDetails getActivationDetails(String user_id,String classid);

    @Query("update activation_details set activation_end_date=:activation_end_date , activation_start_date=:activation_start_date , activation_type=:activation_type , activation_key=:activation_key , secure_url=:secure_url,data_url=:data_url,days_left=:days_left , current_date=:current_date where classId=:classid and user_id=:user_id")
    public void updateActivationDetails(String activation_end_date,String activation_start_date,String activation_type,String activation_key,String current_date,String days_left,String classid,String user_id,String secure_url,String data_url);

    @Query("delete from activation_details where user_id= :userid and classid= :classid")
    public void deleteActivationDetails(String userid,String classid);

    @Query("select * from activation_details where user_id=:user_id and classid=:classid ")
    public List<ActivationDetails> getActivationDetailsList(String user_id, String classid);

    @Query("select * from activation_details where user_id=:user_id  order by _id DESC limit 1")
    ActivationDetails getActivationDetails(String user_id);
}
