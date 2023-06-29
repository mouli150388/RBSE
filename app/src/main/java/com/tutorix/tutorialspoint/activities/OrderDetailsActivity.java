package com.tutorix.tutorialspoint.activities;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.tutorix.tutorialspoint.models.MyOrders;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import androidx.appcompat.app.AppCompatActivity;

public class OrderDetailsActivity extends AppCompatActivity {

    RadioButton radio_one;
    RadioButton radio_two;
    RadioButton radio_three;
    RadioButton radio_four;
    TextView txt_one;
    TextView txt_two;
    TextView txt_three;
    int status;
    private String classId;
    private String userId;
    String access_token;
    String loginType;
    String order_id;
    TextView txt_title;
    TextView txt_orderid;
    ImageView img_item;
    TextView txt_shipping;
    TextView txt_desc;
    TextView txt_amnt;
    TextView txt_date;
    LinearLayout lnr_shipping;
    LinearLayout lnr_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        order_id=getIntent().getStringExtra("order_id");
        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userId = userInfo[0];;
        loginType = userInfo[2];

        classId = userInfo[4];

        initUI();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initUI()
    {
        radio_one=findViewById(R.id.radio_one);
        radio_two=findViewById(R.id.radio_two);
        radio_three=findViewById(R.id.radio_three);
        radio_four=findViewById(R.id.radio_four);

        txt_title=findViewById(R.id.txt_title);
        txt_orderid=findViewById(R.id.txt_orderid);
        img_item=findViewById(R.id.img_item);
        txt_desc=findViewById(R.id.txt_desc);
        txt_shipping=findViewById(R.id.txt_shipping);
        lnr_shipping=findViewById(R.id.lnr_shipping);
        lnr_status=findViewById(R.id.lnr_status);
        txt_amnt=findViewById(R.id.txt_amnt);
        txt_date=findViewById(R.id.txt_date);


        txt_one=findViewById(R.id.txt_one);
        txt_two=findViewById(R.id.txt_two);
        txt_three=findViewById(R.id.txt_three);

        txt_one.setBackgroundResource(R.color.track_order_p_color);
        txt_two.setBackgroundResource(R.color.track_order_p_color);
        txt_three.setBackgroundResource(R.color.track_order_p_color);


        getOrderDetails();

    }

    private void animate(View view)
    {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(view,

                PropertyValuesHolder.ofFloat("scaleX", 1.2f));
        scaleDown.setDuration(300);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();
    }


    private void getOrderDetails()
    {
        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
            finish();
            return;
        }
        final JSONObject fjson = new JSONObject();
        String encryption="";
        String urlencode="";

