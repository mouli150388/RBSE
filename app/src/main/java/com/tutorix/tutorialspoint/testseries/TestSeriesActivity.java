package com.tutorix.tutorialspoint.testseries;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.testseries.data.TestSeries;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestSeriesActivity extends AppCompatActivity {

    @BindView(R.id.rel_top_main)
    RelativeLayout rel_top_main;
    @BindView(R.id.txt_header)
    TextView txt_header;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lnr_home)
    LinearLayout lnr_home;
    @BindView(R.id.lnr_reload)
    LinearLayout lnr_reload;
    @BindView(R.id.lnr_nosdcard)
    LinearLayout lnr_nosdcard;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.img_gif)
    ImageView img_gif;
    @BindView(R.id.txt_nodata_test)
    TextView txt_nodata_test;
    TestSeriesAdapter testSeriesAdapter;
    ArrayList<TestSeries>listSeries;
    int currrentClsId;
    TestSeriesActivity activity;
    String BaseURL = "";
    String disclaimer = "";
    List<String> rules = new ArrayList<>();
    int pysics_marks;
    int pysics_qtns;
    int chemistry_marks;
    int chemistry_qtns;
    int other_marks;
    int other_qtns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_series);
        ButterKnife.bind(this);
        String[]userInfo= SessionManager.getUserInfo(this);
        String access_token = userInfo[1];
        String userid = userInfo[0];;
        String loginType = userInfo[2];
        String class_id = userInfo[4];
        activity=this;
        try {
            toolbar.setTitle("");
            currrentClsId = Integer.parseInt(class_id);
            if (currrentClsId <=7) {

            } else if (currrentClsId ==8) {
                txt_header.setText("JEE Test Series");
                BaseURL = AppConfig.getOnlineURLImage(currrentClsId+"")+"test-series/iit-jee-main.json";
                //BaseURL = "https://origin.tutorix.com/dev/iit-jee/"+"test-series/iit-jee-main.json";
                Glide.with(getApplicationContext()).load(R.drawable.iit_jee_test).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_gif);

            } else if (currrentClsId ==9){
                txt_header.setText("NEET Test Series");
                BaseURL = AppConfig.getOnlineURLImage(currrentClsId+"")+"test-series/neet-main.json";
                //BaseURL = "https://origin.tutorix.com/dev/neet/"+"test-series/neet-main.json";
                Glide.with(getApplicationContext()).load(R.drawable.neet_test).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_gif);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        recycler_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        listSeries=new ArrayList<>();



       // Glide.with(getApplicationContext()).load(R.drawable.test_series).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_gif);
            getJSON();
        
    }
    @OnClick({R.id.lnr_home})
    public void onViewClicked(View view) {

        switch (view.getId())
        {
            case R.id.lnr_home:
                finish();
                break;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void getJSON()
    {

        String tag_string_req = Constants.reqRegister;
        //loadingPanelID.show();
        if(activity==null)
            return;
        try {
            CustomDialog.showDialog(activity, true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        StringRequest strReq = new StringRequest(Request.Method.GET,
                BaseURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            // loadingPanelID.hide();
                            try {
                                CustomDialog.closeDialog();
                            }catch (Exception e)
                            {

                            }
                            JSONObject jObj = new JSONObject(response);
                            boolean show_flag=false;
                            if(jObj.has("show_flag"))
                            {
                                show_flag=jObj.getBoolean("show_flag");
                            }
                            if(!show_flag)
                            {
                              //  CommonUtils.showToast(getApplicationContext(),"Coming soon!");
                                lnr_reload.setVisibility(View.VISIBLE);
                                txt_nodata_test.setText("Coming Soon!");
                                return;
                            }
                            String marks;
                            if(jObj.has("marks"))
                             marks=jObj.getString("marks");
                            else
                             marks="<style>table,th, td { border: 1px solid black; border-collapse: collapse;} td{text-align: center;padding:5px;}th{background:#1F34A7;color:#FFF;}</style><table width=100%><tr><th>Subject</th><th>Questions</th> <th>Marks</th></tr><tr><td>Mathematics</td><td>25</td><td>100</td></tr><tr><td>Physics</td><td>25</td><td>100</td></tr><tr><td>Chemistry</td><td>25</td><td>100</td></tr></table>";
                          /*  pysics_qtns=marksObj.getJSONObject("physics").getInt("total_questions");
                            pysics_marks=marksObj.getJSONObject("physics").getInt("total_marks");

                            chemistry_qtns=marksObj.getJSONObject("chemistry").getInt("total_questions");
                            chemistry_marks=marksObj.getJSONObject("chemistry").getInt("total_marks");*/


                            Gson gson=new Gson();
                            if (currrentClsId ==8) {
                                listSeries = gson.fromJson(jObj.getJSONArray("iit-jee").toString(), new TypeToken<List<TestSeries>>() {
                                }.getType());

                               /* other_qtns=marksObj.getJSONObject("mathematics").getInt("total_questions");
                                other_marks=marksObj.getJSONObject("mathematics").getInt("total_marks");*/
                            }else {
                                listSeries = gson.fromJson(jObj.getJSONArray("neet").toString(), new TypeToken<List<TestSeries>>() {
                                }.getType());

                              /*  other_qtns=marksObj.getJSONObject("biology").getInt("total_questions");
                                other_marks=marksObj.getJSONObject("biology").getInt("total_marks");*/
                            }

                            if(jObj.has("disclaimer"))
                            disclaimer=jObj.getString("disclaimer");
                            else disclaimer="I have read and understood the instructions. i declare I am not in possession of/not wearing/not carrying any prohibited items such as mobile phones, Blue tooth etc. I agree that in case of not adhering to the instructions, I shall be liable to be debarred from this Test and/or to disciplinary actions, which may include ban from future trends/examinations. \\n\\nThis is just a mock test provided for practicing and preparing for the actual exams, which may differ in its pattern, instructions and questions provided.\\n\\nBy starting this mock test exams, I agree to accept all the above mentioned terms and conditions.";
                            disclaimer=jObj.getString("disclaimer");

                            if(jObj.has("rules"))
                            rules=gson.fromJson(jObj.getJSONArray("rules").toString(), new TypeToken<List<String>>() {
                            }.getType());
                            else {
                                rules.add("This mock test comprises of 60 Multiple Choice Questions (MCQs) and 15 Numeric Value Questions from Mathematics, Physics and Chemistry");
                                rules.add("Every question is followed by different options. Just Tab on the correct option to select your answer for the question.");
                                rules.add("Total duration of this mock test is 180 minutes. You also have option to pause and resume the test anytime. During pause, your test time will not be calculated.");
                                rules.add("Every attempted correct answer will reward you 4 marks whereas every attempted wrong answer will penalize you for -1 Mark.");
                            }

                            testSeriesAdapter=new TestSeriesAdapter(listSeries,activity,disclaimer,rules,marks);
                            recycler_view.setAdapter(testSeriesAdapter);


                            if(testSeriesAdapter.getItemCount()<=0)
                            {
                                CommonUtils.showToast(TestSeriesActivity.this,"No Data found");
                                finish();
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
                try {
                    CustomDialog.closeDialog();
                }catch (Exception e)
                {

                }
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    msg = activity.getResources().getString(R.string.error_1);
                } else if (error instanceof AuthFailureError) {
                    msg = activity.getResources().getString(R.string.error_2);
                } else if (error instanceof ServerError) {
                    msg = activity.getResources().getString(R.string.error_2);
                } else if (error instanceof NetworkError) {
                    msg = activity.getResources().getString(R.string.error_3);
                } else if (error instanceof ParseError) {
                    msg = activity.getResources().getString(R.string.error_4);
                }

                // Toasty.warning(SubjectActivity.this, error.getMessage(), Toast.LENGTH_SHORT, true).show();

              finish();
            }
        })


       ;

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
