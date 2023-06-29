package com.tutorix.tutorialspoint.toc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.gson.Gson;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TOCActivity extends AppCompatActivity {

    String toc_json;
    @BindView(R.id.toc_recyclerview)
    RecyclerView toc_recyclerview;
    @BindView(R.id.img_back)
    View img_back;
    TOCAdapter tocAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tocactivity);
        ButterKnife.bind(this);
        toc_json = getIntent().getStringExtra("toc_json");
        tocAdapter = new TOCAdapter();
        toc_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        toc_recyclerview.setAdapter(tocAdapter);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.back_sound);
                onBackPressed();
            }
        });

        fetchTOC();
    }

    private void fetchTOC() {

        String tag_string_req = Constants.reqRegister;
//loadingPanelID.show();

        try {
            CustomDialog.showDialog(TOCActivity.this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        StringRequest strReq = new StringRequest(Request.Method.GET,
                toc_json,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
// loadingPanelID.hide();
                            try {
                                CustomDialog.closeDialog();
                            } catch (Exception e) {

                            }

                            JSONObject jObj = new JSONObject(response);


                            Gson gson = new Gson();
                            TOCSectionModel tocSectionModel;
                            tocSectionModel = gson.fromJson(jObj.toString(), TOCSectionModel.class);
                            Log.d("Response", "Response " + tocSectionModel.getTOCSections());
                            tocAdapter.setList((ArrayList<TOCSection>) tocSectionModel.getTOCSections());
                            Log.d("Response", "Response " + tocSectionModel.getTOCSections());
                            Log.d("Response", "Response " + tocSectionModel.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                CustomDialog.closeDialog();
                            } catch (Exception ee) {

                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = "";
//loadingPanelID.hide();
                try {
                    CustomDialog.closeDialog();
                } catch (Exception e) {

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
                CommonUtils.showToast(getApplicationContext(), msg);
// Toasty.warning(Subjectthis, error.getMessage(), Toast.LENGTH_SHORT, true).show();

                finish();
            }
        }) {
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
                } catch (Exception e) {
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