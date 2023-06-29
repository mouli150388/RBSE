package com.tutorix.tutorialspoint.anaylysis;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.login.LoginActivity;
import com.tutorix.tutorialspoint.models.TrackModel;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.views.CustomBarChartRender;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeVsSubFragment extends Fragment {


    @BindView(R.id.chart)
    BarChart barChart;
    @BindView(R.id.txt_days)
    TextView txtDays;
    @BindView(R.id.img_filter)
    ImageView imgFilter;
    @BindView(R.id.lnr_filter)
    LinearLayout lnrFilter;
    @BindView(R.id.txt_marks_title)
    TextView txt_marks_title;
    Typeface tf;
    int whichDay;
    private String access_token;
    private String userid;
    private String loginType;
    private String class_id;
    int k = 0;

    public TimeVsSubFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_vs_sub, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "opensans_regular.ttf");
        String[] userInfo = SessionManager.getUserInfo(getActivity());
        barChart.setNoDataText("");
        access_token = userInfo[1];
        userid = userInfo[0];
        ;
        loginType = userInfo[2];
        class_id = userInfo[4];
        try {
            int currrentClsId = Integer.parseInt(class_id);
            if (currrentClsId <= 7) {
                k = 0;
            } else if (currrentClsId == 8) {
                k = 1;
                //radioBio.setVisibility(View.GONE);

            } else if (currrentClsId == 9) {
                // radioMath.setVisibility(View.GONE);
                k = 2;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtDays.setText("7 " + getString(R.string.days));
        // txt_marks_title.setVisibility(View.GONE);
        try {
            whichDay = 1;
            chartData();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setData(BarData data) {
        if (getActivity() == null)
            return;
        Resources res = getResources();
        boolean isTablet = res.getBoolean(R.bool.isTablet);
        if (data == null || barChart == null)
            return;
        data.setBarWidth(0.4f);
        DecimalFormat mFormat = new DecimalFormat("###,###,###,##0");
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return mFormat.format(value);
            }
        });
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setContentDescription("");
        barChart.getAxisLeft().setAxisMinimum(0);

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        CustomBarChartRender barChartRender = new CustomBarChartRender(barChart, barChart.getAnimator(), barChart.getViewPortHandler());
        barChartRender.setRadius(10);
        barChart.setRenderer(barChartRender);

        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12);

        if (!isTablet) {
            xAxis.setTextSize(12);
            rightYAxis.setTextSize(12);
        } else {
            xAxis.setTextSize(14);
            rightYAxis.setTextSize(14);
        }

        final ArrayList<String> xVals = new ArrayList<>();
        if (k == 0) {
            xVals.add(getString(R.string.mathematics));
            xVals.add(getString(R.string.physics));
            xVals.add(getString(R.string.chemistry));
            xVals.add(getString(R.string.biology));
        } else if (k == 2) {
            xVals.add(getString(R.string.physics));
            xVals.add(getString(R.string.chemistry));
            xVals.add(getString(R.string.biology));
        } else if (k == 1) {
            xVals.add(getString(R.string.mathematics));
            xVals.add(getString(R.string.physics));
            xVals.add(getString(R.string.chemistry));
        }


        txt_marks_title.setVisibility(View.VISIBLE);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                //Log.d("asa", "data X" + xVals);
                return xVals.get((int) value % xVals.size());
            }
        });
        //barChart.getAxisLeft().setValueFormatter(new MyYAxisValueFormatter());
        barChart.setExtraBottomOffset(10);
        barChart.setPinchZoom(false);
        barChart.setTouchEnabled(false);
        barChart.clear();
        barChart.invalidate();
        if (tf != null)
            barChart.getXAxis().setTypeface(tf);
        barChart.setData(data);


    }


    PopupWindow popupWindow;
    PopupWindow popupWindowDays;

    private void showPopup(View v) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (popupWindowDays == null) {
            @SuppressLint("InflateParams")
            View view = getLayoutInflater().inflate(R.layout.popup_menu, null);

            TextView txt_phy = view.findViewById(R.id.txt_phy);
            TextView txt_che = view.findViewById(R.id.txt_che);
            TextView txt_bio = view.findViewById(R.id.txt_bio);
            TextView txt_math = view.findViewById(R.id.txt_math);
            TextView txt_all = view.findViewById(R.id.txt_all);
            txt_all.setVisibility(View.GONE);
            txt_phy.setText("7 " + getString(R.string.days));
            txt_che.setText("30 " + getString(R.string.days));
            txt_bio.setText("3 " + getString(R.string.months));
            txt_math.setText(getString(R.string.overall));

            popupWindowDays = new PopupWindow(
                    view,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindowDays.setOutsideTouchable(true);
            popupWindowDays.setBackgroundDrawable(new BitmapDrawable());
            popupWindowDays.setOutsideTouchable(true);

            txt_phy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    whichDay = 1;
                    txtDays.setText("7 " + getString(R.string.days));
                    // chartData();
                    try {
                        chartData();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            });
            txt_che.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    //subject_id=2;
                    whichDay = 2;
                    txtDays.setText("30 "+getString(R.string.days));
                    //chartData();
                    try {
                        chartData();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            });
            txt_bio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    //subject_id=3;
                    whichDay = 3;
                    txtDays.setText("3 " + getString(R.string.months));
                    //chartData();
                    try {
                        chartData();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            });
            txt_math.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    //subject_id=4;
                    whichDay = 4;
                    txtDays.setText(getString(R.string.overall));
                    //chartData();
                    try {
                        chartData();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            txt_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindowDays.dismiss();
                    txtDays.setText(getString(R.string.overall));

                    // chartData();
                }
            });
        }
        popupWindowDays.showAsDropDown(v);
    }

    private void chartData() throws UnsupportedEncodingException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        final JSONObject fjson3 = new JSONObject();
        try {
            fjson3.put(Constants.userId, userid);
            fjson3.put(Constants.classId, class_id);
            if (whichDay == 4) {
                fjson3.put("from_date", "0");
            } else if (whichDay == 1) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -7);
                fjson3.put("from_date", formatter.format(cal.getTime()));

            } else if (whichDay == 2) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -30);
                fjson3.put("from_date", formatter.format(cal.getTime()));

            } else if (whichDay == 3) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -90);
                fjson3.put("from_date", formatter.format(cal.getTime()));

            }
            fjson3.put("end_date", formatter.format(date));
            fjson3.put(Constants.accessToken, access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message3 = fjson3.toString();

        final String encryption3 = Security.encrypt(message3, Key);

        String tag_string_req = Constants.reqRegister;


        if (loginType.isEmpty()) {
            callOnline(tag_string_req, encryption3);
        } else if (loginType.equalsIgnoreCase("O")) {

            if (AppConfig.checkSDCardEnabled(getActivity(), userid, class_id) && AppConfig.checkSdcard(class_id,getContext())) {
                callOffline();
            } else {
                callOnline(tag_string_req, encryption3);
            }
        } else {
            callOffline();
        }

  /*      if (loginType.equalsIgnoreCase("O") || loginType.isEmpty()) {
            callOnline(tag_string_req);
        } else {callOffline(); }*/
    }

    private void callOffline() {

        MyDatabase dbhelper = MyDatabase.getDatabase(getActivity());
        List<TrackModel> trackList = null;
        try {
            //String startDate = fjson3.getString("from_date") + " 00:00:00";
            //String enddate = fjson3.getString("end_date") + " 23:59:59";
            if (whichDay == 4) {
                trackList = (List<TrackModel>) dbhelper.trackDAO().getTrackDetailsTimeLine(userid, class_id);

            } else if (whichDay == 1) {
                trackList = dbhelper.trackDAO().getSevenDayTrackDetailsTimeLine(userid, class_id);
            } else if (whichDay == 2) {
                trackList = dbhelper.trackDAO().getMonthTrackDetailsTimeLine(userid, class_id);
            } else if (whichDay == 3) {
                trackList = dbhelper.trackDAO().getThreeMonthTrackDetailsTimeLine(userid, class_id);
            }
            System.out.print("" + trackList);


            float physicsCount = 0, chemistryCount = 0, mathsCount = 0, biologyCount = 0;
            for (int i = 0; i < Objects.requireNonNull(trackList).size(); i++) {
                // Log.v("SubjectId","SubjectId "+(trackList.get(i).subject_id));

                String[] tokens = trackList.get(i).activity_duration.split(":");
                // Log.v("SubjectId","SubjectId "+(trackList.get(i).activity_duration));
                float value = (Integer.parseInt(tokens[0]) * 60 * 60) + (Integer.parseInt(tokens[1]) * 60) + (Integer.parseInt(tokens[2]));
                if (trackList.get(i).subject_id.equalsIgnoreCase("1")) {
                    physicsCount = physicsCount + value;
                } else if (trackList.get(i).subject_id.equalsIgnoreCase("2")) {
                    chemistryCount = chemistryCount + value;
                } else if (trackList.get(i).subject_id.equalsIgnoreCase("3")) {
                    biologyCount = biologyCount + value;
                } else if (trackList.get(i).subject_id.equalsIgnoreCase("4")) {
                    mathsCount = mathsCount + value;
                }

            }
            // Log.v("SubjectId","SubjectId "+physicsCount/60);

            ArrayList<BarEntry> entries = new ArrayList<>();


            // BarDataSet dataSet = new BarDataSet(entries, "");
            // dataSet.setColors(new int[]{ R.color.math_background,R.color.phy_background, R.color.che_background, R.color.bio_background}, Objects.requireNonNull(getActivity()).getBaseContext());

            // data.setBarWidth(0.6f);


            BarDataSet dataSet = null;
            if (k == 0) {
                entries.add(new BarEntry(0f, mathsCount / 60));
                entries.add(new BarEntry(1f, physicsCount / 60));
                entries.add(new BarEntry(2f, chemistryCount / 60));
                entries.add(new BarEntry(3f, biologyCount / 60));
                dataSet = new BarDataSet(entries, "");
                dataSet.setColors(new int[]{R.color.math_background, R.color.phy_background, R.color.che_background, R.color.bio_background}, Objects.requireNonNull(getActivity()).getBaseContext());

            } else if (k == 1) {

                entries.add(new BarEntry(0f, mathsCount / 60));
                entries.add(new BarEntry(1f, physicsCount / 60));
                entries.add(new BarEntry(2f, chemistryCount / 60));
                dataSet = new BarDataSet(entries, "");
                dataSet.setColors(new int[]{R.color.math_background, R.color.phy_background, R.color.che_background}, Objects.requireNonNull(getActivity()).getBaseContext());

            } else if (k == 2) {

                entries.add(new BarEntry(0f, physicsCount / 60));
                entries.add(new BarEntry(1f, chemistryCount / 60));
                entries.add(new BarEntry(2f, biologyCount / 60));
                dataSet = new BarDataSet(entries, "");
                dataSet.setColors(new int[]{R.color.phy_background, R.color.che_background, R.color.bio_background}, Objects.requireNonNull(getActivity()).getBaseContext());


            }


            BarData data = new BarData(dataSet);

            setData(data);


            //noData(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callOnline(String tag_string_req, String encryption3) {


        try {


            if (AppStatus.getInstance(Objects.requireNonNull(getActivity()).getBaseContext()).isOnline()) {
                StringRequest strReq = new StringRequest(Request.Method.GET,
                        Constants.USER_TRACK_TIME + "/" + userid + "?json_data=" + URLEncoder.encode(encryption3, "UTF-8"),
                        new Response.Listener<String>() {

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
                                //Log.d(Constants.response, response);
                                if (getContext() == null)
                                    return;
                                try {
                                    CustomDialog.closeDialog();

                                    JSONObject jObj = new JSONObject(response);
                                    boolean error = jObj.getBoolean(Constants.errorFlag);
                                    float a = 0;
                                    float b = 0;
                                    float c = 0;
                                    float d = 0;
                                    ArrayList<BarEntry> entries = new ArrayList<>();
                                    if (!error) {

                                        JSONObject track_data = jObj.getJSONObject("time_array");

                                        if (k == 0) {
                                            a = (float) track_data.getDouble("Physics");
                                            b = (float) track_data.getDouble("Chemistry");
                                            c = (float) track_data.getDouble("Biology");
                                            d = (float) track_data.getDouble("Mathematics");
                                        } else if (k == 1) {
                                            a = (float) track_data.getDouble("Physics");
                                            b = (float) track_data.getDouble("Chemistry");

                                            d = (float) track_data.getDouble("Mathematics");
                                        } else if (k == 2) {
                                            a = (float) track_data.getDouble("Physics");
                                            b = (float) track_data.getDouble("Chemistry");
                                            c = (float) track_data.getDouble("Biology");

                                        }

                                        //noData(false);
                                    } else {

                                        //CommonUtils.showToast(getActivity(),errorMsg);
                                        String errorMsg = jObj.getString(Constants.message);
                                        if (errorMsg.contains("Invalid access token")) {
                                            MyDatabase dbHandler = MyDatabase.getDatabase(getActivity());
                                            dbHandler.userDAO().deleteUser(userid);
                                            SessionManager.logoutUser(getActivity());
                                            Intent i = new Intent(getActivity(), LoginActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(i);
                                            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                            getActivity().finish();
                                        }
                                        //noData(true);
                                        // Toasty.warning(Objects.requireNonNull(getActivity()).getBaseContext(), errorMsg, Toast.LENGTH_SHORT, true).show();
                                    }
                                    BarDataSet dataSet = null;
                                    if (k == 0) {
                                        entries.add(new BarEntry(0f, d));
                                        entries.add(new BarEntry(1f, a));
                                        entries.add(new BarEntry(2f, b));
                                        entries.add(new BarEntry(3f, c));
                                        dataSet = new BarDataSet(entries, "");
                                        dataSet.setColors(new int[]{R.color.math_background, R.color.phy_background, R.color.che_background, R.color.bio_background}, Objects.requireNonNull(getActivity()).getBaseContext());

                                    } else if (k == 1) {

                                        entries.add(new BarEntry(0f, d));
                                        entries.add(new BarEntry(1f, a));
                                        entries.add(new BarEntry(2f, b));
                                        dataSet = new BarDataSet(entries, "");
                                        dataSet.setColors(new int[]{R.color.math_background, R.color.phy_background, R.color.che_background}, Objects.requireNonNull(getActivity()).getBaseContext());

                                    } else if (k == 2) {

                                        entries.add(new BarEntry(0f, a));
                                        entries.add(new BarEntry(1f, b));
                                        entries.add(new BarEntry(2f, c));
                                        dataSet = new BarDataSet(entries, "");
                                        dataSet.setColors(new int[]{R.color.phy_background, R.color.che_background, R.color.bio_background}, Objects.requireNonNull(getActivity()).getBaseContext());


                                    }


                                    dataSet.setValueTypeface(tf);

                                    BarData data = new BarData(dataSet);

                                    data.setBarWidth(0.4f);

                                    if (a > 5 || b > 5 || c > 5 || d > 5) {
                                        barChart.getAxisLeft().setLabelCount(5);
                                    } else {
                                        barChart.getAxisLeft().setLabelCount(3);
                                    }
                                    setData(data);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //CommonUtils.showToast(getActivity(),getString(R.string.json_error)+e.getMessage());

                                    //Toasty.warning(Objects.requireNonNull(getActivity()).getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String msg = "";
                        if (getActivity() == null)
                            return;
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
                        // CommonUtils.showToast(getActivity(), msg);

                        //noData(true);
                        //Toasty.warning(Objects.requireNonNull(getContext()), error.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                }) {
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
                CustomDialog.showDialog(getActivity(), true);
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            } else {
                //CommonUtils.showToast(getActivity(),getString(R.string.no_internet));
                //Toasty.info(getActivity().getBaseContext(), "There is no internet.", Toast.LENGTH_SHORT, true).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.lnr_filter)
    public void onViewClicked(View v) {
        showPopup(v);
    }

    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }


        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value) + " Mnts";
        }
    }


}
