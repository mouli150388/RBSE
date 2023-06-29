package com.tutorix.tutorialspoint.database;

import com.tutorix.tutorialspoint.models.TrackModel;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;

@Dao
public interface TrackDAO {


    @Query("delete from trackingdetails where user_id=:user_id and classid=:classId")
    public void deleteTrackDeactivateUser(String classId,String user_id);


    @Insert
    public long insertTrack(TrackModel track);


    @Query("select * from trackingdetails where user_id=:user_id and classid=:classId and subjectid=:subjectId and sectionid=:sectionId and lectureid=:lectureId and activitytype=:activityType and trackcreateddtm=:trackCreatedDtm  ORDER BY id limit 1")
    public TrackModel isTrackAdded(String user_id,String classId,String subjectId,String sectionId,String
            lectureId,String activityType ,String trackCreatedDtm);


    @Query("select * from trackingdetails where trackcreateddtm>(SELECT DATETIME('now', '-1 day')) and classid=:class_id and user_id=:user_id")
    public List<TrackModel> getOneDayTrackDetailsTimeLine(String user_id, String class_id);


    @Query("select * from trackingdetails where trackcreateddtm>(SELECT DATETIME('now', '-7 day')) and classid=:class_id and user_id=:user_id")
    public List<TrackModel>getSevenDayTrackDetailsTimeLine(String user_id, String class_id);

    @Query("select * from trackingdetails where trackcreateddtm>(SELECT DATETIME('now', '-30 day')) and classid=:class_id and user_id=:user_id")
    public List<TrackModel>getMonthTrackDetailsTimeLine(String user_id,String class_id);

    @Query("select * from trackingdetails where trackcreateddtm>(SELECT DATETIME('now', '-90 day')) and classid=:class_id and user_id=:user_id")
    public List<TrackModel>getThreeMonthTrackDetailsTimeLine(String user_id,String class_id);

    @Query("select * from trackingdetails where  classid=:class_id and user_id=:user_id")
    public List<TrackModel>getTrackDetailsTimeLine(String user_id,String class_id);

    @Query("select * from trackingdetails where  classid=:class_id and user_id=:user_id order by trackcreateddtm DESC LIMIT :offset, :limit")
    public List<TrackModel>getAllTrackDetails(String user_id,String class_id,int offset, int limit);

    @Query("select * from trackingdetails where  classid=:class_id and user_id=:user_id and lectureid=:lecture_id order by trackcreateddtm DESC LIMIT :offset, :limit")
    public List<TrackModel>getLectureAllTrackDetails(String user_id,String class_id,String lecture_id,int offset, int limit);



    @Query("select * from trackingdetails where  classid=:class_id and user_id=:user_id AND sync='0' LIMIT 1")
    public TrackModel getUnSyncTrackingDetails(String user_id,String class_id);


    @Query("UPDATE trackingdetails SET sync=:status WHERE id in(:ids)")
    public int updateTrackSyncStatus(String status,String ids);



    @Query("delete from trackingdetails where user_id=:user_id and classid=:class_id")
    public void deleteTracksForDeactUser(String user_id,String class_id);



    @Query("select * from trackingdetails where user_id=:user_id  and classid=:classid order by trackcreateddtm  DESC LIMIT :offset , :limit ")
    public List<TrackModel> getTrackingDetailsAll(String user_id,   int offset, int limit,String classid) ;


    @Query("select * from trackingdetails where user_id=:user_id and classid=:classid and lectureid=:lecture_id  order by trackcreateddtm  DESC LIMIT :offset , :limit ")
    public List<TrackModel> getTrackingDetailsLecture(String user_id, String lecture_id,  int offset, int limit,String classid) ;


    @Query("select * from trackingdetails where user_id=:user_id and classid=:classid and subjectid=:subject_id and sectionid=:section_id and lectureid=:lecture_id  order by trackcreateddtm  DESC LIMIT :offset , :limit ")
    public List<TrackModel> getTrackingDetailsLecture(String user_id, String subject_id,String section_id,String lecture_id,  int offset, int limit,String classid) ;



    /*Progress Time*/
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select SUM(duration_insec) AS duration_insec,activityduration,trackcreateddtm, id, sync from trackingdetails where DATETIME(trackcreateddtm)>(SELECT DATETIME('now', '-7 day')) and classid=:class_id and user_id=:user_id and subjectid=:subjectid group by DATE(trackcreateddtm)")
    public List<TrackModel>getSevenDayTrackTimeLine(String user_id, String class_id,String subjectid);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select SUM(duration_insec)/60 AS duration_insec,activityduration,trackcreateddtm, id, sync from trackingdetails where trackcreateddtm>(SELECT DATETIME('now', '-30 day')) and classid=:class_id and user_id=:user_id and subjectid=:subjectid group by DATE(trackcreateddtm)")
    public List<TrackModel>getMonthTrackTimeLine(String user_id, String class_id,String subjectid);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select SUM(duration_insec) as duration_sec from trackingdetails where DATE(trackcreateddtm)>DATE( :start_date )and  DATE(trackcreateddtm)<=DATE( :end_date ) and classid=:class_id and user_id=:user_id and subjectid=:subjectid  ")
    public long getTrackTimeLineWithDates(String user_id, String class_id,String subjectid,String start_date,String end_date);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select SUM(duration_insec)/60 AS duration_insec,activityduration,trackcreateddtm, id, sync from trackingdetails where trackcreateddtm>(SELECT DATETIME('now', '-90 day')) and classid=:class_id and user_id=:user_id and subjectid=:subjectid group by DATE(trackcreateddtm)")
    public List<TrackModel>getThreeMonthTrackTimeLine(String user_id, String class_id,String subjectid);
}

