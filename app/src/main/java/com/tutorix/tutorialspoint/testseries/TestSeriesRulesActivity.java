package com.tutorix.tutorialspoint.testseries;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.views.CustomWebview;

import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestSeriesRulesActivity extends AppCompatActivity {
    @BindView(R.id.txt_header)
    TextView txt_header;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lnr_home)
    LinearLayout lnr_home;
    @BindView(R.id.webview)
    CustomWebview webview;
    @BindView(R.id.lnr_ruls_1)
    LinearLayout lnr_ruls_1;
    @BindView(R.id.lnr_ruls_2)
    LinearLayout lnr_ruls_2;
    @BindView(R.id.lnr_ruls_3)
    LinearLayout lnr_ruls_3;

    @BindView(R.id.btn_prev)
    Button btn_prev;
    @BindView(R.id.btn_next)
    Button btn_next;
    @BindView(R.id.img_background)
    ImageView img_background;
    @BindView(R.id.btn_start)
    Button btn_start;
    @BindView(R.id.img_gif)
    ImageView img_gif;
    int current=1;
    int currrentClsId;
    String test_series_type;
    String test_series_name;
    String test_series_file_name;

    String disclaimer;
    List<String> rules;
    /*int pysics_marks;
    int pysics_qtns;
    int chemistry_marks;
    int chemistry_qtns;
    int other_marks;
    int other_qtns;*/

    String marks="";
    @BindView(R.id.txt_r1)
    TextView txt_r1;
    @BindView(R.id.txt_r2)
    TextView txt_r2;
    @BindView(R.id.txt_r3)
    TextView txt_r3;
    @BindView(R.id.txt_r4)
    TextView txt_r4;

    TextView txt_rule_3_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_series_rules);
        ButterKnife.bind(this);

        String[]userInfo= SessionManager.getUserInfo(this);
        String access_token = userInfo[1];
        String userid = userInfo[0];;
        String loginType = userInfo[2];
        String class_id = userInfo[4];
        txt_rule_3_1=findViewById(R.id.txt_rule_3_1);
        try {
            toolbar.setTitle("");
            currrentClsId = Integer.parseInt(class_id);
            if (currrentClsId <=7) {

            } else if (currrentClsId ==8) {
                txt_header.setText("JEE Test Series");
                Glide.with(getApplicationContext()).load(R.drawable.iit_jee_test).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_gif);
            } else if (currrentClsId ==9){
                txt_header.setText("NEET Test Series");
                Glide.with(getApplicationContext()).load(R.drawable.neet_test).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_gif);

            }







        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent in=getIntent();
        if(in!=null)
        {
            test_series_type=in.getStringExtra("test_series_type");
            test_series_name=in.getStringExtra("test_series_name");
            test_series_file_name=in.getStringExtra("test_series_file_name");
            disclaimer=in.getStringExtra("disclaimer");
            marks=in.getStringExtra("marks");
           /* pysics_marks=in.getIntExtra("pysics_marks",0);
            pysics_qtns=in.getIntExtra("pysics_qtns",0);
            chemistry_marks=in.getIntExtra("chemistry_marks",0);
            chemistry_qtns=in.getIntExtra("chemistry_qtns",0);
            other_marks=in.getIntExtra("other_marks",0);
            other_qtns=in.getIntExtra("other_qtns",0);*/
            rules=in.getStringArrayListExtra("rules");

            txt_rule_3_1.setText(disclaimer);
            txt_r1.setText(rules.get(0));
            txt_r2.setText(rules.get(1));
            txt_r3.setText(rules.get(2));
            txt_r4.setText(rules.get(3));

        }
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        //Glide.with(getApplicationContext()).load(R.drawable.test_series).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_gif);

        if(current==1)
            btn_prev.setVisibility(View.INVISIBLE);







        loadWeb();
    }

    @OnClick({R.id.lnr_home, R.id.btn_prev, R.id.btn_next, R.id.btn_start})
    public void onViewClicked(View view) {

        switch (view.getId())
        {
            case R.id.btn_start:
                Intent in=new Intent(getApplicationContext(),TestQuizActivity.class);
                in.putExtra("test_series_file_name",test_series_file_name);
                in.putExtra("test_series_type",test_series_type);
                in.putExtra("test_series_name",test_series_name);
                startActivityForResult(in,200);
                break;
            case R.id.lnr_home:

                finish();
                break;
            case R.id.btn_prev:
                current=current-1;
               /* if(current==1)
                {*/
                    btn_next.setVisibility(View.VISIBLE);
                    btn_start.setVisibility(View.GONE);
               // }


                display();
                break;
            case R.id.btn_next:
                if(current==3)
                {
                    Intent inn=new Intent(getApplicationContext(),TestQuizActivity.class);
                    inn.putExtra("test_series_file_name",test_series_file_name);
                    inn.putExtra("test_series_type",test_series_type);
                    inn.putExtra("test_series_name",test_series_name);
                    startActivityForResult(inn,200);
                  return;
                }
                //btn_prev.setVisibility(View.VISIBLE);
                current=current+1;

                display();
                if(current==3)
                {
                    btn_next.setVisibility(View.INVISIBLE);
                    btn_start.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    void display()
    {
        if(current==3)
        {
            btn_next.setText("Start Test");
        }else
        {
            btn_next.setText(getString(R.string.next));
        }
        lnr_ruls_1.setVisibility(View.GONE);
        lnr_ruls_2.setVisibility(View.GONE);
        lnr_ruls_3.setVisibility(View.GONE);
        img_background.setVisibility(View.GONE);
        switch (current)
        {
            case 1:
                lnr_ruls_1.setVisibility(View.VISIBLE);
                img_background.setVisibility(View.GONE);
            break;
            case 2:
                lnr_ruls_2.setVisibility(View.VISIBLE);

                break;
            case 3:
                lnr_ruls_3.setVisibility(View.VISIBLE);

                break;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        if(current!=1)
        {
            btn_prev.performClick();

            if(current==3)
            {
                btn_next.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.GONE);
            }
            return false;
        }
        finish();
        return true;
    }

    private void loadWeb()
    {


        webview.getSettings().setJavaScriptEnabled(true);
        //webview.loadData(text, "text/html", "UTF_8");
        webview.loadDataWithBaseURL(null,"<html><body>"+marks+"</html></body>","text/html", "UTF-8",null);
       /* if(currrentClsId==9)
        webview.loadData(marks);
        else  if(currrentClsId==8)
            webview.loadUrl("file:///android_asset/table_neet.html");*/

        webview.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200)
        {
            btn_prev.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);
            current=1;
            display();
        }
    }
}
