package com.tutorix.tutorialspoint.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.SubAdapter;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.utility.PaginationScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

public class BookMarkActivity extends AppCompatActivity {
    Activity _this;
    private String access_token;
    private String userId;
    private String classId;
    private ArrayList<SubChapters> data;

    String loginType;
    String[]userInfo;

    ImageView img_filter;
    LinearLayout lnr_home;
    RecyclerView recyclerView;
    SubAdapter subchaptersAdapter;
    private static int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;
    LinearLayout lnr_reload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bookmark);
        _this=this;
        initData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        AppController.getInstance().playAudio(R.raw.back_sound);
        finish();
        return super.onSupportNavigateUp();
    }

    private void initData()
    {


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        userInfo= SessionManager.getUserInfo(_this);

        img_filter=findViewById(R.id.img_filter);
        lnr_home=findViewById(R.id.lnr_home);
        lnr_reload=findViewById(R.id.lnr_reload);
        img_filter.setVisibility(View.VISIBLE);
        access_token = userInfo[1];
        userId = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];


        initUI();
        if(/*AppConfig.checkSDCardEnabled(_this,userId,classId)&&*/AppConfig.checkSdcard(classId,getApplicationContext()))
        {
            LoadData();
        }else
        {
            if (AppStatus.getInstance(_this).isOnline()) {
                try {
                    fillWithData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                CommonUtils.showToast(_this,getString(R.string.no_internet));
                // Toasty.info(BookmarkActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        }
        /*if(loginType.isEmpty())
        {
            if (AppStatus.getInstance(_this).isOnline()) {
                try {
                    fillWithData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                CommonUtils.showToast(_this,getString(R.string.no_internet));
                // Toasty.info(BookmarkActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(_this,userId,classId)&&AppConfig.checkSdcard(classId,getApplicationContext()))
            {
                LoadData();
            }else
            {
                if (AppStatus.getInstance(_this).isOnline()) {
                    try {
                        fillWithData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtils.showToast(_this,getString(R.string.no_internet));
                    // Toasty.info(BookmarkActivity.this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                }
            }
        }else
        {
            LoadData();
        }
        */

        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(BookMarkActivity.this, HomeTabActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            }
        }); img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
    }
    private void initUI()
    {
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager;
        recyclerView.setLayoutManager(linearLayoutManager=new LinearLayoutManager(_this, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                currentPage += 1;



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

        /*String subjectName = getResources().getString(R.string.bookmark);
        TextView subjectnameTVID = getView().findViewById(R.id.subjectnameTVID);
        subjectnameTVID.setText(subjectName);
        subjectnameTVID.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bookmark_128, 0, 0);
*/
        //  initCollapsingToolbar(subjectName);

        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    }
    PopupWindow popupWindow;
    private void showPopup(View v) {
        if (popupWindow == null) {
            @SuppressLint("InflateParams")
            View view = getLayoutInflater().inflate(R.layout.popup_menu, null);

            TextView txt_phy = view.findViewById(R.id.txt_phy);
            TextView txt_che = view.findViewById(R.id.txt_che);
            TextView txt_bio = view.findViewById(R.id.txt_bio);
            TextView txt_math = view.findViewById(R.id.txt_math);
            TextView txt_all = view.findViewById(R.id.txt_all);

            popupWindow = new PopupWindow(
                    view,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);

            txt_phy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    filterData("1");

                }
            });
            txt_che.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    filterData("2");

                }
            });
            txt_bio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    filterData("3");
                }
            });
            txt_math.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    filterData("4");
                }
            });
            txt_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    filterData("");
                }
            });
        }
        popupWindow.showAsDropDown(v);
    }

    private void filterData(String subject_id)
    {
        if(subject_id.isEmpty())
        {
            addAdapter(data);
            return;
        }
        if(data.size()>0)
        {
            ArrayList<SubChapters>listData=new ArrayList<>();
            for(SubChapters subChapter:data)
            {
                if(subChapter.subjectid.equalsIgnoreCase(subject_id))
                    listData.add(subChapter);
            }
            addAdapter(listData);
        }
        nodata();
    }
    /*private void initCollapsingToolbar(final String subjectName) {
        final CollapsingToolbarLayout collapsingToolbar =
                getView().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = getView().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        collapsingToolbar.setStatusBarScrimResource(R.color.statusbar_color_new);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(subjectName);

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;

                }
            }
        });
    }*/


    private void LoadData() {

        MyDatabase dbhelper = MyDatabase.getDatabase(_this);
        data = (ArrayList<SubChapters>) dbhelper.subjectChapterDAO().getBookMarkDetailsBookmark( userId,classId);

        if (data.size() > 0) {
            ArrayList<SubChapters>list=new ArrayList<>();
            for(SubChapters sub:data)
            {
                sub.status="P";
                list.add(sub);
            }
            //lnr_reload.setVisibility(View.GONE);
            addAdapter(data);
        } else {
            //lnr_reload.setVisibility(View.VISIBLE);
            //CommonUtils.showToast(getActivity(),"No Bookmarks Found");
            // Toasty.warning(BookmarkActivity.this, "No Bookmarks Found", Toast.LENGTH_SHORT, true).show();
        }
        nodata();
    }

    private void fillWithData() throws UnsupportedEncodingException {


        data = new ArrayList<>();

        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.action_type, "B");
            fjson.put(Constants.subjectId, "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String tag_string_req = Constants.reqRegister;

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.BASE_URL + "lectures/actions/" + userId + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"), new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //Log.d(Constants.response, response);

                CustomDialog.closeDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean(Constants.errorFlag);
                    if (!error) {
                        JSONArray answers = jObj.getJSONArray("lectures");
                        for (int i = 0; i < answers.length(); i++) {
                            JSONObject json_data = answers.getJSONObject(i);
                            SubChapters chapters = new SubChapters();
                            chapters.lecture_id = json_data.getString("lecture_id");
                            chapters.txt = json_data.getString("lecture_name");
                           /* chapters.lecture_video_url = json_data.getString("lecture_video_url");
                            chapters.lecture_video_thumb = json_data.getString("lecture_video_thumb");*/
                            chapters.lecture_video_url =Constants.VIDEO_NAME;
                            chapters.lecture_video_thumb =Constants.VIDEO_THUMB_NAME;
                            chapters.video_srt =Constants.VIDEO_SRT;
                            chapters.lecture_duration = json_data.getString("lecture_duration");
                            chapters.subjectid = json_data.getString("subject_id");
                            chapters.classid = json_data.getString(Constants.classId);
                            chapters.userid = userId;
                            chapters.section_id = json_data.getString("section_id");
                            chapters.lecture_completed = json_data.getString("completed_flag").equals("Y");
                            chapters.lecture_bookmark = json_data.getString("bookmark_flag").equals("Y");
                            String s=json_data.getString("user_lecture_notes");
                            chapters.status="P";
                            if(s!=null&&s.length()>0&&!s.equals("null")) {
                                chapters.is_notes = true;
                                chapters.lecture_notes = s;
                            } else {
                                chapters.lecture_notes = "";
                                chapters.is_notes=false;
                            }

                            chapters.is_demo = true;
                            data.add(chapters);
                        }

                        addAdapter(data);
                    } else {
                        String errorMsg = jObj.getString(Constants.message);
                        //CommonUtils.showToast(getActivity(),errorMsg);
                        nodata();
                        // Toasty.warning(BookmarkActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //CommonUtils.showToast(getActivity(),e.getMessage());
                    nodata();
                    //Toasty.warning(BookmarkActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg="";
                CustomDialog.closeDialog();

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
                CommonUtils.showToast(_this, msg);

               finish();

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
        CustomDialog.showDialog(_this,true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void addAdapter(ArrayList<SubChapters> dataList)
    {

        subchaptersAdapter = new SubAdapter(dataList,"", _this, false,true,true);
        recyclerView.setAdapter(subchaptersAdapter);
        subchaptersAdapter.notifyDataSetChanged();
        nodata();

    }

    private void nodata()
    {
        if(subchaptersAdapter!=null&&subchaptersAdapter.getItemCount()>1)
        {
            lnr_reload.setVisibility(View.GONE);
        }else{
            lnr_reload.setVisibility(View.VISIBLE);
        }
    }

    public void home(View v)
    {
        Intent in=new Intent(_this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);


    }


    CoordinatorLayout.Behavior behavior;


}
