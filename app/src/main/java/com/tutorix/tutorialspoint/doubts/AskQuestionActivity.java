package com.tutorix.tutorialspoint.doubts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
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
import com.tutorix.tutorialspoint.cropper.CropImage;
import com.tutorix.tutorialspoint.cropper.CropImageView;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.home.HomeTabActivity;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;
import com.tutorix.tutorialspoint.utility.PermissionRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AskQuestionActivity extends AppCompatActivity {

    AskQuestionActivity _this;

    Bundle bundle;
    String subjectId;
    String encodedString = "";
    String question_id ;

    RelativeLayout questionPicLayout;
    ImageView ivQuestionPic;
    RadioGroup radio_sub_group;

    String[]subjects=new String[]{"Select Subject",getString(R.string.physics),getString(R.string.chemistry),getString(R.string.biology),getString(R.string.mathematics)};

    SpinnerAdapter adpter;
    String questionDescription ;
    ImageView img_cam;
    ImageView img_gallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = AskQuestionActivity.this;
        setContentView(R.layout.activity_ask_question);

        radio_sub_group=findViewById(R.id.radio_sub_group);
        img_cam=findViewById(R.id.img_cam);
        img_gallery=findViewById(R.id.img_gallery);
        /*img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(v);
            }
        });
        img_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage(v);
            }
        });*/

        bundle = (getIntent().getExtras()).getBundle(Constants.intentType);
        if (bundle != null) {
            subjectId = bundle.getString(Constants.subjectId);
            question_id = bundle.getString("question_id");


            final EditText etQuestionDescription = findViewById(R.id.etQuestionDescription);

            TextView tvPost = findViewById(R.id.tvPostQuestion);

            questionPicLayout = findViewById(R.id.questionPicLayout);
            ivQuestionPic = findViewById(R.id.ivQuestionPic);

            etQuestionDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(radio_sub_group.getVisibility()!=View.GONE)
                    {
                        slideDown();
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });





            tvPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.getInstance().playAudio(R.raw.button_click);

                    questionDescription = etQuestionDescription.getText().toString().trim();
                    if (questionDescription.isEmpty()) {
                        etQuestionDescription.setError("* required");
                        etQuestionDescription.requestFocus();
                    }else  if(question_id==null||question_id.isEmpty())
                    {
                        startActivityForResult(new Intent(getApplicationContext(),DoubtSubjectActivty.class),2001);
                        //slideUp();
                    }else
                    {
                        postQuestionReplsy( questionDescription);
                    }

                }
            });
        }


        radio_sub_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                AppController.getInstance().playAudio(R.raw.button_click);
                switch (checkedId)
                {
                    case R.id.radio_math:
                        subjectId=4+"";
                        break;
                    case R.id.radio_phy:
                        subjectId=1+"";
                        break;
                    case R.id.radio_che:
                        subjectId=2+"";
                        break;
                    case R.id.radio_bio:
                        subjectId=3+"";
                        break;

                }
                AppController.getInstance().playAudio(R.raw.button_click);
                postQuestion( questionDescription);


            }
        });
    }

    public void home(View v) {

        Intent in = new Intent(AskQuestionActivity.this, HomeTabActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        finish();
    }

    private void selectSubject() {
        final Dialog dialog = new Dialog(_this, R.style.DialogTheme);
        dialog.setContentView(R.layout.dialog_subject);

       /* Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = Objects.requireNonNull(window).getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.M'ATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;*/

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans_back)));

        ImageView txt_phy = dialog.findViewById(R.id.txt_phy);
        ImageView txt_che = dialog.findViewById(R.id.txt_che);
        ImageView txt_bio = dialog.findViewById(R.id.txt_bio);
        ImageView txt_math = dialog.findViewById(R.id.txt_math);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        LinearLayout lnr_back = dialog.findViewById(R.id.lnr_back);
        txt_phy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectId = "1";
                AppController.getInstance().playAudio(R.raw.button_click);
                dialog.dismiss();
            }
        });
        txt_che.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectId = "2";

                AppController.getInstance().playAudio(R.raw.button_click);
                dialog.dismiss();
            }
        });
        txt_bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectId = "3";
                AppController.getInstance().playAudio(R.raw.button_click);
                dialog.dismiss();
            }
        });
        txt_math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectId = "4";
                AppController.getInstance().playAudio(R.raw.button_click);
                dialog.dismiss();
            }
        });
        lnr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
            }
        });

        dialog.setCancelable(false);
        // dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void postQuestion( String description) {
        final JSONObject fjson = new JSONObject();

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        String access_token = userInfo[1];
        String userId = userInfo[0];
        String loginType = userInfo[2];
        String classId = userInfo[4];


        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.subjectId, subjectId);
            fjson.put(Constants.classId, classId);
            fjson.put("base64_question_image", encodedString);
            fjson.put("question", description);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reqRegister = Constants.reqRegister;
        if(_this==null)
            return;
        slideDown();
        CustomDialog.showDialog(_this,true);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.BASE_URL + "users/ask/doubts",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.v("Response","Response "+response);
                        if(_this==null)
                            return;
                        try {
                            CustomDialog.closeDialog();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        try {

                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {


                                Dialog dialog=new Dialog(AskQuestionActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.setContentView(R.layout.dialog_alert);
                                TextView txt_msg=dialog.findViewById(R.id.txt_msg);
                                txt_msg.setText("We have noted your query. One of our SMEs will check and respond soon!");
                                TextView txt_cancel=dialog.findViewById(R.id.txt_cancel);
                                TextView txt_ok=dialog.findViewById(R.id.txt_ok);
                                txt_ok.setText("Ok");
                                txt_cancel.setVisibility(View.GONE);
                                txt_cancel.setText("Cancel");
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
                                        setResult(Activity.RESULT_OK);
                                        finish();

                                    }
                                });
                                if(_this==null)
                                    return;

                                try{
                                dialog.show();
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                //CommonUtils.showToast(getApplicationContext(), "Thank you for raising your doubt, Our faculty will check and respond it soon...");

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);
                                setResult(Activity.RESULT_CANCELED);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), "Json error : " + e.getMessage());
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
                try{
                CustomDialog.closeDialog();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
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
    private void postQuestionReplsy( String description) {
        final JSONObject fjson = new JSONObject();

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        String access_token = userInfo[1];
        String userId = userInfo[0];
        String loginType = userInfo[2];
        String classId = userInfo[4];


        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.subjectId, subjectId);
            fjson.put(Constants.classId, classId);
            fjson.put("base64_reply_image", encodedString);
            fjson.put("reply_text", description);
            fjson.put("question_id", question_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(_this==null)
        {
            return;
        }
        String reqRegister = Constants.reqRegister;
        CustomDialog.showDialog(_this,true);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.DOUBT_REPLY,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(_this==null)
                        {
                            return;
                        }
                        //Log.v("Response","Response "+response);
                        CustomDialog.closeDialog();
                        try {

                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                if(_this==null)
                                {
                                    return;
                                }
                                Dialog dialog=new Dialog(AskQuestionActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                dialog.setContentView(R.layout.dialog_alert);
                                TextView txt_msg=dialog.findViewById(R.id.txt_msg);
                                txt_msg.setText("We have noted your query. One of our SMEs will check and respond soon!");
                                TextView txt_cancel=dialog.findViewById(R.id.txt_cancel);
                                TextView txt_ok=dialog.findViewById(R.id.txt_ok);
                                txt_ok.setText("Ok");
                                txt_cancel.setVisibility(View.GONE);
                                txt_cancel.setText("Cancel");
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
                                        setResult(200);
                                        finish();

                                    }
                                });
                                dialog.show();

                                //CommonUtils.showToast(getApplicationContext(), "Thank you for raising your doubt, Our faculty will check and respond it soon...");

                                //CommonUtils.showToast(getApplicationContext(), "");

                            } else {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(getApplicationContext(), errorMsg);
                                // setResult(Activity.RESULT_CANCELED);
                                // finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(getApplicationContext(), "Json error : " + e.getMessage());
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
                try {
                    CommonUtils.showToast(getApplicationContext(), msg);
                    CustomDialog.closeDialog();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    int MY_PERMISSIONS_REQUEST_DOCUMENT = 201;
    Uri mCropImageUri;

    public void pickImage(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        hideKeyboard();
        /*if (PermissionRequest.selfPermissionGranted(AskQuestionActivity.this, Manifest.permission.CAMERA) && PermissionRequest.selfPermissionGranted(AskQuestionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) && PermissionRequest.selfPermissionGranted(AskQuestionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            loadImage();
        } else {
            permisionCheckHere(MY_PERMISSIONS_REQUEST_DOCUMENT);
            // ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_DOCUMENT);
        }*/
        CropImage.startPickImageActivity(AskQuestionActivity.this);
    }
    public void captureImage(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if(CropImage.isExplicitCameraPermissionRequired(getApplicationContext())||ActivityCompat.checkSelfPermission(AskQuestionActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(AskQuestionActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(AskQuestionActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},2002);
            return;
        }
        /*Uri outputFileUri = getCaptureImageOutputUri(getApplicationContext());

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (outputFileUri != null) {
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        }

        startActivityForResult(captureIntent, CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);*/
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);

    }

    public static Uri getCaptureImageOutputUri(@NonNull Context context) {
        Uri outputFileUri = null;

        File getImage = Environment.getExternalStorageDirectory();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath()));
        }
        return outputFileUri;
    }
    private void loadImage() {
        CropImage.startPickImageActivity(AskQuestionActivity.this);
    }
    public InputStream iStream;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handle result of pick image chooser

        if(requestCode==2001)
        {
            if(resultCode>0)
            {
                subjectId=resultCode+"";
                postQuestion(questionDescription);
            }
            return;
        }

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(AskQuestionActivity.this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(AskQuestionActivity.this, imageUri)) {
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                //  Toast.makeText(getActivity(), "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();

                try {
                    //questionPicLayout.setVisibility(View.VISIBLE);
                    //ivQuestionPic.setImageURI(result.getUri());
                    iStream = getContentResolver().openInputStream(result.getUri());
                    long fileSize= iStream.available();

                    if(fileSize/(1000*1000)>1)
                    {
                        CommonUtils.showToast(getApplicationContext(),"Image size too big");
                        return;
                    }
                    //Log.v("InputSream","InputSream "+iStream.available());

                    DoubtImagePick lecturDescFragment =
                            DoubtImagePick.newInstance();
                    lecturDescFragment.show(getSupportFragmentManager(),
                            "add_photo_dialog_fragment");

                    if(true)
                    {
                        return;
                    }
                    //byte[] fileData = getBytes(iStream);
                    //encodedString = Base64.encodeToString(fileData, Base64.DEFAULT);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            }
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(AskQuestionActivity.this);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void hideKeyboard() {
        try{
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (manager != null&&getCurrentFocus()!=null&&getCurrentFocus().getWindowToken()!=null) {
                manager.hideSoftInputFromWindow((getCurrentFocus()).getWindowToken(), 0);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void permisionCheckHere(final int requestCode) {
        if (ContextCompat.checkSelfPermission(AskQuestionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AskQuestionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AskQuestionActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(AskQuestionActivity.this)
                        .setTitle("Permission required")
                        .setMessage("Permissions are required for this application to work ! ")
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, requestCode);
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

            } else {
                CommonUtils.showToast(getApplicationContext(), "Go to settings>Enable permission to open the application.");
                //requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2001) {
            if (PermissionRequest.selfPermissionGranted(AskQuestionActivity.this, Manifest.permission.CAMERA) && PermissionRequest.selfPermissionGranted(AskQuestionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) && PermissionRequest.selfPermissionGranted(AskQuestionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Uri outputFileUri = getCaptureImageOutputUri(getApplicationContext());

                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (outputFileUri != null) {
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                }

                startActivityForResult(captureIntent, CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
            }
        } else if (PermissionRequest.selfPermissionGranted(AskQuestionActivity.this, Manifest.permission.CAMERA) && PermissionRequest.selfPermissionGranted(AskQuestionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) && PermissionRequest.selfPermissionGranted(AskQuestionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            loadImage();
        } else {
            permisionCheckHere(MY_PERMISSIONS_REQUEST_DOCUMENT);
            // ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_DOCUMENT);
        }
    }

    public void removeQuestionPic(View view) {
        if (!encodedString.isEmpty()) {
            ivQuestionPic.setImageDrawable(null);
            encodedString = "";
            questionPicLayout.setVisibility(View.GONE);
        }
    }

    public void goBack(View view) {
        onBackPressed();
    }


    private void slideUp()
    {
        radio_sub_group.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                radio_sub_group.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        radio_sub_group.startAnimation(animate);
    }
    private void slideDown()
    {

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                radio_sub_group.getHeight()); // toYDelta
        animate.setDuration(500);
        //animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                radio_sub_group.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        radio_sub_group.startAnimation(animate);


    }

    public  String convert(Bitmap bitmap)
    {

        try {
            //CustomDialog.showDialog(AskQuestionActivity.this,true);
            questionPicLayout.setVisibility(View.VISIBLE);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            encodedString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        //CustomDialog.closeDialog();
        return "";
    }
}
