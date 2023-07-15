package com.tutorix.tutorialspoint.anaylysis;


import static java.lang.StrictMath.round;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.QuizModel;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.views.CustomBarChartRender;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TotalPerformFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TotalPerformFragment extends Fragment {


    @BindView(R.id.txt_days)
    TextView txtDays;
    @BindView(R.id.img_filter)
    ImageView imgFilter;
    @BindView(R.id.lnr_filter)
    LinearLayout lnrFilter;
    @BindView(R.id.chart)
    BarChart chart;
    @BindView(R.id.txt_marks_title)
    TextView txt_marks_title;
    Typeface tf;
    int whichDay;
    int subject_id=0;
    private String access_token;
    private String userid;
    private String loginType;
    private String class_id;
    List<QuizModel> quizList;
    boolean isTablet;
    int k=0;
    String selected_user_id;
    String selected_class_id;
    public TotalPerformFragment(String user_id,String selected_class_id) {
        // Required empty public constructor
        this.selected_user_id=user_id;
        this.selected_class_id=selected_class_id;
    }


    // TODO: Rename and change types and number of parameters
   /* public static TotalPerformFragment newInstance() {
        TotalPerformFragment fragment = new TotalPerformFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_perform, container, false);
        ButterKnife.bind(this, view);
        return view;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chart.setNoDataText("");
        tf = Typeface.createFromAsset(getActivity().getAssets(), "opensans_regular.ttf");
        String[]userInfo= SessionManager.getUserInfo(getActivity());
        Resources res = getResources();
         isTablet = res.getBoolean(R.bool.isTablet);
        access_token = userInfo[1];
        userid = userInfo[0];;
        loginType = userInfo[2];
        class_id = userInfo[4];
        txtDays.setText(getString(R.string.overall));
        whichDay=4;
        try {
            int currrentClsId = Integer.parseInt(class_id);
            if (currrentClsId <=7) {
                k=0;
            } else if (currrentClsId ==8) {
                k=1;
                //radioBio.setVisibility(View.GONE);

            } else if (currrentClsId ==9){
                // radioMath.setVisibility(View.GONE);
                k=2;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //txt_marks_title.setVisibility(View.GONE);
        quizList=new ArrayList<>();
            chartData();

    }

    @OnClick({R.id.img_filter, R.id.lnr_filter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_filter:
            case R.id.lnr_filter:
                AppController.getInstance().playAudio(R.raw.button_click);
                showPopup(view);
                break;
        }
    }

    PopupWindow popupWindowDays;

    @Override
    public void onStop() {

        if(   tag_string_req!=null)
        {
            AppController.getInstance().cancelPendingRequests(tag_string_req);
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void showPopup(View v) {
        if (popupWindowDays == null) {

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
                    whichDay = 1;
                    txtDays.setText("7 "+getString(R.string.days));
                    // chartData();

                        chartData();


                }
            });
            txt_che.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    //subject_id=2;
                    whichDay = 2;
                    txtDays.setText("30 "+getString(R.string.days));
                    //chartData();
                    chartData();

                }
            });
            txt_bio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    //subject_id=3;
                    whichDay = 3;
                    txtDays.setText("3 "+getString(R.string.months));
                    //chartData();
                    chartData();

                }
            });
            txt_math.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    //subject_id=4;
                    whichDay = 4;
                    txtDays.setText(getString(R.string.overall));
                    //chartData();
                    chartData();
                }
            });
            txt_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    txtDays.setText(getString(R.string.overall));
                    whichDay=4;
                     chartData();
                }
            });
        }
        popupWindowDays.showAsDropDown(v);
    }
    String tag_string_req;
    private void chartData()   {

        if(loginType==null)
            return;


        if(/*AppConfig.checkSDCardEnabled(getActivity(),userid,class_id)&&*/AppConfig.checkSdcard(class_id,getContext()))
        {
            callOfflline();
        }else
        {
            callOnline();
        }

       /* if(loginType.isEmpty())
        {
            callOnline();
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(getActivity(),userid,class_id)&&AppConfig.checkSdcard(class_id,getContext()))
            {
                callOfflline();
            }else
            {
                callOnline();
            }
        }else
        {
            callOfflline();
        }*/



    }

    private void callOfflline()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDatabase dbhelper = MyDatabase.getDatabase(getActivity());


                try {

                    if (whichDay == 4) {
                        quizList = dbhelper.quizModelDAO().getAllDayQuizTimeLineSub(userid,  class_id);

                    } else if (whichDay == 1) {
                        quizList = dbhelper.quizModelDAO().getSevenDayQuizTimeLineSub(userid,  class_id);

                    } else if (whichDay == 2) {
                        quizList = dbhelper.quizModelDAO().getMonthDayQuizTimeLineSub(userid,  class_id);

                    } else if (whichDay == 3) {
                        quizList = dbhelper.quizModelDAO().getThreeMonthDayQuizTimeLineSub(userid,  class_id);

                    }
                    System.out.print("" + quizList);

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
    private void callOnline()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        final JSONObject fjson3 = new JSONObject();
        try {
            if(selected_user_id==userid) {
                fjson3.put(Constants.userId, selected_user_id);
                fjson3.put(Constants.classId, class_id);
            }
            else {
                fjson3.put(Constants.userId, userid);
                fjson3.put(Constants.student_id, selected_user_id);
                fjson3.put(Constants.classId, selected_class_id);
            }


            fjson3.put(Constants.accessToken, access_token);
            //fjson3.put(Constants.subjectId, subject_id );
            if (whichDay==4) {
                fjson3.put("from_date", "0");
            } else if (whichDay==1) {
                fjson3.put("from_date", formatter.format(date));
            } else if (whichDay==2) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -7);
                fjson3.put("from_date", formatter.format(cal.getTime()));
            } else if (whichDay==3) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -30);
                fjson3.put("from_date", formatter.format(cal.getTime()));
            }
            fjson3.put("end_date", formatter.format(date));
            fjson3.put(Constants.accessToken, access_token);

            //Log.v("Request","Request "+fjson3.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message3 = fjson3.toString();

        final String encryption3 = Security.encrypt(message3, Key);


        tag_string_req = Constants.reqRegister;

        if (AppStatus.getInstance(Objects.requireNonNull(getActivity()).getBaseContext()).isOnline()) {
            StringRequest strReq = null;
            try {

                strReq = new StringRequest(Request.Method.GET,
                        Constants.USER_GET_MOCKTEST_PERFORMANCE + "?json_data=" + URLEncoder.encode(encryption3, "UTF-8"),
                        new Response.Listener<String>() {

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
                                //Log.d(Constants.response, response);

                                try {
                                    if(getActivity()==null)
                                        return;
                                    CustomDialog.closeDialog();
                                    quizList.clear();
                                    JSONObject jObj = new JSONObject(response);
                                    boolean error = jObj.getBoolean(Constants.errorFlag);



                                    if (!error) {

                                        JSONObject objAvg=jObj.getJSONObject("averages");
                                        float phy= (float) objAvg.getDouble("1");
                                        float che= (float) objAvg.getDouble("2");
                                        float math= (float) objAvg.getDouble("4");
                                        float bio= (float) objAvg.getDouble("3");


                                        showChart(phy,che,bio,math);

                                    } else {
                                        String errorMsg = jObj.getString(Constants.message);
                                        //CommonUtils.showToast(getActivity(),errorMsg);

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
                                        //nodata(new ArrayList<QuizModel>());
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //nodata(new ArrayList<QuizModel>());
                                    //CommonUtils.showToast(getActivity(),getString(R.string.json_error)+e.getMessage());

                                    //Toasty.warning(Objects.requireNonNull(getActivity()).getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


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
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CustomDialog.showDialog(getActivity(),true);
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {
            CommonUtils.showToast(getActivity(),getString(R.string.no_internet));
            //Toasty.info(getActivity().getBaseContext(), "There is no internet.", Toast.LENGTH_SHORT, true).show();
        }
    }
    private void showChart(float a,float b,float c,float d)
    {


        ArrayList<BarEntry> entries = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "");
        if(k==0)
        {
            entries.add(new BarEntry(0f, d));
            entries.add(new BarEntry(1f, a));
            entries.add(new BarEntry(2f, b));
            entries.add(new BarEntry(3f, c));
            dataSet = new BarDataSet(entries, "");
            dataSet.setColors(new int[]{ R.color.math_background,R.color.phy_background, R.color.che_background, R.color.bio_background}, Objects.requireNonNull(getActivity()).getBaseContext());

        }else if(k==1)
        {

            entries.add(new BarEntry(0f, d));
            entries.add(new BarEntry(1f, a));
            entries.add(new BarEntry(2f, b));
            dataSet = new BarDataSet(entries, "");
            dataSet.setColors(new int[]{ R.color.math_background,R.color.phy_background, R.color.che_background}, Objects.requireNonNull(getActivity()).getBaseContext());

        }else if(k==2)
        {

            entries.add(new BarEntry(0f, a));
            entries.add(new BarEntry(1f, b));
            entries.add(new BarEntry(2f, c));
            dataSet = new BarDataSet(entries, "");
            dataSet.setColors(new int[]{ R.color.phy_background, R.color.che_background, R.color.bio_background}, Objects.requireNonNull(getActivity()).getBaseContext());


        }
       /* entries.add(new BarEntry(0f, math));
        entries.add(new BarEntry(1f, phy));
        entries.add(new BarEntry(2f, che));
        entries.add(new BarEntry(3f, bio));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(new int[]{R.color.math_background,R.color.phy_background, R.color.che_background, R.color.bio_background}, Objects.requireNonNull(getActivity()).getBaseContext());
        */dataSet.setValueTypeface(tf);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new MyYAxisValueFormatter());
        BarData   data = new BarData(dataSet);
        data.setBarWidth(0.4f);
        data.setValueFormatter(new DefaultValueFormatter(0));
        /*DecimalFormat mFormat = new DecimalFormat("###,###,###,##0");
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return  mFormat.format(value) ;
            }
        });*/

        CustomBarChartRender barChartRender = new CustomBarChartRender(chart,chart.getAnimator(), chart.getViewPortHandler());
        barChartRender.setRadius(10);
        chart.setRenderer(barChartRender);
        txt_marks_title.setVisibility(View.VISIBLE);


        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setAxisMaximum(100);
        chart.getAxisLeft().setLabelCount(5);

        chart.setContentDescription("");
        //chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        if(!isTablet) {
            xAxis.setTextSize(12);
            chart.getAxisLeft().setTextSize(12);
        }
        else {
            xAxis.setTextSize(14);
            chart.getAxisLeft().setTextSize(14);
        }
        xAxis.setGranularityEnabled(true);

        final ArrayList<String> xVals = new ArrayList<>();

        if(k==0)
        {
            xVals.add(getString(R.string.mathematics));
            xVals.add(getString(R.string.physics));
            xVals.add(getString(R.string.chemistry));
            xVals.add(getString(R.string.biology));
        }else
        if(k==2)
        {
            xVals.add(getString(R.string.physics));
            xVals.add(getString(R.string.chemistry));
            xVals.add(getString(R.string.biology));
        }else if(k==1)
        {
            xVals.add(getString(R.string.mathematics));
            xVals.add(getString(R.string.physics));
            xVals.add(getString(R.string.chemistry));
        }

       /* xVals.add("Mathematics");
        xVals.add("Physics");
        xVals.add("Chemistry");
        xVals.add("Biology");*/

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                //Log.d("asa", "data X" + xVals);
                return xVals.get((int) value % xVals.size());
            }
        });
        chart.setExtraBottomOffset(10);
        chart.setPinchZoom(false);
        chart.setTouchEnabled(false);
        chart.clear();
        chart.invalidate();
        if (tf != null)
            chart.getXAxis().setTypeface(tf);
        chart.setData(data);


    }

    private void setData() {

        if(getActivity()==null)
            return;
        if(quizList==null)
            return;
        int TOTAL_PHY=0;
        float ANS_PHY=0;

        int TOTAL_CHE=0;
        float ANS_CHE=0;

        int TOTAL_BIO=0;
        float ANS_BIO=0;

        int TOTAL_MATH=0;
        float ANS_MATH=0;

            for(QuizModel review:quizList)
            {

                if(review.subject_id.equals("1"))
                {
                    TOTAL_PHY=TOTAL_PHY+review.total;
                    if(((review.total_correct.equalsIgnoreCase("0"))||(review.total_correct.isEmpty())))
                        ANS_PHY=ANS_PHY+0;
                    else  ANS_PHY=ANS_PHY+Integer.parseInt(review.total_correct);
                }else if(review.subject_id.equals("2"))
                {
                    TOTAL_CHE=TOTAL_CHE+review.total;
                    if(((review.total_correct.equalsIgnoreCase("0"))||(review.total_correct.isEmpty())))
                        ANS_CHE=ANS_CHE+0;
                    else  ANS_CHE=ANS_CHE+Integer.parseInt(review.total_correct);
                }else if(review.subject_id.equals("3"))
                {
                    TOTAL_BIO=TOTAL_BIO+review.total;
                    if(((review.total_correct.equalsIgnoreCase("0"))||(review.total_correct.isEmpty())))
                        ANS_BIO=ANS_BIO+0;
                    else  ANS_BIO=ANS_BIO+Integer.parseInt(review.total_correct);
                }else if(review.subject_id.equals("4"))
                {
                    TOTAL_MATH=TOTAL_MATH+review.total;
                    if(((review.total_correct.equalsIgnoreCase("0"))||(review.total_correct.isEmpty())))
                        ANS_MATH=ANS_MATH+0;
                    else  ANS_MATH=ANS_MATH+Integer.parseInt(review.total_correct);
                }



            }
            if(TOTAL_PHY==0)
                ANS_PHY=0;
            else ANS_PHY=round(((float)ANS_PHY/TOTAL_PHY)*100);

        if(TOTAL_CHE==0)
            ANS_CHE=0;
        else ANS_CHE=((float)ANS_CHE/TOTAL_CHE)*100;

        if(TOTAL_BIO==0)
            ANS_BIO=0;
        else ANS_BIO=((float)ANS_BIO/TOTAL_BIO)*100;

        if(TOTAL_MATH==0)
            ANS_MATH=0;
        else ANS_MATH=((float)ANS_MATH/TOTAL_MATH)*100;

        CustomBarChartRender barChartRender = new CustomBarChartRender(chart,chart.getAnimator(), chart.getViewPortHandler());
        barChartRender.setRadius(10);
        chart.setRenderer(barChartRender);
        ArrayList<BarEntry> entries = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "");
        if(k==0)
        {
            entries.add(new BarEntry(0f,  round(ANS_MATH)));
            entries.add(new BarEntry(1f, round( ANS_PHY)));
            entries.add(new BarEntry(2f, round(ANS_CHE)));
            entries.add(new BarEntry(3f, round(ANS_BIO)));
            dataSet = new BarDataSet(entries, "");
            dataSet.setColors(new int[]{ R.color.math_background,R.color.phy_background, R.color.che_background, R.color.bio_background}, Objects.requireNonNull(getActivity()).getBaseContext());

        }else if(k==1)
        {

            entries.add(new BarEntry(0f,  round(ANS_MATH)));
            entries.add(new BarEntry(1f, round( ANS_PHY)));
            entries.add(new BarEntry(2f, round(ANS_CHE)));
            dataSet = new BarDataSet(entries, "");
            dataSet.setColors(new int[]{ R.color.math_background,R.color.phy_background, R.color.che_background}, Objects.requireNonNull(getActivity()).getBaseContext());

        }else if(k==2)
        {

            entries.add(new BarEntry(0f, round( ANS_PHY)));
            entries.add(new BarEntry(1f, round(ANS_CHE)));
            entries.add(new BarEntry(2f, round(ANS_BIO)));
            dataSet = new BarDataSet(entries, "");
            dataSet.setColors(new int[]{ R.color.phy_background, R.color.che_background, R.color.bio_background}, Objects.requireNonNull(getActivity()).getBaseContext());


        }
        /*entries.add(new BarEntry(0f, round(ANS_MATH)));
        entries.add(new BarEntry(1f,round( ANS_PHY)));
        entries.add(new BarEntry(2f, round(ANS_CHE)));
        entries.add(new BarEntry(3f, round(ANS_BIO)));


        dataSet.setColors(new int[]{R.color.math_background,R.color.phy_background, R.color.che_background, R.color.bio_background}, Objects.requireNonNull(getActivity()).getBaseContext());
       */ dataSet.setValueTypeface(tf);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.4f);

        DecimalFormat mFormat = new DecimalFormat("###,###,###,##0");
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return  mFormat.format(value)+"%" ;
            }
        });
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.setContentDescription("");
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis rightYAxis = chart.getAxisRight();
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setAxisMaximum(100);
        chart.getAxisLeft().setLabelCount(5);
        rightYAxis.setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
       // xAxis.mLabelHeight=40;
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        final ArrayList<String> xVals = new ArrayList<>();
        if(k==0)
        {
            xVals.add(getString(R.string.mathematics));
            xVals.add(getString(R.string.physics));
            xVals.add(getString(R.string.chemistry));
            xVals.add(getString(R.string.biology));
        }else
        if(k==2)
        {
            xVals.add(getString(R.string.physics));
            xVals.add(getString(R.string.chemistry));
            xVals.add(getString(R.string.biology));
        }else if(k==1)
        {
            xVals.add(getString(R.string.mathematics));
            xVals.add(getString(R.string.physics));
            xVals.add(getString(R.string.chemistry));
        }

        /*xVals.add("Mathematics");
        xVals.add("Physics");
        xVals.add("Chemistry");
        xVals.add("Biology");*/

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                //Log.d("asa", "data X" + xVals);
                return xVals.get((int) value % xVals.size());
            }
        });
        Resources res = getResources();
        boolean isTablet = res.getBoolean(R.bool.isTablet);
        if(!isTablet) {
            xAxis.setTextSize(12);
            chart.getAxisLeft().setTextSize(12);
        }
        else {
            xAxis.setTextSize(14);
            chart.getAxisLeft().setTextSize(14);
        }
        chart.setExtraBottomOffset(10);
        chart.setPinchZoom(false);
        chart.setDragEnabled(false);
        chart.clear();
        chart.invalidate();
        if (tf != null)
            chart.getXAxis().setTypeface(tf);
        chart.setData(data);


    }
    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter () {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }



        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value) + " %";
        }
    }
}
