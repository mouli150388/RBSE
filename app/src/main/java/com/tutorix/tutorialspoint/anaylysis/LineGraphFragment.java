package com.tutorix.tutorialspoint.anaylysis;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LineGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineGraphFragment extends Fragment {


    @BindView(R.id.txt_days)
    TextView txtDays;
    @BindView(R.id.txt_timespent)
    TextView txt_timespent;
    @BindView(R.id.img_filter)
    ImageView imgFilter;
    @BindView(R.id.lnr_filter)
    LinearLayout lnrFilter;
    @BindView(R.id.chart)
    LineChart chart;
   static String subject;
    private String access_token;
    private String userid;
    private String loginType;
    private String class_id;
    List<TrackModel>    trackList;
    int whichDay;

    int year;
    public LineGraphFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LineGraphFragment newInstance(String subjects) {
        subject=subjects;
        LineGraphFragment fragment = new LineGraphFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_graph, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        chart.setNoDataText("");
        String[]userInfo= SessionManager.getUserInfo(getActivity());

        access_token = userInfo[1];
        userid = userInfo[0];;
        loginType = userInfo[2];
        class_id = userInfo[4];


        whichDay=1;
        txtDays.setText("7 "+getString(R.string.days));
        year=calendar.get(Calendar.YEAR);
        fetachData();

    }


    private void fetachData()
    {

        if(getActivity()==null)
        {
            return;
        }
        if(loginType.isEmpty())
        {
            callOnline();
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(getActivity(),userid,class_id)&&AppConfig.checkSdcard(class_id,getContext()))
            {
                callOffline();
            }else
            {
                callOnline();
            }
        }else
        {
            callOffline();
        }

       /* if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
            callOnline();
        }else{
            callOffline();
        }*/


        /*users/progress/graph*/

    }

    private void callOffline() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDatabase dbhelper = MyDatabase.getDatabase(getActivity());

                String min_date="";
                try {


                    if (whichDay==4) {



                        daysList.clear();
                        trackList=new ArrayList<>();
                        trackList.clear();
                        TrackModel trackModel;
                        calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_YEAR, -365);
                        min_date=CommonUtils.ddmmyyyy.format(calendar.getTime());
                        for (int k = 0; k <=365;k= k+52) {
                            calendar = Calendar.getInstance();
                            calendar.add(Calendar.DAY_OF_YEAR, -k);

                            if(daysList.size()<7) {
                                daysList.add(CommonUtils.ddmmyyyy.format(calendar.getTime()));
                                //Log.v("Date","Date Before "+daysList.get(daysList.size()-1));
                            }



                        }

                        Collections.sort(daysList);


                        for(int k=0;k<daysList.size();k++)
                        {
                            // Log.v("Date","Date After "+daysList.get(k));
                            trackModel=new TrackModel();
                            trackModel.activity_date=daysList.get(k);
                            if(trackList.size()>0)
                                trackModel.duration_insec=trackModel.duration_insec+trackList.get(trackList.size()-1).duration_insec;
                            if(k==0)
                                trackModel.duration_insec=trackModel.duration_insec+dbhelper.trackDAO().getTrackTimeLineWithDates(userid, class_id,subject,min_date,daysList.get(0));
                            else trackModel.duration_insec= trackModel.duration_insec+ dbhelper.trackDAO().getTrackTimeLineWithDates(userid, class_id,subject,daysList.get(k-1),daysList.get(k));
                            trackList.add(trackModel);
                        }

                    } else if (whichDay==1) {
                        daysList.clear();
                        trackList = dbhelper.trackDAO().getSevenDayTrackTimeLine(userid, class_id,subject);
                    } else if (whichDay==2) {
                        daysList.clear();
                        trackList=new ArrayList<>();
                        trackList.clear();
                        TrackModel trackModel;
                        calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_YEAR, -30);
                        min_date=CommonUtils.ddmmyyyy.format(calendar.getTime());
                        for (int k = 0; k <=30;k= k+30/7) {
                            calendar = Calendar.getInstance();
                            calendar.add(Calendar.DAY_OF_YEAR, -k);

                            if(daysList.size()<7) {
                                daysList.add(CommonUtils.ddmmyyyy.format(calendar.getTime()));
                                //Log.v("Date","Date Before "+daysList.get(daysList.size()-1));
                            }



                        }

                        Collections.sort(daysList);

                        for(int k=0;k<daysList.size();k++)
                        {
                            //Log.v("Date","Date After "+daysList.get(k));
                            trackModel=new TrackModel();
                            trackModel.activity_date=daysList.get(k);
                            if(trackList.size()>0)
                                trackModel.duration_insec=trackModel.duration_insec+trackList.get(trackList.size()-1).duration_insec;
                            if(k==0)
                                trackModel.duration_insec=trackModel.duration_insec+dbhelper.trackDAO().getTrackTimeLineWithDates(userid, class_id,subject,min_date,daysList.get(0));
                            else trackModel.duration_insec= trackModel.duration_insec+ dbhelper.trackDAO().getTrackTimeLineWithDates(userid, class_id,subject,daysList.get(k-1),daysList.get(k));
                            trackList.add(trackModel);
                        }

                    } else if (whichDay==3) {


                        daysList.clear();
                        trackList=new ArrayList<>();
                        trackList.clear();
                        TrackModel trackModel;
                        calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_YEAR, -90);
                        min_date=CommonUtils.ddmmyyyy.format(calendar.getTime());
                        for (int k = 0; k <=90;k= k+12) {
                            calendar = Calendar.getInstance();
                            calendar.add(Calendar.DAY_OF_YEAR, -k);

                            if(daysList.size()<7) {
                                daysList.add(CommonUtils.ddmmyyyy.format(calendar.getTime()));
                                //Log.v("Date","Date Before "+daysList.get(daysList.size()-1));
                            }



                        }

                        Collections.sort(daysList);

                        for(int k=0;k<daysList.size();k++)
                        {
                            // Log.v("Date","Date After "+daysList.get(k));
                            trackModel=new TrackModel();
                            trackModel.activity_date=daysList.get(k);
                            if(trackList.size()>0)
                                trackModel.duration_insec=trackModel.duration_insec+trackList.get(trackList.size()-1).duration_insec;
                            if(k==0)
                                trackModel.duration_insec=trackModel.duration_insec+dbhelper.trackDAO().getTrackTimeLineWithDates(userid, class_id,subject,min_date,daysList.get(0));
                            else trackModel.duration_insec= trackModel.duration_insec+ dbhelper.trackDAO().getTrackTimeLineWithDates(userid, class_id,subject,daysList.get(k-1),daysList.get(k));
                            trackList.add(trackModel);
                        }
                    }
                    //Log.v("MinDate","MinDate "+min_date);
                    for(int k=0;k<trackList.size();k++)
                        //Log.v("DaysLi","trackList "+trackList.get(k).duration_insec);
                        // Log.v("DaysLi","trackList "+trackList.toString());
                        System.out.print("" + trackList);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setData();
                        }
                    });





                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    List<String>daysList=new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    private void setData()
    {
        Date newDate;


        List<Float>duration=new ArrayList<>();

        if(whichDay==1)
            {
                for (int k = 6; k >= 0; k--) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, -k);
                    daysList.add(CommonUtils.ddmmyyyy.format(calendar.getTime()));
                }


                int m = -1;
                if (trackList != null) {
                    for (int l = 0; l < daysList.size(); l++) {

                        m = -1;
                        for (int k = 0; k < trackList.size(); k++) {
                            if (trackList.get(k).activity_date.contains(daysList.get(l))) {
                                m = k;
                            }
                            //Log.v("Track Data", "Track Data " + daysList.get(l) + " " + trackList.get(k).duration_insec + " " + trackList.get(k).activity_date.contains(daysList.get(l)));
                        }
                        if (m == -1) {
                            if (duration.size() > 0)
                                duration.add((duration.get(duration.size() - 1)));
                            else duration.add((float) 0);
                        } else {
                            if (duration.size() > 0)
                                duration.add(duration.get(duration.size() - 1) + ((float)trackList.get(m).duration_insec)/60);
                            else
                                duration.add((float)( (float)trackList.get(m).duration_insec)/60);
                        }
                    }


                    ArrayList<Entry> yVals = new ArrayList<Entry>();
                    float values=0;
                    for (int k = 0; k < duration.size(); k++) {
                        yVals.add(new Entry(k, (duration.get(k))));
                        values=duration.get(k);
                    }

                    try {
                        if (values < 60 * 24)
                            txt_timespent.setText(" " + new DecimalFormat("##").format(values) + " Minutes");
                        else
                            txt_timespent.setText(" " + (int) (values / 60 * 24) + " Days");
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    updateGraph(yVals, daysList);
                }


        }
        else
        {
            ArrayList<Entry> yVals = new ArrayList<Entry>();

            float values=0;
            for (int k = 0; k <trackList.size() ; k++) {
                yVals.add(new Entry(k, ((float)trackList.get(k).duration_insec)/60));
                values=(float)trackList.get(k).duration_insec/60;
            }

            try {
                if (values < 60 * 24)
                    txt_timespent.setText(" " + new DecimalFormat("##.##").format(values) + " Minutes");
                else
                    txt_timespent.setText(" " + (int) (values / 60 * 24) + " Days");
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            updateGraph(yVals, daysList);

        }

    }
    private void updateGraph(ArrayList<Entry> yVals, List<String> daysLists)
    {
        LineDataSet set1;
        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "");

        //set1.setFillAlpha(110);
        int[] colors = { getResources().getColor(R.color.phy_background),
                getResources().getColor(android.R.color.white) };
        set1.setDrawFilled(true);
        float[] index = { 0, 1 };
        Drawable drawable=null;

        if(subject.equals("1"))
         drawable = ContextCompat.getDrawable(getActivity(), R.drawable.linechart_gradient);
        else  if(subject.equals("2"))
            drawable = ContextCompat.getDrawable(getActivity(), R.drawable.linechart_che_gradient);
        if(subject.equals("3"))
            drawable = ContextCompat.getDrawable(getActivity(), R.drawable.linechart_bio_gradient);
        if(subject.equals("4"))
            drawable = ContextCompat.getDrawable(getActivity(), R.drawable.linechart_math_gradient);

        set1.setFillDrawable(drawable);
      //  set1.fillDrawable (colors, index);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        DecimalFormat mFormat = new DecimalFormat("###,###,###,##0");
        set1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return  mFormat.format(value) ;
            }
        });
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setLabelCount(5);


        chart.getAxisLeft().setValueFormatter(new MyYAxisValueFormatter());
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                try {
                  /*  if(daysList.get(((int) value % daysList.size())).contains(year+""))
                    return ddmmm.format(CommonUtils.ddmmyyyy.parse(daysList.get(((int) value % daysList.size()))));
                    else*/

                  return CommonUtils.monthth.format(CommonUtils.ddmmyyyy.parse(daysLists.get(((int) value % daysLists.size()))));


                } catch (ParseException e) {
                    e.printStackTrace();
                    return daysLists.get(((int) value % daysLists.size()));
                }
            }
        });

        // create a data object with the datasets
        LineData data = new LineData( dataSets);

        chart.getDescription().setText("");


        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().setTextSize(12);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextSize(12);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.setContentDescription("");
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextSize(12);
        xAxis.setLabelRotationAngle(90);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        xAxis.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"opensans_regular.ttf"));
        if(!isTablet) {
            xAxis.setTextSize(8);
            chart.getAxisLeft().setTextSize(12);
        }
        else {
            xAxis.setTextSize(14);
            chart.getAxisLeft().setTextSize(14);
        }

        chart.setData(data);
        chart.setPinchZoom(true);
        chart.setTouchEnabled(true);
        chart.invalidate();
    }
    public static SimpleDateFormat ddmmm = new SimpleDateFormat("dd-MMM");
    @OnClick(R.id.lnr_filter)
    public void onViewClicked(View v) {
        showPopup(v);
    }


    PopupWindow popupWindow;
    PopupWindow popupWindowDays;
    private void showPopup(View v) {
        if (popupWindowDays == null) {
            @SuppressLint("InflateParams")
            View view = getLayoutInflater().inflate(R.layout.popup_menu, null);

            TextView txt_phy = view.findViewById(R.id.txt_phy);
            TextView txt_che = view.findViewById(R.id.txt_che);
            TextView txt_bio = view.findViewById(R.id.txt_bio);
            TextView txt_math = view.findViewById(R.id.txt_math);
            TextView txt_all = view.findViewById(R.id.txt_all);
            txt_all.setVisibility(View.GONE);
            txt_phy.setText("7 "+getString(R.string.days));
            txt_che.setText("30 "+getString(R.string.days));
            txt_bio.setText("3 "+getString(R.string.months));
            txt_math.setText(getString(R.string.overall));
            popupWindowDays = new PopupWindow(
                    view,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindowDays.setOutsideTouchable(true);
            popupWindowDays.setBackgroundDrawable(new BitmapDrawable());
            popupWindowDays.setOutsideTouchable(true);

            txt_phy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    whichDay=1;
                    txtDays.setText("7 "+getString(R.string.days));
                    // chartData();

                        fetachData();


                }
            });
            txt_che.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    //subject_id=2;
                    whichDay=2;
                    txtDays.setText("30 "+getString(R.string.days));
                    //chartData();
                    fetachData();

                }
            });
            txt_bio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    //subject_id=3;
                    whichDay=3;
                    txtDays.setText("3 "+getString(R.string.months));
                    //chartData();
                   fetachData();

                }
            });
            txt_math.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    //subject_id=4;
                    whichDay=4;
                    txtDays.setText(getString(R.string.overall));
                    fetachData();
                }
            });
            txt_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    txtDays.setText(getString(R.string.overall));
                    whichDay=4;
                    fetachData();
                }
            });
        }
        popupWindowDays.showAsDropDown(v);
    }
    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter () {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }



        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value) ;
        }
    }

    private void callOnline()
    {


        final JSONObject fjson3 = new JSONObject();
        try {
            fjson3.put(Constants.userId, userid);
            fjson3.put(Constants.classId, class_id);
            fjson3.put(Constants.subjectId, subject);
            if(whichDay==1)
                fjson3.put("days_flag", "7");
            else if(whichDay==2)
                fjson3.put("days_flag", "30");
            else if(whichDay==3)
                fjson3.put("days_flag", "90");
            else if(whichDay==4)
                fjson3.put("days_flag", "365");


            fjson3.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message3 = fjson3.toString();

        final String encryption3 = Security.encrypt(message3, Key);
        String encode="";
        try {
            encode= URLEncoder.encode(encryption3, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (AppStatus.getInstance(Objects.requireNonNull(getActivity()).getBaseContext()).isOnline()) {


            StringRequest strReq = new StringRequest(Request.Method.GET,
                    Constants.USER_TRACK_TIME_SUBJECT +  "?json_data=" + encode,
                    new Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {
                            CustomDialog.closeDialog();
                            //Log.d(Constants.response, response);
                            if(getContext()==null)
                                return;
                            try {
                                JSONObject obj=new JSONObject(response);
                                if(!obj.getBoolean("error_flag"))
                                {
                                    JSONObject objTime=obj.getJSONObject("time_array");
                                    Iterator<String>keys= objTime.keys();
                                    daysList.clear();
                                    ArrayList<Entry> yVals = new ArrayList<Entry>();
                                    int k=0;
                                    double values=0;
                                    while (keys.hasNext())
                                    {
                                        String key=keys.next();
                                        daysList.add(key);
                                        yVals.add(new Entry(k, (float) objTime.getDouble(key)));
                                        values=(float) objTime.getDouble(key);
                                        k=k+1;
                                        //Log.v("K","k Values "+k);
                                    }

                                    try {
                                        if (values < (60 * 24)) {
                                            txt_timespent.setText(" " + new DecimalFormat("##").format(values) + getString(R.string.minutes));
                                        }else
                                            txt_timespent.setText(" " + (int) (values / 60 * 24) + " Days");
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    updateGraph(yVals, daysList);
                                }else
                                {
                                    String errorMsg = obj.getString(Constants.message);
                                    if(errorMsg.contains("Invalid access token"))
                                    {
                                        MyDatabase dbHandler=MyDatabase.getDatabase(getActivity());
                                        dbHandler.userDAO().deleteUser(userid);
                                        SessionManager.logoutUser(getActivity());
                                        Intent i = new Intent(getActivity(), LoginActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);
                                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                        getActivity().finish();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //ArrayList<Entry> yVals = new ArrayList<Entry>();

                            //updateGraph(yVals, daysList);

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    String msg="";
                    CustomDialog.closeDialog();
                    if(getActivity()==null)
                        return;
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        msg=getResources().getString(R.string.error_1);
                    } else if (error instanceof AuthFailureError) {
                        msg=getResources().getString(R.string.error_2);
                    } else if (error instanceof ServerError) {
                        msg=getResources().getString(R.string.error_2);
                    } else if (error instanceof NetworkError) {
                        msg=getResources().getString(R.string.error_3);
                    } else if (error instanceof ParseError) {
                        msg=getResources().getString(R.string.error_4);
                    }
                    // CommonUtils.showToast(getActivity(), msg);

                    //noData(true);
                    //Toasty.warning(Objects.requireNonNull(getContext()), error.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            }){
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }


                        cacheEntry.data = response.data;

                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                        "UTF-8");
                        return Response.success((jsonString), cacheEntry);
                    } catch ( Exception e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }

                @Override
                public void deliverError(VolleyError error) {
                    super.deliverError(error);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    return super.parseNetworkError(volleyError);
                }
            };
            CustomDialog.showDialog(getActivity(),true);
            AppController.getInstance().addToRequestQueue(strReq);
        }
    }
}
