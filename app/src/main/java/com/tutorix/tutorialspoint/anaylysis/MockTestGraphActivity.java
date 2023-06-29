package com.tutorix.tutorialspoint.anaylysis;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.MockTestModelTable;
import com.tutorix.tutorialspoint.models.RecomandedModel;
import com.tutorix.tutorialspoint.quiz.MockTestPreviousActivity;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.video.VideoPlayerActivity;
import com.tutorix.tutorialspoint.views.CustomBarChartRender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MockTestGraphActivity extends AppCompatActivity {

    @BindView(R.id.txt_header)
    TextView txt_header;
    @BindView(R.id.chart)
    BarChart barchart;
    @BindView(R.id.lnr_mock_tests)
    LinearLayout lnr_mock_tests;
    @BindView(R.id.lnr_recommand_items)
    LinearLayout lnrRecommandItems;
    @BindView(R.id.img_background)
    ImageView img_background;
   /* @BindView(R.id.lnr_back)
    LinearLayout lnr_back;*/


    private String access_token;
    private String userId;
    private String loginType;
    private String classId;
    private String section_id;
    private String subject_id;
    private String section_name;

    int drawable_play_icon;
    int color_theme;
    String selected_user_id="";
    String selected_class_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_test_graph);
        ButterKnife.bind(this);

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());
        access_token = userInfo[1];
        userId = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];

        subject_id=getIntent().getStringExtra("subject_id");
        section_id=getIntent().getStringExtra("section_id");
        section_name=getIntent().getStringExtra("section_name");
        selected_user_id=getIntent().getStringExtra("selected_user_id");
        selected_class_id=getIntent().getStringExtra("selected_class_id");


        if(selected_user_id!=userId)
        {
            classId=selected_class_id;
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        txt_header.setText(section_name);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        if(subject_id.equals("1"))
            getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(MockTestGraphActivity.this, R.drawable.ic_phy_bg_green));
        else  if(subject_id.equals("2"))
            getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(MockTestGraphActivity.this, R.drawable.ic_chemistry_bg_green));
        else  if(subject_id.equals("3"))
            getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(MockTestGraphActivity.this, R.drawable.ic_bio_bg_green));
        else  if(subject_id.equals("4"))
            getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(MockTestGraphActivity.this, R.drawable.ic_math_bg_green));

        switch (subject_id)
        {
            case "1":
                color_theme=R.color.phy_background;
                drawable_play_icon=R.drawable.ic_phy_play;
                img_background.setImageResource(R.drawable.ic_phy_bg_white);
                //txtPrevious.setBackgroundResource(R.drawable.ic_phy_notes);
                break;
            case "2":
                color_theme=R.color.che_background;
                drawable_play_icon=R.drawable.ic_che_play;
                img_background.setImageResource(R.drawable.ic_chemistry_bg_white);
                //txtPrevious.setBackgroundResource(R.drawable.ic_che_notes);
                break;
            case "3":
                color_theme=R.color.bio_background;
                img_background.setImageResource(R.drawable.ic_bio_bg_white);
                drawable_play_icon=R.drawable.ic_bio_play;
                //txtPrevious.setBackgroundResource(R.drawable.ic_bio_notes);
                break;
            case "4":
                color_theme=R.color.math_background;
                img_background.setImageResource(R.drawable.ic_math_bg_white);
                //txtPrevious.setBackgroundResource(R.drawable.ic_math_notes);
                drawable_play_icon=R.drawable.ic_math_play;
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            switch (subject_id) {
                case "1":
                    window.setStatusBarColor(getResources().getColor(R.color.phy_background_status));
                    break;
                case "2":
                    window.setStatusBarColor(getResources().getColor(R.color.che_background_status));
                    break;
                case "3":
                    window.setStatusBarColor(getResources().getColor(R.color.bio_background_status));
                    break;
                case "4":
                    window.setStatusBarColor(getResources().getColor(R.color.math_background_status));
                    break;
            }
        }
       // txtPrevious.setTextColor(getResources().getColor(color_theme));

        if(loginType.isEmpty())
        {
            fetchData();
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(this,userId,classId)&&AppConfig.checkSdcard(classId,getApplicationContext()))
            {
                fetchLocalMockTests();
            }else
            {
                fetchData();
            }
        }else
        {
            fetchLocalMockTests();
        }
       /* if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
            fetchData();
        }else
        {
            fetchLocalMockTests();
        }*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        AppController.getInstance().playAudio(R.raw.back_sound);
        finish();
        return super.onSupportNavigateUp();
    }

    @OnClick({ R.id.lnr_recommand_items})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_previous:
                AppController.getInstance().playAudio(R.raw.button_click);
                if (new SharedPref().isExpired(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(), SubscribePrePage.class);
                    i.putExtra("flag", "M");
                    startActivity(i);
                    return;
                }
                Intent i = new Intent(MockTestGraphActivity.this, MockTestPreviousActivity.class);
                i.putExtra(Constants.classId, classId);
                i.putExtra(Constants.userId, userId);
                i.putExtra(Constants.subjectId, subject_id);
                i.putExtra(Constants.sectionId, section_id);
                i.putExtra(Constants.lectureId, "");
                i.putExtra(Constants.lectureName, "");
                i.putExtra(Constants.sectionName, section_name);
                i.putExtra(Constants.mockTest, "");
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

                break;

                case R.id.lnr_back:
                    finish();
                break;
            case R.id.lnr_recommand_items:
                break;
        }
    }


    private void fetchData() {
        //fillWithData();
        String encryption = "";
        String encryption2 = "";
        final JSONObject fjson = new JSONObject();
        try {

            if(selected_user_id==userId){
                fjson.put(Constants.userId, selected_user_id);
            fjson.put(Constants.classId, classId);}
            else {
                fjson.put(Constants.userId, userId);
                fjson.put(Constants.student_id, selected_user_id);
                fjson.put(Constants.classId, selected_class_id);
            }
            /*if(selected_class_id==classId)
                fjson.put(Constants.classId, selected_class_id);
            else {
                fjson.put(Constants.classId, classId);
            }*/

            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.subjectId, subject_id);
            fjson.put(Constants.sectionId, section_id);

            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            encryption = Security.encrypt(message, Key);

            encryption2 = URLEncoder.encode(encryption, "utf-8");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.GET, Constants.QUIZ_SECTION_MOCK_GRAPH+ "?json_data=" +encryption2  , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    //Log.v("response","response"+response);
                    //loadingPanelID.hide();
                    CustomDialog.closeDialog();
                    JSONObject obj = new JSONObject(response);

                    if(!obj.getBoolean("error_flag"))
                    {
                        JSONArray grpghArray=obj.getJSONArray("mocktest_graph_data");

                        ArrayList<MockTestModelTable>listGraph=new ArrayList<>();
                        MockTestModelTable model;
                        JSONObject objData;
                        for(int k=0;k<grpghArray.length();k++)

                        {
                            objData=grpghArray.getJSONObject(k);
                            model=new MockTestModelTable();
                            //model.id=objData.getString("id");
                            model.mocktest_type=objData.getString("mocktest_type");
                            model.total_attempts=objData.getInt("total_attempts");
                            model.total_marks=objData.getInt("total_marks");
                            model.total_questions=objData.getInt("total_questions");
                            model.low_marks=objData.getInt("low_marks");
                            model.high_marks=objData.getInt("high_marks");
                            listGraph.add(model);
                        }

                        JSONArray recomArray=obj.getJSONArray("recommended_videos");
                        RecomandedModel recomandedModel;
                        ArrayList<RecomandedModel>listRecom=new ArrayList<>();


                        for(int k=0;k<recomArray.length();k++)
                        {
                            objData=recomArray.getJSONObject(k);
                            recomandedModel=new RecomandedModel();
                            recomandedModel.lecture_count=objData.getString("lecture_count");
                            recomandedModel.lecture_id=objData.getString("lecture_id");
                            recomandedModel.lecture_title=objData.getString("lecture_title");

                            listRecom.add(recomandedModel);
                        }
                        setGraph(listGraph);
                        showMocks(listGraph);
                        showRecomanded(listRecom);



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = "";
                //loadingPanelID.hide();
                CustomDialog.closeDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg = getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg = getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg = getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg = getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg = getResources().getString(R.string.error_4);
                }
                CommonUtils.showToast(getApplicationContext(), msg);
            }
        })/*{
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }
        }*/{
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

                CustomDialog.showDialog(MockTestGraphActivity.this, true);
                AppController.getInstance().addToRequestQueue(request);



    }


    private void fetchLocalMockTests()
    {

        MyDatabase databse=MyDatabase.getDatabase(getApplicationContext());
        ArrayList<MockTestModelTable>listData= (ArrayList<MockTestModelTable>) databse.mockTestDAO().getMockTestBySection(userId,classId,subject_id,section_id);
       if(listData!=null&&listData.size()>0) {
           setGraph(listData);
           showMocks(listData);
       }

        ArrayList<RecomandedModel>listRecom= (ArrayList<RecomandedModel>) databse.recomandedDAO().getRecomanded(userId,classId,subject_id,section_id);

        for(int k=0;k<listRecom.size();k++)
        {
            //Log.v("recom","recom "+listRecom.get(k).section_id+" "+listRecom.get(k).lecture_id+" "+listRecom.get(k)._id);
        }

        showRecomanded(listRecom);

    }

    private void setGraph(ArrayList<MockTestModelTable> listGraph)
    {
        ArrayList<BarEntry> valuesLow = new ArrayList<>();
        ArrayList<BarEntry> valuesHigh = new ArrayList<>();
        final ArrayList<String> xVals = new ArrayList<>();
        for(int k=0;k<listGraph.size();k++)
        {
            if(listGraph.get(k).low_marks==0)
                valuesLow.add(new BarEntry(k,new float[]{.5f}));
                else
            valuesLow.add(new BarEntry(k,new float[]{listGraph.get(k).low_marks}));

            if(listGraph.get(k).high_marks==0)
                valuesHigh.add(new BarEntry(k,new float[]{.5f}));
            else
            valuesHigh.add(new BarEntry(k,new float[]{listGraph.get(k).high_marks}));


            xVals.add(listGraph.get(k).mocktest_type);

        }


        Legend l = barchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setEnabled(false);
        //l.setTypeface(mTfLight);
        l.setYOffset(0f);
        //l.setXOffset(10f);
        //l.setYEntrySpace(0f);
        //l.setTextSize(8f);







        YAxis leftAxis = barchart.getAxisLeft();
       // leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(5f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightYAxis = barchart.getAxisRight();
        rightYAxis.setEnabled(false);
       /* float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"


        int groupCount = 4;*/

        float groupSpace = 0.3f;

        float barSpace = 0.02f;
        float barWidth = 0.35f;
        int groupCount = xVals.size();

        //IMPORTANT *****


        CustomBarChartRender barChartRender = new CustomBarChartRender(barchart,barchart.getAnimator(), barchart.getViewPortHandler());
        barChartRender.setRadius(5);
        barchart.setRenderer(barChartRender);

        BarDataSet set1 = new BarDataSet(valuesLow, "");
        DecimalFormat mFormat = new DecimalFormat("###,###,###,##0");
        set1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return  mFormat.format(value) ;
            }
        });

        BarDataSet set2 = new BarDataSet(valuesHigh, "");
        set2.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return  mFormat.format(value) ;
            }
        });
        set1.setColors(new int[]{R.color.LOW_BAR_COLOR}, getApplicationContext());
        set2.setColors(new int[]{R.color.ANS_BAR_COLOR}, getApplicationContext());

        BarData data = new BarData(set1, set2);
        barchart.setData(data);
        barchart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range

        barchart.getDescription().setEnabled(false);
        barchart.getAxisRight().setDrawGridLines(false);
        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        barchart.getXAxis().setAxisMinimum(0);
        barchart.getXAxis().setAxisMaximum(0 + barchart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barchart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping

        barchart.getAxisLeft().setAxisMinimum(0);
        barchart.getAxisLeft().setLabelCount(5);
        barchart.getAxisLeft().setAxisMaximum(20);
        XAxis xAxis = barchart.getXAxis();
        YAxis aAxis = barchart.getAxisRight();
        aAxis.setDrawAxisLine(false);
        //xAxis.setTypeface(mTfLight);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
       // xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        /*xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value+"";
            }
        });*/

        Resources res = getResources();
        boolean isTablet = res.getBoolean(R.bool.isTablet);

        if(!isTablet) {
            xAxis.setTextSize(12);
            barchart.getAxisLeft().setTextSize(12);
           // barchart.getLegend().setTextSize(12);
        }
        else {
            xAxis.setTextSize(14);
            barchart.getAxisLeft().setTextSize(12);
           // barchart.getLegend().setTextSize(12);
        }
        barchart.getXAxis().setDrawGridLines(false);
        barchart.getAxisLeft().setDrawAxisLine(false);
        barchart.getAxisLeft().setDrawGridLines(true);
        barchart.getAxisRight().setDrawGridLines(false);
        barchart.setContentDescription("");
        barchart.getAxisRight().setEnabled(false);
        barchart.invalidate();
        barchart.setPinchZoom(false);
        barchart.setTouchEnabled(false);
        barchart.invalidate();
    }

    private void showRecomanded(ArrayList<RecomandedModel> listRecom)
    {


        lnrRecommandItems.removeAllViews();
        for ( int k=0;k<listRecom.size();k++) {



            View view=getLayoutInflater().inflate(R.layout.recommanded_item,null);
            TextView txt_recommanded=view.findViewById(R.id.txt_recommanded);
            ImageView playButton=view.findViewById(R.id.playButton);
            playButton.setImageResource(drawable_play_icon);
            LinearLayout lnr_play=view.findViewById(R.id.lnr_play);
            txt_recommanded.setText(listRecom.get(k).lecture_title);
            txt_recommanded.setTextColor(getResources().getColor(color_theme));
            lnr_play.setTag(listRecom.get(k).lecture_id);
            lnr_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                    AppController.getInstance().startItemAnimation(v);
                    if (new SharedPref().isExpired(getApplicationContext())) {
                        Intent i = new Intent(getApplicationContext(), SubscribePrePage.class);
                        i.putExtra("flag", "M");
                        startActivity(i);
                        return;
                    }
                    Intent i = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(Constants.lectureVideoUrl, "");
                    i.putExtra(Constants.sectionName, section_name);

                    i.putExtra(Constants.lectureId, lnr_play.getTag().toString());
                    i.putExtra(Constants.subjectId, subject_id);
                    i.putExtra(Constants.classId, classId);
                    i.putExtra(Constants.userId, userId);
                    i.putExtra(Constants.sectionId, section_id);
                    i.putExtra(Constants.lectureName, txt_recommanded.getText());
                    i.putExtra("selected_user_id", selected_user_id);
                    i.putExtra("selected_class_id", selected_class_id);

                    startActivity(i);

                }
            });
            lnrRecommandItems.addView(view);
        }
    }

    private void showMocks(ArrayList<MockTestModelTable> listRecom)
    {


        lnr_mock_tests.removeAllViews();
        for ( int k=0;k<listRecom.size();k++) {



            View view=getLayoutInflater().inflate(R.layout.recommanded_item,null);
            TextView txt_recommanded=view.findViewById(R.id.txt_recommanded);
            ImageView playButton=view.findViewById(R.id.playButton);
            playButton.setImageResource(drawable_play_icon);
            playButton.setVisibility(View.GONE);
            LinearLayout lnr_play=view.findViewById(R.id.lnr_play);
            txt_recommanded.setText(listRecom.get(k).mocktest_type.replace("M",getString(R.string.mock_tests)+" "));
            txt_recommanded.setTextColor(getResources().getColor(color_theme));
            lnr_play.setTag(listRecom.get(k).mocktest_type);
            lnr_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                    AppController.getInstance().startItemAnimation(v);
                    Intent i = new Intent(MockTestGraphActivity.this, MockTestPreviousActivity.class);
                    i.putExtra(Constants.classId, classId);
                    i.putExtra(Constants.userId, userId);
                    i.putExtra(Constants.subjectId, subject_id);
                    i.putExtra(Constants.sectionId, section_id);
                    i.putExtra(Constants.lectureId, "");
                    i.putExtra(Constants.lectureName, "");
                    i.putExtra(Constants.sectionName, section_name);
                    i.putExtra(Constants.mockTest, v.getTag().toString());
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);

                }
            });
            lnr_mock_tests.addView(view);
        }
    }



    public class StackedFormatter implements IValueFormatter
    {

        int values;
        /**
         * if true, all stack values of the stacked bar entry are drawn, else only top
         */
        private boolean mDrawWholeStack;

        /**
         * a string that should be appended behind the value
         */
        private String mAppendix;

        private DecimalFormat mFormat;

        /**
         * Constructor.
         *
         * @param drawWholeStack if true, all stack values of the stacked bar entry are drawn, else only top
         * @param appendix       a string that should be appended behind the value
         * @param decimals       the number of decimal digits to use
         */
        public StackedFormatter(boolean drawWholeStack, String appendix, int decimals) {
            this.mDrawWholeStack = drawWholeStack;
            this.mAppendix = appendix;
            values=-1;
            StringBuffer b = new StringBuffer();
            for (int i = 0; i < decimals; i++) {
                if (i == 0)
                    b.append(".");
                b.append("0");
            }

            this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

            if (!mDrawWholeStack && entry instanceof BarEntry) {

                BarEntry barEntry = (BarEntry) entry;
                float[] vals = barEntry.getYVals();


                if (vals != null) {

                    // find out if we are on top of the stack
                    if (vals[vals.length - 1] == value) {

                        // return the "sum" across all stack values
                        return mFormat.format(barEntry.getY()) + mAppendix;
                    } else {
                        return ""; // return empty
                    }
                }
            }
            //Log.v("vals ","vals "+value);
            values=values+1;
            if(values%2!=0) {
                return "";
            }
            // return the "proposed" value
            return mFormat.format(value) + mAppendix;
        }
    }
}
