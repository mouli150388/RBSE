package com.tutorix.tutorialspoint.database;


import com.tutorix.tutorialspoint.models.RecomandedModel;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;

@Dao
public interface RecomandedDAO {
    @Insert
    public long insertRecomanded(RecomandedModel model);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select  COUNT(*) as _id,lecture_id, lecture_title,section_id,lecture_id from mocktest_recommended_videos where user_id=:user_id and class_id=:class_id and subject_id=:subject_id and section_id=:section_id group by lecture_id order by _id ASC limit 3 ")
    public List<RecomandedModel>getRecomanded(String user_id,String class_id,String subject_id,String section_id);

    @Query("select  * from mocktest_recommended_videos where user_id=:user_id and class_id=:class_id and sync='N' LIMIT 1 ")
    public RecomandedModel getRecomanded(String user_id,String class_id);

    @Query("UPDATE mocktest_recommended_videos SET sync=:status WHERE _id in(:ids)")
    public int updateMockTestSyncStatus(String status,String ids);


    @Query("delete from mocktest_recommended_videos where user_id= :userid and class_id= :classid")
    public void deleteMockTestRecomanded(String userid,String classid);


    @Query("select  * from mocktest_recommended_videos where user_id=:user_id and class_id=:class_id")
    public List<RecomandedModel> getRecomandedAll(String user_id,String class_id);

}
