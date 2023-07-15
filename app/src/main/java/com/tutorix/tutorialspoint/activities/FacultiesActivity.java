package com.tutorix.tutorialspoint.activities;

import static com.tutorix.tutorialspoint.utility.Constants.FACULTY_CURRENT;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.FacultiesAdapter;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.utility.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FacultiesActivity extends AppCompatActivity {

    FacultiesActivity _this;
    String class_id;
    String loginType;
    List<Data> data;
    @BindView(R.id.rel_toolbar)
    RelativeLayout relToolbar;
    @BindView(R.id.viewpager_analysis)
    ViewPager viewpagerAnalysis;

    @BindView(R.id.radio_grp)
    RadioGroup radio_grp;
    @BindView(R.id.img_back)
    View img_back;

    FacultiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculties);
        ButterKnife.bind(this);
        _this = this;
        initUI();
    }

    private void initUI() {
        String[] userInfo = SessionManager.getUserInfo(_this);
        final String access_token = userInfo[1];
        final String user_id = userInfo[0];
        class_id = userInfo[4];
        loginType = userInfo[2];
        data = new ArrayList<>();
       /* Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setTitle("Subject Matter Experts");*/

        adapter=new FacultiesAdapter(_this);
        viewpagerAnalysis.setAdapter(adapter);
        viewpagerAnalysis.setPageTransformer(true,new ParallaxPageTransformer());
       /* if (AppStatus.getInstance(_this).isOnline()) {
            initList(access_token, user_id);
        } else {
            if(AppConfig.checkSDCardEnabled(_this,user_id,class_id)&&AppConfig.checkSdcard(class_id,getApplicationContext()))
            {
                initList(access_token, user_id);
            }else
            {
                CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
            }

        }
*/
        if(/*AppConfig.checkSDCardEnabled(_this,user_id,class_id)&&*/AppConfig.checkSdcard(class_id,getApplicationContext()))
        {
            initList(access_token, user_id);
        }else
        {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
        }
        viewpagerAnalysis.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AppController.getInstance().playAudio(R.raw.qz_next);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.back_sound);
                onBackPressed();
            }
        });
    }

    private void initList(String access_token, String user_id) {
        if(/*AppConfig.checkSDCardEnabled(_this,user_id,class_id)&&*/AppConfig.checkSdcard(class_id,getApplicationContext()))
        {

            File sdcard = new File(AppConfig.getFAQSSDCardPath(getApplicationContext()));

//Get the text file
            File file = new File(sdcard,"faculties.json");

//Read text from file
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);

                }
                br.close();

                try {
                    data.clear();
                    JSONObject jObj = new JSONObject(text.toString());
                    boolean error = jObj.getBoolean(Constants.errorFlag);
                    if (!error) {
                        JSONArray faculties = jObj.getJSONArray(Constants.faculties);
                        for (int i = 0; i < faculties.length(); i++) {
                            JSONObject json_data = faculties.getJSONObject(i);
                            Data fishData = new Data();
                            fishData.uid = json_data.getString(Constants.facultyId);
                            fishData.full_name = json_data.getString(Constants.facultyName);
                            fishData.introduction = json_data.getString(Constants.introduction);
                            fishData.photo_url = "file:///"+AppConfig.getFAQSSDCardPath(getApplicationContext())+json_data.getString(Constants.photoUrl);
                            fishData.video_url = json_data.getString(Constants.videoUrl);
                            fishData.video_thumb_url = json_data.getString(Constants.videoThumbUrl);
                            fishData.expertise = json_data.getString(Constants.expertise);
                            data.add(fishData);
                        }
                        radio_grp.removeAllViews();



                        int height=10;
                        height= (int) convertDpToPixel(10,getApplicationContext());
                        RadioGroup.LayoutParams param=new RadioGroup.LayoutParams(height,height);
                        param.leftMargin=2;
                        for(int k=0;k<data.size();k++)
                        {
                            RadioButton btn=new RadioButton(_this);
                            btn.setBackgroundResource(R.drawable.welcome_selector);
                            btn.setButtonDrawable(null);
                            btn.setLayoutParams(param);
                            radio_grp.addView(btn);
                        }
                        adapter.addFaculties(data);
                        viewpagerAnalysis.setAdapter(adapter);
                        if(radio_grp.getChildCount()>0)
                            ((RadioButton) radio_grp.getChildAt(0)).setChecked(true);
                        viewpagerAnalysis.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int i, float v, int i1) {

                            }

                            @Override
                            public void onPageSelected(int i) {

                                if(radio_grp.getChildCount()>i)
                                    ((RadioButton) radio_grp.getChildAt(i)).setChecked(true);
                            }

                            @Override
                            public void onPageScrollStateChanged(int i) {

                            }
                        });

                    } else {
                        String errorMsg = jObj.getString(Constants.message);
                        CommonUtils.showToast(getApplicationContext(), errorMsg);
                        //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                    //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
            }
            return;
        }
        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, user_id);
            fjson.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;
        String url = String.format(FACULTY_CURRENT, AppConfig.getClassName(class_id));
        //Log.v("url ", "url " + url);
        CustomDialog.showDialog(_this, true);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        //Log.d(Constants.response, response);


                        try {
                            data.clear();
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                JSONArray faculties = jObj.getJSONArray(Constants.faculties);
                                for (int i = 0; i < faculties.length(); i++) {
                                    JSONObject json_data = faculties.getJSONObject(i);
                                    Data fishData = new Data();
                                    fishData.uid = json_data.getString(Constants.facultyId);
                                    fishData.full_name = json_data.getString(Constants.facultyName);
                                    fishData.introduction = json_data.getString(Constants.introduction);
                                    fishData.photo_url = json_data.getString(Constants.photoUrl);
                                    fishData.video_url = json_data.getString(Constants.videoUrl);
                                    fishData.video_thumb_url = json_data.getString(Constants.videoThumbUrl);
                                    fishData.expertise = json_data.getString(Constants.expertise);
                                    data.add(fishData);
                                }
                                radio_grp.removeAllViews();



                                int height=10;
                                height= (int) convertDpToPixel(10,getApplicationContext());
                                RadioGroup.LayoutParams param=new RadioGroup.LayoutParams(height,height);
                                param.leftMargin=2;
                                for(int k=0;k<data.size();k++)
                                {
                                    RadioButton btn=new RadioButton(_this);
                                    btn.setBackgroundResource(R.drawable.welcome_selector);
                                    btn.setButtonDrawable(null);
                                    btn.setLayoutParams(param);
                                    radio_grp.addView(btn);
                                }
                                adapter.addFaculties(data);
                                viewpagerAnalysis.setAdapter(adapter);
                                if(radio_grp.getChildCount()>0)
                                    ((RadioButton) radio_grp.getChildAt(0)).setChecked(true);
                                viewpagerAnalysis.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int i, float v, int i1) {

                                    }

                                    @Override
                                    public void onPageSelected(int i) {

                                        if(radio_grp.getChildCount()>i)
                                            ((RadioButton) radio_grp.getChildAt(i)).setChecked(true);
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int i) {

                                    }
                                });

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);
                                //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialog.closeDialog();
                String msg = "";

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
                finish();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private static final float MIN_SCALE = 0.65f;
    private static final float MIN_ALPHA = 0.3f;
    class ParallaxPageTransformer implements  ViewPager.PageTransformer

    {

        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();
            if (position <-1){  // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            }
            else if (position <=1){ // [-1,1]

                page.setScaleX(Math.max(MIN_SCALE,1-Math.abs(position)));
                page.setScaleY(Math.max(MIN_SCALE,1-Math.abs(position)));
                page.setAlpha(Math.max(MIN_ALPHA,1-Math.abs(position)));

            }
            else {  // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }


           /* page.setTranslationX(-position*page.getWidth());



            if (position<-1){    // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            }
            else if (position<=0){    // [-1,0]
                page.setAlpha(1);
                page.setPivotX(0);
                page.setRotationY(90*Math.abs(position));

            }
            else if (position <=1){    // (0,1]
                page.setAlpha(1);
                page.setPivotX(page.getWidth());
                page.setRotationY(-90*Math.abs(position));

            }else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }*/

        }
    }


    public static float convertDpToPixel(float dp, Context context){

        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
