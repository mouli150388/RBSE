package com.tutorix.tutorialspoint.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.google.android.material.textfield.TextInputLayout;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.adapters.ClassesAdapter;
import com.tutorix.tutorialspoint.adapters.CountryAdapter;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.Classes;
import com.tutorix.tutorialspoint.models.CountryModel;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class EditPersonalDetailsActivity extends AppCompatActivity {

    EditPersonalDetailsActivity _this;

    EditText etName, etFatherName, etDOB, etMailId, etMobileNumber, etAddress, etCity, etState,etZip,etCountry,etStudyClass;
    TextInputLayout txtInputtudyClass;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    String access_token, userId;
    Pattern emailPattern;
    List<CountryModel>listCountryData;
    List<Classes>listClassesData;
    String[] userInfo;
    CountryAdapter countryAdapter;
    ClassesAdapter classAdapter;
    String country_code="";
    String user_current_class="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = EditPersonalDetailsActivity.this;
        setContentView(R.layout.activity_edit_personal_details);
        listCountryData=new ArrayList<>();
        listClassesData=new ArrayList<>();
        countryAdapter=new CountryAdapter();
        classAdapter=new ClassesAdapter();
        user_current_class=getIntent().getStringExtra("user_current_class");
        initViews();
        getInfoUpdateUI();
        emailPattern= Patterns.EMAIL_ADDRESS;

    }
    private void initViews() {
        etName = findViewById(R.id.etName);
        etFatherName = findViewById(R.id.etFatherName);
        etDOB = findViewById(R.id.etDOB);
        etMailId = findViewById(R.id.etMailId);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        etZip = findViewById(R.id.etZip);
        etCountry = findViewById(R.id.etCountry);
        etStudyClass = findViewById(R.id.etStudyClass);
        txtInputtudyClass = findViewById(R.id.txtInputtudyClass);
        txtInputtudyClass.setVisibility(View.VISIBLE);
    }
    private void getInfoUpdateUI() {
        userInfo = SessionManager.getUserInfo(getApplicationContext());
        access_token = userInfo[1];
        userId = userInfo[0];

        MyDatabase db=MyDatabase.getDatabase(getApplicationContext());
        UserProfile userProfile=db.userDAO().getUserProfile(userId);
        if(userProfile==null)
        {
            return;
        }
        etName.setText(userProfile.full_name);
        etFatherName.setText(userProfile.father_name);
        etDOB.setText(userProfile.date_of_birth);
        etMailId.setText(userProfile.email_id);
        etMobileNumber.setText(userProfile.mobile_number);
        etAddress.setText(userProfile.address);
        etCity.setText(userProfile.city);
        etState.setText(userProfile.state);
        etZip.setText(userProfile.postal_code);
        etCountry.setText(checkCountry(country_code=userProfile.country_code));
        etStudyClass.setText(checkStduyClass(user_current_class));

        if (userProfile.gender!=null&&userProfile.gender.equalsIgnoreCase(Constants.male)) {
            rbMale.setChecked(true);
            rbFemale.setChecked(false);
        } else {
            rbMale.setChecked(false);
            rbFemale.setChecked(true);
        }
    }
    public void openDateDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        String dates=etDOB.getText().toString();



        if(!dates.isEmpty()&&dates.contains("-"))
        {

            try {
                SimpleDateFormat format=new SimpleDateFormat("dd-MMM-yyyy");
                Date d=format.parse(dates);
                Calendar cc=Calendar.getInstance();
                cc.setTime(d);
                 startYear = cc.get(Calendar.YEAR);
                 startMonth = cc.get(Calendar.MONTH);
                 startDay = cc.get(Calendar.DAY_OF_MONTH);


            } catch (ParseException e) {
                e.printStackTrace();
            }
           /* String d[]=dates.split("-");
            startDay=Integer.parseInt(d[0]);
            startMonth=Integer.parseInt(d[1]);
            startYear=Integer.parseInt(d[2]);*/
        }


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                String date = selectedDay + "-" + (getMonthForInt(selectedMonth)) + "-" + selectedYear;

                etDOB.setText(date);
            }
        }, startYear, startMonth, startDay);

        Calendar today_plus_year = Calendar.getInstance();
        today_plus_year.add( Calendar.YEAR, -10 );
        datePickerDialog.getDatePicker().setMaxDate(today_plus_year.getTimeInMillis());
        datePickerDialog.show();
    }
    String getMonthForInt(int num) {
        String month = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
    public void updatePersonalDetails(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        final JSONObject fjson = new JSONObject();
        String name = etName.getText().toString().trim();
        String fatherName = etFatherName.getText().toString().trim();
        String dob = etDOB.getText().toString().trim();
        String mailId = etMailId.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String gender = rbMale.isChecked() ? Constants.male : Constants.female;
        String school_zip = etZip.getText().toString().trim();
        String school_country = etCountry.getText().toString().trim();
        if(!mailId.isEmpty()&&!emailPattern.matcher(mailId).matches())
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter valid Email Id");
            return;
        } if(user_current_class.isEmpty())
        {
            CommonUtils.showToast(getApplicationContext(),"Please select current studying class");
            return;
        }
        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.fullName, name);
            fjson.put(Constants.fatherName, fatherName);
            fjson.put(Constants.dob, dob);
            fjson.put(Constants.emailId, mailId);
            fjson.put(Constants.address, address);
            fjson.put(Constants.city, city);
            fjson.put(Constants.state, state);
            fjson.put(Constants.gender, gender);
            fjson.put(Constants.postal_code, school_zip);
            fjson.put(Constants.country_code, country_code);
            fjson.put(Constants.user_current_class, user_current_class);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String reqRegister = Constants.reqRegister;
        //DialogUtils.showProgressDialog(_this);
        CustomDialog.showDialog(EditPersonalDetailsActivity.this,false);

        StringRequest strReq = new StringRequest(Request.Method.PUT,
               Constants.USERS + "/" + userId,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {

                        CustomDialog.closeDialog();
                        try {

                            //Log.v("Response","Response "+response);
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                CommonUtils.showToast(getApplicationContext(),jObj.getString(Constants.message));
                                //Toast.makeText(getApplicationContext(), jObj.getString(Constants.message), Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_CANCELED, returnIntent);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();
                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(),errorMsg);
                                //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(),getString(R.string.json_error)+e.getMessage());
                           // Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.warning(_this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
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
                CustomDialog.closeDialog();
               // DialogUtils.dismissProgressDialog();
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
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }
    public void goBack(View view) {
        onBackPressed();
        AppController.getInstance().playAudio(R.raw.back_sound);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // AppController.getInstance().playAudio(R.raw.button_click);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
    public void countryselect(View view) {
        if(listCountryData==null||listCountryData.size()<=0)
        {
            readCountry();


        }
        final Dialog dialog = new Dialog(_this, R.style.DialogTheme);
        dialog.setContentView(R.layout.alert_dialog_country);

        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans_back)));

        final ListView list_country=dialog.findViewById(R.id.list_country);
        list_country.setAdapter(countryAdapter);
        list_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                country_code=listCountryData.get(position).code;
                etCountry.setText(listCountryData.get(position).name);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        // dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        AppController.getInstance().playAudio(R.raw.alert_audio);

    }
    public void readCountry()
    {

        try {
            InputStream is=getAssets().open("CountryJson.json");
            StringBuffer s=new StringBuffer();
            byte[]b=new byte[is.available()];
            is.read(b);
            JSONArray array=new JSONArray(new String(b));
            CountryModel countryModel;
            JSONObject obj;
            listCountryData.clear();
            for(int k=0;k<array.length();k++)
            {
                countryModel=new CountryModel();
                obj=array.getJSONObject(k);
                countryModel.name=obj.getString("name");
                countryModel.code=obj.getString("code");
                listCountryData.add(countryModel);
            }
            countryAdapter.setCountryList(listCountryData);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String checkCountry(String code)
    {
        if(listCountryData==null||listCountryData.size()<=0)
        {
            readCountry();


        }
        for(CountryModel c:listCountryData) {

            if (c.code.equals(code))
                return c.name;
        }
        return "";
    }


    /////Classs Info Update
    public void classSelect(View view) {
        if(listClassesData==null||listClassesData.size()<=0)
        {
            readClasses();


        }
        final Dialog dialog = new Dialog(_this, R.style.DialogTheme);
        dialog.setContentView(R.layout.alert_dialog_country);

        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans_back)));

        final ListView list_country=dialog.findViewById(R.id.list_country);
        final TextView txt_header=dialog.findViewById(R.id.txt_header);
        txt_header.setText("Select Studying Class");
        list_country.setAdapter(classAdapter);
        list_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user_current_class=listClassesData.get(position).class_id;
                etStudyClass.setText(listClassesData.get(position).class_name);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        // dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        AppController.getInstance().playAudio(R.raw.alert_audio);

    }
    public void readClasses()
    {

        try {
            InputStream is=getAssets().open("class.json");
            StringBuffer s=new StringBuffer();
            byte[]b=new byte[is.available()];
            is.read(b);
            JSONArray array=new JSONArray(new String(b));
            Classes classesModel;
            JSONObject obj;
            listClassesData.clear();
            for(int k=0;k<array.length();k++)
            {
                classesModel=new Classes();
                obj=array.getJSONObject(k);
                classesModel.class_name=obj.getString("name");
                classesModel.class_id=obj.getString("id");
                listClassesData.add(classesModel);
            }
            classAdapter.setCountryList(listClassesData);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private String checkStduyClass(String code)
    {
        if(listClassesData==null||listClassesData.size()<=0)
        {
            readClasses();


        }
        for(Classes c:listClassesData) {

            if (c.class_id.equals(code))
                return c.class_name;
        }
        return "";
    }


}
