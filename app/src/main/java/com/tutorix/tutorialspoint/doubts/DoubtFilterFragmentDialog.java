package com.tutorix.tutorialspoint.doubts;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.models.DoubtFilterModel;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DoubtFilterFragmentDialog extends DialogFragment {


    @BindView(R.id.img_back_filter)
    ImageView img_back_filter;
    @BindView(R.id.lnr_filters)
    LinearLayout lnrFilters;
    @BindView(R.id.txt_apply)
    TextView txtApply;

    Activity activity;
    DoubtFilterAdapter filterAdapter1;
    DoubtFilterAdapter filterAdapter2;
    DoubtFilterAdapter filterAdapter3;
    DoubtFilterAdapter filterAdapter4;
    public DoubtFilterFragmentDialog() {
        // Required empty public constructor
    }

    public static DoubtFilterFragmentDialog newInstance() {
        return  new DoubtFilterFragmentDialog();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_doubt_filter_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity(),getTheme()){
            @Override
            public void onBackPressed() {

                dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity=getActivity();

        txtApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sub=filterAdapter1.getSelected();
                String cls=filterAdapter2.getSelected();
                String ref=filterAdapter3.getSelected();
                String marks=filterAdapter4.getSelected();
                if(sub.isEmpty()&&cls.isEmpty()&&ref.isEmpty()&&marks.isEmpty())
                {
                    CommonUtils.showToast(activity,"Please select Filter");
                    return;
                }
                ((LatestDoubtsActivity)activity).disMissDialog(sub,cls,ref,marks);
                dismiss();
            }
        });

        img_back_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        try {

            String filter_data= SharedPref.getDoubtFilterData(activity);
            if(filter_data.isEmpty())
            {
                if(CommonUtils.doubt_filter_version>SharedPref.getLastFilterSyncDate(activity))
                {
                    getFilters();
                    return;
                }


            }else
            {
                if(CommonUtils.doubt_filter_version>SharedPref.getLastFilterSyncDate(activity))
                {
                    getFilters();
                    return;
                }
            }
            addFilters(new JSONObject(filter_data)) ;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200) {

        }
    }




    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    StringRequest strReq;
    private void getFilters()  {
        final JSONObject fjson = new JSONObject();

        String[] userInfo = SessionManager.getUserInfo(activity.getApplicationContext());
        String access_token = userInfo[1];
        String userId = userInfo[0];


        try {
            fjson.put(Constants.accessToken, access_token);

            fjson.put(Constants.userId, userId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = "getquestions";
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);

        strReq = new StringRequest(Request.Method.POST, Constants.GET_DOUBT_FILTERS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                        if(activity==null)
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
                                if(activity==null)
                                {
                                    return;
                                }

                                    SharedPref.setDoubtFilterData(activity,jObj.toString());
                                    SharedPref.setFilterSyncDate(activity,CommonUtils.doubt_filter_version);
                                      addFilters(jObj);



                                if(activity==null)
                                {
                                    return;
                                }




                            } else {


                                //finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            //CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(activity==null)
                {
                    return;
                }
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
                CommonUtils.showToast(activity, msg);

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
        if(activity!=null) {
            CustomDialog.showDialog(activity,false);
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        }
    }

    private void addFilters(JSONObject jObj) {
        try{
            JSONObject dataObj;
            DoubtFilterModel model;
            List<DoubtFilterModel> listFilter;
            View view;
            TextView txt_filter_type;
            RecyclerView recycler_filter_values;
            JSONArray subject_array = jObj.getJSONArray("subject_array");

            listFilter=new ArrayList<>();
            for(int k=0;k<subject_array.length();k++)
            {
                model=new DoubtFilterModel();
                dataObj=subject_array.getJSONObject(k);
                model.id=dataObj.getString("id");
                model.name=dataObj.getString("name");
                model.type=1;
                listFilter.add(model);
            }
            view=activity.getLayoutInflater().inflate(R.layout.layout_doubt_filter_main,null);
            txt_filter_type=view.findViewById(R.id.txt_filter_type);
            recycler_filter_values=view.findViewById(R.id.recycler_filter_values);
            filterAdapter1=new DoubtFilterAdapter(listFilter);
            recycler_filter_values.setLayoutManager(new GridLayoutManager(activity,2));
            recycler_filter_values.setAdapter(filterAdapter1);
            txt_filter_type.setText("Subjects");
            lnrFilters.addView(view);


            JSONArray classes_array = jObj.getJSONArray("classes_array");
            listFilter=new ArrayList<>();
            for(int k=0;k<classes_array.length();k++)
            {
                model=new DoubtFilterModel();
                dataObj=classes_array.getJSONObject(k);
                model.id=dataObj.getString("id");
                model.name=dataObj.getString("name");
                model.type=2;
                listFilter.add(model);
            }
            view=activity.getLayoutInflater().inflate(R.layout.layout_doubt_filter_main,null);
            txt_filter_type=view.findViewById(R.id.txt_filter_type);
            recycler_filter_values=view.findViewById(R.id.recycler_filter_values);
            filterAdapter2=new DoubtFilterAdapter(listFilter);
            recycler_filter_values.setLayoutManager(new GridLayoutManager(activity,2));
            recycler_filter_values.setAdapter(filterAdapter2);
            txt_filter_type.setText("Classes");
            lnrFilters.addView(view);

            JSONArray references_array = jObj.getJSONArray("references_array");
            listFilter=new ArrayList<>();
            for(int k=0;k<references_array.length();k++)
            {
                model=new DoubtFilterModel();
                dataObj=references_array.getJSONObject(k);
                model.id=dataObj.getString("id");
                model.name=dataObj.getString("name");
                model.type=3;
                listFilter.add(model);
            }

            view=activity.getLayoutInflater().inflate(R.layout.layout_doubt_filter_main,null);
            txt_filter_type=view.findViewById(R.id.txt_filter_type);
            recycler_filter_values=view.findViewById(R.id.recycler_filter_values);
            filterAdapter3=new DoubtFilterAdapter(listFilter);
            recycler_filter_values.setLayoutManager(new GridLayoutManager(activity,2));
            recycler_filter_values.setAdapter(filterAdapter3);
            txt_filter_type.setText("References");
            lnrFilters.addView(view);

            JSONArray marks_array = jObj.getJSONArray("marks_array");
            listFilter=new ArrayList<>();
            for(int k=0;k<marks_array.length();k++)
            {
                model=new DoubtFilterModel();
                dataObj=marks_array.getJSONObject(k);
                model.id=dataObj.getString("id");
                model.name=dataObj.getString("name");
                model.type=4;
                listFilter.add(model);
            }

            view=activity.getLayoutInflater().inflate(R.layout.layout_doubt_filter_main,null);
            txt_filter_type=view.findViewById(R.id.txt_filter_type);
            recycler_filter_values=view.findViewById(R.id.recycler_filter_values);
            filterAdapter4=new DoubtFilterAdapter(listFilter);
            recycler_filter_values.setLayoutManager(new GridLayoutManager(activity,2));
            recycler_filter_values.setAdapter(filterAdapter4);
            txt_filter_type.setText(activity.getString(R.string.marks));
            lnrFilters.addView(view);



        }catch (JSONException ex)
        {
            ex.printStackTrace();
        }
    }

}
