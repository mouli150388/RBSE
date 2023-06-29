package com.tutorix.tutorialspoint.activities;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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
import com.tutorix.tutorialspoint.adapters.CountryAdapter;
import com.tutorix.tutorialspoint.database.MyDatabase;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class EditSchoolDetailsActivity extends AppCompatActivity {
    EditSchoolDetailsActivity _this;
    TextView profileName;
    EditText etSchoolName, etRollNumber, etSectionName,etAddress,etCity,etState,etZip,etCountry;

    String access_token, userId, classId,school_address,school_city;

    CountryAdapter countryAdapter;
    List<CountryModel>listCountryData;
    String country_code="";
    UserProfile profile;
    String[]userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = EditSchoolDetailsActivity.this;
        setContentView(R.layout.activity_edit_school_details);
        countryAdapter=new CountryAdapter();
        listCountryData=new ArrayList<>();
        countryAdapter=new CountryAdapter();

        MyDatabase db=MyDatabase.getDatabase(getApplicationContext());
        userInfo= SessionManager.getUserInfo(getApplicationContext());
        userId=userInfo[0];
        access_token=userInfo[1];
        classId=userInfo[4];
        profile=db.userDAO().getUserProfile(userId);

        initViews();
        getInfoUpdateUI();
      //  readCountry();
    }

    private void initViews() {
        profileName = findViewById(R.id.profileName);
        etSchoolName = findViewById(R.id.etSchoolName);
        etRollNumber = findViewById(R.id.etRollNumber);
        etSectionName = findViewById(R.id.etSectionName);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etZip = findViewById(R.id.etZip);
        etCountry = findViewById(R.id.etCountry);
    }

    private void getInfoUpdateUI() {


        //profileName.setText(profile.user_name);
        etSchoolName.setText(profile.school_name);
        etRollNumber.setText(profile.roll_number);
        etSectionName.setText(profile.section_name);
        etAddress.setText(profile.school_address);
        etCity.setText(profile.school_city);
        etState.setText(profile.school_state);
        etZip.setText(profile.school_postal_code);
        etCountry.setText(checkCountry(country_code=profile.school_country_code));

    }

    public void updatePersonalDetails(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        final JSONObject fjson = new JSONObject();

        String schoolName = etSchoolName.getText().toString().trim();
        String rollNumber = etRollNumber.getText().toString().trim();
        String sectionName = etSectionName.getText().toString().trim();
        String school_address = etAddress.getText().toString().trim();
        String school_city = etCity.getText().toString().trim();
        String school_state = etState.getText().toString().trim();
        String school_zip = etZip.getText().toString().trim();
        String school_country = etCountry.getText().toString().trim();

        if(schoolName.isEmpty())
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter School Name");
            return;
        }

        /*if(rollNumber.isEmpty())
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter Role Number");
            return;
        }*/
        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.schoolName, schoolName);
            fjson.put(Constants.rollNumber, rollNumber);
            fjson.put(Constants.sectionName, sectionName);
            fjson.put(Constants.school_address, school_address);
            fjson.put(Constants.school_city, school_city);
            fjson.put(Constants.school_state, school_state);
            fjson.put(Constants.school_zip, school_zip);
            fjson.put(Constants.school_country, country_code);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String reqRegister = Constants.reqRegister;
        //DialogUtils.showProgressDialog(_this);
        CustomDialog.showDialog(EditSchoolDetailsActivity.this,false);
        StringRequest strReq = new StringRequest(Request.Method.PUT,
                 Constants.USER_SCHOOL + "/" + userId,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        CustomDialog.closeDialog();
                       // DialogUtils.dismissProgressDialog();
                        try {

                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {
                                String errorMsg = jObj.getString(Constants.message);
                                //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                                CommonUtils.showToast(getApplicationContext(),errorMsg);
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
                            //Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
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
                CustomDialog.closeDialog();
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


    public void cancelPersonalDetails(View view) {
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void goBack(View view) {
        AppController.getInstance().playAudio(R.raw.back_sound);
        onBackPressed();
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
}
