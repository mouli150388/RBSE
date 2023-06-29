package com.tutorix.tutorialspoint.doubts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.DoubtModel;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.PaginationScrollListener;
import com.tutorix.tutorialspoint.views.LockableBottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DoubtsActivity extends AppCompatActivity implements View.OnClickListener{

    Activity _this;
    Bundle bundle;
    String subjectId="";
    String selectd_subject;
    View img_filter;
    //View lnr_reload;
    View home;
    DoubtsNewAdapter mAdapter;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbar_layout;
    AppBarLayout app_bar;
    View lnr_top_main;
    List<DoubtModel> listDoubts;
    private static int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int OFFSET = 0;
    private int LIMIT = 10;
    private int currentPage = PAGE_START;
    LinearLayout lnr_reload;
    FloatingActionButton fab_ask;
    TextView txt_ask;
    View cars_latest;
    View rel_ask_doubt;
    LinearLayout lnr_loading_layout;
    Resources res;
    LinearLayout bottomSheet;
    LinearLayout lnr_mydoubts_layout;

    ImageView imgPhy;

    CheckedTextView txtPhy;

    ImageView imgChe;

    CheckedTextView txtChe;

    ImageView imgBio;

    CheckedTextView txtBio;

    ImageView imgMath;

    CheckedTextView txtMath;

    ImageView img_back_filter1;

    BottomSheetBehavior sheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = DoubtsActivity.this;
        setContentView(R.layout.fragment_doubts);
        SharedPreferences sh=getSharedPreferences("doubts",MODE_PRIVATE);
        sh.edit().clear().apply();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar.setBackgroundResource(R.drawable.rectangle_gradient_home_actionabr);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                AppController.getInstance().playAudio(R.raw.back_sound);
            }
        });

        img_filter = findViewById(R.id.img_filter);
        rel_ask_doubt = findViewById(R.id.rel_ask_doubt);
        lnr_reload = findViewById(R.id.lnr_reload);
        lnr_mydoubts_layout = findViewById(R.id.lnr_mydoubts_layout);
        // lnr_reload = findViewById(R.id.lnr_reload);
        home = findViewById(R.id.home);
        fab_ask = findViewById(R.id.fab_ask);
        txt_ask = findViewById(R.id.txt_ask);
        cars_latest = findViewById(R.id.cars_latest);
        bottomSheet = findViewById(R.id.bottom_sheet);
        imgPhy = findViewById(R.id.img_phy);
        txtPhy = findViewById(R.id.txt_phy);
        imgChe = findViewById(R.id.img_che);
        txtChe = findViewById(R.id.txt_che);
        imgBio = findViewById(R.id.img_bio);
        txtBio = findViewById(R.id.txt_bio);
        imgMath = findViewById(R.id.img_math);
        txtMath = findViewById(R.id.txt_math);
        img_back_filter1 = findViewById(R.id.img_back_filter1);

        imgPhy.setOnClickListener(this);
        txtPhy.setOnClickListener(this);
        imgChe.setOnClickListener(this);
        txtChe.setOnClickListener(this);
        imgBio.setOnClickListener(this);
        txtBio.setOnClickListener(this);
        imgMath.setOnClickListener(this);
        txtMath.setOnClickListener(this);
        img_back_filter1.setOnClickListener(this);

        rvQuestions = findViewById(R.id.rvQuestions);
        app_bar = findViewById(R.id.app_bar);
        lnr_top_main =findViewById(R.id.lnr_top_main);
        toolbar_layout = findViewById(R.id.toolbar_layout);
        lnr_loading_layout = findViewById(R.id.lnr_loading_layout);
        rvQuestions.setHasFixedSize(true);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        // sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //Log.v("XX","XXXXXX "+newState);

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        if (sheetBehavior instanceof LockableBottomSheetBehavior) {
                            ((LockableBottomSheetBehavior) sheetBehavior).setLocked(true);
                        }
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //collapsingToolbar.setTitle(chapter);
                    //lnr_top_main.setVisibility(View.INVISIBLE);
                    isShow = true;
                } else if (isShow) {
                    //collapsingToolbar.setTitle(" ");
                    //lnr_top_main.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });
        _this=this;
        listDoubts=new ArrayList<>();
        mAdapter = new DoubtsNewAdapter(_this);
        rvQuestions.setAdapter(mAdapter);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(_this);
        rvQuestions.setLayoutManager(layoutManager);

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(_this, resId);
        //rvQuestions.setLayoutAnimation(animation);
        //rvQuestions.scheduleLayoutAnimation();
        selectd_subject="";

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    getAllQuestions(subjectId);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        },1000);



        cars_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(_this,LatestDoubtsActivity.class));
            }
        });  home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home();
            }
        });

        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        /*txt_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askQuestion(v);
            }
        }); */
        rel_ask_doubt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askQuestion(v);
            }
        });

        rvQuestions.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                currentPage += 1;


                if (AppStatus.getInstance(_this).isOnline()) {
                    try {
                        OFFSET=OFFSET+LIMIT;
                        getAllQuestions(selectd_subject);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
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
    }

    public void home() {
        Intent in = new Intent(DoubtsActivity.this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            if(AppController.QUESTION_ASKED==0)
                return;
            AppController.QUESTION_ASKED=0;
            clearOffSet();
            getAllQuestions(selectd_subject);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void getAllQuestions(String subj) throws UnsupportedEncodingException {
        if(rvQuestions.getVisibility()!=View.VISIBLE)
            rvQuestions.setVisibility(View.VISIBLE);
        lnr_reload.setVisibility(View.GONE);
        final JSONObject fjson = new JSONObject();

        String[] userInfo = SessionManager.getUserInfo(_this.getApplicationContext());
        String access_token = userInfo[1];
        String userId = userInfo[0];
        String classId = userInfo[4];

        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.subjectId, subj);
            fjson.put("start_from", OFFSET);
            fjson.put("limit", LIMIT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = "getquestions";
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        if(OFFSET==0)
            lnr_loading_layout.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.DOUBT_GET_MYDOUBTS ,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        lnr_loading_layout.setVisibility(View.GONE);
                        try {
                            //Log.v("Resposne","Resposne "+response);
                            JSONObject jObj = new JSONObject(response);
                            //Log.v("Resposne","Resposne "+jObj.toString());
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            //listDoubts.clear();
                            if (!error) {

                                JSONArray array=jObj.getJSONArray("question_array");
                                JSONObject jsonObject;
                                DoubtModel model;
                                for(int k=0;k<array.length();k++)
                                {
                                    jsonObject=array.getJSONObject(k);

                                    model=new DoubtModel();
                                    model.user_id=jsonObject.getString("user_id");
                                    model.full_name=jsonObject.getString("full_name");
                                    model.user_photo=jsonObject.getString("user_photo");
                                    model.class_id=jsonObject.getString("class_id");
                                    model.subject_id=jsonObject.getInt("subject_id");
                                    model.question_id=jsonObject.getString("question_id");
                                    model.question=(Constants.JS_FILES+jsonObject.getString("question").replaceAll("<img src=\"","<img src=\"https://www.tutorix.com")).trim();
                                    model.question_rating=jsonObject.getString("question_rating");
                                    model.question_status=jsonObject.getString("question_status");
                                    model.question_asked_time=jsonObject.getString("question_asked_time");
                                    model.question_image=jsonObject.getString("question_image");
                                    model.question_chapter_name=jsonObject.getString("question_chapter_name");
                                    model.question_board=jsonObject.getString("question_board");
                                    try{
                                    model.question_year=jsonObject.getString("question_year");
                                    model.question_marks=jsonObject.getInt("question_marks");
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                                    if(!model.question_image.isEmpty())
                                        model.question=model.question+model.question_image;
                                    model.question_view_count=jsonObject.getString("question_view_count");
                                    model.class_name=AppConfig.getClassNameDisplayClass(model.class_id)+"th".replaceAll("-","");
                                    listDoubts.add(model);
                                }

                                if (array.length() < LIMIT) {
                                    System.out.print("Empty");
                                } else {
                                    TOTAL_PAGES = TOTAL_PAGES + 1;
                                }


                                if (OFFSET==0) {
                                    mAdapter.addDoubts(listDoubts);
                                    mAdapter.notifyDataSetChanged();

                                    if (currentPage == TOTAL_PAGES) {
                                        isLastPage = true;
                                    } else if (currentPage < TOTAL_PAGES) {
                                        mAdapter.addLoadingFooter();
                                    }
                                } else {
                                    mAdapter.removeLoadingFooter();
                                    isLoading = false;

                                    mAdapter.addDoubts(listDoubts);

                                    if (currentPage != TOTAL_PAGES) mAdapter.addLoadingFooter();
                                    else isLastPage = true;
                                }




                                nodata();
                            } else {
                                //CommonUtils.showToast(getApplicationContext(), jObj.getString(Constants.message));
                                String errorMsg = jObj.getString(Constants.message);
                                if (errorMsg.contains("Invalid access token")) {
                                    MyDatabase dbHandler = MyDatabase.getDatabase(_this.getApplicationContext());
                                    dbHandler.userDAO().deleteUser(userId);
                                    SessionManager.logoutUser(_this.getApplicationContext());
                                    Intent i = new Intent(_this, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(i);
                                    _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                                    return;
                                }
                                if (OFFSET==0) {
                                    // String errorMsg = jObj.getString(Constants.message);
                                    //CommonUtils.showToast(getActivity(),errorMsg);
                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                } else {
                                    mAdapter.removeLoadingFooter();
                                    isLoading = false;
                                    isLastPage = true;
                                }
                                nodata();

                                //finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            nodata();
                            //CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if(_this==null)
                        return;
                    lnr_loading_layout.setVisibility(View.GONE);
                    String msg = "";

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        msg = getResources().getString(R.string.error_1);
                    } else if (error instanceof AuthFailureError) {
                        msg =  getResources().getString(R.string.error_2);
                    } else if (error instanceof ServerError) {
                        msg =  getResources().getString(R.string.error_2);
                    } else if (error instanceof NetworkError) {
                        msg =  getResources().getString(R.string.error_3);
                    } else if (error instanceof ParseError) {
                        msg =  getResources().getString(R.string.error_4);
                    }
                    CommonUtils.showToast(_this, msg);
                    nodata();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                //finish();
                // Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }

        }){


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }};
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    RecyclerView rvQuestions;
    private void populateData() {

        nodata();
    }

    public void askQuestion(View view) {

        AppController.QUESTION_ASKED=0;
        Intent i = new Intent(_this, AskDoubtMainActivity.class);
        i.putExtra(Constants.intentType, bundle);
        startActivityForResult(i, 100);
        _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        AppController.getInstance().playAudio(R.raw.qz_next);
        AppController.getInstance().startItemAnimation(view);
        //showAlertQestions();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    clearOffSet();
                    getAllQuestions(selectd_subject);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AppController.getInstance().playAudio(R.raw.back_sound);

        }
        return super.onOptionsItemSelected(item);
    }

    public void filter(View view) {
        showPopup(view);
    }

    PopupWindow popupWindow;

    private void showPopup(View v) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (popupWindow == null) {

            View view = getLayoutInflater().inflate(R.layout.popup_menu, null);

            TextView txt_phy = findViewById(R.id.txt_phy);
            TextView txt_che = findViewById(R.id.txt_che);
            TextView txt_bio = findViewById(R.id.txt_bio);
            TextView txt_math = findViewById(R.id.txt_math);
            TextView txt_all = findViewById(R.id.txt_all);

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
                    try {
                        clearOffSet();
                        selectd_subject="1";
                        getAllQuestions("1");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            txt_che.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();

                    try {
                        clearOffSet();
                        selectd_subject="2";
                        getAllQuestions("2");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            txt_bio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    try {
                        clearOffSet();
                        selectd_subject="3";
                        getAllQuestions("3");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            txt_math.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    try {
                        clearOffSet();
                        selectd_subject="4";
                        getAllQuestions("4");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            txt_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    try {
                        clearOffSet();
                        selectd_subject="";
                        getAllQuestions("");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        popupWindow.showAsDropDown(v);
    }


    public void goBack(View view) {

    }

    private void nodata()
    {
        img_filter.setVisibility(View.GONE);
        if(mAdapter!=null&&mAdapter.getItemCount()>0) {
            rvQuestions.setVisibility(View.VISIBLE);
            lnr_reload.setVisibility(View.GONE);
            lnr_mydoubts_layout.setVisibility(View.VISIBLE);
            /*if(mAdapter.getItemCount()>5)
            {*/
                img_filter.setVisibility(View.VISIBLE);
           /* }else
            {
                img_filter.setVisibility(View.GONE);
            }*/
        }
        else {
            rvQuestions.setVisibility(View.GONE);
            lnr_reload.setVisibility(View.VISIBLE);
            lnr_mydoubts_layout.setVisibility(View.GONE);
        }
    }



    private void clearOffSet()
    {
        isLoading=false;
        isLastPage=false;
        OFFSET=0;
        currentPage=0;
        TOTAL_PAGES=0;
        PAGE_START=0;
        listDoubts.clear();
        mAdapter.clear();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.img_back_filter1:
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.img_phy:
            case R.id.txt_phy:
                selectd_subject = "1";
                clearOffSet();

                try {
                    getAllQuestions(selectd_subject);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.img_che:
            case R.id.txt_che:
                selectd_subject = "2";
                clearOffSet();

                try {
                    getAllQuestions(selectd_subject);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.img_bio:
            case R.id.txt_bio:
                selectd_subject = "3";
                clearOffSet();

                try {
                    getAllQuestions(selectd_subject);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.img_math:
            case R.id.txt_math:
                selectd_subject = "4";
                clearOffSet();

                try {
                    getAllQuestions(selectd_subject);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
