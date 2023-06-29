package com.tutorix.tutorialspoint.database;

import com.tutorix.tutorialspoint.models.QuizModel;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface QuizModelDAO {


@Insert
public void addQuiz(QuizModel model);

@Query("select * from quizdetails where quizcreateddtm=:timeStamp")
public QuizModel getQuizDetails(String timeStamp) ;

    @Query("select * from quizdetails where userid=:userId and classid=:classId")
    public QuizModel getQuizDetails(String userId, String classId) ;

    @Query("select * from quizdetails where userid=:userId and classid=:classId and sync!='Y'")
    public QuizModel getQuizDetailsSync(String userId, String classId) ;

    @Query("UPDATE quizdetails SET sync=:status WHERE id in(:ids)")
    public int updateQuizSyncStatus(String status,String ids);


    @Query("select * from quizdetails where userid=:user_id and quizcreateddtm > (SELECT DATETIME('now', '-1 day'))")
    public List<QuizModel> getOneDayQuizTimeLineNoSub(String user_id);

    @Query("select * from quizdetails where userid=:user_id  and quizcreateddtm > (SELECT DATETIME('now', '-7 day'))")
    public List<QuizModel> getSevenDayQuizTimeLineNoSub(String user_id);

    @Query("select * from quizdetails where userid=:user_id  and quizcreateddtm > (SELECT DATETIME('now', '-30 day'))")
    public List<QuizModel> getMonthDayQuizTimeLineNoSub(String user_id);

    @Query("select * from quizdetails where userid=:user_id ")
    public List<QuizModel> getAllDayQuizTimeLineNoSub(String user_id);






    @Query("select * from quizdetails where userid=:user_id and subjectid=:subjectId and classid=:classId and quizcreateddtm > (SELECT DATETIME('now', '-1 day'))")
    public List<QuizModel> getOneDayQuizTimeLineSub(String user_id,  String subjectId,String classId);

    @Query("select * from quizdetails where userid=:user_id and subjectid=:subjectId  and classid=:classId and quizcreateddtm > (SELECT DATETIME('now', '-7 day'))")
    public List<QuizModel> getSevenDayQuizTimeLineSub(String user_id,  String subjectId,String classId);

    @Query("select * from quizdetails where userid=:user_id and subjectid=:subjectId  and classid=:classId and quizcreateddtm > (SELECT DATETIME('now', '-30 day'))")
    public List<QuizModel> getMonthDayQuizTimeLineSub(String user_id,  String subjectId,String classId);

    @Query("select * from quizdetails where userid=:user_id and subjectid=:subjectId and classid=:classId")
    public List<QuizModel> getAllDayQuizTimeLineSub(String user_id, String subjectId,String classId);


    @Query("delete from quizdetails where userid=:user_id and classid=:class_id")
    public void deleteQuizeDetails(String user_id,String class_id);



    @Query("select * from quizdetails where userid=:user_id and subjectid=:subjectId and classid=:classId and sectionid=:sectionId and mock_test=:mock_test order by quizcreateddtm desc")
    public List<QuizModel> getMoCkPrevious(String user_id,  String classId, String subjectId, String sectionId,String mock_test );

    @Query("select * from quizdetails where userid=:user_id and subjectid=:subjectId and classid=:classid and quizcreateddtm=:crdtm")
    public QuizModel getQuizModel(String user_id, String classid,String subjectId,String crdtm);



        /*   Performance Data       */

    @Query("select * from quizdetails where userid=:user_id  and classid=:classId and quizcreateddtm > (SELECT DATETIME('now', '-1 day'))")
    public List<QuizModel> getOneDayQuizTimeLineSub(String user_id,  String classId);

    @Query("select * from quizdetails where userid=:user_id and mock_test!=''  and classid=:classId and quizcreateddtm > (SELECT DATETIME('now', '-7 day'))")
    public List<QuizModel> getSevenDayQuizTimeLineSub(String user_id,  String classId);

    @Query("select * from quizdetails where userid=:user_id  and classid=:classId and mock_test!='' and quizcreateddtm > (SELECT DATETIME('now', '-30 day'))")
    public List<QuizModel> getMonthDayQuizTimeLineSub(String user_id,  String classId);

    @Query("select * from quizdetails where userid=:user_id  and classid=:classId and mock_test!='' and quizcreateddtm > (SELECT DATETIME('now', '-90 day'))")
    public List<QuizModel> getThreeMonthDayQuizTimeLineSub(String user_id,  String classId);

    @Query("select * from quizdetails where userid=:user_id and  classid=:classId and mock_test!=''")
    public List<QuizModel> getAllDayQuizTimeLineSub(String user_id, String classId);

}
