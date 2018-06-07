package video.jd.jdvideoeditorlib;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import video.jd.Jni.VideoUitls;
import video.jd.editor.JVEditor;
import video.jd.editor.OnEditorListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext = this;
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
    final String testVideo = path + "video1.mp4";
    long duration = VideoUitls.getDuration(path + testVideo);

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sample_text) {
            String res;
            // 转码另存
            String cmd = "-i " + testVideo + " -acodec copy -vcodec copy -f mp4 " + path + "test_out.mp4";
            // 创建文件拼接视频
            String cmd1 = "-f concat -i " + path + "filelist.txt -c copy " + path + "拼接视频.mp4";
            // 添加水印[watermark] 用不了 使用-filter_complex
            String cmd3 = "-i %s -i %s -filter_complex overlay=0:0 %s";
            res =  String.format(cmd3, testVideo, path+"water.jpg", path + "添加水印.mp4");
            // 截取GIF
            String screenShotCmd = "-i %s -ss %d -t %d -s 320x240 -f gif %s";
            res = String.format(screenShotCmd, testVideo, 0, 4 , path + "视频转GIF.gif");
            // 视频截取
            String cutVideoCmd = "-i %s -ss %d -t %d %s";
            res = String.format(cutVideoCmd, testVideo, 0, 4 , path + "视频截取.mp4");
            // 去除声音
            String removeAudio = "-i %s -vcodec copy -an %s";
            res = String.format(removeAudio, testVideo, path + "去除声音.mp4");
            // 音频合成（原视频必须没有音频）
            String addAudio = "-i %s -i %s %s";
            res = String.format(addAudio, path + "noAudio.mp4", path+"audio.mp3", path + "音频添加(原视频没声音).mp4");
            // 音视频混合(原视频有声音)
            String mixAV = "-i %s -i %s -c:v copy -map 0:v:0 -filter_complex [0:a][1:a]amerge=inputs=2[aout] -map [aout] -ac 2 -y %s";
            res = String.format(mixAV, testVideo, path+"audio.mp3", path + "音频混合(在原视频基础上).mp4");
            // 转场黑屏(设置COLOR视频变黑)
            String blackEnd = "-i %s -filter_complex fade=t=in:st=0:d=3,fade=t=out:st=6:d=3 -y %s";
            res = String.format(blackEnd, testVideo, path + "转场黑.mp4");
            // 动图淡入淡出
            String gif_in_out = "-i %s -i %s -filter_complex [1:v]fade=t=in:st=0:d=1,fade=t=out:st=6:d=0.1[v1];[0:v][v1]overlay=x=0:y=0 %s";
            res = String.format(gif_in_out, testVideo, path + "timg.gif", path + "动图淡入淡出.mp4");
            // 调整冷暖色(失败)
            String s1 = "-i %s -filter_complex geq=r='X/W*r(X,Y)':g='(1-X/W)*g(X,Y)':b='(H-Y)/H*b(X,Y)' %s";
            res = String.format(s1, testVideo, path + "geq1.mp4");
            // 画中画效果
            String viv = "-i %s -i %s -filter_complex [1:v]scale=w=800:h=600:force_original_aspect_ratio=decrease[ckout];[0:v][ckout]overlay=x=W/2-w/2:y=H/2-h/2[out] -map [out] -movflags faststart %s";
            res = String.format(viv, testVideo, testVideo, path + "画中画.mp4");
            // 周围模糊效果
            String mh = "-i %s -filter_complex [0:v]avgblur=sizeX=64:sizeY=0[out1];[0:v]scale=800x600[out2];[out1][out2]overlay=x=50:y=50[outo] -map [outo] -y %s";
            res = String.format(mh, testVideo, path + "四周模糊.mp4");
            // 四格播放
            String fv = "-i %s -i %s -i %s -i %s -filter_complex nullsrc=size=480x640[base];[0:v]setpts=PTS-STARTPTS,scale=240x320[upperleft];[1:v]setpts=PTS-STARTPTS,scale=240x320[upperright];[2:v]setpts=PTS-STARTPTS,scale=240x320[lowerleft];[3:v]setpts=PTS-STARTPTS,scale=240x320[lowerright];[base][upperleft]overlay=shortest=1[tmp1];[tmp1][upperright]overlay=shortest=1:x=240[tmp2];[tmp2][lowerleft]overlay=shortest=1:y=320[tmp3];[tmp3][lowerright]overlay=shortest=1:x=240:y=320 -c:v libx264 %s";
            res = String.format(fv, testVideo, testVideo, testVideo, testVideo, path + "四格播放.mp4");
            // 视频滑入
            String hr = "-i %s -i %s -filter_complex [0:v][1:v]overlay=x='if(gte(t,1),max(0,w-(t-1)*480),NAN)':y=0 -y %s";
            res = String.format(hr, testVideo, testVideo, path + "视频滑入.mp4");


            JVEditor.execCmd(res, VideoUitls.getDuration(testVideo), new OnEditorListener() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext.getApplicationContext(), "OK了", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext.getApplicationContext(), "出错了", Toast.LENGTH_SHORT).show();
                        }
                    });
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
