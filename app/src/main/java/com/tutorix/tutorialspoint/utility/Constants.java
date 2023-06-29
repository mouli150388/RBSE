package com.tutorix.tutorialspoint.utility;


import com.tutorix.tutorialspoint.AppConfig;

@SuppressWarnings("unused")
public class Constants {

    // API
    //public static String USER_LOGIN = AppConfig.BASE_URL +"users/login";
    public static String USER_FORGOT = AppConfig.BASE_URL + "users/forgot";
    public static String USER_LOGOUT = AppConfig.BASE_URL + "users/logout";
    public static String USER_TRACK = AppConfig.BASE_URL + "users/track";
    public static String USER_QUIZ_ALL = AppConfig.BASE_URL + "users/quiz/all";
    public static String USER_ACTIVATION_KEY = AppConfig.BASE_URL + "users/activation/key";
    public static String USER_NOTIFICATIONS = AppConfig.BASE_URL + "users/notifications";
    public static String OTP = AppConfig.BASE_URL + "otp";
    public static String SWITCH_CLASS = AppConfig.BASE_URL + "payment";
    public static String PAYMENT = AppConfig.BASE_URL + "payment";
    public static String PAYMENT_PRICE = AppConfig.BASE_URL + "payment/price";
    public static String PAYMENT_COUPON = AppConfig.BASE_URL + "payment/coupon";
    public static String FORGOT_PASSWORD = AppConfig.BASE_URL + "users/forgot/validate";
    public static String RESEND_OTP = AppConfig.BASE_URL + "otp/resend";
    public static String USER_PASSWORD = AppConfig.BASE_URL + "users/password";
    //public static String USER_TRACK_TIME = AppConfig.BASE_URL +"users/track/time";
    public static String USER_TRACK_TIME = AppConfig.BASE_URL + "users/progress/graph";
    public static String USER_TRACK_TIME_SUBJECT = AppConfig.BASE_URL + "users/progress/subjects/graph";
    public static String USER_QUIZ_ANALYSIS = AppConfig.BASE_URL + "users/quiz/graph";//{subject_id}
    public static String USER_QUIZ_COMPLETED = AppConfig.BASE_URL + "users/quiz/completed";
    public static String USER_QUIZ = AppConfig.BASE_URL + "users/quiz";
    public static String USER_MOCKTEST = AppConfig.BASE_URL + "users/quiz";
    public static String USER_GET_MOCKTEST = AppConfig.BASE_URL + "users/quiz/mocktests";
    public static String USER_GET_MOCKTEST_PERFORMANCE = AppConfig.BASE_URL + "users/quiz/mocktests/percentage";
    public static String QUIZ_ADD_RECOMANDED = AppConfig.BASE_URL + "users/quiz/mocktests/recommended";
    public static String QUIZ_SYNC_TO_SERVER_RECOMANDED = AppConfig.BASE_URL + "users/quiz/mocktests/recommended/sync";
    public static String QUIZ_SYNC_FROM_SERVER_MOCKSTATS = AppConfig.BASE_URL + "users/quiz/mocktests/stats/sync";
    public static String QUIZ_SECTION_MOCK_GRAPH = AppConfig.BASE_URL + "users/quiz/mocktests/sections/graph";
    public static String USERS = AppConfig.BASE_URL + "users";
    public static String USERS_FEEDBACKS = AppConfig.BASE_URL + "users/feedbacks";
    public static String USER_SCHOOL = AppConfig.BASE_URL + "users/school";
    public static String LECTURE_ACTIONS = AppConfig.BASE_URL + "lectures/actions";
    public static String LECTURE_ACTIONS_NOTES = AppConfig.BASE_URL + "lectures/actions/notes";
    public static String FIREBASE = AppConfig.BASE_URL + "firebase";
    public static String UPLOAD_IMAGE = AppConfig.BASE_URL + "users/image";
    public static String CHECK_ACCESSTOKEN = AppConfig.BASE_URL + "users/accesstoken/check";
    public static String DEATCTIVATE_USER = AppConfig.BASE_URL + "users/activation/deactive";
    public static String IMAGE_PATH = AppConfig.BASE_URL_ONE + "assets/profiles/";
    public static String MY_ORDERS = AppConfig.BASE_URL + "users/orders";
    public static String ORDER_DETAILS = AppConfig.BASE_URL + "users/orders";//{order_id}
    public static String GET_ALL_PRICE = AppConfig.BASE_URL + "payment/price/all/";//{order_id}
    public static String GET_ALL_YEARS = AppConfig.BASE_URL + "payment/price/years";//{order_id}
    public static String GET_ALL_MONTHS = AppConfig.BASE_URL + "payment/price/months";//{order_id}


