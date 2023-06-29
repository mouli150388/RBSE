package com.tutorix.tutorialspoint.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.SpannableString;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.tutorix.tutorialspoint.adapters.CountryAdapter;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.CountryModel;
import com.tutorix.tutorialspoint.models.PriceModel;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.views.countrycode.Country;
import com.tutorix.tutorialspoint.views.countrycode.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

public class SubscribeOnlineActivity extends AppCompatActivity {
    RadioGroup rgGender;
    EditText etName,  etMailId, etMobileNumber, etAddress, etCity, etState,etZip,etCountry;
    EditText etName_shp,  etMailId_shp, etMobileNumber_shp, etAddress_shp, etCity_shp, etState_shp,etZip_shp,etCountry_shp;
    LinearLayout lnr_not_required;
    LinearLayout lnr_bottom_btns;
    LinearLayout subscribeLayout;
    LinearLayout subscribeLayout2;

    // ActivationBroadCastReceiver receiver;
    Pattern emailPattern;
    String country_code="";
    String country_code_shp="";
    CountryAdapter countryAdapter;

    String []userInfo;
    String userId,access_token,classId,device_id;
    String product_type;
    List<CountryModel> listCountryData;
    EditText etCouponCode;
    TextView tvCouponError, tvActualAmount, tvDiscountAmount, tvCGSTAmount, tvSGSTAmount, tvGrandTotal,txt_amount_deatils;
    RelativeLayout enterCouponCodeLayout, displayCouponCodeLayout;
    RadioButton rbINR, rbUSD;
    Button btn_continue;
    LinearLayout lnr_coupon_main;

    String actualPrice, discountPrice;
    CheckBox checkbox;
    //TextView txt_price_o;
    //TextView txt_price_s;
    //TextView txt_price_t;
    TextView txt_price_o_a;
    //TextView txt_price_s_a;
    // TextView txt_price_t_a;
    View subscribeLayout3;

    //RadioGroup rgp_succribe_typ;
    String activation_type="";
    View lnr_online;
    View lnr_sdcard;
    View lnr_tab;
    RadioButton rb_online;
    RadioButton rb_sdcard;
    RadioButton rb_tab;
    CountryCodePicker ccp_ship;
    String bill_mbl_countryCode;
    String shp_mbl_countryCode;
    int MAX_LENGTH;
    UserProfile userProfile;
    AppCompatSpinner spinner_years;

    List<PriceModel>listPrice;
    int selectedYear=1;

    String moneySymbol;
    MyYearAdapter adapter;
    String order_years="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_online);
        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userId = userInfo[0];
        device_id = userInfo[3];
        classId = userInfo[4];

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // String flag = extras.getString("flag");
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Buy Activation");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_gradient_home));
        initUI();
        lnr_coupon_main=findViewById(R.id.lnr_coupon_main);
        lnr_online=findViewById(R.id.lnr_online);
        lnr_sdcard=findViewById(R.id.lnr_sdcard);
        lnr_tab=findViewById(R.id.lnr_tab);
        //txt_price_o=findViewById(R.id.txt_price_o);
        //txt_price_s=findViewById(R.id.txt_price_s);
        //txt_price_t=findViewById(R.id.txt_price_t);

        txt_price_o_a=findViewById(R.id.txt_price_o_a);
        //txt_price_s_a=findViewById(R.id.txt_price_s_a);
        //txt_price_t_a=findViewById(R.id.txt_price_t_a);

        spinner_years=findViewById(R.id.spinner_years);

        activation_type="O";
        Paint p = new Paint();
        p.setColor(ContextCompat.getColor(this,R.color.red));
        p.setFlags(Paint.UNDERLINE_TEXT_FLAG);

        listPrice=new ArrayList<>();


        spinner_years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                updatePrice(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter=new MyYearAdapter(getApplicationContext());
        spinner_years.setAdapter(adapter);



        rb_online=findViewById(R.id.rb_online);
        rb_sdcard=findViewById(R.id.rb_sdcard);
        rb_tab=findViewById(R.id.rb_tab);
        rb_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.button_click);

