package com.tutorix.tutorialspoint.database;

import com.tutorix.tutorialspoint.models.Notifications;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NotificationsDAO {
    @Insert
    public long insert(Notifications notifications);

    @Query("select * from notifications order by id DESC limit 25")
    public List<Notifications> getAll();
}
