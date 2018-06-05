package video.jd.jdvideoeditorlib;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import video.jd.Jni.FFmpegCmd;
import video.jd.Jni.VideoUitls;
import video.jd.editor.JVEditor;
import video.jd.editor.OnEditorListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("JDVideoEditor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        Button tv = (Button) findViewById(R.id.sample_text);
        tv.setOnClickListener(this);
    }

    final String path = Environment.getExternalStorageDirectory().getPath() + "/ffmpegtest/";
    String testVideo = "video1.mp4";

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sample_text) {
            // 转码另存
            String cmd = "-i " + path + testVideo + " -acodec copy -vcodec copy -f mp4 " + path + "test_out.mp4";
            // 创建文件拼接视频
            String cmd1 = "-f concat -i " + path + "filelist.txt -c copy " + path + "pj.mp4";
            // 添加水印
            String cmd2 = "-i " + path + testVideo +
                    " -vf \"movie=" +path+"water.jpg"+ " [watermark]; [in][watermark] overlay=10:10 [out]\" wm.mp4";
//            final String cmd = "-i " + path + testVideo + " " + path + "/test_out.flv";
            JVEditor.execCmd(cmd2, VideoUitls.getDuration(path + testVideo), new OnEditorListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure() {

                }

                @Override
                public void onProgress(float progress) {

                }
            });
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
}
