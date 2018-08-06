package video.jd.jdvideoeditorlib;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.videoeditorsdk.activity.PreviewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        Button tv = (Button) findViewById(R.id.sample_text);
        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sample_text) {
            Intent intent=new Intent(MainActivity.this,PreviewActivity.class);
            intent.putExtra("path","/sdcard/ffmpegtest/1080.mp4");
            startActivity(intent);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
}
