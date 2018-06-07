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
    long duration = VideoUitls.getDuration(path + testVideo);

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sample_text) {
            String res;
            // 转码另存
            String cmd = "-i " + path + testVideo + " -acodec copy -vcodec copy -f mp4 " + path + "test_out.mp4";
            // 创建文件拼接视频
            String cmd1 = "-f concat -i " + path + "filelist.txt -c copy " + path + "pj.mp4";
            // 添加水印[watermark] 用不了 使用-filter_complex
//            String cmd2 = "-i " + path + testVideo +
//                    " -vf \"movie=" +path+"water.jpg"+ " [watermark]; [in][watermark] overlay=10:10 [out]\" wm.mp4";
            String cmd3 = "-i %s -i %s -filter_complex overlay=0:0 %s";
            res =  String.format(cmd3, path + testVideo, path+"water.jpg", path + "wm.mp4");
            // 截取GIF
            String screenShotCmd = "-i %s -ss %d -t %d -s 320x240 -f gif %s";
            res = String.format(screenShotCmd, path + testVideo, 0, 4 , path + "test.gif");
            // 视频截取
            String cutVideoCmd = "-i %s -ss %d -t %d %s";
            res = String.format(cutVideoCmd, path + testVideo, 0, 4 , path + "cut.mp4");
            // 去除声音
            String removeAudio = "-i %s -vcodec copy -an %s";
            res = String.format(removeAudio, path + testVideo, path + "noAudio.mp4");
            // 音频合成（原视频必须没有音频）
            String addAudio = "-i %s -i %s %s";
            res = String.format(addAudio, path + testVideo, path+"audio.mp3", path + "addAudio.mp4");
            JVEditor.execCmd(res, VideoUitls.getDuration(path + testVideo), new OnEditorListener() {
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
