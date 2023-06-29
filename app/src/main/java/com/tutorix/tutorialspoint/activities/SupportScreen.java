package com.tutorix.tutorialspoint.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;

import java.util.Objects;

public class SupportScreen extends AppCompatActivity {

//    View lnr_whatsapp;
    View lnr_call_support;
//    View lnr_support;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_support_screen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.get_in_touch);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_main_background_small));

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);
        setContentView(R.layout.activity_support_screen);
//        lnr_whatsapp=findViewById(R.id.lnr_whatsapp);
        lnr_call_support=findViewById(R.id.lnr_call_support);
//        lnr_support=findViewById(R.id.lnr_support);

//        lnr_whatsapp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PackageManager pm=getPackageManager();
//
//                try {
//                    Uri uri = Uri.parse("smsto:" + "8886018182");
//                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
//                    i.setPackage("com.whatsapp");
//                    startActivity(Intent.createChooser(i, ""));
//                }catch (Exception e)
//                {
//
//                }
//
//                   /*
//                    Intent waIntent = new Intent(Intent.ACTION_SEND);
//                    waIntent.setType("text/plain");
//                    String text = "YOUR TEXT HERE";
//                   PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//                    //Check if package exists or not. If not then code
//                    //in catch block will be called
//                    waIntent.setPackage("com.whatsapp");
//
//                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
//                    startActivity(Intent.createChooser(waIntent, "Share with"));*/
//
//            }
//        });

        lnr_call_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "18008336464", null));
                startActivity(intent);
                /*Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "18008336464"));
                startActivity(intent);*/
            }
        });
//        lnr_support.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
//                    ZohoSalesIQ.Chat.show();
//
//                } else {
//                    CommonUtils.showToast(getApplicationContext(), getString(R.string.no_internet_message));
//                    //Toasty.info(_this, getString(R.string.no_internet_message), Toast.LENGTH_SHORT, true).show();
//                }
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        AppController.getInstance().playAudio(R.raw.back_sound);
        onBackPressed();
        return false;
    }
}