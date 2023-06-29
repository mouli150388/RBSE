package com.tutorix.tutorialspoint.search;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.PaginationScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class SearchFragment extends Fragment {


    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.lnr_toolbar)
    RelativeLayout lnrToolbar;
    @BindView(R.id.img_background)
    ImageView imgBackground;
    @BindView(R.id.recycler_search)
    RecyclerView recyclerSearch;
    List<SubChapters> listChapters;
    SearchAdapter searchAdapter;
    String[] userInfo;
    String userId;
    String loginType;
    String access_token;
    String classId = "0";
    String subjectId = "0";


    private static int PAGE_START = 0;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.img_mic)
    ImageView imgMic;
    @BindView(R.id.loading_progress)
    ProgressBar loadingProgress;
    @BindView(R.id.txt_nosearch)
    TextView txt_nosearch;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;

    private int OFFSET = 0;
    private int LIMIT = 10;
    Activity _this;

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        AppConfig.setLanguages(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        search.setIconified(false);
        search.clearFocus();
        search.requestFocusFromTouch();
        //ImageView closeBtn = (ImageView) search.findViewById(R.id.search_close_btn);
        //closeBtn.setEnabled(false);
        //closeBtn.setImageDrawable(null);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _this = getActivity();
        listChapters = new ArrayList<>();
        searchAdapter = new SearchAdapter(_this,this);
        userInfo = SessionManager.getUserInfo(getActivity());
        userId = userInfo[0];
        loginType = userInfo[2];
        classId = userInfo[4];
        access_token = userInfo[1];

        try {
            AutoCompleteTextView searchText = (AutoCompleteTextView) search.findViewById(R.id.search_src_text);
            searchText.setHintTextColor(Color.parseColor("#898888"));
            searchText.setTextColor(Color.parseColor("#898888"));
            searchText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "opensans_regular.ttf"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        checkData();

        checkPermission();
        LinearLayoutManager linearLayoutManager;
        recyclerSearch.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getActivity()));
        recyclerSearch.setAdapter(searchAdapter);
        recyclerSearch.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                currentPage += 1;

                if (AppStatus.getInstance(_this).isOnline()) {

                    OFFSET = OFFSET + LIMIT;
                    searchQuery(search.getQuery().toString());
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
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                checkData();
                return false;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    clearOffSet();
                    checkData();
                    searchQuery(query);
                } else {
                    searchAdapter.listSubChapters.clear();
                    listChapters.clear();
                    searchAdapter.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    searchAdapter.listSubChapters.clear();
                    listChapters.clear();
                    searchAdapter.notifyDataSetChanged();
                    checkData();
                    if (newText.length() > 3) {


                        clearOffSet();

                        searchQuery(newText);

                    }


                } else {
                    searchAdapter.listSubChapters.clear();
                    listChapters.clear();
                    searchAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchAdapter.listSubChapters.clear();
                listChapters.clear();
                search.setQuery("", false);
                searchAdapter.notifyDataSetChanged();
                checkData();
                return false;
            }
        });
        setVoice();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(strReq!=null)
            strReq.cancel();

        try{
            speechRecognizer.destroy();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.search, R.id.lnr_toolbar, R.id.img_background, R.id.recycler_search,R.id.img_search, R.id.img_mic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search:
                break;
            case R.id.lnr_toolbar:
                break;
            case R.id.img_background:
                break;
            case R.id.recycler_search:
                break;
            case R.id.img_search:
                break;
            case R.id.img_mic:
                break;
        }
    }


    StringRequest strReq;
    String tag_string_req = "search_videos";

    private void searchQuery(String search_text) {
        if (_this == null) {
            return;
        }
        if (!AppStatus.getInstance(_this).isOnline()) {
            CommonUtils.showToast(_this, "Please check your connection!");
            return;
        }


        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.accessToken, access_token);
            //fjson.put(Constants.classId, classId);
            //fjson.put(Constants.subjectId, subjectId);
            fjson.put("start_from", OFFSET);
            fjson.put("limit", LIMIT);
            fjson.put("search_text", search_text);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);


         strReq = new StringRequest(Request.Method.POST,
                Constants.SEARCH_VIDEOS, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(Constants.response, response);

                loadingProgress.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean(Constants.errorFlag);
                    //TOTAL_PAGES = jObj.getInt("total_count");
                    if (!error) {

                        JSONArray lecture_array = jObj.getJSONArray("lecture_array");
                        JSONObject jsonObject;
                        SubChapters chapters;
                        for (int k = 0; k < lecture_array.length(); k++) {
                            jsonObject = lecture_array.getJSONObject(k);

                            chapters = new SubChapters();
                            chapters.txt = jsonObject.getString("title");
                            chapters.classid = jsonObject.getString("class_id");
                            chapters.subjectid = jsonObject.getString("subject_id");
                            chapters.section_id = jsonObject.getString("section_id");
                            chapters.lecture_id = jsonObject.getString("lecture_id");
                            chapters.start_time_secs = jsonObject.getInt("start_time_secs");
                            chapters.lecture_duration = jsonObject.getString("total_duration");
                            chapters.userid=userId;
                            listChapters.add(chapters);
                        }


                        Log.v("item count", "Item count " + searchAdapter.getItemCount());
                        checkData();
                        if (lecture_array.length() < LIMIT) {
                            System.out.print("Empty");
                        } else {
                            TOTAL_PAGES = TOTAL_PAGES + 1;
                        }
                        if (OFFSET == 0) {
                            searchAdapter.addAll(listChapters);
                            searchAdapter.notifyDataSetChanged();
                            if (currentPage == TOTAL_PAGES) {
                                isLastPage = true;
                            } else if (currentPage < TOTAL_PAGES) {
                                searchAdapter.addLoadingFooter();
                            }
                        } else {
                            if (isLoading) {
                                searchAdapter.removeLoadingFooter();
                                isLoading = false;
                            }

                            searchAdapter.addAll(listChapters);
                            searchAdapter.notifyDataSetChanged();

                            if (currentPage != TOTAL_PAGES) searchAdapter.addLoadingFooter();
                            else isLastPage = true;
                        }

                        /*if (currentPage == TOTAL_PAGES) {
                            isLastPage = true;
                        } else if (currentPage < TOTAL_PAGES) {
                            searchAdapter.addLoadingFooter();
                        }*/
                    } else {
                        String errorMsg = jObj.getString(Constants.message);
                        //CommonUtils.showToast(getActivity(),errorMsg);
                        checkData();
                        // Toasty.warning(BookmarkActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //CommonUtils.showToast(getActivity(),e.getMessage());
                    checkData();
                    //Toasty.warning(BookmarkActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(_this==null)
                {
                    return;
                }
                String msg = "";
                loadingProgress.setVisibility(View.GONE);

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


            }
        }) {
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

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
        ;
        AppController.getInstance().cancelPendingRequests(tag_string_req);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        txt_nosearch.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);

    }

    private void checkData() {
        if (listChapters.size() > 0) {
            recyclerSearch.setVisibility(View.VISIBLE);
            imgBackground.setVisibility(View.GONE);
            txt_nosearch.setVisibility(View.GONE);
        } else {
            recyclerSearch.setVisibility(View.GONE);
            imgBackground.setVisibility(View.VISIBLE);
            txt_nosearch.setVisibility(View.VISIBLE);
        }
    }

    private void clearOffSet() {
        isLoading = false;
        isLastPage = false;
        OFFSET = 0;
        currentPage = 0;
        TOTAL_PAGES = 0;
        PAGE_START = 0;
        listChapters.clear();
        searchAdapter.clear();

        searchAdapter.notifyDataSetChanged();

    }

    Dialog dialogExpeiry;

    public void showExpieryAlert() {
        Intent i = new Intent(_this, SubscribePrePage.class);
        i.putExtra("flag", "M");
        startActivity(i);
        /*boolean isActivation=(loginType.isEmpty());
        try {
            if (dialogExpeiry != null && dialogExpeiry.isShowing())
                dialogExpeiry.dismiss();
            dialogExpeiry = null;
            View view = getLayoutInflater().inflate(R.layout.activation_end_layout, null);
            dialogExpeiry = new Dialog(_this);
            Objects.requireNonNull(dialogExpeiry.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Button btn_cancel = view.findViewById(R.id.btn_cancel);
            TextView dialog_message = view.findViewById(R.id.dialog_message);
            Button btn_subscribe = view.findViewById(R.id.btn_subscribe);


            if (!isActivation) {
                dialog_message.setText("Your activation expired, To use this feature Renew your subscription");


            } else {
                dialog_message.setText("This is a premium feature, This only available for Activated users, Please Activate your account with Tutorix");
            }
            //btn_cancel.setText("Ok");
            //btn_subscribe.setVisibility(View.GONE);


            btn_subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogExpeiry.dismiss();
                    Intent i = new Intent(_this, SubscribePrePage.class);
                    i.putExtra("flag", "M");
                    startActivity(i);

                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogExpeiry.dismiss();
                }
            });

            dialogExpeiry.setContentView(view);
            dialogExpeiry.setCancelable(false);
            if (!isActivation) {

                btn_subscribe.setText("Renew Now");


            } else {
                btn_subscribe.setText("Subscribe Now");
            }
            dialogExpeiry.show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    SpeechRecognizer speechRecognizer;
    private void setVoice()
    {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(_this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                search.setQuery("",false);
                search.setQueryHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                search.setQuery(data.get(0),false);

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        imgMic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                    try {
                        speechRecognizer.stopListening();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try{
            speechRecognizer.destroy();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            speechRecognizer.destroy();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(_this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(_this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }
int RecordAudioRequestCode=101;
}
