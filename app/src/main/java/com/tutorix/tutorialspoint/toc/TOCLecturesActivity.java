package com.tutorix.tutorialspoint.toc;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TOCLecturesActivity extends AppCompatActivity {

    String toc_json;
    @BindView(R.id.toc_recyclerview)
    RecyclerView toc_recyclerview;
    @BindView(R.id.img_back)
    View img_back;
    TOCLecturesAdapter tocAdapter;
    ArrayList<TOCLecture> tocLectureList;
    TOCSection tocSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tocactivity);
        ButterKnife.bind(this);
        toc_json = getIntent().getStringExtra("toc_json");
        getIntent().getParcelableArrayListExtra("lecture");

        Bundle args = getIntent().getBundleExtra("BUNDLE");
        tocSection = (TOCSection) args.getSerializable("ARRAYLIST");
        tocLectureList = (ArrayList<TOCLecture>) tocSection.getLectures();
        tocAdapter = new TOCLecturesAdapter(tocSection.getSectionId(), tocSection.getSectionName());

        toc_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        toc_recyclerview.setAdapter(tocAdapter);
        tocAdapter.setList(tocLectureList);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.back_sound);
                onBackPressed();
            }
        });


    }


}