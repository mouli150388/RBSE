package com.tutorix.tutorialspoint.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.cropper.CropImage;
import com.tutorix.tutorialspoint.cropper.CropImageView;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.models.Classes;
import com.tutorix.tutorialspoint.models.CountryModel;
import com.tutorix.tutorialspoint.models.UserProfile;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.utility.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    ProfileActivity _this;

    CircleImageView cvLogo;

    TextView txt_class_Name;
    String class_id;
    String access_token;
    String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = ProfileActivity.this;
        setContentView(R.layout.activity_profile);


        cvLogo = findViewById(R.id.logo);
        txt_class_Name = findViewById(R.id.txt_class_Name);

        cvLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(ProfileActivity.this);

                // changeLogo();
            }
        });
        userInfo = SessionManager.getUserInfo(getApplicationContext());
        user_id = userInfo[0];
        access_token = userInfo[1];
        class_id = userInfo[4];

        initViews();
        txt_class_Name.setText(AppConfig.getClassNameDisplay(class_id)+" Details");

       /* switch (class_id) {
            case "1":
                txt_class_Name.setText("6th Class School Details");
                break;
            case "2":
                txt_class_Name.setText("7th Class School Details");
                break;
            case "3":
                txt_class_Name.setText("8th Class School Details");
                break;
            case "4":
                txt_class_Name.setText("9th Class School Details");
                break;


        }*/
        try {
            if (!AppStatus.getInstance(getApplication()).isOnline()) {
                fetchProfile();
                return;

            }
            getProfileInformation();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void fetchProfile() {
        MyDatabase db = MyDatabase.getDatabase(getApplicationContext());

        UserProfile profile = db.userDAO().getUserProfile(user_id);


        setProfileLogo(profile.photo);


        if (!(profile.user_name).isEmpty()) {
            tvUserName.setText("Name: " + capitalizeFirstChar(profile.user_name));
        }
        if (!profile.mobile_number.isEmpty()) {
            tvUserMobile.setText("Mobile Number: " + profile.mobile_number);
        }


        if (profile.father_name.isEmpty() || profile.father_name.trim().equals("null")) {
            tvUserFatherName.setHint("Father Name");
        } else {
            tvUserFatherName.setText("Father Name: " + capitalizeFirstChar(profile.father_name));

        }

        if (profile.email_id.equalsIgnoreCase("null") || profile.email_id.isEmpty()) {
            tvUserMailId.setHint("Email Id");
        } else {
            tvUserMailId.setText("Email Id: " + profile.email_id);

        }
        if (profile.address.isEmpty()) {
            tvUserAddress.setHint("Address");
        } else {
            tvUserAddress.setText("Address: " + capitalizeFirstChar(profile.address));

        }

        if (profile.city.isEmpty()) {
            tvUserCity.setHint("City");
        } else {
            tvUserCity.setText("City: " + capitalizeFirstChar(profile.city));

        }

        if (profile.state.isEmpty()) {
            tvUserState.setHint("State");
        } else {
            tvUserState.setText("State: " + capitalizeFirstChar(profile.state));
        }

        if (profile.postal_code.isEmpty()) {
            userZip.setHint("Postal Code");
        } else {
            userZip.setText("Postal: " + capitalizeFirstChar(profile.postal_code));
        }

        if (profile.country_code.isEmpty()) {
            userCountry.setHint("Country");
        } else {
            userCountry.setText("Country: " + capitalizeFirstChar(checkCountry(profile.country_code)));
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String gender_dob = "";
        if (profile.gender.isEmpty() &&
                profile.date_of_birth.isEmpty()) {
           // tvClassName.setVisibility(View.GONE);
        } else if (!profile.gender.isEmpty() &&
                !profile.date_of_birth.isEmpty()) {
            try {
                gender_dob = gender_dob +
                        capitalizeFirstChar(profile.gender) ;
                       /* + ", " +CommonUtils.calculateAge(sdf.parse(profile.date_of_birth)).getYears()*/;
            } catch (Exception e) {
                e.printStackTrace();
            }
            tvUserDOB.setText("DOB: " + profile.date_of_birth);

        } else if (!profile.gender.isEmpty()) {
            gender_dob = gender_dob + capitalizeFirstChar(profile.gender);

        } else if (!profile.date_of_birth.isEmpty()) {
           /* try {
                gender_dob = gender_dob +
                        CommonUtils.calculateAge(sdf.parse(profile.date_of_birth)).getYears();
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            tvUserDOB.setText("DOB: " + profile.date_of_birth);

        }

        //tvClassName.setText(gender_dob);

        if (profile.school_name.isEmpty()) {
            tvSchoolName.setHint("School name");
        } else {
            tvSchoolName.setText("School name: " + profile.school_name);

        }

        if (profile.roll_number.isEmpty()) {
            tvRollNo.setHint("Roll no");
        } else {
            tvRollNo.setText("Roll no: " + profile.roll_number);

        }

        if (profile.section_name.isEmpty()) {
            tvSectionName.setHint("Section name");
        } else {
            tvSectionName.setText("Section name: " + profile.section_name);

        }

        if (profile.school_address.isEmpty()) {
            tv_schl_address.setHint("School Address");
        } else {
            tv_schl_address.setText("Address : " + profile.school_address);

        }

        if (profile.school_city.isEmpty()) {
            tv_schl_city.setHint("School City");
        } else {
            tvSectionName.setText("City : " + profile.school_city);

        }

        if (profile.school_state.isEmpty()) {
            tv_schl_state.setHint("School State ");
        } else {
            tv_schl_state.setText("State : " + profile.school_state);

        }

        if (profile.school_postal_code.isEmpty()) {
            tv_schl_zip.setHint("School Postal ");
        } else {
            tv_schl_zip.setText("Postal : " + profile.school_postal_code);

        }

        if (profile.school_country_code.isEmpty()) {
            tv_schl_country.setHint("School Country ");
        } else {
            tv_schl_country.setText("Country : " + checkCountry(profile.school_country_code));

        }
    }


    private void setProfileLogo(String path) {

        path = checkNotNull(path);
        if (!path.isEmpty()) {
            path = Constants.IMAGE_PATH + user_id + "/" + path;
            Log.e("logo", "setProfileLogo: "+path );
            Picasso.with(getApplicationContext()).load(path)/*.networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)*/.into(cvLogo);
        }

    }


    String[] userInfo;
    TextView tvProfileName, tvClassName;
    TextView tvUserName, tvUserFatherName, tvUserDOB, tvUserMailId, tvUserMobile, tvUserAddress, tvUserCity, tvUserState, userCountry, userZip;
    TextView tvSchoolName, tvRollNo, tvSectionName, tv_schl_address, tv_schl_city, tv_schl_state, tv_schl_country, tv_schl_zip;
TextView userstdyClass;
    private void initViews() {
        tvProfileName = findViewById(R.id.profileName);
        //tvClassName = findViewById(R.id.classname);

        tvUserName = findViewById(R.id.username);
        tvUserFatherName = findViewById(R.id.userFatherName);
        tvUserDOB = findViewById(R.id.userDOB);
        tvUserMailId = findViewById(R.id.userMailId);
        tvUserMobile = findViewById(R.id.userMobile);
        tvUserAddress = findViewById(R.id.userAddress);
        tvUserCity = findViewById(R.id.userCity);
        tvUserState = findViewById(R.id.userState);
        userCountry = findViewById(R.id.userCountry);
        userZip = findViewById(R.id.userZip);

        tvSchoolName = findViewById(R.id.schoolName);
        tvRollNo = findViewById(R.id.rollNo);
        tvSectionName = findViewById(R.id.sectionName);
        tv_schl_address = findViewById(R.id.tv_schl_address);
        tv_schl_city = findViewById(R.id.tv_schl_city);
        tv_schl_state = findViewById(R.id.tv_schl_state);
        tv_schl_zip = findViewById(R.id.tv_schl_zip);
        tv_schl_country = findViewById(R.id.tv_schl_country);
        userstdyClass = findViewById(R.id.userstdyClass);
    }

    private void getProfileInformation() throws UnsupportedEncodingException {
        if (AppStatus.getInstance(_this).isOnline()) {
            final JSONObject fjson = new JSONObject();

            try {
                fjson.put(Constants.accessToken, access_token);
                fjson.put(Constants.classId, class_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tag_string_req = Constants.reqRegister;
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            CustomDialog.showDialog(ProfileActivity.this, true);
            StringRequest strReq = new StringRequest(
                    Request.Method.GET,
                    Constants.USERS + "/" + user_id + "?json_data=" + URLEncoder.encode(encryption, "UTF-8"),
                    new Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {
                            try {
                                CustomDialog.closeDialog();
                                JSONObject jObj = new JSONObject(response);


                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {
                                    MyDatabase db = MyDatabase.getDatabase(getApplicationContext());

                                    UserProfile profile = db.userDAO().getUserProfile(user_id);
                                    if (profile == null) {
                                        profile.user_id = user_id;
                                        profile.accessToken = access_token;

                                    }
                                    String photo = jObj.getString("photo");


                                    photo = checkNotNull(photo);
                                    if (!photo.isEmpty()) {
                                        setProfileLogo(photo);
                                    }

                                    profile.photo = photo;

                                    profile.user_name = jObj.getString(Constants.userName);
                                    //tvProfileName.setText(capitalizeFirstChar(jObj.getString(Constants.fullName)));
                                    tvUserName.setText("Name: " + capitalizeFirstChar(jObj.getString(Constants.fullName)));
                                    tvUserMobile.setText("Mobile Number: " + jObj.getString(Constants.mobileNumber));
                                    profile.full_name = jObj.getString(Constants.fullName);
                                    if (!jObj.getString(Constants.mobileNumber).isEmpty()) {
                                        tvUserMobile.setText("Mobile Number: " + jObj.getString(Constants.mobileNumber));
                                    }

                                    String fName=jObj.getString(Constants.fatherName);
                                        if(fName==null||fName.equals("null"))
                                            profile.father_name="";
                                        else profile.father_name=fName;
                                    //profile.father_name = jObj.getString(Constants.fatherName);
                                    profile.mobile_number = jObj.getString(Constants.mobileNumber);

                                    if (fName.isEmpty() ||fName.equals("null")) {
                                        tvUserFatherName.setHint("Father Name");
                                    } else {
                                        tvUserFatherName.setText("Father Name: " + capitalizeFirstChar(fName));

                                    }

                                    if (jObj.getString(Constants.emailId).equalsIgnoreCase("null") || jObj.getString(Constants.emailId).isEmpty()) {
                                        tvUserMailId.setHint("Email Id");
                                        profile.email_id = "";
                                    } else {
                                        tvUserMailId.setText("Email Id: " + jObj.getString(Constants.emailId));
                                        profile.email_id = jObj.getString(Constants.emailId);
                                    }
                                    if (jObj.getString(Constants.address).isEmpty()) {
                                        tvUserAddress.setHint("Address");
                                        profile.address = "";
                                    } else {
                                        tvUserAddress.setText("Address: " + capitalizeFirstChar(jObj.getString(Constants.address)));
                                        profile.address = jObj.getString(Constants.address);
                                    }

                                    if (jObj.getString(Constants.city).isEmpty()) {

                                        tvUserCity.setHint("City");
                                        profile.city = "";
                                    } else {
                                        tvUserCity.setText("City: " + capitalizeFirstChar(jObj.getString(Constants.city)));
                                        profile.city = jObj.getString(Constants.city);
                                    }

                                    if (jObj.getString(Constants.state).isEmpty()) {
                                        tvUserState.setHint("State");
                                        profile.state = "";
                                    } else {
                                        tvUserState.setText("State: " + capitalizeFirstChar(jObj.getString(Constants.state)));
                                        profile.state = jObj.getString(Constants.state);
                                    }

                                    if (jObj.getString(Constants.country_code).isEmpty()) {
                                        userCountry.setHint("Country");
                                        profile.country_code = "";
                                    } else {
                                        userCountry.setText("Country: " + (checkCountry(jObj.getString(Constants.country_code))));
                                        profile.country_code = jObj.getString(Constants.country_code);
                                    }

                                    if (!jObj.has(Constants.user_current_class)||jObj.getString(Constants.user_current_class).isEmpty()) {
                                        userstdyClass.setHint("Current Studying Class");
                                        //profile.country_code = "";
                                    } else {
                                        userstdyClass.setText("Studying : " + (checkClass(jObj.getString(Constants.user_current_class))));
                                        //profile.country_code = jObj.getString(Constants.country_code);
                                    }
                                    if (jObj.getString(Constants.postal_code).isEmpty()) {
                                        userZip.setHint("Postal code");
                                        profile.postal_code = "";
                                    } else {
                                        userZip.setText("Postal: " + capitalizeFirstChar(jObj.getString(Constants.postal_code)));
                                        profile.postal_code = jObj.getString(Constants.postal_code);
                                    }


                                    @SuppressLint("SimpleDateFormat")
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                    String gender_dob = "";
                                    if (jObj.getString(Constants.gender).isEmpty() &&
                                            jObj.getString(Constants.dob).isEmpty()) {
                                       // tvClassName.setVisibility(View.GONE);
                                        profile.gender = "";
                                        profile.date_of_birth = "";
                                    } else if (!jObj.getString(Constants.gender).isEmpty() &&
                                            !jObj.getString(Constants.dob).isEmpty()) {
                                        gender_dob = gender_dob +
                                                capitalizeFirstChar(jObj.getString(Constants.gender))/* + ", " +
                                                CommonUtils.calculateAge(sdf.parse(jObj.getString(Constants.dob))).getYears()*/;
                                        tvUserDOB.setText("DOB: " + jObj.getString(Constants.dob));
                                        profile.gender = jObj.getString(Constants.gender);
                                        profile.date_of_birth = jObj.getString(Constants.dob);
                                    } else if (!jObj.getString(Constants.gender).isEmpty()) {
                                        gender_dob = gender_dob + capitalizeFirstChar(jObj.getString(Constants.gender));
                                        profile.gender = jObj.getString(Constants.gender);
                                    } else if (!jObj.getString(Constants.dob).isEmpty()) {
                                        gender_dob = gender_dob /*+
                                                CommonUtils.calculateAge(sdf.parse(jObj.getString(Constants.dob))).getYears()*/;
                                        tvUserDOB.setText("DOB: " + jObj.getString(Constants.dob));
                                        profile.date_of_birth = jObj.getString(Constants.dob);
                                    }

                                   // tvClassName.setText(gender_dob);

                                    if (checkNotNull(jObj.getString(Constants.schoolName)).isEmpty()) {
                                        tvSchoolName.setHint("School name");
                                        profile.school_name = "";
                                    } else {
                                        tvSchoolName.setText("School name: " + jObj.getString(Constants.schoolName));
                                        profile.school_name = jObj.getString(Constants.schoolName);
                                    }

                                    if (checkNotNull(jObj.getString(Constants.rollNumber)).isEmpty()) {
                                        tvRollNo.setHint("Roll no");
                                        profile.roll_number = "";
                                    } else {
                                        tvRollNo.setText("Roll no: " + jObj.getString(Constants.rollNumber));
                                        profile.roll_number = jObj.getString(Constants.rollNumber);
                                    }

                                    if (checkNotNull(jObj.getString(Constants.sectionName)).isEmpty()) {
                                        tvSectionName.setHint("Section name");
                                        profile.section_name = "";
                                    } else {
                                        tvSectionName.setText("Section name: " + jObj.getString(Constants.sectionName));
                                        profile.section_name = jObj.getString(Constants.sectionName);
                                    }
                                    String s = "";
                                    if (checkNotNull(s = jObj.getString(Constants.school_address)).isEmpty()) {
                                        tv_schl_address.setHint("School Address");
                                        profile.school_address = "";

                                    } else {
                                        tv_schl_address.setText("Address: " + s);
                                        profile.school_address = s;
                                    }


                                    if (checkNotNull(s = jObj.getString(Constants.school_city)).isEmpty()) {
                                        tv_schl_city.setHint("School City");
                                        profile.school_city = "";
                                    } else {
                                        tv_schl_city.setText("City: " + s);
                                        profile.school_city = s;
                                    }


                                    if (checkNotNull(s = jObj.getString(Constants.school_state)).isEmpty()) {
                                        tv_schl_state.setHint("School State");
                                        profile.school_state = "";
                                    } else {
                                        tv_schl_state.setText("State: " + s);
                                        profile.school_state = s;
                                    }
                                    s = checkCountry(jObj.getString(Constants.school_country));
                                    if (checkNotNull(s).isEmpty()) {
                                        tv_schl_country.setHint("School Country");
                                        profile.school_country_code = "";
                                    } else {
                                        tv_schl_country.setText("Country: " + s);
                                        profile.school_country_code = jObj.getString(Constants.school_country);
                                    }

                                    if (checkNotNull(s = jObj.getString(Constants.school_zip)).isEmpty()) {
                                        tv_schl_zip.setHint("School Postal ");
                                        profile.school_postal_code = "";
                                    } else {
                                        tv_schl_zip.setText("Postal: " + s);
                                        profile.school_postal_code = s;
                                    }
                                    db.userDAO().updateProfile(profile);
                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(), errorMsg);
                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                                // Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                            } catch (Exception e) {
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
            }) {

            };
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {


            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
            // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
        }
    }

    private String capitalizeFirstChar(String input) {
        if (input.isEmpty())
            return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void editPersonalDetails(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (!AppStatus.getInstance(getApplication()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
            return;

        }
        Intent i = new Intent(_this, EditPersonalDetailsActivity.class);
        i.putExtra("user_current_class",user_current_class);
        startActivityForResult(i, 1);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void editSchoolDetails(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (!AppStatus.getInstance(getApplication()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
            return;

        }
        Intent i = new Intent(_this, EditSchoolDetailsActivity.class);
        startActivityForResult(i, 2);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void changePassword(View view) {
        AppController.getInstance().playAudio(R.raw.button_click);
        if (!AppStatus.getInstance(getApplication()).isOnline()) {
            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet));
            return;

        }
        Intent i = new Intent(_this, ChangePasswordActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    Uri mCropImageUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handle result of pick image chooser

        if (requestCode == 1 || requestCode == 2) {
            try {
                getProfileInformation();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return;
        }
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(ProfileActivity.this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(ProfileActivity.this, imageUri)) {

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
                InputStream iStream = null;


                try {
                    iStream = getContentResolver().openInputStream(result.getUri());
                    byte[] fileData = getBytes(iStream);
                    String encodedString = Base64.encodeToString(fileData, Base64.DEFAULT);
                    // bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());

                    //runTextRecognition();
                    uploadImage(encodedString);
                    cvLogo.setImageURI(result.getUri());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();

                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(ProfileActivity.this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
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
                .start(ProfileActivity.this);
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

    private void uploadImage(String encodedString) {
        if (AppStatus.getInstance(_this).isOnline()) {
            final JSONObject fjson = new JSONObject();

            try {
                fjson.put(Constants.accessToken, access_token);
                fjson.put(Constants.userId, user_id);
                fjson.put(Constants.IMG_FILED, encodedString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tag_string_req = Constants.reqRegister;
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);

            CustomDialog.showDialog(ProfileActivity.this, false);
            StringRequest strReq = new StringRequest(
                    Request.Method.POST,
                    Constants.UPLOAD_IMAGE,
                    new Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {
                            try {
                                CustomDialog.closeDialog();
                                JSONObject jObj = new JSONObject(response);


                                boolean error = jObj.getBoolean(Constants.errorFlag);
                                if (!error) {
                                    String photo = jObj.getString("image_name");
                                    MyDatabase db = MyDatabase.getDatabase(getApplicationContext());
                                    db.userDAO().updateProfilePhoto(user_id, photo);
                                    if (!(photo = checkNotNull(photo)).isEmpty()) {
                                       // setProfileLogo(photo);
                                        photo = checkNotNull(photo);
                                        if (!photo.isEmpty()) {
                                            photo = Constants.IMAGE_PATH + user_id + "/" + photo;
                                            Picasso.with(getApplicationContext()).load(photo).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(cvLogo);
                                        }
                                    }

                                } else {
                                    String errorMsg = jObj.getString(Constants.message);
                                    CommonUtils.showToast(getApplicationContext(), errorMsg);
                                    //Toasty.warning(_this, errorMsg, Toast.LENGTH_SHORT, true).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                CommonUtils.showToast(getApplicationContext(), getString(R.string.json_error) + e.getMessage());
                                // Toasty.warning(_this, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
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
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {


            CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
            // Toasty.info(_this, "There is no internet.", Toast.LENGTH_SHORT, true).show();
        }
    }

    public void goBack(View view) {
        AppController.getInstance().playAudio(R.raw.back_sound);
        onBackPressed();
    }

    /* private  String getStringPre(String s)
     {
        return pref.getString(s,"");
     }
 */
    private String checkNotNull(String s) {
        if (s == null || s.equals("null") || s.isEmpty())
            return "";
        else return s;
    }

    List<CountryModel> listCountryData = new ArrayList<>();
    List<Classes> listClassesData = new ArrayList<>();

    public void readCountry() {
        try {
            InputStream is = getAssets().open("CountryJson.json");
            StringBuffer s = new StringBuffer();
            byte[] b = new byte[is.available()];
            is.read(b);
            JSONArray array = new JSONArray(new String(b));
            CountryModel countryModel;
            JSONObject obj;
            for (int k = 0; k < array.length(); k++) {
                countryModel = new CountryModel();
                obj = array.getJSONObject(k);
                countryModel.name = obj.getString("name");
                countryModel.code = obj.getString("code");
                listCountryData.add(countryModel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String checkCountry(String code) {
        if (listCountryData == null || listCountryData.size() <= 0) {
            readCountry();
        }
        for (CountryModel c : listCountryData) {

            if (c.code.equals(code))
                return c.name;
        }
        return "";
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


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    String user_current_class="";
    private String checkClass(String code) {
        user_current_class=code;
        if (listClassesData == null || listClassesData.size() <= 0) {
            readClasses();
        }
        for (Classes c : listClassesData) {

            if (c.class_id.equals(code))
                return c.class_name;
        }
        return "";
    }

}