    public static String IMAGE_REQUAET_URL = AppConfig.BASE_URL_ONE + "assets/questions/";//{userid}/{file_name}
    public static String GET_DOUBT_DETAILS = AppConfig.BASE_URL + "users/ask/doubts/answers/";//{{question_id}}
    public static String CLOSE_DOUBT = AppConfig.BASE_URL + "users/ask/doubts/answers/close";//{{question_id}}
    public static String DOUBT_REPLY = AppConfig.BASE_URL + "users/ask/doubts/answers/reply";//{{question_id}}
    public static String SEND_VIDEO_RATING = AppConfig.BASE_URL + "users/feedbacks/lecture";//{{question_id}}


    public static String SYLLABUS = AppConfig.BASE_URL_ONE + "syllabus.htm?vma=app";
    public static String ABOUT = AppConfig.BASE_URL_ONE + "about_us_m.htm";
    public static String FAQ = AppConfig.BASE_URL_ONE + "faqs_m.htm";
    public static String TERMS_POLICY = AppConfig.BASE_URL_ONE + "terms_of_use_m.htm";
    public static String EDUCATION_FOR_ALL = AppConfig.BASE_URL_ONE + "education-for-all.php";
    public static String OFFICIAL_WEBSITE = "https://www.tutorix.com/";
    public static String REFUND_POLICY = AppConfig.BASE_URL_ONE + "refund_policy_m.htm";
    public static String PRIVACY_POLICY = AppConfig.BASE_URL_ONE + "privacy_policy_m.htm";
    public static String FACULTY_CURRENT = "https://d2vgb5tug4mj1f.cloudfront.net/%s/faculties/faculties.json";

    public static String UN_CHECK_COOKIES = AppConfig.BASE_URL_ONE + "unset_cookies.php";
    //public static String CHECK_EXPIERY =AppConfig.BASE_URL+"users/activation/check" ;
    public static String CHECK_EXPIERY = AppConfig.BASE_URL + "users/activation/checkUpdate";

    public static String GET_ALL_CLASSES = "https://d2vgb5tug4mj1f.cloudfront.net/classes_v2.json";
    public static String CHECK_COOKIES = AppConfig.BASE_URL_ONE + "set_cookies.php";
    //public static String CHECK_COOKIES =AppConfig.BASE_URL_ONE+"dev/set_cookies.php" ;
    //public static String GET_ALL_CLASSES ="https://d2vgb5tug4mj1f.cloudfront.net/classes_demo.json";


    /*New APIS  08-01-2020*/
    public static String SET_REFRESH_TOKEN = AppConfig.BASE_URL + "users/accesstoken/refresh";
    public static String SDCARD_ACTIVATION = AppConfig.BASE_URL + "sdcard/activation";
    public static String GET_MY_REWARDS = AppConfig.BASE_URL + "rewards/points";
    public static String GET_YEARS_SUBSCRIPTION = AppConfig.BASE_URL + "payment/coupon";
    // public static String SDCARD_ACTIVATION =AppConfig.BASE_URL+"sdcard/activation" ;


