package com.tutorix.tutorialspoint.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.home.HomeTabActivity;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentExecutionActivity extends AppCompatActivity {

    // TextView dialog_message;
    //  Button btDialogYes;
    // Button btDialogNo;
    // RelativeLayout colored_circle;
    //RelativeLayout colored_circle_failure;
    String[] userInfo;
    String userId;
    String accessToken;
    String classId;
    String device_id;
    String android_id;
    @BindView(R.id.btn_continue)
    Button btnContinue;
    @BindView(R.id.layout_success)
    LinearLayout layoutSuccess;
    @BindView(R.id.btn_tryagain)
    Button btnTryagain;
    @BindView(R.id.layout_failure)
    LinearLayout layoutFailure;
    @BindView(R.id.txt_message)
    TextView txt_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_execution_new);
        ButterKnife.bind(this);


        AppController.getInstance().playAudio(R.raw.alert_audio);
        userInfo = SessionManager.getUserInfo(getApplicationContext());
        userId = userInfo[0];
        accessToken = userInfo[1];
        classId = userInfo[4];
        device_id = userInfo[3];
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        initUI();

        String intentData = getIntent().getDataString();
       // Log.v("intentData ","intentData "+intentData);
        //intentData="com.tutorix.payment://cancel/?returndata=LroQ9A+X0XgJFXVa7LH6y5VJakBaJraxo5nkYysoou9mPxTmz7hIau217Kv4nsN55mgvb/JM4LRTBPX/n0jwMA==";
        String resPosne = intentData.split("://")[1];
        String succesRes = resPosne.substring(0, resPosne.indexOf("/"));


        String returnData = resPosne.split("returndata=")[1];
        // String returnData=resPosne.substring(resPosne.indexOf("/?returndata=")+1);


        //Make decrypt the data which received after payment
        final String Key = AppConfig.ENC_KEY;
        //Security.decrypt(keyResposne,Key);
        String filenResposne = Security.getDecryptKey(returnData, Key);


        try {
            JSONObject obj = new JSONObject(filenResposne);
            if (!obj.getBoolean("error_flag")) {
                String message = obj.getString("message");
                String activation_type = obj.getString("activation_type");
                String activation_key = obj.getString("activation_key");
                String activation_start_date = obj.getString("activation_start_date");
                String activation_end_date = obj.getString("activation_end_date");
                String current_date = obj.getString("current_date");


                if (!activation_key.isEmpty()) {
                    //long date = CommonUtils.daysBetween(activation_start_date, activation_end_date);

                   /* MyDatabase db=MyDatabase.getDatabase(getApplicationContext());
                    ActivationDetails aDetails=new ActivationDetails();
                    aDetails.user_id=userId;
                    aDetails.activation_key=activation_key;
                    aDetails.activation_type=activation_type;
                    aDetails.activation_start_date=activation_start_date;
                    aDetails.activation_end_date=activation_end_date;
                    aDetails.class_id=classId;
                    aDetails.days_left="365";
                    aDetails.current_date=current_date;

                    ActivationDetails activationDetails= MyDatabase.getDatabase(getApplicationContext()).activationDAO().getActivationDetails(userId,classId);
                    if(activationDetails==null)
                        MyDatabase.getDatabase(getApplicationContext()).activationDAO().inserActivation(aDetails);
                    else MyDatabase.getDatabase(getApplicationContext()).activationDAO().updateActivationDetails(aDetails.activation_end_date,aDetails.activation_start_date,aDetails.activation_type,aDetails.activation_key,aDetails.current_date,aDetails.days_left,aDetails.class_id,aDetails.user_id);


                    //long value=  db.activationDAO().inserActivation(aDetails);


                    SessionManager session = new SessionManager();
                    String[]userinfo= SessionManager.getUserInfo(getApplicationContext());
                    String data_url="";
                    if(obj.has(Constants.DATA_URL))
                        data_url = obj.getString(Constants.DATA_URL);

                    String secure_data_url = "";
                    if (obj.has(Constants.SECURE_DATA_URL))
                        secure_data_url = obj.getString(Constants.SECURE_DATA_URL);

                    session.setLogin(PaymentExecutionActivity.this,userId,userinfo[1],android_id,activation_type,classId,secure_data_url,data_url);
                   */
                }

               /* dialog_message.setText(message);

                colored_circle.setVisibility(View.VISIBLE);
                btDialogYes.setVisibility(View.VISIBLE);
                colored_circle_failure.setVisibility(View.GONE);
                btDialogNo.setVisibility(View.GONE);*/
                // CommonUtils.showToast(getApplicationContext(),message);
                layoutFailure.setVisibility(View.GONE);
                layoutSuccess.setVisibility(View.VISIBLE);
                txt_message.setText(message+"");
            } else {
              /*  String message=obj.getString("message");
                dialog_message.setText(message);
                if(succesRes.contains("can"))
                    btDialogNo.setText("Cancelled");
                colored_circle.setVisibility(View.GONE);
                btDialogYes.setVisibility(View.GONE);
                colored_circle_failure.setVisibility(View.VISIBLE);
                btDialogNo.setVisibility(View.VISIBLE);*/
                layoutFailure.setVisibility(View.VISIBLE);
                layoutSuccess.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //com.tutorix.paypalcustomtabdemo:success/a.0lgU1yyuiQ0xRVx9OHCiDLuOhTOluLL0ZDnRBkrAg-
    }

    private void initUI() {
        /*dialog_message=findViewById(R.id.dialog_message);
        colored_circle_failure=findViewById(R.id.colored_circle_failure);
        colored_circle=findViewById(R.id.colored_circle);
        btDialogYes=findViewById(R.id.btDialogYes);
        btDialogNo=findViewById(R.id.btDialogNo);
        colored_circle_failure.setVisibility(View.GONE);
        btDialogNo.setVisibility(View.GONE);
        colored_circle.setVisibility(View.VISIBLE);
        btDialogYes.setVisibility(View.VISIBLE);
        btDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setResult(200);
                //Intent in=new Intent("paymentprocess");
                // in.putExtra(Constants.activationStatus,200);
                // sendBroadcast(in);

                Intent i = new Intent(PaymentExecutionActivity.this, HomeTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                startActivity(i);
                finish();
            }
        });
        btDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });*/


    }

    @OnClick({R.id.btn_continue, R.id.btn_tryagain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_continue:
                Intent i = new Intent(PaymentExecutionActivity.this, HomeTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                startActivity(i);
                finish();
                break;
            case R.id.btn_tryagain:
                AppController.getInstance().playAudio(R.raw.back_sound);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