                rb_online.setChecked(true);
                rb_sdcard.setChecked(false);
                rb_tab.setChecked(false);
            }
        });


        rb_online.setChecked(true);
        lnr_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activation_type="O";
                AppController.getInstance().playAudio(R.raw.button_click);
                rb_online.setChecked(true);
                rb_sdcard.setChecked(false);
                rb_tab.setChecked(false);
            }
        });

        try {

            getYearSubscription();
            //getAmountToBePad();
            // getAllPrice();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



    private void initUI()
    {
        initLayout2Views();
        subscribeLayout = findViewById(R.id.subscribeLayout);
        subscribeLayout2 = findViewById(R.id.subscribeLayout2);
        subscribeLayout3 = findViewById(R.id.subscribeLayout3);
        lnr_not_required=findViewById(R.id.lnr_not_required);
        lnr_bottom_btns=findViewById(R.id.lnr_bottom_btns);
        btn_continue=findViewById(R.id.btn_continue);
        rgGender=findViewById(R.id.rgGender);
        checkbox=findViewById(R.id.checkbox);



        lnr_not_required.setVisibility(View.GONE);
        lnr_bottom_btns.setVisibility(View.GONE);
        rgGender.setVisibility(View.GONE);


        etName = findViewById(R.id.etName);
        etMailId = findViewById(R.id.etMailId);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etZip = findViewById(R.id.etZip);
        etCountry = findViewById(R.id.etCountry);

        etName_shp = findViewById(R.id.etName_shp);
        etMailId_shp = findViewById(R.id.etMailId_shp);
        etMobileNumber_shp = findViewById(R.id.etMobileNumber_shp);
        etAddress_shp = findViewById(R.id.etAddress_shp);
        etCity_shp = findViewById(R.id.etCity_shp);
        etState_shp = findViewById(R.id.etState_shp);
        etZip_shp = findViewById(R.id.etZip_shp);
        etCountry_shp = findViewById(R.id.etCountry_shp);
        ccp_ship = findViewById(R.id.ccp_ship);

        emailPattern= Patterns.EMAIL_ADDRESS;

        listCountryData=new ArrayList<>();
        countryAdapter=new CountryAdapter();

        userInfo= SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userId = userInfo[0];
        classId = userInfo[4];

        MyDatabase db=MyDatabase.getDatabase(getApplicationContext());
        userProfile=db.userDAO().getUserProfile(userId);
        if(userProfile==null)
        {
            return;
        }
        if(userProfile.country_code==null)
            userProfile.country_code="";
        bill_mbl_countryCode =(userProfile.mobile_country_code);
        etName.setText(userProfile.full_name);
        etMailId.setText(userProfile.email_id);
        etMobileNumber.setText(bill_mbl_countryCode +" "+userProfile.mobile_number);
        etAddress.setText(userProfile.address);
        etCity.setText(userProfile.city);
        etState.setText(userProfile.state);
        etZip.setText(userProfile.postal_code);
        etCountry.setText(checkCountry(country_code=userProfile.country_code));


        etName_shp.setText(userProfile.full_name);
        etMailId_shp.setText(userProfile.email_id);
        etMobileNumber_shp.setText(userProfile.mobile_number);
        etAddress_shp.setText(userProfile.address);
        etCity_shp.setText(userProfile.city);
        etState_shp.setText(userProfile.state);
        etZip_shp.setText(userProfile.postal_code);
        etCountry_shp.setText(checkCountry(country_code_shp=userProfile.country_code));
        shp_mbl_countryCode = bill_mbl_countryCode;

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppController.getInstance().playAudio(R.raw.button_click);
                if(isChecked) {
                    subscribeLayout3.setVisibility(View.GONE);
                    etName_shp.setText(userProfile.full_name);
                    etMailId_shp.setText(userProfile.email_id);
                    etMobileNumber_shp.setText(userProfile.mobile_number);
                    etAddress_shp.setText(userProfile.address);
                    etCity_shp.setText(userProfile.city);
                    etState_shp.setText(userProfile.state);
                    etZip_shp.setText(userProfile.postal_code);
                    etCountry_shp.setText(checkCountry(country_code_shp=userProfile.country_code));
                }
                else {
                    subscribeLayout3.setVisibility(View.VISIBLE);

                    etName_shp.setText("");
                    etMailId_shp.setText("");
                    etMobileNumber_shp.setText("");
                    etAddress_shp.setText("");
                    etCity_shp.setText("");
                    etState_shp.setText("");
                    etZip_shp.setText("");
                    etCountry_shp.setText("");
                    country_code_shp="";
                }
            }
        });



        ccp_ship.setCountryForNameCode(shp_mbl_countryCode);
        initFilter();

        ccp_ship.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                shp_mbl_countryCode = country.getPhoneCode();
                etMobileNumber_shp.setText("");
                initFilter();
            }
        });
    }
    private void initFilter()
    {

        if(shp_mbl_countryCode.contains("91"))
        {
            MAX_LENGTH=10;

        }else
        {
            MAX_LENGTH=15;
        }

        etMobileNumber_shp.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
    }
    private void initLayout2Views() {
        etCouponCode = findViewById(R.id.etCouponCode);
        enterCouponCodeLayout = findViewById(R.id.enterCouponCodeLayout);
        displayCouponCodeLayout = findViewById(R.id.displayCouponCodeLayout);
        tvCouponError = findViewById(R.id.tvCouponError);
        tvDiscountAmount = findViewById(R.id.tvDiscountAmount);
        tvActualAmount = findViewById(R.id.tvActualAmount);
        txt_amount_deatils = findViewById(R.id.txt_amount_deatils);
        tvCGSTAmount = findViewById(R.id.tvCGSTAmount);
        tvSGSTAmount = findViewById(R.id.tvSGSTAmount);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        rbUSD = findViewById(R.id.rbPayInDollars);
        rbINR = findViewById(R.id.rbPayInRupees);
        RadioGroup rgPaymentMode = findViewById(R.id.rgPaymentMode);
        tvActualAmount.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        txt_amount_deatils.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        rgPaymentMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                AppController.getInstance().playAudio(R.raw.button_click);
                changeInValues(checkedId);
            }
        });


    }


    private void changeInValues(int checkedId) {

        int k=spinner_years.getSelectedItemPosition();
        if(listPrice==null||k<0||k>=listPrice.size())
        {
            finish();
            return;
        }
        PriceModel priceModel=listPrice.get(spinner_years.getSelectedItemPosition());
        //DecimalFormat df = new DecimalFormat(".##");
        discountPrice="0";
        if (checkedId == R.id.rbPayInDollars) {
            currency_type = Constants.USD;
            int price=Integer.parseInt(priceModel.priceDoller);
            if(couponType!=null&&couponType.equals("P"))
            {

                discountPrice = ((price*(((float)couponUSDValue/100))))+"";
            }else  if(couponType!=null&&couponType.equals("F"))
            {
                discountPrice = (couponUSDValue)+"";
            }

            actualPrice =Integer.parseInt(usdPrice)+"";
        } else {
            currency_type = Constants.INR;
            int price=Integer.parseInt(priceModel.priceRupee);
            if(couponType!=null&&couponType.equals("P"))
            {

                discountPrice = ((price*(((float)couponINRValue/100))))+"";
            }else  if(couponType!=null&&couponType.equals("F"))
            {
                discountPrice = (couponINRValue)+"";
            }
            actualPrice = Integer.parseInt(inrPrice)+"";
        }

        grandTotal = (Float.parseFloat(actualPrice) - Float.parseFloat(discountPrice))+"";

        String moneySymbol;
        if (currency_type.equalsIgnoreCase(Constants.USD)) {
            moneySymbol = getString(R.string.dollar_symbol);
        } else {
            moneySymbol = getString(R.string.rupee_symbol);
        }

        tvActualAmount.setText(moneySymbol + actualPrice);
        tvDiscountAmount.setText(moneySymbol + discountPrice);
        //tvDiscountAmount.setVisibility(View.INVISIBLE);
        tvCGSTAmount.setText(moneySymbol + 0);
        tvSGSTAmount.setText(moneySymbol + 0);
        tvGrandTotal.setText(moneySymbol + grandTotal);
    }
    String currency_type = Constants.INR;
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
    public void countryselect(View v) {
        if(listCountryData==null||listCountryData.size()<=0)
        {
            readCountry();
        }
        final Dialog dialog = new Dialog(SubscribeOnlineActivity.this, R.style.DialogTheme);
        dialog.setContentView(R.layout.alert_dialog_country);

        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans_back)));

        final ListView list_country=dialog.findViewById(R.id.list_country);
        list_country.setAdapter(countryAdapter);
        list_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(listCountryData.size()>position)
                {
                    if(checkbox.isChecked()) {
                        country_code = listCountryData.get(position).code;
                        country_code_shp = listCountryData.get(position).code;
                        etCountry.setText(listCountryData.get(position).name);
                        etCountry_shp.setText(listCountryData.get(position).name);
                    }else{
                        if(v.getId()==R.id.etCountry)
                        {
                            country_code = listCountryData.get(position).code;
                            etCountry.setText(listCountryData.get(position).name);
                        }else if(v.getId()==R.id.etCountry_shp)
                        {
                            country_code_shp = listCountryData.get(position).code;
                            etCountry_shp.setText(listCountryData.get(position).name);
                        }

                    }
                }

                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        // dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        AppController.getInstance().playAudio(R.raw.alert_audio);

    }




    String inrPrice = "0";
    String usdPrice = "0";




    float couponINRValue = 0;
    float couponUSDValue = 0;
    String couponType;
    String couponCode;
    String grandTotal="";

    TextView tvCouponCode;

    public void applyCoupon(View view) {



        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));

            return;
        }

        CommonUtils.hideSoftKeyboard(SubscribeOnlineActivity.this);
        EditText etCouponCode = findViewById(R.id.etCouponCode);
        tvCouponCode = findViewById(R.id.tvCouponCode);
        couponCode = etCouponCode.getText().toString().trim();
        if (!couponCode.isEmpty()) {
            final JSONObject fjson = new JSONObject();

            try {
                fjson.put(Constants.couponCode, couponCode);
                fjson.put(Constants.accessToken, access_token);
                fjson.put(Constants.userId, userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tag_string_req = Constants.reqRegister;
            CustomDialog.showDialog(SubscribeOnlineActivity.this, false);
            StringRequest strReq = new StringRequest(
                    Request.Method.POST,
                    Constants.PAYMENT_COUPON,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            //Log.d(Constants.response, response);
                            CustomDialog.closeDialog();
                            try {
                                JSONObject jObj = new JSONObject(response);

                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {
                                    couponType = jObj.getString("coupon_type");
                                    couponINRValue = jObj.getInt("coupon_inr_value");
                                    couponUSDValue = jObj.getInt("coupon_usd_value");


                                    /*P,F*/
                                    if (currency_type.equalsIgnoreCase(Constants.INR)) {
                                        changeInValues(R.id.rbPayInRupees);
                                    } else {
                                        changeInValues(R.id.rbPayInDollars);
                                    }
                                    enterCouponCodeLayout.setVisibility(View.GONE);
                                    displayCouponCodeLayout.setVisibility(View.VISIBLE);
                                    tvCouponError.setVisibility(View.GONE);
                                    tvCouponCode.setText(couponCode);
                                    CommonUtils.showToast(getApplicationContext(), jObj.getString(Constants.message));
                                } else {
                                    CommonUtils.showToast(getApplicationContext(), jObj.getString(Constants.message));
                                    tvCouponError.setVisibility(View.VISIBLE);
                                    tvCouponError.setText(jObj.getString(Constants.message));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomDialog.closeDialog();
                    CommonUtils.showToast(getApplicationContext(), error.getMessage());
                }
            }) {
                final String message = fjson.toString();
                final String encryption = Security.encrypt(message, AppConfig.ENC_KEY);

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfig.JSON_DATA, encryption);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    public void removeCoupon(View view) {
        couponINRValue = 0;
        couponUSDValue = 0;
        couponType = "";
        etCouponCode.setText("");
        tvCouponError.setText("");
        if (currency_type.equalsIgnoreCase(Constants.INR))
            changeInValues(R.id.rbPayInRupees);
        else
            changeInValues(R.id.rbPayInDollars);

        enterCouponCodeLayout.setVisibility(View.VISIBLE);
        displayCouponCodeLayout.setVisibility(View.GONE);
    }

    private final int CHROME_CUSTOM_TAB_REQUEST_CODE = 100;


    public void payAmount(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
            return;
        }
        final JSONObject fjson = new JSONObject();
        EditText etCouponCode = findViewById(R.id.etCouponCode);
        String couponCode = etCouponCode.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String mailId = etMailId.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String zip = etZip.getText().toString().trim();

        String name_shp = etName_shp.getText().toString().trim();
        String mobile_shp = etMobileNumber_shp.getText().toString().trim();
        String mailId_shp = etMailId_shp.getText().toString().trim();
        String address_shp = etAddress_shp.getText().toString().trim();
        String city_shp = etCity_shp.getText().toString().trim();
        String state_shp = etState_shp.getText().toString().trim();
        String zip_shp = etZip_shp.getText().toString().trim();

        String school_zip = etZip.getText().toString().trim();
        if(name.length()<=0)
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter Name");
            return;
        }
        if(mailId.isEmpty()||!emailPattern.matcher(mailId).matches())
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter valid Email Id");
            return;
        }
        if(address.length()<=0)
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter Address");
            return;
        }
        if(city.length()<=0)
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter city name");
            return;
        }
        if(state.length()<=0)
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter state name");
            return;
        }
        if(zip.length()<=0)
        {
            CommonUtils.showToast(getApplicationContext(),"Please enter postal code");
            return;
        }
        if(country_code.length()<=0)
        {
            CommonUtils.showToast(getApplicationContext(),"Please select country");
            return;
        }


        if(!activation_type.equalsIgnoreCase("O")&&!checkbox.isChecked())
        {
            if(name_shp.length()<=0)
            {
                CommonUtils.showToast(getApplicationContext(),"Please enter all Shipping details");
                return;
            }
            if(mailId_shp.isEmpty()||!emailPattern.matcher(mailId_shp).matches())
            {
                CommonUtils.showToast(getApplicationContext(),"Please enter all valid email address");
                return;
            }
            if(mobile_shp.length()<10)
            {
                CommonUtils.showToast(getApplicationContext(),"Please enter valid shipping mobile number ");
                return;
            }
            if(address_shp.length()<=0)
            {
                CommonUtils.showToast(getApplicationContext(),"Please enter all Shipping address");
                return;
            }
            if(city_shp.length()<=0)
            {
                CommonUtils.showToast(getApplicationContext(),"Please enter all Shipping details");
                return;
            }
            if(state_shp.length()<=0)
            {
                CommonUtils.showToast(getApplicationContext(),"Please enter all Shipping details");
                return;
            }
            if(zip_shp.length()<=0)
            {
                CommonUtils.showToast(getApplicationContext(),"Please enter all Shipping details");
                return;
            }
            if(country_code_shp.length()<=0)
            {
                CommonUtils.showToast(getApplicationContext(),"Please enter all Shipping details");
                return;
            }
            if(!country_code_shp.equalsIgnoreCase("IN"))
            {
                CommonUtils.showToast(getApplicationContext(),"Currently shipping service is available only in India!");
                return;
            }
        }
        if(!activation_type.equalsIgnoreCase("O")&&checkbox.isChecked())
        {
            name_shp = etName.getText().toString().trim();
            mobile_shp = etMobileNumber.getText().toString().trim();
            mailId_shp = etMailId.getText().toString().trim();
            address_shp = etAddress.getText().toString().trim();
            city_shp = etCity.getText().toString().trim();
            state_shp = etState.getText().toString().trim();
            zip_shp = etZip.getText().toString().trim();
            country_code_shp=country_code;
            shp_mbl_countryCode=bill_mbl_countryCode;
            if(!country_code_shp.equalsIgnoreCase("IN"))
            {
                CommonUtils.showToast(getApplicationContext(),"Currently shipping service is available only in India!");
                return;
            }
        }
        try {
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.classId, classId);
            fjson.put(Constants.accessToken, access_token);
            fjson.put("order_amount", actualPrice);
            fjson.put("total_amount", grandTotal);
            fjson.put("order_months", order_years);//passing months details
            //fjson.put("total_amount", grandTotal); Need to uncomment when Discount enabled
            fjson.put(Constants.couponCode, couponCode);
            fjson.put("currency_type", currency_type);
            fjson.put("os_type", "A");
            fjson.put("activation_type", activation_type);
            if (currency_type.equalsIgnoreCase(Constants.INR)) {
                fjson.put("coupon_value", (couponINRValue==0)?"":(couponINRValue+""));
            } else {
                fjson.put("coupon_value", (couponUSDValue==0)?"":(couponUSDValue+""));
            }

            //personal Billing Infoo
            fjson.put("billing_name", name);
            fjson.put("billing_email_id", mailId);
            fjson.put("billing_address", address);
            fjson.put("billing_city", city);
            fjson.put("billing_state", state);
            fjson.put("billing_postal_code", zip);
            fjson.put("billing_country_code", country_code);
            fjson.put("billing_mobile_number", userProfile.mobile_number);
            fjson.put("billing_mobile_country_code", bill_mbl_countryCode);


            fjson.put("shipping_name", name_shp);
            fjson.put("shipping_email_id", mailId_shp);
            fjson.put("shipping_address", address_shp);
            fjson.put("shipping_city", city_shp);
            fjson.put("shipping_state", state_shp);
            fjson.put("shipping_postal_code", zip_shp);
            fjson.put("shipping_country_code", country_code_shp);
            fjson.put("shipping_mobile_number", mobile_shp);
            fjson.put("shipping_mobile_country_code", shp_mbl_countryCode);
            String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            fjson.put(Constants.deviceId, android_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //CustomDialog.showDialog(SubscribeActivity.this,false);
        String reqRegister = Constants.reqRegister;

        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, AppConfig.ENC_KEY);
        String encode_data = "";
        try {
            encode_data = URLEncoder.encode(encryption, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent in=new Intent(getApplicationContext(), WebviewPaymentActivity.class);
        in.putExtra("url", Constants.PAYMENT + "?" + AppConfig.JSON_DATA + "=" + encode_data);

        //Log.v("url","url "+Constants.PAYMENT + "?" + AppConfig.JSON_DATA + "=" + encode_data);
        try{
            AppController.getInstance().addBuyActivationEvents(mailId,userProfile.mobile_number);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        startActivity(in);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 200) {
            setResult(200);
            finish();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        AppController.getInstance().playAudio(R.raw.back_sound);
        onBackPressed();
        return true;
    }
    boolean isback=true;
    Dialog dialog;
    public void proceedSubscribe(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if(activation_type==null||activation_type.isEmpty())
        {
            CommonUtils.showToast(getApplicationContext(),"Please select activation type");
            return;
        }



        checkbox.setVisibility(View.VISIBLE);
        rbINR.setChecked(true);
        changeInValues(R.id.rbPayInRupees);
        if(activation_type.equalsIgnoreCase("O"))
        {
            checkbox.setVisibility(View.GONE);
            subscribeLayout3.setVisibility(View.GONE);
            isback=false;
            subscribeLayout2.setVisibility(View.VISIBLE);
            subscribeLayout.setVisibility(View.GONE);


        }else
        {

            dialog = new Dialog(SubscribeOnlineActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_alert);
            TextView txt_msg = dialog.findViewById(R.id.txt_msg);
            txt_msg.setText("Currently shipping service is available only in India!");
            TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
            TextView txt_ok = dialog.findViewById(R.id.txt_ok);
            txt_ok.setText("Proceed");
            txt_cancel.setText(getString(R.string.cancel));
            txt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                    dialog.dismiss();
                }
            });
            txt_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    AppController.getInstance().playAudio(R.raw.button_click);
                    checkbox.setVisibility(View.VISIBLE);
                    subscribeLayout2.setVisibility(View.VISIBLE);
                    subscribeLayout.setVisibility(View.GONE);

                }
            });
            dialog.show();

        }


    }

    @Override
    public void onBackPressed() {
        if(!isback)
        {
            subscribeLayout2.setVisibility(View.GONE);
            subscribeLayout.setVisibility(View.VISIBLE);
            isback=true;
           /* try{

                tvDiscountAmount.clearAnimation();
            }catch (Exception e)
            {

            }*/
            return;
        }
        super.onBackPressed();
    }






    class MyYearAdapter extends BaseAdapter
    {

        public MyYearAdapter(@NonNull Context context) {

        }

        @Override
        public int getCount() {
            return listPrice.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.price_year_layout,null);
            TextView txt_year=view.findViewById(R.id.txt_year);
            txt_year.setText(listPrice.get(position).yearName);
            return view;
        }
    }


    private void getYearSubscription() throws UnsupportedEncodingException {
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
            return;
        }

        final JSONObject fjson = new JSONObject();
        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String Key = AppConfig.ENC_KEY;
        final String message = fjson.toString();
        final String encryption = Security.encrypt(message, Key);
        CustomDialog.showDialog(SubscribeOnlineActivity.this, false);
        String reqRegister = Constants.reqRegister;
        StringRequest strReq = new StringRequest(
                Request.Method.GET,
                Constants.GET_ALL_MONTHS  + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d(Constants.response, response);
                        CustomDialog.closeDialog();
                        try {
                            if (response != null) {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {
                                    JSONArray array=jObj.getJSONArray("price_data");
                                    listPrice=new ArrayList<>();
                                    PriceModel model;
                                    JSONObject obj;
                                    int months=0;
                                    for(int k=0;k<array.length();k++)
                                    {
                                        obj=array.getJSONObject(k);
                                        model=new PriceModel();
                                        model.yearID=obj.getString("id");
                                        model.priceRupee=obj.getString("inr_price");
                                        model.priceDoller=obj.getString("usd_price");
                                        model.months=obj.getString("months");
                                        months=obj.getInt("months");
                                        if(months>=12)
                                        {
                                            model.yearName=months/12+" Year(s)";
                                        }else
                                        {
                                            model.yearName=months+" Month(s)";
                                        }


                                        listPrice.add(model);
                                    }
                                    adapter.notifyDataSetChanged();
                                    updatePrice(0);
                                } else {
                                    String uid = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(), uid);
                                    finish();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            finish();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialog.closeDialog();
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
                finish();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, reqRegister);
    }

    private void updatePrice(int position) {
        if(listPrice!=null&&listPrice.size()>position)
        {


            PriceModel priceModel=listPrice.get(position);
            usdPrice=(priceModel.priceDoller);
            inrPrice=(priceModel.priceRupee);
            order_years=priceModel.months;

            try {
                couponINRValue = 0;
                couponUSDValue = 0;
                couponType = "";
                etCouponCode.setText("");
                tvCouponError.setText("");


                enterCouponCodeLayout.setVisibility(View.VISIBLE);
                displayCouponCodeLayout.setVisibility(View.GONE);

                if (Integer.parseInt(priceModel.months) >= 12) {
                    lnr_coupon_main.setVisibility(View.VISIBLE);
                } else {
                    lnr_coupon_main.setVisibility(View.GONE);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            if (rbUSD.isChecked()) {
                currency_type = Constants.USD;

                actualPrice =(Integer.parseInt(usdPrice))+"";
                moneySymbol = getString(R.string.dollar_symbol);
            } else if (rbINR.isChecked())  {
                currency_type = Constants.INR;

                actualPrice = (Integer.parseInt(inrPrice))+"";
                moneySymbol = getString(R.string.rupee_symbol);
            }







            txt_price_o_a.setText(""+getString(R.string.rupee_symbol)+""+(Integer.parseInt(inrPrice))+" / $"+ (Integer.parseInt(usdPrice))+"");

            SpannableString content = new SpannableString(txt_price_o_a.getText().toString());
            //content.setSpan(new StrikethroughSpan(), 0, content.length(), 0);
            //content.setSpan(new ForegroundColorSpan(Color.RED), 0, content.length(), 0);
            txt_price_o_a.setText("Actual Price ");
            txt_price_o_a.append(content);
        }
    }
}