    //Version V1 APIS
    public static String DOUBT_RELATED_QTNS = AppConfig.BASE_URL_V1 + "relatedDoubts";
    public static String DOUBT_ASK_QTNS = AppConfig.BASE_URL_V1 + "askDoubt";
    public static String DOUBT_GET_MYDOUBTS = AppConfig.BASE_URL_V1 + "getUserDoubts";
    public static String DOUBT_GET_LATESTDOUBTS = AppConfig.BASE_URL_V1 + "getPublishedDoubts";
    public static String DOUBT_GET_ANSWERS = AppConfig.BASE_URL_V1 + "getUserDoubt";
    public static String DOUBT_GET_ANSWERS_PUBLISHED = AppConfig.BASE_URL_V1 + "getPublishedDoubt";
    public static String DOUBT_SET_LIKE = AppConfig.BASE_URL_V1 + "setAnswerLike";
    public static String DOUBT_REPLY_NEW = AppConfig.BASE_URL_V1 + "setAnswerReply";
    public static String DOUBT_REPORT = AppConfig.BASE_URL_V1 + "serAnswerReport";
    public static String DOUBT_DELETE = AppConfig.BASE_URL_V1 + "deleteQuestion";
    public static String SEARCH_VIDEOS = AppConfig.BASE_URL_V1 + "searchLectures";
    public static String GET_DOUBT_FILTERS = AppConfig.BASE_URL_V1 + "getFilterOptions";
    public static String GET_OTP = AppConfig.BASE_URL_V1 + "getOtp";//{{question_id}}
    public static String REGISTEATION_REF = AppConfig.BASE_URL_V1 + "setNewUser";//{{question_id}}
    public static String REFERRAL_DETAILS = AppConfig.BASE_URL_V1 + "getReferralCode";//{{question_id}}

    public static String USER_LOGIN = AppConfig.BASE_URL_V1 + "getUserLogin";
    public static String MOCKTEST_NEW_LIST = AppConfig.BASE_URL_V1 + "mock_test_list.json";
    public static String SUBMIT_TEST_SERIES = AppConfig.BASE_URL_V1 + "setTestSeriesData";
    public static String GET_TEST_SERIES_TRACK = AppConfig.BASE_URL_V1 + "getTestSeriesTrackData";
    public static String GET_TEST_SERIES_TRACK_REVIEW = AppConfig.BASE_URL_V1 + "getTestSeriesReview";
    public static String GET_DELETE_ACCOUNT_OTP = AppConfig.BASE_URL_V1 + "getDeleteAccountOTP";
    public static String GET_DELETE_ACCOUNT = AppConfig.BASE_URL_V1 + "deleteAccount";
    public static String GET_TUTION_STUDENTS = AppConfig.BASE_URL_V1 + "getTuitionStudents";
    public static String GET_BATCH_FILTERS = AppConfig.BASE_URL_V1 + "getBatchFilters";
    public static String GET_BATCH_LIST = AppConfig.BASE_URL_V1 + "getBatchFilters";
    public static String SET_STUDENT_BATCH = AppConfig.BASE_URL_V1 + "setStudentToBatch";
    public static String GET_SCHOOL_INFO = AppConfig.BASE_URL_V1 + "getSchoolData";
    public static String SET_SCHOOL_INFO = AppConfig.BASE_URL_V1 + "setUserSchoolData";

    // Reg Tag
    public static String reqRegister = "req_register";

    //Intent
    public static String chapterIntent = "chapterIntent";
    public static String imageUrl = "imageUrl";

    //Coupon
    public static String couponCode = "coupon_code";

    //Currency Type
    public static String INR = "INR";
    public static String USD = "USD";
    public static final String DATA_URL = "data_url";
    public static final String SECURE_DATA_URL = "secure_url";
    public static String inrPrice = "class_inr_price";
    public static String usdPrice = "class_usd_price";
    public static String inrDiscountPrice = "discount_inr_price";
    public static String usdDiscountPrice = "discount_usd_price";

    // Request and Response
    public static String request = "request";
    public static String response = "response";

    // gender
    public static String male = "Male";
    public static String female = "Female";

