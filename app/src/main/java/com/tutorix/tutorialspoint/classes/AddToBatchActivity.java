package com.tutorix.tutorialspoint.classes;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.tutorix.tutorialspoint.classes.model.StudentBatch;
import com.tutorix.tutorialspoint.classes.model.StudentBranch;
import com.tutorix.tutorialspoint.classes.model.StudentCity;
import com.tutorix.tutorialspoint.classes.model.StudentClass;
import com.tutorix.tutorialspoint.classes.model.StudentDetails;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddToBatchActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_class)
    Spinner spinner_class;
    @BindView(R.id.spinner_city)
    Spinner spinner_city;
    @BindView(R.id.spinner_batch)
    Spinner spinner_batch;
    @BindView(R.id.spinner_branch)
    Spinner spinner_branch;
    @BindView(R.id.txt_continue)
    TextView txt_continue;

    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_phone)
    EditText inputPhoneNumber;
    @BindView(R.id.input_email)
    EditText input_email;

    ClassesAdapter classesAdapter;
    ClassesAdapter cityAdapter;
    ClassesAdapter batchAdapter;
    ClassesAdapter branchAdapter;
    ArrayList<Object>classList;
    ArrayList<Object>cityList;
    ArrayList<Object> branchList;
    ArrayList<Object>batchList;
    ArrayList<Object>classbatchList;

    String selectedClass="";
    String selectedCity="";
    String selectedBranch="";
    String selectedBatch="";
    String access_token="";
    String loginType="";
    String userId="";
    String mobile_number="";
    String full_name="";
    String email_id="";
    AddToBatchActivity _this;
    Pattern EmailPattern;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_batch);
        ButterKnife.bind(this);
        _this=this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        EmailPattern= Patterns.EMAIL_ADDRESS;
        String[] userinfo = SessionManager.getUserInfo(_this);
        access_token = userinfo[1];
        loginType = userinfo[2];
        //class_id = userinfo[4];
        userId = userinfo[0];
        classesAdapter=new ClassesAdapter();
        cityAdapter=new ClassesAdapter();
        batchAdapter=new ClassesAdapter();
        branchAdapter=new ClassesAdapter();

        classList=new ArrayList<>();
        cityList=new ArrayList<>();
        branchList=new ArrayList<>();
        batchList=new ArrayList<>();

        loadClassesList();

        StudentCity stdCity=new StudentCity();
        stdCity.title="Select City";

        cityList.add(stdCity);

        StudentBranch stdBrach=new StudentBranch();
        stdBrach.title="Select Branch";
        branchList.add(stdBrach);

        StudentBatch stdBatch=new StudentBatch();
        stdBatch.title="Select Batch";
        batchList.add(stdBatch);

        spinner_class.setAdapter(classesAdapter);
        spinner_city.setAdapter(cityAdapter);
        spinner_branch.setAdapter(branchAdapter);
        spinner_batch.setAdapter(batchAdapter);

        classesAdapter.setList(classList);
        cityAdapter.setList(cityList);
        branchAdapter.setList(branchList);
        batchAdapter.setList(batchList);



        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0)
                {
                    selectedCity=((StudentCity)adapterView.getAdapter().getItem(i)).title;
                }else
                {
                    selectedCity="";
                }

                selectedBatch="";
                selectedBranch="";
                selectedBatch="";
                selectedClass="";
                branchList.clear();
                batchAdapter.list.clear();
                batchAdapter.notifyDataSetChanged();
                batchList.clear();
                branchList.addAll(((StudentCity)adapterView.getAdapter().getItem(i)).branchesList);

                StudentBatch stdBatch=new StudentBatch();
                stdBatch.title="Select Batch";
                batchList.add(stdBatch);
                if(branchList.size()==0)
                {
                    StudentBranch stdBrach=new StudentBranch();
                    stdBrach.title="Select Branch";
                    branchList.add(stdBrach);
                }
                branchAdapter.setList(branchList);
                branchAdapter.notifyDataSetChanged();

                batchAdapter.setList(batchList);
                batchAdapter.notifyDataSetChanged();

                spinner_branch.setSelection(0);
                spinner_batch.setSelection(0);
                classbatchList.clear();
                spinner_class.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0)
                {
                    selectedBranch=((StudentBranch)adapterView.getAdapter().getItem(i)).id;
                }else
                {
                    selectedBranch="";
                }
                selectedBatch="";
                selectedBatch="";
                selectedClass="";

                batchList.clear();
                classbatchList.clear();
                batchAdapter.list.clear();
                classbatchList.addAll(((StudentBranch)adapterView.getAdapter().getItem(i)).batchesList);
                if(batchList.size()==0)
                {
                    StudentBatch stdBatch=new StudentBatch();
                    stdBatch.title="Select Batch";
                    batchList.add(stdBatch);
                }
                batchAdapter.setList(batchList);
                batchAdapter.notifyDataSetChanged();
                spinner_batch.setSelection(0);
                spinner_class.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i>=0)
                {
                    selectedClass=((StudentClass)adapterView.getAdapter().getItem(i)).id;
                }else
                {
                    selectedClass="";
                }
                batchList.clear();

                batchAdapter.list.clear();
                // classbatchList.addAll(((StudentBranch)adapterView.getAdapter().getItem(i)).batchesList);
                if(batchList.size()==0)
                {
                    StudentBatch stdBatch=new StudentBatch();
                    stdBatch.title="Select Batch";
                    batchList.add(stdBatch);
                }

                for(Object btach:classbatchList)
                {
                    if(selectedClass.equalsIgnoreCase(((StudentBatch)btach).class_id))
                    {
                        StudentBatch stdBatch=new StudentBatch();
                        stdBatch.title=((StudentBatch)btach).title;
                        stdBatch.id=((StudentBatch)btach).id;
                        batchList.add(stdBatch);
                    }

                }
                batchAdapter.setList(batchList);
                batchAdapter.notifyDataSetChanged();
                spinner_batch.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0)
                {
                    selectedBatch=((StudentBatch)adapterView.getAdapter().getItem(i)).id;
                }else
                {
                    selectedBatch="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getBatchList();
        txt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_id=input_email.getText().toString();
                mobile_number=inputPhoneNumber.getText().toString();
                full_name=inputName.getText().toString();
                if(full_name.isEmpty())
                {
                    CommonUtils.showToast(getApplicationContext(),"Please Full Name");
                    return;
                }
                if(mobile_number.isEmpty()||mobile_number.length()<10)
                {
                    CommonUtils.showToast(getApplicationContext(),"Please Enter Mobile Number");
                    return;
                }
                if(email_id.isEmpty())
                {
                    CommonUtils.showToast(getApplicationContext(),"Please Valid Name");
                    return;
                }
                if(!EmailPattern.matcher(email_id).find())
                {
                    CommonUtils.showToast(getApplicationContext(),"Please enter valid Email id");
                    return;
                }
                if(selectedCity.isEmpty())
                {
                    CommonUtils.showToast(getApplicationContext(),"Please Select City");
                    return;
                }

                if(selectedBranch.isEmpty())
                {
                    CommonUtils.showToast(getApplicationContext(),"Please Select Branch");
                    return;
                }

                if(selectedBatch.isEmpty())
                {
                    CommonUtils.showToast(getApplicationContext(),"Please Select Batch");
                    return;
                }
                addStudent();

            }
        });
    }
    private void addStudent()  {




        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.classId, selectedClass);
            fjson.put(Constants.mobileNumber, mobile_number);
            fjson.put(Constants.fullName, full_name);
            fjson.put(Constants.emailId, email_id);
            fjson.put("city", selectedCity);
            fjson.put("branch_id", selectedBranch);
            fjson.put("batch_id", selectedBatch);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String tag_string_req = Constants.reqRegister;

        Log.v("Constants.",Constants.SET_STUDENT_BATCH);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.SET_STUDENT_BATCH, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //Log.d(Constants.response, response);

                CustomDialog.closeDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean(Constants.errorFlag);
                    StudentDetails studentDetails;
                    if (!error) {
                        String errorMsg = jObj.getString(Constants.message);
                        CommonUtils.showToast(getApplicationContext(),errorMsg);
                  finish();
                    } else {
                        String errorMsg = jObj.getString(Constants.message);
                        CommonUtils.showToast(getApplicationContext(),errorMsg);
                        //nodata();
                        // Toasty.warning(BookmarkActivity.this, errorMsg, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //CommonUtils.showToast(getActivity(),e.getMessage());
                    //nodata();
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
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }
        };
        CustomDialog.showDialog(_this,true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void getBatchList()  {




        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.classId, "");
            fjson.put("city", "");
            fjson.put("branch_id", "");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        String tag_string_req = Constants.reqRegister;

        Log.v("Constants.",Constants.GET_BATCH_FILTERS);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.GET_BATCH_FILTERS, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //Log.d(Constants.response, response);

                CustomDialog.closeDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean(Constants.errorFlag);
                    StudentDetails studentDetails;
                    if (!error) {
                        JSONObject dataObj=jObj.getJSONObject("data");
                        Iterator itCities=dataObj.keys();

                        cityList.clear();
                        StudentCity stdCity=new StudentCity();
                        stdCity.title="Select City";

                        cityList.add(stdCity);
                        while(itCities.hasNext())
                        {
                            String city=itCities.next().toString();
                            JSONObject branchObj=dataObj.getJSONObject(city).getJSONObject("branches");


                            stdCity=new StudentCity();
                            stdCity.title=city;
                            stdCity.branchesList=new ArrayList<Object>();
                            StudentBranch stdBrach=new StudentBranch();
                            stdBrach.title="Select Branch";
                            stdCity.branchesList.add(stdBrach);
                            Iterator itBranches=branchObj.keys();
                            while(itBranches.hasNext())
                            {
                                String branchId=itBranches.next().toString();
                                String branch_name=branchObj.getJSONObject(branchId).getString("branch_name");
                                JSONObject batchJson=branchObj.getJSONObject(branchId).getJSONObject("batches");

                                stdBrach=new StudentBranch();
                                stdBrach.title=branch_name;
                                stdBrach.id=branchId;
                                stdBrach.batchesList=new ArrayList<>();

                                StudentBatch stdBatch=new StudentBatch();
                                stdBatch.title="Select Batch";
                                stdBrach.batchesList.add(stdBatch);



                                Iterator itBatches=batchJson.keys();
                                while(itBatches.hasNext())
                                {
                                    String batchId=itBatches.next().toString();
                                    String batch_name=batchJson.getJSONObject(batchId).getString("batch_name");
                                    stdBatch=new StudentBatch();
                                    stdBatch.title=batch_name;
                                    stdBatch.id=batchId;
                                    stdBatch.class_id=batchJson.getJSONObject(batchId).getString("class_id");
                                    stdBrach.batchesList.add(stdBatch);
                                }

                                stdCity.branchesList.add(stdBrach);
                            }


                            cityList.add(stdCity);
                        }

                        cityAdapter.setList(cityList);
                        branchAdapter.setList(branchList);
                        batchAdapter.setList(batchList);

                    } else {
                        String errorMsg = jObj.getString(Constants.message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

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
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }
        };
        CustomDialog.showDialog(_this,true);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void goBack(View view) {
        AppController.getInstance().playAudio(R.raw.back_sound);
        onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadClassesList() {
        classbatchList=new ArrayList<>();
        StudentClass stdCls=new StudentClass();
        stdCls.title="Select Class";
        classList.add(stdCls);


        for(int k=1;k<=9;k++)
        {
            stdCls=new StudentClass();
            stdCls.id=k+"";
            stdCls.title=AppConfig.getClassNameDisplay(stdCls.id);
            classList.add(stdCls);
        }

    }
}