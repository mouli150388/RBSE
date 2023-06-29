package com.tutorix.tutorialspoint.doubts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.common.util.concurrent.ListenableFuture;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.cropper.CropImage;
import com.tutorix.tutorialspoint.cropper.CropImageView;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AskDoubtMainActivity extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.home)
    LinearLayout home;
    @BindView(R.id.appbar)
    RelativeLayout appbar;
    @BindView(R.id.view_finder)
    PreviewView viewFinder;
    @BindView(R.id.lnr_gallery)
    LinearLayout lnrGallery;
    @BindView(R.id.lnr_camera)
    LinearLayout lnrCamera;
    @BindView(R.id.lnr_keyboard)
    LinearLayout lnrKeyboard;
    @BindView(R.id.card_buttons)
    MaterialCardView cardButtons;
    @BindView(R.id.lnr_loading)
    LinearLayout lnrLoading;
    @BindView(R.id.img_scanning)
    ImageView img_scanning;
    private int REQUEST_CODE_PERMISSIONS = 101;
    private String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};

    private Executor executor = Executors.newSingleThreadExecutor();
    ImageCapture imageCapture;
    public InputStream iStream;
    String encodedString;
    String question_id;
    String subject_id;
    AskDoubtMainActivity _this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_doubt_main);
        ButterKnife.bind(this);
        _this=this;
        lnrLoading.setVisibility(View.GONE);
        SharedPreferences sh=getSharedPreferences("doubts",MODE_PRIVATE);
        sh.edit().clear().apply();
        Bundle b = getIntent().getBundleExtra(Constants.intentType);
        if (b != null) {
            subject_id = b.getString("subject_id");
            question_id = b.getString("question_id");
            if (question_id != null) {
                lnrGallery.setVisibility(View.INVISIBLE);
                lnrKeyboard.setVisibility(View.INVISIBLE);
            }
        }

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.Q)
        {
            REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};
        }

        if (allPermissionGranted()) {

            startCamera();
        } else {

            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        Glide.with(getApplicationContext()).load(R.drawable.scanning_gif).into(img_scanning);
    }

    @OnClick({R.id.img_back, R.id.lnr_gallery, R.id.lnr_camera, R.id.lnr_keyboard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.lnr_gallery:
                AppController.getInstance().playAudio(R.raw.qz_next);
                AppController.getInstance().startItemAnimation(view);
                CropImage.startPickImageActivity(AskDoubtMainActivity.this);
                break;
            case R.id.lnr_camera:
                SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                //File file = new File(Environment.getExternalStorageDirectory(), mDateFormat.format(new Date())+ ".jpg");
                //Log.v("file Image ","OnSaved Image file "+Environment.getExternalStorageDirectory());

                File  mImageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "TDoubtImage");
                boolean   isDirectoryCreated = mImageDir.exists() || mImageDir.mkdirs();
                if (isDirectoryCreated) {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TDoubtImage", mDateFormat.format(new Date())+".jpg");



                    //Log.v("file Image ", "OnSaved Image file " + file.exists());

                    ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
                    imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                           // Log.v("OnSaved Image ", "OnSaved Image " + file.getAbsolutePath());
                            //Uri.fromFile(file);
                            startCropImageActivity(Uri.fromFile(file));

                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException error) {
                            error.printStackTrace();
                        }
                    });
                } else {
                    mImageDir = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "TDoubtImage");
                    isDirectoryCreated = mImageDir.exists() || mImageDir.mkdirs();
                    if (isDirectoryCreated) {
                        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/TDoubtImage", mDateFormat.format(new Date())+".jpg");

                        //Log.v("file Image ", "OnSaved Image file " + file.exists());

                        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
                        imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                            @Override
                            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                                // Log.v("OnSaved Image ", "OnSaved Image " + file.getAbsolutePath());
                                //Uri.fromFile(file);
                                startCropImageActivity(Uri.fromFile(file));

                            }

                            @Override
                            public void onError(@NonNull ImageCaptureException error) {
                                error.printStackTrace();
                            }
                        });
                    }
                }
                break;
            case R.id.lnr_keyboard:
                Intent i = new Intent(getApplicationContext(), AskTextQuestionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("question_id", "");
                bundle.putBoolean("isReply", false);
                bundle.putString(Constants.subjectId, "");
                i.putExtra(Constants.intentType, bundle);
                startActivityForResult(i, 100);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                AppController.getInstance().playAudio(R.raw.qz_next);
                AppController.getInstance().startItemAnimation(view);
                finish();
                break;
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(AskDoubtMainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionGranted() {

        for (String permission : REQUIRED_PERMISSIONS) {

            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

                return false;
            }
        }
        return true;
    }

    private void startCamera() {

        try {
            final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

            cameraProviderFuture.addListener(new Runnable() {
                @Override
                public void run() {
                    try {

                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                        bindPreview(cameraProvider);

                    } catch (ExecutionException | InterruptedException e) {
                        // No errors need to be handled for this Future.
                        // This should never be reached.
                    }
                }
            }, ContextCompat.getMainExecutor(this));
        }catch (Exception e)
        {
            e.printStackTrace();
            CommonUtils.showToast(getApplicationContext(),"Camera Option not working, use gallery images");
        }
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

         preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector ;
        try {
            cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();
            ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                    .build();

            ImageCapture.Builder builder = new ImageCapture.Builder();

            //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
            //HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

            // Query if extension is available (optional).
            // if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            //  hdrImageCaptureExtender.enableExtension(cameraSelector);
            // }

            imageCapture = builder
                    .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                    .build();
            preview.setSurfaceProvider(viewFinder.createSurfaceProvider());
            try {
                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis, imageCapture);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }catch (Exception e)
        {
            CommonUtils.showToast(getApplicationContext(),"Camera Option not working, use gallery images");
        }




    }
    Camera camera;
    Preview preview;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(AskDoubtMainActivity.this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(AskDoubtMainActivity.this, imageUri)) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                //  Toast.makeText(getActivity(), "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();

                try {
                    //questionPicLayout.setVisibility(View.VISIBLE);
                    //ivQuestionPic.setImageURI(result.getUri());
                    iStream = getContentResolver().openInputStream(result.getUri());
                    long fileSize = iStream.available();
                    long MB=1024L*1024L;


                    if (((float)fileSize / (MB)) > 25) {
                        CommonUtils.showToast(getApplicationContext(), "Image size too big");
                        return;
                    }
                    //Log.v("InputSream","InputSream "+iStream.available());
                    lnrLoading.setVisibility(View.VISIBLE);
                    /*DoubtImagePick lecturDescFragment =
                            DoubtImagePick.newInstance();
                    lecturDescFragment.show(getSupportFragmentManager(),
                            "add_photo_dialog_fragment");*/
                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    resizeImage(iStream);

                                }
                            }
                    ).start();


                    if (true) {
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

    public String getBatchDirectoryName() {

        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {

        }

        return app_folder_path;
    }

    public static Uri getCaptureImageOutputUri(@NonNull Context context) {
        Uri outputFileUri = null;

        File getImage = Environment.getExternalStorageDirectory();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath()));
        }
        return outputFileUri;
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

    public void convert(Bitmap bitmap) {

        try {
            //CustomDialog.showDialog(AskQuestionActivity.this,true);


            if (question_id != null) {
                postReplyQuestionImg(bitmap);
            } else {
                postQuestion(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    StringRequest strReqRelated;
    private void postQuestion(Bitmap bitmap) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext()).load(R.drawable.scanning_gif).into(img_scanning);
                viewFinder.setVisibility(View.GONE);
            }
        });

        final JSONObject fjson = new JSONObject();

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        String access_token = userInfo[1];
        String userId = userInfo[0];
        String loginType = userInfo[2];
        String classId = userInfo[4];

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            encodedString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        }catch (OutOfMemoryError e)
        {
            CommonUtils.showToast(_this,"Somthing went wrong, Please try again");
           onBackPressed();
           return;
        }catch (Exception e)
        {

        }

        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);
            //fjson.put(Constants.subjectId, subjectId);
            fjson.put(Constants.classId, classId);
            if (!encodedString.isEmpty())
                encodedString = "data:image/jpeg;base64," + encodedString;
            fjson.put(Constants.base64_question_image, encodedString);
            fjson.put(Constants.question_text, "");


        } catch (JSONException e) {
            e.printStackTrace();
            CustomDialog.closeDialog();
        }

        String reqRegister = Constants.reqRegister;
        if (_this == null) {
            try {
                CustomDialog.closeDialog();
            } catch (Exception e) {

            }
            return;
        }


        strReqRelated = new StringRequest(Request.Method.POST,
                Constants.DOUBT_RELATED_QTNS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.v("Response","Response "+response);
                        if (_this == null)
                            return;


                        try {

                            JSONObject jObj = new JSONObject(response);
                            //Log.v("Reponse", "Reponse " + jObj.toString());
                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (!error) {

                                SharedPreferences sh = getSharedPreferences("doubts", MODE_PRIVATE);
                                sh.edit().putString("doubts_data", jObj.toString()).apply();

                                iStream=null;

                                startActivity(new Intent(getApplicationContext(), SimilarDoubtActivity.class));
                                finish();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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

                        CommonUtils.showToast(getApplicationContext(), msg);
                        lnrLoading.setVisibility(View.GONE);
                    }
                });

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
        if(_this != null)
        AppController.getInstance().addToRequestQueue(strReqRelated, reqRegister);
    }

    @Override
    public void onBackPressed() {
        _this=null;
        if(strReqRelated!=null)
            AppController.getInstance().cancelPendingRequests(Constants.reqRegister);
        finish();
    }
    public void disMissDialog()
    {
        if(lnrLoading.getVisibility()==View.VISIBLE)
        {
            lnrLoading.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    protected void onDestroy() {

        if(strReqRelated!=null)
            AppController.getInstance().cancelPendingRequests(Constants.reqRegister);
        super.onDestroy();
    }

    private void postReplyQuestionImg(Bitmap bitmap ) {
        //lnrLoading.setVisibility(View.VISIBLE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext()).load(R.drawable.scanning_gif).into(img_scanning);
                viewFinder.setVisibility(View.GONE);
            }
        });
        final JSONObject fjson = new JSONObject();

        String[] userInfo = SessionManager.getUserInfo(getApplicationContext());

        String access_token = userInfo[1];
        String userId = userInfo[0];
        String loginType = userInfo[2];
        String classId = userInfo[4];
        try{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        encodedString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
            }catch (OutOfMemoryError e)
            {
                CommonUtils.showToast(_this,"Somthing went wrong, Please try again");
                onBackPressed();
                return;
            }catch (Exception e)
            {

            }

        try {
            fjson.put(Constants.accessToken, access_token);
            fjson.put(Constants.userId, userId);
            fjson.put(Constants.subjectId, subject_id);
            fjson.put(Constants.question_id, question_id);
            fjson.put(Constants.classId, classId);
            if(!encodedString.isEmpty())
                encodedString="data:image/jpeg;base64,"+encodedString;
            fjson.put("base64_answer_image", encodedString);
            fjson.put("reply_text", "");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String reqRegister = Constants.reqRegister;
        if(_this==null)
            return;



        strReqRelated = new StringRequest(Request.Method.POST,
                Constants.DOUBT_REPLY_NEW,
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


                                setResult(200);
                                finish();

                                CommonUtils.showToast(getApplicationContext(), "Noted your question!");

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
        if(_this!=null)
        AppController.getInstance().addToRequestQueue(strReqRelated, reqRegister);
    }


    private void resizeImage(InputStream iStream)
    {

       //change "getPic()" for whatever you need to open the image file.
        Bitmap b = BitmapFactory.decodeStream(iStream);

        if(b==null)
        {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CommonUtils.showToastLong(getApplicationContext(),"Unable to process your image doubt, please try again.");
                }
            });

            finish();
            return;
        }
// original measurements
        int origWidth = b.getWidth();
        int origHeight = b.getHeight();

        final int destWidth = 400;//or the width you need

        if(origWidth > destWidth){
            // picture is wider than we want it, we calculate its target height
            int destHeight = origHeight/( origWidth / destWidth ) ;
            // we create an scaled bitmap so it reduces the image, not just trim it
            Bitmap b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            // compress to the format you want, JPEG, PNG...
            // 70 is the 0-100 quality percentage
            b2.compress(Bitmap.CompressFormat.JPEG,100 , outStream);
            // we save the file, at least until we have made use of it
            convert(b2);
        }
    }
}
