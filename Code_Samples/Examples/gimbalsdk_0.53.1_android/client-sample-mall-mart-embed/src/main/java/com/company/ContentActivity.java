package com.innominds.company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ContentActivity extends Activity {

    private static final String TAG = ContentActivity.class.getSimpleName();
    public static final String CONTENT_KEY = "com.company.CONTENT_URL";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        textView = (TextView) findViewById(R.id.textView);
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(onClickGoBack());
        Button notNowButton = (Button) findViewById(R.id.not_now_button);
        notNowButton.setOnClickListener(onClickGoBack());
    }

    private OnClickListener onClickGoBack() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String content = intent.getStringExtra(CONTENT_KEY);
        textView.setText(content);
    }

}