    // Params
    public static String mobileNumber = "mobile_number";
    public static String password = "password";
    public static String oldPassword = "old_password";
    public static String newPassword = "new_password";
    public static String deviceId = "device_id";
    public static String app_version = "app_version";
    public static String firebaseToken = "firebase_token";
    public static String mobile_country_code = "mobile_country_code";
    public static String otpCode = "otp_code";
    public static String errorFlag = "error_flag";
    public static String message = "message";
    public static String url = "url";
    public static String phoneNumber = "PhoneNumber";
    public static String userId = "user_id";
    public static String student_id = "student_id";
    public static String userName = "user_name";
    public static String fullName = "full_name";
    public static String fatherName = "father_name";
    public static String dob = "date_of_birth";
    public static String emailId = "email_id";
    public static String address = "address";
    public static String city = "city";
    public static String state = "state";
    public static String country_code = "country_code";
    public static String user_current_class = "user_current_class";
    public static String postal_code = "postal_code";
    public static String gender = "gender";
    public static String schoolName = "school_name";
    public static String rollNumber = "roll_number";
    public static String sectionName = "section_name";
    public static String classId = "class_id";
    public static String sectionId = "section_id";
    public static String mockTest = "mock_test";
    public static String lectureId = "lecture_id";
    public static String accessToken = "access_token";
    public final static String activationType = "activation_type";
    public final static String action_type = "action_type";
    public final static String activationKey = "activation_key";
    public final static String activationEndDate = "activation_end_date";
    public final static String currentDate = "current_date";
    public static String ACTIVATION_DEVICE_ID = "activation_device_id";
    public final static String remaingDays = "days_left";
    public static String notes = "notes";
    public static String OS_TYPE = "os_type";//A-android
    public static String IMG_FILED = "base64_image";//A-android
    public static String photo = "photo";//A-android

    public static String activationStatus = "status";
    public static String school_address = "school_address";
    public static String school_city = "school_city";
    public static String school_state = "school_state";
    public static String school_zip = "school_postal_code";
    public static String school_country = "school_country_code";


    public static String subjectName = "subject_name";
    public static String subjectId = "subject_id";
    public static String marks = "marks";
    public static String reference = "reference";

    public static String trackData = "track_data";

    public static String physics = "physics";
    public static String chemistry = "chemistry";
    public static String mathematics = "mathematics";
    public static String biology = "biology";

    public static String physicsId = "1";
    public static String chemistryId = "2";
    public static String biologyId = "3";
    public static String mathsId = "4";

    public static String faculties = "faculties";
    public static String facultyId = "faculty_id";
    public static String facultyName = "full_name";
    public static String introduction = "introduction";
    public static String expertise = "expertise";
    public static String photoUrl = "photo_url";
    public static String videoUrl = "video_url";
    public static String videoThumbUrl = "video_thumb_url";

    // Preference Key
    public static String userInfo = "USER_INFO";
    public static String logo = "logo";
    public static String intentType = "intentType";
    public static String global = "global";
    public static String lecture = "lecture";
    public static String lectureName = "lecture_name";
    public static String lectureDuration = "lecture_duration";
    public static String lectureVideoUrl = "lecture_video_url";
    public static String lectureSRTUrl = "lecture_video_srt";
    public static String lectureNotes = "user_lecture_notes";
    public static String lectureVideoThumb = "lecture_video_thumb";
    public static String completedFlag = "completed_flag";
    public static String bookmarkFlag = "bookmark_flag";
    public static String notes_flag = "lecture_notes_flag";
    public static String token_register = "token_register";
    public static String userType = "user_type";
    public static String NETWORKSTATUS = "android.net.conn.CONNECTIVITY_CHANGE";

    public final static String activationStartData = "activation_start_date";//yyyy-mm-dd(2019-01-17)


    public static String base64_question_image = "base64_question_image";
    public static String question_text = "question_text";
    public static String question_id = "question_id";
    public static String question_class_id = "question_class_id";
    public static String answer_id = "answer_id";
    public static String report_message = "report_message";
    public static String VIDEO_NAME = "playlist.m3u8";
    public static String VIDEO_SRT = "closed_captions.srt";
    public static String NOTE_FILE = "notes.html";
    public static String QUIZ_FILE = "quiz.json";
    public static String VIDEO_THUMB_NAME = "";

