package com.tutorix.tutorialspoint.testseries;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.testseries.data.TestTrackModel;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.utility.PaginationScrollListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestSeriesTrack extends AppCompatActivity {
    @BindView(R.id.txt_header)
    TextView txt_header;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lnr_home)
    LinearLayout lnr_home;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.lnr_nodata)
    LinearLayout lnr_nodata;
    TestSeriesTrackAdapter testSeriesTrackAdapter;
    ArrayList<TestTrackModel>listTrack;
    private String access_token;
    private String userId;
    private String loginType;
    private String classid;
    TestSeriesTrack _this;

    private static int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;
    LinearLayoutManager linearLayoutManager;
    String test_series_type;
    String test_series_name;
    String test_series_file_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_series_track);
        ButterKnife.bind(this);

        _this=this;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());
        Intent in=getIntent();
        if(in!=null)
        {
            test_series_type=in.getStringExtra("test_series_type");
            test_series_name=in.getStringExtra("test_series_name");
            test_series_file_name=in.getStringExtra("test_series_file_name");
        }
        access_token = userInfo[1];
        userId = userInfo[0];;
        loginType = userInfo[2];
        classid = userInfo[4];
        listTrack=new ArrayList<>();

        recyclerview.setLayoutManager(linearLayoutManager=new LinearLayoutManager(this));
        testSeriesTrackAdapter=new TestSeriesTrackAdapter( _this);
        recyclerview.setAdapter(testSeriesTrackAdapter);
        recyclerview.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                currentPage += 1;


                    if (AppStatus.getInstance(_this).isOnline()) {

                            fillWithData(String.valueOf(currentPage * 6),false);

                    }



            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scroll Down

                } else if (dy < 0) {
                    // Scroll Up

                }
            }
        });
        fillWithData("0",true);
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

    public void updateUI()
    {
        if(testSeriesTrackAdapter.getItemCount()>0)
            lnr_nodata.setVisibility(View.GONE);
        else
            lnr_nodata.setVisibility(View.VISIBLE);
    }

    private void fillWithData(final String Offset,boolean isShow)  {



        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.classId, classid);
            fjson.put(Constants.accessToken, access_token);
            fjson.put("test_series_type", test_series_type);
            fjson.put("offset", Offset);
            fjson.put("limit", 6);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tag_string_req = Constants.reqRegister;




        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.GET_TEST_SERIES_TRACK,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        // loadingPanelID.hide();
                        CustomDialog.closeDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                              List<TestTrackModel>listdata  =new Gson().fromJson(jObj.getJSONArray("track_data").toString(),new TypeToken<List<TestTrackModel>>(){}.getType());


                                updateUI();

                                if (listdata.size() < 6) {

                                } else {
                                    TOTAL_PAGES = TOTAL_PAGES + 1;
                                }

                                if (Offset.equalsIgnoreCase("0")) {
                                    testSeriesTrackAdapter.addAll(listdata);
                                    testSeriesTrackAdapter.notifyDataSetChanged();

                                    if (currentPage == TOTAL_PAGES) {
                                        isLastPage = true;
                                    } else if (currentPage < TOTAL_PAGES) {
                                        testSeriesTrackAdapter.addLoadingFooter();
                                    }
                                } else {
                                    testSeriesTrackAdapter.removeLoadingFooter();
                                    isLoading = false;

                                    testSeriesTrackAdapter.addAll(listdata);

                                    if (currentPage != TOTAL_PAGES) testSeriesTrackAdapter.addLoadingFooter();
                                    else isLastPage = true;
                                }

                                updateUI();
                            } else {
                                if (Offset.equalsIgnoreCase("0")) {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(),errorMsg);
                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                } else {
                                    testSeriesTrackAdapter.removeLoadingFooter();
                                    isLoading = false;
                                    isLastPage = true;
                                }
                                updateUI();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(_this,e.getMessage());
                            updateUI();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg="";

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
                CommonUtils.showToast(getApplicationContext(), msg);
                //loadingPanelID.hide();
                CustomDialog.closeDialog();
                finish();

            }
        }){
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);


            @Override
            protected Map<String, String> getParams() {
                //Log.v("JSON","JSON "+encryption);
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };;
        if(isShow)
        CustomDialog.showDialog(TestSeriesTrack.this,true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
