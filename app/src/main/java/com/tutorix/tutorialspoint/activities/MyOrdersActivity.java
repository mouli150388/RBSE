package com.tutorix.tutorialspoint.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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
import com.tutorix.tutorialspoint.adapters.OrdersAdapter;
import com.tutorix.tutorialspoint.models.MyOrders;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrdersActivity extends AppCompatActivity {

    RecyclerView recycler_list;
    OrdersAdapter ordersAdapter;
    List<MyOrders>listOrders;

    private String classId;
    private String userId;
    String access_token;
    String loginType;
    LinearLayout lnr_reload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        getSupportActionBar().setTitle(getString(R.string.myorders));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));
        initUI();
    }

    @Override
    public boolean onSupportNavigateUp() {
        AppController.getInstance().playAudio(R.raw.back_sound);
        finish();
        return super.onSupportNavigateUp();
    }

    private void initUI()
    {
        recycler_list=findViewById(R.id.recycler_list);
        lnr_reload=findViewById(R.id.lnr_reload);
        ordersAdapter=new OrdersAdapter();
        recycler_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        listOrders=new ArrayList<>();
        ordersAdapter.setOrders(listOrders);
        recycler_list.setAdapter(ordersAdapter);
        String[]userInfo= SessionManager.getUserInfo(getApplicationContext());

        access_token = userInfo[1];
        userId = userInfo[0];;
        loginType = userInfo[2];

        classId = userInfo[4];
        getOrders();
    }

    private void getOrders()
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

        StringRequest request = new StringRequest(Request.Method.GET, Constants.MY_ORDERS+"?json_data="+urlencode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    CustomDialog.closeDialog();
                    JSONObject obj=new JSONObject(response);
                    if(obj.has("error_flag")&&!obj.getBoolean("error_flag"))
                    {
                        listOrders.clear();

                        JSONArray array=obj.getJSONArray("orders");
                        MyOrders myOrders;
                        for(int k=0;k<array.length();k++)
                        {
                            myOrders=new MyOrders();
                            myOrders.user_id=array.getJSONObject(k).getString("user_id");
                            myOrders.order_id=array.getJSONObject(k).getString("order_id");
                            myOrders.order_number=array.getJSONObject(k).getString("order_number");
                            myOrders.class_id=array.getJSONObject(k).getString("class_id");
                            myOrders.order_status=array.getJSONObject(k).getString("order_status");
                            myOrders.total_amount=array.getJSONObject(k).getString("total_amount");
                            myOrders.order_activation_type=array.getJSONObject(k).getString("order_activation_type");
                            myOrders.order_currency=array.getJSONObject(k).getString("order_currency");
                            myOrders.created_dtm=array.getJSONObject(k).getString("created_dtm");
                            myOrders.order_activation_type=array.getJSONObject(k).getString("order_activation_type");
                            listOrders.add(myOrders);
                        }

                        ordersAdapter.setOrders(listOrders);
                        lnr_reload.setVisibility(View.GONE);
                    }else
                    {
                        lnr_reload.setVisibility(View.VISIBLE);
                        //CommonUtils.showToast(getApplicationContext(),"No Orders found");
                        //finish();
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

            }
        });
        if(getApplicationContext()==null)
            return;
        CustomDialog.showDialog(MyOrdersActivity.this,true);
            AppController.getInstance().addToRequestQueue(request);

    }
}
