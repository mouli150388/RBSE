package com.tutorix.tutorialspoint.ncert;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NCERTListActivity extends AppCompatActivity {

    @BindView(R.id.lnr_top)
    LinearLayout lnr_top;
    @BindView(R.id.img_gif)
    ImageView imgGif;
    @BindView(R.id.img_filter)
    ImageView imgFilter;
    @BindView(R.id.list_grid)
    CheckBox listGrid;
    @BindView(R.id.lnr_gridlist)
    LinearLayout lnrGridlist;
    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.lnr_home)
    LinearLayout lnrHome;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /*@BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;*/
    @BindView(R.id.lnr_reload)
    LinearLayout lnrReload;
    @BindView(R.id.img_nodata)
    ImageView imgNodata;
    @BindView(R.id.img_background)
    ImageView img_background;
    @BindView(R.id.rel_top_main)
    RelativeLayout rel_top_main;
    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    @BindView(R.id.txt_header)
    TextView txt_header;
    @BindView(R.id.lnr_nosdcard)
    LinearLayout lnrNosdcard;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String classId;
    private String userId;
    private String subjectId, subjectName;
    private String sectionId;
    private String sectionName;
    String access_token;
    String loginType;
    NCERTAdapter ncertAdapter;
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ncertlist);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        recyclerView.setLayoutManager(new LinearLayoutManager(NCERTListActivity.this, RecyclerView.VERTICAL, false));
        if (extras != null) {
            subjectId = extras.getString(Constants.subjectId);
            subjectName = extras.getString(Constants.subjectName);
            sectionId = extras.getString(Constants.sectionId);
            classId=extras.getString(Constants.classId);
            sectionName=extras.getString(Constants.sectionName);
        }else
        {
            sectionName="";
        }
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        imgGif.setVisibility(View.VISIBLE);

        if (subjectName.equalsIgnoreCase("physics")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_phy).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            rel_top_main.setBackgroundResource(R.drawable.ic_phy_bg_green);
            //img_background.setImageResource(R.drawable.ic_phy_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_phy_bg_white).into(img_background);
            txt_header.setText(sectionName);

        } else if (subjectName.equalsIgnoreCase("chemistry")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_che).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            //Glide.with(getApplicationContext()).load(R.drawable.ic_chemistry_bg_white).into(img_background);
            rel_top_main.setBackgroundResource(R.drawable.ic_chemistry_bg_green);
            //img_background.setImageResource(R.drawable.ic_chemistry_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_chemistry_bg_white).into(img_background);
            txt_header.setText(sectionName);

        } else if (subjectName.equalsIgnoreCase("biology")) {

            Glide.with(getApplicationContext()).load(R.drawable.gif_bio).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            rel_top_main.setBackgroundResource(R.drawable.ic_bio_bg_green);
            //img_background.setImageResource(R.drawable.ic_bio_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_bio_bg_white).into(img_background);
            txt_header.setText(sectionName);

        } else {

            Glide.with(getApplicationContext()).load(R.drawable.gif_math).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgGif);
            rel_top_main.setBackgroundResource(R.drawable.ic_math_bg_green);
            //img_background.setImageResource(R.drawable.ic_math_bg_white);
            Glide.with(getApplicationContext()).load(R.drawable.ic_math_bg_white).into(img_background);
            txt_header.setText(sectionName);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            switch (subjectId) {
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
        fetchQuestions();
    }

    private void fetchQuestions() {

        String tag_string_req = Constants.reqRegister;
        //loadingPanelID.show();
        
        try {
            CustomDialog.showDialog(NCERTListActivity.this, true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
       
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.getOnlineURLImage(classId) +"ncert/" +subjectId+"/"+sectionId+ ".json",
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
                            JSONArray questionArray=jObj.getJSONArray(sectionId);
                            ArrayList<NcertModel>listQuestions=new ArrayList<>();
                            NcertModel ncertModel;
                            JSONObject question;
                            for(int k=0;k<questionArray.length();k++)
                            {
                                ncertModel=new NcertModel();
                                question=questionArray.getJSONObject(k);
                                ncertModel.board=question.getString("board");
                                ncertModel.question=question.getString("question");
                                ncertModel.class_id=question.getString("class_id");
                                ncertModel.exercise=question.getString("exercise");
                                ncertModel.subject_id=question.getString("subject_id");
                                ncertModel.chapter_id=question.getString("chapter_id");
                                ncertModel.board=question.getString("board");
                                ncertModel.exercise_qnum=question.getString("exercise_qnum");
                                ncertModel.user_id=question.getString("user_id");
                                ncertModel.question_id=question.getString("question_id");
                                try {
                                    ncertModel.question = ncertModel.question.replaceAll("\\\\\\(|\\]", "\\$");
                                    ncertModel.question = ncertModel.question.replaceAll("\\\\\\)|\\]", "\\$");
                                }catch (Exception e)
                                {

                                }
                                //  ncertModel.question=ncertModel.question.replaceAll("\\\\","$");
                                listQuestions.add(ncertModel);
                            }
                          String  basePath = AppConfig.getOnlineURLImage(classId);
                           ncertAdapter=new NCERTAdapter(listQuestions,NCERTListActivity.this,basePath,sectionName);
                            recyclerView.setAdapter(ncertAdapter);
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
               // subjectView.showMessage( msg);
                // Toasty.warning(Subjectthis, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                finish();
            }
        })


        {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
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

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}