        try {

            fjson.put(Constants.userId, userId);
            fjson.put(Constants.accessToken, access_token);

            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            encryption = Security.encrypt(message, Key);
            urlencode= URLEncoder.encode(encryption,"UTF-8");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.GET, Constants.ORDER_DETAILS+"/"+order_id+"?json_data="+urlencode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject obj=new JSONObject(response);
                    if(obj.has("error_flag")&&!obj.getBoolean("error_flag"))
                    {


                        JSONObject array=obj.getJSONObject("orders");
                        MyOrders myOrders;

                            myOrders=new MyOrders();
                            myOrders.user_id=array.getString("user_id");
                            myOrders.order_id=array.getString("order_id");
                            myOrders.order_number=array.getString("order_number");
                            myOrders.class_id=array.getString("class_id");
                            myOrders.order_status=array.getString("order_status");
                            myOrders.total_amount=array.getString("total_amount");
                            myOrders.order_activation_type=array.getString("order_activation_type");
                            myOrders.order_currency=array.getString("order_currency");
                            myOrders.created_dtm=array.getString("created_dtm");
                            myOrders.shipping_address=array.getString("shipping_address");
                            myOrders.shipping_city=array.getString("shipping_city");
                            myOrders.shipping_state=array.getString("shipping_state");
                            myOrders.shipping_postal_code=array.getString("shipping_postal_code");
                            myOrders.shipping_country_code=array.getString("shipping_country_code");
                            myOrders.order_description=array.getString("order_description");
                            myOrders.order_activation_type=array.getString("order_activation_type");

                        updateUI(myOrders);
                        CustomDialog.closeDialog();



                    }else
                    {
                        CommonUtils.showToast(getApplicationContext(),"No Orders found");
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
        CustomDialog.showDialog(OrderDetailsActivity.this,true);

        AppController.getInstance().addToRequestQueue(request);

    }

    private void updateUI(MyOrders myOrders)
    {
        if(myOrders==null)
        {
            finish();
            return;
        }

        txt_desc.setText(myOrders.order_description);
        txt_title.setText(getClassname(myOrders.class_id));
        txt_orderid.setText(myOrders.order_number);
        txt_date.setText(myOrders.created_dtm);
        txt_amnt.setText(getCurrency(myOrders.order_currency)+myOrders.total_amount);
        txt_shipping.setText("");
        if(!myOrders.shipping_address.isEmpty()) {
            txt_shipping.append(" " + myOrders.shipping_address + "\n");
            txt_shipping.append("City : " + myOrders.shipping_city + "\n");
            txt_shipping.append("State : " + myOrders.shipping_state + "\n");
            txt_shipping.append(myOrders.shipping_postal_code + " " + myOrders.shipping_country_code + "\n");
        }

        if(myOrders.order_activation_type.equalsIgnoreCase("O"))
        {

            img_item.setImageResource(0);
        }else
        if(myOrders.order_activation_type.equalsIgnoreCase("S"))
        {
            img_item.setImageResource(R.drawable.ic_sd_card);
        }else
        {
            img_item.setImageResource(R.drawable.tablet);
        }
        switch (myOrders.order_status)
        {
            case "OP":
                radio_one.setChecked(true);
                animate(radio_one);
                break;
            case "PR":
                radio_one.setChecked(true);
                radio_two.setChecked(true);
                animate(radio_two);
                txt_one.setBackgroundResource(R.color.track_order_c_color);
                break;
            case "DP":
                radio_one.setChecked(true);
                radio_two.setChecked(true);
                radio_three.setChecked(true);
                animate(radio_three);
                txt_one.setBackgroundResource(R.color.track_order_c_color);
                txt_two.setBackgroundResource(R.color.track_order_c_color);
                break;
            case "DL":
                lnr_shipping.setVisibility(View.GONE);
                radio_one.setChecked(true);
                radio_two.setChecked(true);
                radio_three.setChecked(true);
                radio_four.setChecked(true);
                animate(radio_four);
                txt_one.setBackgroundResource(R.color.track_order_c_color);
                txt_two.setBackgroundResource(R.color.track_order_c_color);
                txt_three.setBackgroundResource(R.color.track_order_c_color);
                break;
            case "CL":
                txt_one.setBackgroundResource(R.color.red);
                txt_two.setBackgroundResource(R.color.red);
                txt_three.setBackgroundResource(R.color.red);
                radio_two.setVisibility(View.GONE);
                lnr_status.setVisibility(View.GONE);
                radio_three.setVisibility(View.GONE);
                radio_one.setBackgroundResource(R.drawable.circle_cancel);
                radio_four.setBackgroundResource(R.drawable.circle_cancel);
                break;
        }
    }

    private String getClassname(String classname)
    {
        if (classname.equalsIgnoreCase("1")) {
            return  "Class 6";
        } else if (classname.equalsIgnoreCase("2")) {
            return "Class 7";
        } else if (classname.equalsIgnoreCase("3")) {
            return "Class 8";
        } else if (classname.equalsIgnoreCase("4")) {
            return  "Class 9";
        }
        return "";
    }

    private String getCurrency(String currency)
    {
        if (currency.equalsIgnoreCase("U")) {
            return  "$ ";
        }
        return "Rs ";
    }
}
