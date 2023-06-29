package com.tutorix.tutorialspoint.doubts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.DoubtModel;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.PaginationScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;


public class LatestDoubtsActivity extends AppCompatActivity {
    Activity _this;
    Bundle bundle;
    String subjectId = "";
    String selectd_subject = "";
    String selectd_class = "";
    String selectd_ref = "";
    String selectd_marks = "";

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
    LinearLayout lnr_loading_layout;

    TextView txt_ask;
    TextView txt_nodoubts;
    ImageView img_back;
    private boolean isChecking = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_doubts);
        ButterKnife.bind(this);
        SharedPreferences sh=getSharedPreferences("doubts",MODE_PRIVATE);
        sh.edit().clear().apply();
        toolbar = findViewById(R.id.toolbar);


        lnr_reload = findViewById(R.id.lnr_reload);
        lnr_loading_layout = findViewById(R.id.lnr_loading_layout);
        // lnr_reload = findViewById(R.id.lnr_reload);
        home = findViewById(R.id.home);

        txt_ask = findViewById(R.id.txt_ask);
        img_back = findViewById(R.id.img_back);


        txt_nodoubts = findViewById(R.id.txt_nodoubts);
        rvQuestions = findViewById(R.id.rvQuestions);
        app_bar = findViewById(R.id.app_bar);
        lnr_top_main = findViewById(R.id.lnr_top_main);
        toolbar_layout = findViewById(R.id.toolbar_layout);
        rvQuestions.setHasFixedSize(true);
        txt_nodoubts.setText("");
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
        _this = this;
        listDoubts = new ArrayList<>();
        mAdapter = new DoubtsNewAdapter(_this);
        rvQuestions.setAdapter(mAdapter);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(_this);
        rvQuestions.setLayoutManager(layoutManager);

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(_this, resId);
        //rvQuestions.setLayoutAnimation(animation);
        //rvQuestions.scheduleLayoutAnimation();
        selectd_subject = "";
        selectd_class = "";
        try {
            getAllQuestions();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(LatestDoubtsActivity.this,DoubtFilterActivity.class),200);

               // sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //lnrClass.setVisibility(View.VISIBLE);
               // lnrSubject.setVisibility(View.GONE);

            }
        });


        txt_ask.setOnClickListener(new View.OnClickListener() {
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
                        OFFSET = OFFSET + LIMIT;
                        getAllQuestions();
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

    StringRequest strReq;
    private void getAllQuestions() throws UnsupportedEncodingException {
        final JSONObject fjson = new JSONObject();

        String[] userInfo = SessionManager.getUserInfo(_this.getApplicationContext());
        String access_token = userInfo[1];
        String userId = userInfo[0];
        String classId = userInfo[4];

        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.classId, selectd_class);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.subjectId, selectd_subject);
            fjson.put(Constants.marks, selectd_marks);
            fjson.put(Constants.reference, selectd_ref);
            fjson.put("start_from", OFFSET);
            fjson.put("limit", LIMIT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = "getquestions";
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        if (OFFSET == 0)
            lnr_loading_layout.setVisibility(View.VISIBLE);
         strReq = new StringRequest(Request.Method.POST, Constants.DOUBT_GET_LATESTDOUBTS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        //CustomDialog.closeDialog();
                        if(_this==null)
                        {
                            return;
                        }
                        try {
                            //Log.v("Resposne", "Resposne " + response);
                            JSONObject jObj = new JSONObject(response);
                            //Log.v("Resposne","Resposne "+jObj.toString());
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            //listDoubts.clear();
                            if (!error) {
                                if(_this==null)
                                {
                                    return;
                                }
                                JSONArray array = jObj.getJSONArray("question_array");
                                JSONObject jsonObject;
                                DoubtModel model;
                                for (int k = 0; k < array.length(); k++) {
                                    jsonObject = array.getJSONObject(k);

                                    model = new DoubtModel();
                                    model.user_id = jsonObject.getString("user_id");
                                    model.full_name = jsonObject.getString("full_name");
                                    model.user_photo = jsonObject.getString("user_photo");
                                    model.class_id = jsonObject.getString("class_id");
                                    model.subject_id = jsonObject.getInt("subject_id");
                                    model.question_id = jsonObject.getString("question_id");
                                    model.question = (Constants.JS_FILES + jsonObject.getString("question").replaceAll("<img src=\"", "<img src=\"https://www.tutorix.com")).trim();
                                    model.question_rating = jsonObject.getString("question_rating");
                                    model.question_status = jsonObject.getString("question_status");
                                    model.question_asked_time = jsonObject.getString("question_asked_time");
                                    model.question_image = jsonObject.getString("question_image");
                                    model.question_view_count = jsonObject.getString("question_view_count");
                                    try{
                                    model.question_marks = jsonObject.getInt("question_marks");
                                    model.question_board = jsonObject.getString("question_board");
                                        model.question_year=jsonObject.getString("question_year");
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                    model.class_name = AppConfig.getClassNameDisplayClass(model.class_id)+"th".replaceAll("-","");
                                    listDoubts.add(model);
                                }
                                if(_this==null)
                                {
                                    return;
                                }
                                if (array.length() < LIMIT) {
                                    System.out.print("Empty");
                                } else {
                                    TOTAL_PAGES = TOTAL_PAGES + 1;
                                }

                                lnr_loading_layout.setVisibility(View.GONE);
                                if (OFFSET == 0) {
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


                                populateData();
                            } else {
                                lnr_loading_layout.setVisibility(View.GONE);
                                //CommonUtils.showToast(getApplicationContext(), jObj.getString(Constants.message));
                                String errorMsg = jObj.getString(Constants.message);
                                if (errorMsg.contains("Invalid access token")) {
                                    MyDatabase dbHandler = MyDatabase.getDatabase(_this.getApplicationContext());
                                    dbHandler.userDAO().deleteUser(userId);
                                    SessionManager.logoutUser(_this.getApplicationContext());
                                    Intent i = new Intent(_this, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(i);
                                    _this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                                    return;
                                }
                                if (OFFSET == 0) {
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
                if(_this==null)
                {
                    return;
                }
                lnr_loading_layout.setVisibility(View.GONE);
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
                CommonUtils.showToast(_this, msg);
                nodata();
                //finish();
                // Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }

        }) {


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
            }
        };
        if(_this!=null)
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    RecyclerView rvQuestions;

    private void populateData() {

        nodata();
    }

    public void askQuestion(View view) {

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
                    getAllQuestions();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode==200&&resultCode==200)
        {
            disMissDialog(data.getStringExtra("sub"),data.getStringExtra("cls"),data.getStringExtra("ref"),data.getStringExtra("marks"));
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

            View view = getLayoutInflater().inflate(R.layout.filter_layout, null);

            RadioGroup classradioGroup = view.findViewById(R.id.radio_classes);
            RadioGroup radio_subjects = view.findViewById(R.id.radio_subjects);
            Button btn_apply = view.findViewById(R.id.btn_apply);
            classradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radio_1:
                            selectd_class = "1";
                            break;
                        case R.id.radio_2:
                            selectd_class = "2";
                            break;
                        case R.id.radio_3:
                            selectd_class = "3";
                            break;
                        case R.id.radio_4:
                            selectd_class = "4";
                            break;
                        case R.id.radio_5:
                            selectd_class = "5";
                            break;
                        case R.id.radio_6:
                            selectd_class = "6";
                            break;
                        case R.id.radio_7:
                            selectd_class = "7";
                            break;
                        case R.id.radio_8:
                            selectd_class = "8";
                            break;
                        case R.id.radio_9:
                            selectd_class = "9";
                            break;
                        case R.id.radio_10:
                            selectd_class = "";
                            break;

                    }
                }
            });
            radio_subjects.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radio_sub_1:
                            selectd_subject = "1";
                            break;
                        case R.id.radio_sub_2:
                            selectd_subject = "2";
                            break;
                        case R.id.radio_sub_3:
                            selectd_subject = "3";
                            break;
                        case R.id.radio_sub_4:
                            selectd_subject = "4";
                            break;
                        case R.id.radio_sub_all:
                            selectd_subject = "";
                            break;
                    }
                }
            });
            btn_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearOffSet();

                    try {
                        getAllQuestions();
                        popupWindow.dismiss();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });

            popupWindow = new PopupWindow(
                    view,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            // popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);


        }
        popupWindow.showAsDropDown(v);
    }


    public void goBack(View view) {

    }

    private void nodata() {
        if (mAdapter != null && mAdapter.getItemCount() > 0) {
            lnr_reload.setVisibility(View.GONE);
            lnr_loading_layout.setVisibility(View.GONE);
        }
        else
            lnr_reload.setVisibility(View.VISIBLE);
    }


    private void clearOffSet() {
        isLoading = false;
        isLastPage = false;
        OFFSET = 0;
        currentPage = 0;
        TOTAL_PAGES = 0;
        PAGE_START = 0;
        listDoubts.clear();
        mAdapter.clear();

    }

    public void selectedClass(String selectd_class)
    {
        this.selectd_class=selectd_class;

    }



    @Override
    public void onBackPressed() {
        _this=null;
        if(strReq!=null)
            AppController.getInstance().cancelPendingRequests("getquestions");
        finish();
    }
    @Override
    protected void onDestroy() {

        if(strReq!=null)
            AppController.getInstance().cancelPendingRequests("getquestions");
        super.onDestroy();
    }

    public void disMissDialog(String sub, String cls, String ref, String marks) {
        try {
            selectd_subject=sub;
            selectd_class=cls;
            selectd_ref=ref;
            selectd_marks=marks;
            clearOffSet();
            txt_nodoubts.setText("No Doubts found for current Filter,\n Try search with other filter option");
            getAllQuestions();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