    public static String LANG_VIDEO_SUPPORT[] = {"english"};


    public static boolean isHadCookie = false;
    public static boolean isOpened = false;
    public static String TUTORIX_SCHOOL_FLAG = "tutorix_school_flag";


    static String assets = "file:///android_asset";
   /* public static String JS_FILES = "<link rel=\"stylesheet\" href=" + assets + "/katex.css>" +
            "\n" +
            "<script src=" + assets + "/katex.js></script>\n"+"<style>img{display: inline;height: auto;max-width: 100%;}</style>";
*/
  /*  public static String JS_FILES=

                    "<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/katex@0.11.1/dist/katex.min.css'  crossorigin='anonymous'>"+

"<script type='text/javascript' src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML'> </script>"+
          "<style>img{display: inline;height: auto;max-width: 100%;}</style>";;*/


    public static String JS_FILES = "<script>\n" +
            "MathJax = {" +
            "  tex: {" +
            "    inlineMath: [['$', '$'], ['\\(', '\\)']]\n" +
            "  }" +
            "};" +
            "</script>" + "<style type=\"text/css\">\n" +
            "html, body {" +
            "margin: 0px;" +
            "padding: 0px;" +
            "}" +
            "</style>" +
            "<script id=\"MathJax-script\" async src=\"https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-chtml.js\"></script>\n" +
            "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/katex@0.11.1/dist/katex.min.css\" crossorigin=\"anonymous\">" + "<style>img{display: inline;height: auto;max-width: 100%;}</style>";
    ;


    public static String MathJax_Offline = "<script>\n" +
            "MathJax = {" +
            "  tex: {" +
            "    inlineMath: [['$', '$'], ['\\(', '\\)']]\n" +
            "  }" +
            "};" +
            "</script>" + "<style type=\"text/css\">\n" +
            "html, body {" +
            "margin: 0px;" +
            "padding: 0px;" +
            "}" +
            "</style>" +
            "<script src=" + assets + "/mathjax/es5/tex-chtml.js></script>" +
            "<style>img{" + "max-width: 100%; " +
            "width:auto; height: " +
            "auto" +
            "                }</style>" +
            "<style>img{display: inline;height: auto;max-width: 100%;}</style>";


    public static String MATH_SCRIBE = "<link rel=\"stylesheet\" href=" + assets + "/mathscribe/jqmath-0.4.3.css>" +
            "\n" +
            "<script src=" + assets + "/mathscribe/jquery-1.4.3.min.js></script>\n" +
            "<script src=" + assets + "/mathscribe/jqmath-etc-0.4.6.min.js charset=\"utf-8\"></script>" +
            "<style>img{" + "max-width: 100%; " +
            "width:auto; height: " +
            "auto" +

            "                }</style>";



