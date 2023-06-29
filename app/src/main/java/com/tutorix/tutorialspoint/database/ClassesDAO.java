package com.tutorix.tutorialspoint.database;

import com.tutorix.tutorialspoint.models.ClassModel;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ClassesDAO {

    @Insert
    public long insertClassModel(ClassModel model);

    @Query("select * from classes ")
    public List<ClassModel> getAllClasses();

    @Query("UPDATE classes SET class_status=:status WHERE class_id=:class_id")
    public int updateClass(String class_id,int status);

    @Query("select * from classes WHERE class_id=:class_id")
    public ClassModel getCurrentClass(String class_id);

}
