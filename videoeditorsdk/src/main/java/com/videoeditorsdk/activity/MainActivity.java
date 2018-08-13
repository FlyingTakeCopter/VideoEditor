package com.videoeditorsdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.videoeditorsdk.R;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button recordBtn = (Button) findViewById(R.id.record_activity);
        Button selectBtn = (Button) findViewById(R.id.select_activity);
        Button audioBtn = (Button) findViewById(R.id.audio_activity);
        Button videoBtn = (Button) findViewById(R.id.video_connect);

//        recordBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);
        audioBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.record_activity) {
            startActivity(new Intent(MainActivity.this, RecordedActivity.class));

        } else if (i == R.id.select_activity) {
            VideoSelectActivity.openActivity(this);

        } else if (i == R.id.audio_activity) {
            startActivity(new Intent(MainActivity.this, AudioEditorActivity.class));

        } else if (i == R.id.video_connect) {//                Toast.makeText(this,"该功能还未完成！！！",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, VideoConnectActivity.class));

        }
    }
}
