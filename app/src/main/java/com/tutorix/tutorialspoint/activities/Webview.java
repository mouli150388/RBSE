package com.tutorix.tutorialspoint.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tutorix.tutorialspoint.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Webview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WebView webview=findViewById(R.id.webview);
        webview.loadData("<p>Mirror image of &#39;SCIENCE&#39; is</p>" +
                "<ol>" +
                "<li>ƎƆИƎIƆƧ</li>" +
                "<li>ƎƆƎИIƆƧ</li>" +
                "<li>SCIИIƆƧ</li>" +
                "<li>ƧƆIƎИƆƎ</li>" +
                "</ol>" +
                "<p>Option 1</p>" +
                "<p>The Mirror image is the reflected image of an object that appears almost same but is reversed in the inverse direction to the surface of the mirror.</p>","text/html",null);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