    public static void updateBaseUrl(String NEW_BASE,String BASE_NEW_ONE,String NEW_BASE_V1 ) {

        // API
        //USER_LOGIN = NEW_BASE +"users/login";
        USER_FORGOT = NEW_BASE + "users/forgot";
        USER_LOGOUT = NEW_BASE + "users/logout";
        USER_TRACK = NEW_BASE + "users/track";
        USER_QUIZ_ALL = NEW_BASE + "users/quiz/all";
        USER_ACTIVATION_KEY = NEW_BASE + "users/activation/key";
        USER_NOTIFICATIONS = NEW_BASE + "users/notifications";
        OTP = NEW_BASE + "otp";
        SWITCH_CLASS = NEW_BASE + "payment";
        PAYMENT = NEW_BASE + "payment";
        PAYMENT_PRICE = NEW_BASE + "payment/price";
        PAYMENT_COUPON = NEW_BASE + "payment/coupon";
        FORGOT_PASSWORD = NEW_BASE + "users/forgot/validate";
        RESEND_OTP = NEW_BASE + "otp/resend";
        USER_PASSWORD = NEW_BASE + "users/password";
        //USER_TRACK_TIME = NEW_BASE +"users/track/time";
        USER_TRACK_TIME = NEW_BASE + "users/progress/graph";
        USER_TRACK_TIME_SUBJECT = NEW_BASE + "users/progress/subjects/graph";
        USER_QUIZ_ANALYSIS = NEW_BASE + "users/quiz/graph";//{subject_id}
        USER_QUIZ_COMPLETED = NEW_BASE + "users/quiz/completed";
        USER_QUIZ = NEW_BASE + "users/quiz";
        USER_MOCKTEST = NEW_BASE + "users/quiz";
        USER_GET_MOCKTEST = NEW_BASE + "users/quiz/mocktests";
        USER_GET_MOCKTEST_PERFORMANCE = NEW_BASE + "users/quiz/mocktests/percentage";
        QUIZ_ADD_RECOMANDED = NEW_BASE + "users/quiz/mocktests/recommended";
        QUIZ_SYNC_TO_SERVER_RECOMANDED = NEW_BASE + "users/quiz/mocktests/recommended/sync";
        QUIZ_SYNC_FROM_SERVER_MOCKSTATS = NEW_BASE + "users/quiz/mocktests/stats/sync";
        QUIZ_SECTION_MOCK_GRAPH = NEW_BASE + "users/quiz/mocktests/sections/graph";
        USERS = NEW_BASE + "users";
        USERS_FEEDBACKS = NEW_BASE + "users/feedbacks";
        USER_SCHOOL = NEW_BASE + "users/school";
        LECTURE_ACTIONS = NEW_BASE + "lectures/actions";
        LECTURE_ACTIONS_NOTES = NEW_BASE + "lectures/actions/notes";
        FIREBASE = NEW_BASE + "firebase";
        UPLOAD_IMAGE = NEW_BASE + "users/image";
        CHECK_ACCESSTOKEN = NEW_BASE + "users/accesstoken/check";
        DEATCTIVATE_USER = NEW_BASE + "users/activation/deactive";
        IMAGE_PATH = BASE_NEW_ONE + "assets/profiles/";
        MY_ORDERS = NEW_BASE + "users/orders";
        ORDER_DETAILS = NEW_BASE + "users/orders";//{order_id}
        GET_ALL_PRICE = NEW_BASE + "payment/price/all/";//{order_id}
        GET_ALL_YEARS = NEW_BASE + "payment/price/years";//{order_id}
        GET_ALL_MONTHS = NEW_BASE + "payment/price/months";//{order_id}


        IMAGE_REQUAET_URL = BASE_NEW_ONE + "assets/questions/";//{userid}/{file_name}
        GET_DOUBT_DETAILS = NEW_BASE + "users/ask/doubts/answers/";//{{question_id}}
        CLOSE_DOUBT = NEW_BASE + "users/ask/doubts/answers/close";//{{question_id}}
        DOUBT_REPLY = NEW_BASE + "users/ask/doubts/answers/reply";//{{question_id}}
        SEND_VIDEO_RATING = NEW_BASE + "users/feedbacks/lecture";//{{question_id}}


        SYLLABUS = BASE_NEW_ONE + "syllabus.htm?vma=app";
        ABOUT = BASE_NEW_ONE + "about_us_m.htm";
        FAQ = BASE_NEW_ONE + "faqs_m.htm";
        TERMS_POLICY = BASE_NEW_ONE + "terms_of_use_m.htm";
        EDUCATION_FOR_ALL = BASE_NEW_ONE + "education-for-all.php";
        OFFICIAL_WEBSITE = "https://www.tutorix.com/";
        REFUND_POLICY = BASE_NEW_ONE + "refund_policy_m.htm";
        PRIVACY_POLICY = BASE_NEW_ONE + "privacy_policy_m.htm";
        FACULTY_CURRENT = "https://d2vgb5tug4mj1f.cloudfront.net/%s/faculties/faculties.json";

        UN_CHECK_COOKIES = BASE_NEW_ONE + "unset_cookies.php";
        //CHECK_EXPIERY =NEW_BASE+"users/activation/check" ;
        CHECK_EXPIERY = NEW_BASE + "users/activation/checkUpdate";

        GET_ALL_CLASSES = "https://d2vgb5tug4mj1f.cloudfront.net/classes_v2.json";
        CHECK_COOKIES = BASE_NEW_ONE + "set_cookies.php";
        //CHECK_COOKIES =BASE_NEW_ONE+"dev/set_cookies.php" ;
        //GET_ALL_CLASSES ="https://d2vgb5tug4mj1f.cloudfront.net/classes_demo.json";


        /*New APIS  08-01-2020*/
        SET_REFRESH_TOKEN = NEW_BASE + "users/accesstoken/refresh";
        SDCARD_ACTIVATION = NEW_BASE + "sdcard/activation";
        GET_MY_REWARDS = NEW_BASE + "rewards/points";
        GET_YEARS_SUBSCRIPTION = NEW_BASE + "payment/coupon";
        // SDCARD_ACTIVATION =NEW_BASE+"sdcard/activation" ;


        //Version V1 APIS
        DOUBT_RELATED_QTNS = NEW_BASE_V1 + "relatedDoubts";
        DOUBT_ASK_QTNS = NEW_BASE_V1 + "askDoubt";
        DOUBT_GET_MYDOUBTS = NEW_BASE_V1 + "getUserDoubts";
        DOUBT_GET_LATESTDOUBTS = NEW_BASE_V1 + "getPublishedDoubts";
        DOUBT_GET_ANSWERS = NEW_BASE_V1 + "getUserDoubt";
        DOUBT_GET_ANSWERS_PUBLISHED = NEW_BASE_V1 + "getPublishedDoubt";
        DOUBT_SET_LIKE = NEW_BASE_V1 + "setAnswerLike";
        DOUBT_REPLY_NEW = NEW_BASE_V1 + "setAnswerReply";
        DOUBT_REPORT = NEW_BASE_V1 + "serAnswerReport";
        DOUBT_DELETE = NEW_BASE_V1 + "deleteQuestion";
        SEARCH_VIDEOS = NEW_BASE_V1 + "searchLectures";
        GET_DOUBT_FILTERS = NEW_BASE_V1 + "getFilterOptions";
        GET_OTP = NEW_BASE_V1 + "getOtp";//{{question_id}}
        REGISTEATION_REF = NEW_BASE_V1 + "setNewUser";//{{question_id}}
        REFERRAL_DETAILS = NEW_BASE_V1 + "getReferralCode";//{{question_id}}

        USER_LOGIN = NEW_BASE_V1 + "getUserLogin";
        MOCKTEST_NEW_LIST = NEW_BASE_V1 + "mock_test_list.json";
        SUBMIT_TEST_SERIES = NEW_BASE_V1 + "setTestSeriesData";
        GET_TEST_SERIES_TRACK = NEW_BASE_V1 + "getTestSeriesTrackData";
        GET_TEST_SERIES_TRACK_REVIEW = NEW_BASE_V1 + "getTestSeriesReview";
        GET_DELETE_ACCOUNT_OTP = NEW_BASE_V1 + "getDeleteAccountOTP";
        GET_DELETE_ACCOUNT = NEW_BASE_V1 + "deleteAccount";
        GET_TUTION_STUDENTS = NEW_BASE_V1 + "getTuitionStudents";
        GET_BATCH_FILTERS = NEW_BASE_V1 + "getBatchFilters";
        GET_BATCH_LIST = NEW_BASE_V1 + "getBatchFilters";
        SET_STUDENT_BATCH = NEW_BASE_V1 + "setStudentToBatch";
        GET_SCHOOL_INFO = NEW_BASE_V1 + "getSchoolData";
        SET_SCHOOL_INFO = NEW_BASE_V1 + "setUserSchoolData";
    }



}
