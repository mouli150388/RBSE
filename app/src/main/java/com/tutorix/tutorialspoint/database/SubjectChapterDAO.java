package com.tutorix.tutorialspoint.database;

import com.tutorix.tutorialspoint.models.SubChapters;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SubjectChapterDAO {
    @Query("select * from bookmarkdetails where userid=:user_id and classid=:class_id")
    public List<SubChapters>getAllBookMarks(String user_id,String class_id);

    @Query("select * from bookmarkdetails where userid=:user_id and classid=:class_id and sectionid=:section_id and lectureid=:lecture_id and subjectid=:subject_id limit 1")
    public SubChapters getBookmarkorData(String user_id,String class_id,String section_id,String lecture_id,String subject_id);

    @Query("select * from bookmarkdetails where userid=:userid and classid=:classId and is_bookmarked=1")
    public List<SubChapters> getBookMarkDetailsBookmark( String userid, String classId);

    @Query("select * from bookmarkdetails where userid=:userid and classid=:classId and is_notes=1")
    public List<SubChapters> getBookMarkDetailsNotes( String userid, String classId);

    @Query("select * from bookmarkdetails where userid=:userid and classid=:classId and is_completed=1")
    public List<SubChapters> getBookMarkDetailsComplete( String userid, String classId);


    @Query("select * from bookmarkdetails where userid=:userId and classid=:classId AND sync='0' LIMIT 1")
    public SubChapters getBookSync(String userId, String classId);


    @Query("UPDATE bookmarkdetails SET sync=:status WHERE id in(:ids)")
    public int updateBookmarkSyncStatus(int status,String ids);




    @Query("delete from bookmarkdetails where userid=:user_id and classid=:class_id and sectionid=:section_id and lectureid=:chapter_id and subjectid=:subject_id")
    public void deleteBookmark(String user_id, String class_id, String section_id,
                               String chapter_id,  String subject_id);




    @Insert
    public long addBookMark(SubChapters subChapters);

    @Query("update bookmarkdetails set sync=:sync , is_bookmarked=:isbookmark , is_completed=:iscomplted , is_notes=:isNotes where userid=:user_id and classid=:class_id and sectionid=:section_id and lectureid=:chapter_id and subjectid=:subject_id")
    public void updateBookmarkCompleted(String user_id, String class_id, String section_id,
                                        String chapter_id,  String subject_id,boolean sync,boolean isbookmark, boolean iscomplted,boolean isNotes);


    @Query("update bookmarkdetails set is_notes=:isNotes ,sync=:is_sync, user_lecture_notes=:lecture_notes  where userid=:user_id and classid=:class_id and sectionid=:section_id and lectureid=:chapter_id and subjectid=:subject_id")
    public void updateBookmarkNotes(String user_id, String class_id, String section_id,
                                        String chapter_id,  String subject_id,boolean isNotes,String lecture_notes,boolean is_sync);


    @Query("delete from bookmarkdetails where userid= :user_id and classid= :class_id")
    public void deleteBookmarkForDeactUser(String user_id,String class_id);






}
