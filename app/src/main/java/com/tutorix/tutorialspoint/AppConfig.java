package com.tutorix.tutorialspoint;

import static android.content.Context.STORAGE_SERVICE;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.SDActivationDetails;
import com.tutorix.tutorialspoint.utility.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class AppConfig {

    public static final String BASE_URL_ONE = "https://rbse.tutorix.com/";
    public static final String BASE_URL_ONE_EN = "https://rbse.tutorix.com/en/";
    //Live
    public static final String BASE_URL = BASE_URL_ONE + "mobile/";
    public static final String BASE_URL_EN = BASE_URL_ONE + "en/mobile/";
    public static final String BASE_URL_V1 = BASE_URL_ONE + "api/v1/";
    public static final String BASE_URL_V1_EN = BASE_URL_ONE + "en/api/v1/";

    //Testing
    //public static final String BASE_URL = BASE_URL_ONE+"dev/mobile/";
    //public static final String BASE_URL_V1 = BASE_URL_ONE+"dev/api/v1/";
//Testing


    public static final String ENC_KEY = BuildConfig.ENC_KEY;
    public static final String JSON_DATA = "json_data";

    //public static String ACTIVATION_DATA_URL="https://d2vgb5tug4mj1f.cloudfront.net/";
    public static String ACTIVATION_DATA_URL = "";//Secure URL
    public static String NORMAL_DATA_URL = "";//Data URL
    public static boolean CALLED_EXPIERY = false;//Data URL
    //public static boolean ALERT_SHOULD_SHOW=false;//Data URL
    //public static long ALERT_TIME=0;//Data URL
    //public static long ALERT_TIME_TOTAL=0;//Data URL
    public static int ACTIVATION_EXPIERY_DAYS = 0;//Data URL
    public static String HINDI_AVAILABLE_CLASSES = "";//Data URL

    public static String readFromFile(String filepath) {

        String ret = "";

        try {
            InputStream inputStream = new FileInputStream(new File(filepath));

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString;
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            ret = stringBuilder.toString();
        } catch (FileNotFoundException e) {

        } catch (Exception e) {

        }

        return ret;
    }


    public static byte[] readFromFileBytes(String filepath) {


        byte[] bytes = null;
        try {


            FileInputStream inputStream =
                    new FileInputStream(filepath);

            // read fills buffer with data and returns
            // the number of bytes read (which of course
            // may be less than the buffer size, but
            // it will never be more).
            int total = 0;
            int nRead = 0;

            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);

            // Always close files.
            inputStream.close();


        } catch (FileNotFoundException e) {

        } catch (Exception e) {

        }

        return bytes;
    }

    /*public static String getSdCardPath(String classId) {

        String className = "";
        if (classId.equalsIgnoreCase("1")) {
            className = "class6";
        } else if (classId.equalsIgnoreCase("2")) {
            className = "class7";
        } else if (classId.equalsIgnoreCase("3")) {
            className = "class8";
        } else if (classId.equalsIgnoreCase("4")) {
            className = "class9";
        } else if (classId.equalsIgnoreCase("5")) {
            className = "class10";
        } else if (classId.equalsIgnoreCase("6")) {
            className = "class11";
        } else if (classId.equalsIgnoreCase("7")) {
            className = "class12";
        } else if (classId.equalsIgnoreCase("8")) {
            className = "iit-jee";
        } else if (classId.equalsIgnoreCase("9")) {
            className = "neet";
        }


        File fileList[] = new File("/storage/").listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath())
                        && file.isDirectory()
                        && file.canRead()) {
                    File f = new File(file.getAbsolutePath() + "/tutorix" + className + "/");
                    if (f.exists())
                        return file.getAbsolutePath() + "/tutorix/" + className + "/";
                }
            }
        }


        File sdcard = Environment.getExternalStorageDirectory();
        File files = new File(sdcard, "/tutorix/" + className + "/");
        if (!files.exists()) {
            fileList = new File("/storage/").listFiles();

            if (fileList != null) {
                for (File file : fileList) {
                    File f = new File(file.getAbsolutePath() + "/tutorix");

                    if (f.exists()) {
                        //Log.v("File Path Reading","File Path Reading 2 "+f.exists()+"  "+file.getAbsolutePath() + "/tutorix/" + className);
                        return file.getAbsolutePath() + "/tutorix/" + className + "/";

                    }
                }
            }

        }
        return files.getAbsolutePath();
    }*/

    /*public static String getFAQSSDCardPath() {


        File fileList[] = new File("/storage/").listFiles();

        if (fileList != null) {
            for (File file : fileList) {
                if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath())
                        && file.isDirectory()
                        && file.canRead()) {
                    File f = new File(file.getAbsolutePath() + "/tutorix" + "/");
                    if (f.exists())
                        return file.getAbsolutePath() + "/tutorix/" + "/";
                }
            }
        }

        File sdcard = Environment.getExternalStorageDirectory();
        File files = new File(sdcard, "/tutorix/" + "/");
        if (!files.exists()) {
            fileList = new File("/storage/").listFiles();

            if (fileList != null) {
                for (File file : fileList) {
                    File f = new File(file.getAbsolutePath() + "/tutorix");

                    if (f.exists()) {
                        //Log.v("File Path Reading","File Path Reading 2 "+f.exists()+"  "+file.getAbsolutePath() + "/tutorix/" + className);
                        return file.getAbsolutePath() + "/tutorix/" + "/";

                    }
                }
            }

        }
        return files.getAbsolutePath();
    }*/

  /*  public static boolean checkSdcard(String classId) {


        String className = "";
        if (classId.equalsIgnoreCase("1")) {
            className = "class6";
        } else if (classId.equalsIgnoreCase("2")) {
            className = "class7";
        } else if (classId.equalsIgnoreCase("3")) {
            className = "class8";
        } else if (classId.equalsIgnoreCase("4")) {
            className = "class9";
        } else if (classId.equalsIgnoreCase("5")) {
            className = "class10";
        } else if (classId.equalsIgnoreCase("6")) {
            className = "class11";
        } else if (classId.equalsIgnoreCase("7")) {
            className = "class12";
        } else if (classId.equalsIgnoreCase("8")) {
            className = "iit-jee";
        } else if (classId.equalsIgnoreCase("9")) {
            className = "neet";
        }
        File fileListall[] = new File("/storage/").listFiles();

        if (fileListall != null) {
            for (File file : fileListall) {
                if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath())
                        && file.isDirectory()
                        && file.canRead()) {
                    File f = new File(file.getAbsolutePath() + "/tutorix" + "/" + className + "/");
                    if (f.exists())
                        return true;
                }
            }
        }

        File sdcard = Environment.getExternalStorageDirectory();///storage/emulated/0

        File files = new File(sdcard, "/tutorix/" + className + "/");///storage/emulated/0/tutorix

        if (!files.exists()) {
            File fileList[] = new File("/storage/").listFiles();

            if (fileList != null) {
                for (File file : fileList) {

                    File f = new File(file.getAbsolutePath() + "/tutorix" + "/" + className + "/");
                    if (f.exists())
                        return f.exists();


                }
            }

        }

        return files.exists();
    }*/

    public static File getSdcard(String classId) {


        String className = "";
        if (classId.equalsIgnoreCase("1")) {
            className = "class6";
        } else if (classId.equalsIgnoreCase("2")) {
            className = "class7";
        } else if (classId.equalsIgnoreCase("3")) {
            className = "class8";
        } else if (classId.equalsIgnoreCase("4")) {
            className = "class9";
        } else if (classId.equalsIgnoreCase("5")) {
            className = "class10";
        } else if (classId.equalsIgnoreCase("6")) {
            className = "class11";
        } else if (classId.equalsIgnoreCase("7")) {
            className = "class12";
        } else if (classId.equalsIgnoreCase("8")) {
            className = "iit-jee";
        } else if (classId.equalsIgnoreCase("9")) {
            className = "neet";
        }
        File fileListall[] = new File("/storage/").listFiles();

        for (File file : fileListall) {
            if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath())
                    && file.isDirectory()
                    && file.canRead()) {
                File f = new File(file.getAbsolutePath() + "/tutorix" + "/" + className + "/");
                if (f.exists())
                    return f;
            }
        }
        File sdcard = Environment.getExternalStorageDirectory();///storage/emulated/0

        File files = new File(sdcard, "/tutorix/" + className + "/");///storage/emulated/0/tutorix

        File fileList[] = new File("/storage/").listFiles();

        for (File file : fileList) {
           /* if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath())
                    && file.isDirectory()
                    && file.canRead())
            {*/
            File f = new File(file.getAbsolutePath() + "/tutorix" + "/" + className + "/");
            //Log.v("Path of files","Path of files "+f.getAbsolutePath());
            return f;
            // }


        }
        return files;
    }

    public static String getSubjectName(String subjectid) {
        String subjectname = "";
        if (subjectid.equalsIgnoreCase("1")) {
            subjectname = "physics";
        } else if (subjectid.equalsIgnoreCase("2")) {
            subjectname = "chemistry";
        } else if (subjectid.equalsIgnoreCase("4")) {
            subjectname = "mathematics";
        } else if (subjectid.equalsIgnoreCase("3")) {
            subjectname = "biology";
        }
        return subjectname;
    }

    public static String getSubjectNameCapital(String subjectid) {
        String subjectname = "";
        if (subjectid.equalsIgnoreCase("1")) {
            subjectname = "Physics";
        } else if (subjectid.equalsIgnoreCase("2")) {
            subjectname = "Chemistry";
        } else if (subjectid.equalsIgnoreCase("4")) {
            subjectname = "Mathematics";
        } else if (subjectid.equalsIgnoreCase("3")) {
            subjectname = "Biology";
        }
        return subjectname;
    }


    public static String getOnlineURL(String classname, Boolean hindi) {
        String classid = "";

        if (classname.equalsIgnoreCase("1")) {
            classid = "class6/";
        } else if (classname.equalsIgnoreCase("2")) {
            classid = "class7/";
        } else if (classname.equalsIgnoreCase("3")) {
            classid = "class8/";
        } else if (classname.equalsIgnoreCase("4")) {
            classid = "class9/";
        } else if (classname.equalsIgnoreCase("5")) {
            classid = "class10/";
        } else if (classname.equalsIgnoreCase("6")) {
            classid = "class11/";
        } else if (classname.equalsIgnoreCase("7")) {
            classid = "class12/";
        } else if (classname.equalsIgnoreCase("8")) {
            classid = "iit-jee/";
        } else if (classname.equalsIgnoreCase("9")) {
            classid = "neet/";
        }
        if (hindi) {
            return ACTIVATION_DATA_URL + classid;
        } else if (AppController.childQaulityAudio == 0) {
            //Log.v("ACTIVATION_DATA_URL","ACTIVATION_DATA_URL "+ACTIVATION_DATA_URL);
            return ACTIVATION_DATA_URL + classid;
        } else {
            return ACTIVATION_DATA_URL + "hindi/" + classid;
        }
    }

    public static String getOnlineURLVideo(String classname) {
        String classid = "";

        if (classname.equalsIgnoreCase("1")) {
            classid = "class6/";
        } else if (classname.equalsIgnoreCase("2")) {
            classid = "class7/";
        } else if (classname.equalsIgnoreCase("3")) {
            classid = "class8/";
        } else if (classname.equalsIgnoreCase("4")) {
            classid = "class9/";
        } else if (classname.equalsIgnoreCase("5")) {
            classid = "class10/";
        } else if (classname.equalsIgnoreCase("6")) {
            classid = "class11/";
        } else if (classname.equalsIgnoreCase("7")) {
            classid = "class12/";
        } else if (classname.equalsIgnoreCase("8")) {
            classid = "iit-jee/";
        } else if (classname.equalsIgnoreCase("9")) {
            classid = "neet/";
        }
        return ACTIVATION_DATA_URL + classid;

    }

    public static String getOnlineURLImage(String classname) {
        String classid = "";

        if (classname.equalsIgnoreCase("1")) {
            classid = "class6/";
        } else if (classname.equalsIgnoreCase("2")) {
            classid = "class7/";
        } else if (classname.equalsIgnoreCase("3")) {
            classid = "class8/";
        } else if (classname.equalsIgnoreCase("4")) {
            classid = "class9/";
        } else if (classname.equalsIgnoreCase("5")) {
            classid = "class10/";
        } else if (classname.equalsIgnoreCase("6")) {
            classid = "class11/";
        } else if (classname.equalsIgnoreCase("7")) {
            classid = "class12/";
        } else if (classname.equalsIgnoreCase("8")) {
            classid = "iit-jee/";
        } else if (classname.equalsIgnoreCase("9")) {
            classid = "neet/";
        } else {
            if (NORMAL_DATA_URL.isEmpty())
                return "https://d2vgb5tug4mj1f.cloudfront.net/" + classid;
            else return NORMAL_DATA_URL + classid;
        }


        if (AppController.childQaulityAudio == 0) {
            if (NORMAL_DATA_URL.isEmpty())
                return "https://d2vgb5tug4mj1f.cloudfront.net/" + classid;
            else return NORMAL_DATA_URL + classid;
        } else {
            if (NORMAL_DATA_URL.isEmpty())
                return "https://d2vgb5tug4mj1f.cloudfront.net/" + "hindi/" + classid;
            else return NORMAL_DATA_URL + "hindi/" + classid;
        }
    }

    public static String getClassName(String classId) {
        String classid = "";

        if (classId.equalsIgnoreCase("1")) {
            classid = "class6";
        } else if (classId.equalsIgnoreCase("2")) {
            classid = "class7";
        } else if (classId.equalsIgnoreCase("3")) {
            classid = "class8";
        } else if (classId.equalsIgnoreCase("4")) {
            classid = "class9";
        } else if (classId.equalsIgnoreCase("5")) {
            classid = "class10";
        } else if (classId.equalsIgnoreCase("6")) {
            classid = "class11";
        } else if (classId.equalsIgnoreCase("7")) {
            classid = "class12";
        } else if (classId.equalsIgnoreCase("8")) {
            classid = "iit-jee";
        } else if (classId.equalsIgnoreCase("9")) {
            classid = "neet";
        }

        return classid;
    }

    public static String getClassNameDisplay(String classId) {
        String classid = "";

        if (classId.equalsIgnoreCase("1")) {
            classid = "6th Class";
        } else if (classId.equalsIgnoreCase("2")) {
            classid = "7th Class";
        } else if (classId.equalsIgnoreCase("3")) {
            classid = "8th Class";
        } else if (classId.equalsIgnoreCase("4")) {
            classid = "9th Class";
        } else if (classId.equalsIgnoreCase("5")) {
            classid = "10th Class";
        } else if (classId.equalsIgnoreCase("6")) {
            classid = "11th Class";
        } else if (classId.equalsIgnoreCase("7")) {
            classid = "12th Class";
        } else if (classId.equalsIgnoreCase("8")) {
            classid = "IIT-JEE";
        } else if (classId.equalsIgnoreCase("9")) {
            classid = "NEET";
        }

        return classid;
    }

    public static String getClassNameDisplayClass(String classId) {
        String classid = "";

        String _class = AppController.getInstance().getString(R.string._class);
        if (classId.equalsIgnoreCase("1")) {
            classid = _class + " - 6";
        } else if (classId.equalsIgnoreCase("2")) {
            classid = _class + " - 7";
        } else if (classId.equalsIgnoreCase("3")) {
            classid = _class + " - 8";
        } else if (classId.equalsIgnoreCase("4")) {
            classid = _class + " - 9";
        } else if (classId.equalsIgnoreCase("5")) {
            classid = _class + " - 10";
        } else if (classId.equalsIgnoreCase("6")) {
            classid = _class + " - 11";
        } else if (classId.equalsIgnoreCase("7")) {
            classid = _class + " - 12";
        } else if (classId.equalsIgnoreCase("8")) {
            classid = "IIT-JEE";
        } else if (classId.equalsIgnoreCase("9")) {
            classid = "NEET";
        }

        return classid;
    }

    public static boolean checkSDCardEnabled(Context ctx, String userId, String classId) {
        SDActivationDetails sdActivationDetails = MyDatabase.getDatabase(ctx).sdActivationDAO().getActivationDetails(userId, classId);
        if (sdActivationDetails != null && !sdActivationDetails.activation_key.isEmpty()) {
            //Log.v("SDCARD","SDCARD "+sdActivationDetails.activation_key);
            return true;
        }

        return false;
    }

    public static Configuration setLanguages(Context context) {
        Configuration config = context.getResources().getConfiguration();
        AppController.childQaulityAudio = SessionManager.getAudio(AppController.getInstance());
        String lang = "hi";// your language code
        if (AppController.childQaulityAudio == 0) {
            lang = "en";
            Constants.updateBaseUrl(BASE_URL_EN, BASE_URL_ONE_EN, BASE_URL_V1_EN);
        } else {
            lang = "hi";
            Constants.updateBaseUrl(BASE_URL, BASE_URL_ONE, BASE_URL_V1);
        }
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(locale);
        else
            config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        return config;
    }


    //Updated for Manage External Storage


    public static String getSdCardPath(String classId,Context ctx) {


        String className = "";
        if (classId.equalsIgnoreCase("1")) {
            className = "class6";
        } else if (classId.equalsIgnoreCase("2")) {
            className = "class7";
        } else if (classId.equalsIgnoreCase("3")) {
            className = "class8";
        } else if (classId.equalsIgnoreCase("4")) {
            className = "class9";
        } else if (classId.equalsIgnoreCase("5")) {
            className = "class10";
        } else if (classId.equalsIgnoreCase("6")) {
            className = "class11";
        } else if (classId.equalsIgnoreCase("7")) {
            className = "class12";
        } else if (classId.equalsIgnoreCase("8")) {
            className = "iit-jee";
        } else if (classId.equalsIgnoreCase("9")) {
            className = "neet";
        }

        /*File fileList[] = new File("/storage/").listFiles();
        if (fileList != null) {
            Log.d("Calling the Files","Calling the Files SDCARD 1 ");
            for (File file : fileList) {
                Log.d("Calling the Files","Calling the Files SDCARD 2 ");
                if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath())
                        && file.isDirectory()
                        && file.canRead()) {
                    Log.d("Calling the Files","Calling the Files SDCARD 3 ");
                    File f = new File(file.getAbsolutePath() + "/tutorix" + className + "/");
                    if (f.exists())
                        return file.getAbsolutePath() + "/tutorix/" + className + "/";
                }
            }
        }*/
        if (Build.VERSION.SDK_INT >= 30) {

            List<StorageVolume> volumes = ((StorageManager) ctx.getSystemService(STORAGE_SERVICE)).getStorageVolumes();
            for (int k = 0; k < volumes.size(); k++) {

                File files = new File(volumes.get(k).getDirectory(), "/tutorix/" + className );///storage/emulated/0/tutorix

                if(files.exists())
                {

                    return files.getAbsolutePath()+"/";
                }

            }
            File sdcard = Environment.getExternalStorageDirectory();///storage/emulated/0


            File files = new File(sdcard, "/tutorix/" + className + "/");///storage/emulated/0/tutorix
            return files.getAbsolutePath();


        }

        else {
            File fileList[];
            File sdcard = Environment.getExternalStorageDirectory();
            File files = new File(sdcard, "/tutorix/" + className + "/");
            if (!files.exists()) {
                fileList = new File("/storage/").listFiles();

                if (fileList != null) {
                    for (File file : fileList) {
                        File f = new File(file.getAbsolutePath() + "/tutorix");

                        if (f.exists()) {
                            //Log.v("File Path Reading","File Path Reading 2 "+f.exists()+"  "+file.getAbsolutePath() + "/tutorix/" + className);
                            return file.getAbsolutePath() + "/tutorix/" + className + "/";

                        }
                    }
                }

            }
            return files.getAbsolutePath();

        }



    }

    public static boolean checkSdcard(String classId,Context ctx) {


        String className = "";
        if (classId.equalsIgnoreCase("1")) {
            className = "class6";
        } else if (classId.equalsIgnoreCase("2")) {
            className = "class7";
        } else if (classId.equalsIgnoreCase("3")) {
            className = "class8";
        }else if (classId.equalsIgnoreCase("4")) {
            className = "class9";
        }else if (classId.equalsIgnoreCase("5")) {
            className = "class10";
        }else if (classId.equalsIgnoreCase("6")) {
            className = "class11";
        }else if (classId.equalsIgnoreCase("7")) {
            className = "class12";
        }else if (classId.equalsIgnoreCase("8")) {
            className = "iit-jee";
        }else if (classId.equalsIgnoreCase("9")) {
            className = "neet";
        }
        //  File fileListall[] = new File("/storage/").listFiles();
/*
        if(fileListall!=null)
        {
            for (File file : fileListall) {
                if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath())
                        && file.isDirectory()
                        && file.canRead()) {
                    File f= new File(file.getAbsolutePath() + "/tutorix"+"/"+className+"/");
                    if(f.exists())
                        return true;
                }
            }
        }*/

        File fileListall[];
        if (Build.VERSION.SDK_INT >= 30) {

            List<StorageVolume> volumes = ((StorageManager) ctx.getSystemService(STORAGE_SERVICE)).getStorageVolumes();
            for (int k = 0; k < volumes.size(); k++) {


                File files = new File(volumes.get(k).getDirectory(), "/tutorix/" + className );///storage/emulated/0/tutorix

                if (files.exists()) {
                    return true;


                }else
                {

                    File fileList[] = new File("/storage/").listFiles();

                    if (fileList != null) {
                        for (File file : fileList) {

                            File f = new File(file.getAbsolutePath() + "/tutorix" + "/" + className + "/");
                            if (f.exists())
                                return f.exists();


                        }
                    }
                }

            }

            return  false;


        }else {
            File sdcard = Environment.getExternalStorageDirectory();///storage/emulated/0

            File files = new File(sdcard, "/tutorix/" + className + "/");///storage/emulated/0/tutorix

            if (!files.exists()) {
                File fileList[] = new File("/storage/").listFiles();

                if (fileList != null) {
                    for (File file : fileList) {

                        File f = new File(file.getAbsolutePath() + "/tutorix" + "/" + className + "/");
                        if (f.exists())
                            return f.exists();


                    }
                }

            }

            return files.exists();
        }

    }

    public static String getFAQSSDCardPath(Context ctx) {




        File fileList[] /*= new File("/storage/").listFiles()*/;



        if (Build.VERSION.SDK_INT >= 30) {

            List<StorageVolume> volumes = ((StorageManager) ctx.getSystemService(STORAGE_SERVICE)).getStorageVolumes();
            for (int k = 0; k < volumes.size(); k++) {


                File files = new File(volumes.get(k).getDirectory(), "/tutorix" );///storage/emulated/0/tutorix

                if (files.exists()) {
                    return files.getAbsolutePath()+"/";


                }else
                {
                    fileList = new File("/storage/").listFiles();

                    if (fileList != null) {
                        for (File file : fileList) {

                            File f = new File(file.getAbsolutePath() + "/tutorix" );
                            if (f.exists())
                                return f.getAbsolutePath()+"/";


                        }
                    }
                }

            }

            return "";


        }else {
            File sdcard = Environment.getExternalStorageDirectory();
            File files = new File(sdcard, "/tutorix/" + "/");
            if(!files.exists())
            {
                fileList = new File("/storage/").listFiles();

                if(fileList!=null)
                {
                    for (File file : fileList) {
                        File f= new File(file.getAbsolutePath() + "/tutorix");

                        if(f.exists()) {
                            //Log.v("File Path Reading","File Path Reading 2 "+f.exists()+"  "+file.getAbsolutePath() + "/tutorix/" + className);
                            return file.getAbsolutePath() + "/tutorix/" + "/";

                        }
                    }
                }

            }
            return files.getAbsolutePath();
        }

    }
}
