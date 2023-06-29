package com.tutorix.tutorialspoint.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tutorix.tutorialspoint.R;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ImagePreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Preview");
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(ImagePreviewActivity.this, R.drawable.top_main_background_small));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String uri = bundle.getString("uri");
            ImageView ivPreview = findViewById(R.id.ivPreview);
            Picasso.with(this).load(uri).placeholder(R.drawable.circle_default_load).into(ivPreview);
            ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener(ivPreview));
            ivPreview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mScaleGestureDetector.onTouchEvent(event);
                    return true;
                }
            });

        }


    }
    private float mScaleFactor = 1.0f;
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        ImageView ivPreview;
        public ScaleListener(ImageView ivPreview)
        {
            this.ivPreview=ivPreview;
        }
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            ivPreview.setScaleX(mScaleFactor);
            ivPreview.setScaleY(mScaleFactor);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
