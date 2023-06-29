package com.tutorix.tutorialspoint.database;

import com.tutorix.tutorialspoint.models.MockTestModelTable;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;

@Dao
public interface MockTestDAO {
    @Insert
    public long insertRecomanded(MockTestModelTable model);

    @Query("select * from mocktest_stats where user_id=:user_id and class_id=:class_id and subject_id=:subject_id and section_id=:section_id and mocktest_type=:mocktest_type")
    public MockTestModelTable getMockTest(String user_id,String class_id,String subject_id,String section_id,String mocktest_type);

    @Query("update mocktest_stats set total_attempts=:total_attempts , total_marks=:totalmarks ,   low_marks=:low_marks , high_marks=:high_marks where user_id=:user_id and class_id=:class_id and subject_id=:subject_id and section_id=:section_id and mocktest_type=:mocktest_type")
    public void updateMockTest(String user_id,String class_id,String subject_id,String section_id,String mocktest_type,int totalmarks,int total_attempts,int low_marks,int high_marks);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select SUM(total_marks) as total_marks, SUM(total_questions) as total_questions, _id,total_attempts,low_marks,high_marks,section_id from mocktest_stats where user_id=:user_id and class_id=:class_id and subject_id=:subject_id group by section_id")
    public List<MockTestModelTable> getMockTest(String user_id, String class_id, String subject_id);


    @Query("select * from mocktest_stats where user_id=:user_id and class_id=:class_id and  sync='N' LIMIT 1")
    public MockTestModelTable getMockTestUnSync(String user_id, String class_id);


    @Query("UPDATE mocktest_stats SET sync=:status WHERE _id in(:ids)")
    public int updateMockTestSyncStatus(String status,String ids);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select  _id,total_attempts,low_marks,high_marks,section_id,total_marks,total_questions, mocktest_type from mocktest_stats where user_id=:user_id and class_id=:class_id and subject_id=:subject_id and section_id=:section_id group by mocktest_type  order by mocktest_type ASC")
    public List<MockTestModelTable> getMockTestBySection(String user_id, String class_id, String subject_id,String section_id);


    @Query("delete from mocktest_stats where user_id= :userid and class_id= :classid")
    public void deleteMockTest(String userid,String classid);

    @Query("select * from mocktest_stats where user_id=:user_id and class_id=:class_id ")
    public List<MockTestModelTable> getMockTest(String user_id, String class_id);
}
