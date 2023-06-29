package com.tutorix.tutorialspoint.database;

import android.content.Context;

import com.tutorix.tutorialspoint.models.ActivationDetails;
import com.tutorix.tutorialspoint.models.ClassModel;
import com.tutorix.tutorialspoint.models.MockTestModelTable;
import com.tutorix.tutorialspoint.models.Notifications;
import com.tutorix.tutorialspoint.models.QuizModel;
import com.tutorix.tutorialspoint.models.RecomandedModel;
import com.tutorix.tutorialspoint.models.SDActivationDetails;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.models.UserProfile;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


/*
Version 1 Normal Table
Version 2 MIGRATION_1_2
Version 3 MIGRATION_1_2,MIGRATION_1_3,MIGRATION_2_3
Version 4 MIGRATION_1_4,MIGRATION_2_4,MIGRATION_3_4

*/

@Database(entities = {UserProfile.class, ActivationDetails.class, TrackModel.class, QuizModel.class, SubChapters.class, MockTestModelTable.class, RecomandedModel.class, ClassModel.class, SDActivationDetails.class, Notifications.class}, version = 4,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract TrackDAO trackDAO();
    public abstract ActivationDAO activationDAO();
    public abstract SubjectChapterDAO subjectChapterDAO();
    public abstract QuizModelDAO quizModelDAO();
    public abstract MockTestDAO mockTestDAO();
    public abstract RecomandedDAO recomandedDAO();
    public abstract ClassesDAO classesDAO();
    public abstract SDActivationDAO sdActivationDAO();
    public abstract NotificationsDAO notificationsDAO();

    private static volatile MyDatabase INSTANCE;

    public static MyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class, "tutorix_t.db")
                            .addMigrations(MIGRATION_1_2,MIGRATION_1_3,MIGRATION_2_3,MIGRATION_1_4,MIGRATION_2_4,MIGRATION_3_4)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    final static Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("create table classes (class_id text PRIMARY KEY NOT NULL,class_name text NOT NULL,class_folder text NOT NULL,class_status integer NOT NULL)");
            database.execSQL("ALTER TABLE activation_details "
                    + " ADD COLUMN secure_url text NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE activation_details "
                    + " ADD COLUMN data_url text NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE activation_details "
                    + " ADD COLUMN device_id text NOT NULL DEFAULT ''");



        }
    };
    final static Migration MIGRATION_1_3 = new Migration(1, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("create table classes (class_id text PRIMARY KEY NOT NULL,class_name text NOT NULL,class_folder text NOT NULL,class_status integer NOT NULL)");
            database.execSQL("ALTER TABLE activation_details "
                    + " ADD COLUMN secure_url text NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE activation_details "
                    + " ADD COLUMN data_url text NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE activation_details "
                    + " ADD COLUMN device_id text NOT NULL DEFAULT ''");

            database.execSQL("create table sd_activation_details (_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,user_id text NOT NULL,classid text NOT NULL,activation_end_date text NOT NULL,act_current_date text NOT NULL,activation_key text NOT NULL,device_id text NOT NULL)");

        }
    };
    final static Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("create table sd_activation_details (_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,user_id text NOT NULL,classid text NOT NULL,activation_end_date text NOT NULL,act_current_date text NOT NULL,activation_key text NOT NULL,device_id text NOT NULL)");



        }
    };

    final static Migration MIGRATION_1_4 = new Migration(1, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("create table classes (class_id text PRIMARY KEY NOT NULL,class_name text NOT NULL,class_folder text NOT NULL,class_status integer NOT NULL)");
            database.execSQL("ALTER TABLE activation_details "
                    + " ADD COLUMN secure_url text NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE activation_details "
                    + " ADD COLUMN data_url text NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE activation_details "
                    + " ADD COLUMN device_id text NOT NULL DEFAULT ''");

            database.execSQL("create table sd_activation_details (_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,user_id text NOT NULL,classid text NOT NULL,activation_end_date text NOT NULL,act_current_date text NOT NULL,activation_key text NOT NULL,device_id text NOT NULL)");
            database.execSQL("create table notifications (id integer PRIMARY KEY AUTOINCREMENT NOT NULL,title text ,message text ,image text ,time text )");


        }
    };

    final static Migration MIGRATION_2_4 = new Migration(2, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {


            database.execSQL("create table sd_activation_details (_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,user_id text NOT NULL,classid text NOT NULL,activation_end_date text NOT NULL,act_current_date text NOT NULL,activation_key text NOT NULL,device_id text NOT NULL)");
            database.execSQL("create table notifications (id integer PRIMARY KEY AUTOINCREMENT NOT NULL,title text ,message text ,image text ,time text )");


        }
    };
    final static Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("create table notifications (id integer PRIMARY KEY AUTOINCREMENT NOT NULL,title text ,message text ,image text ,time text )");



        }
    };

}
