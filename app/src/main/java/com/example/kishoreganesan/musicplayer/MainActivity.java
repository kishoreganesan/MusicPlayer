package com.example.kishoreganesan.musicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    public Button playBtn;
    public SeekBar positionBar, volumeBar;
    public TextView startTimeView, endTimeView;
    public MediaPlayer mediaPlayer;
    public int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (Button) findViewById(R.id.btn);
        startTimeView = (TextView) findViewById(R.id.textView);
        endTimeView = (TextView) findViewById(R.id.textView1);

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.hall_of_fame);
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(0.5f, 0.5f);
        totalTime = mediaPlayer.getDuration();

        positionBar = (SeekBar) findViewById(R.id.seekBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser){

                    mediaPlayer.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        volumeBar = (SeekBar) findViewById(R.id.seekBar1);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser){

                    float volume = progress / 100f;
                    mediaPlayer.setVolume(volume, volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (mediaPlayer != null){

                    try{

                        Message message = new Message();
                        message.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e){

                    }
                }
            }
        });
    }

    public Handler handler = new Handler(){

        @Override
        public void handleMessage(Message message) {

            int currentPosition = message.what;
            positionBar.setProgress(currentPosition);

            String startTime = createTimeLable(currentPosition);
            startTimeView.setText(startTime);

            String endTime = createTimeLable(totalTime - currentPosition);
            endTimeView.setText("- "+endTime);
        }
    };

    public String createTimeLable(int time){

        String calTime = "";
        int min = time/1000/60;
        int sec = time/1000%60;

        calTime = min+":";
        if (sec < 10)
            calTime += "0";
        calTime += sec;

        return calTime;
    }

    public void play(View view){

        if (!mediaPlayer.isPlaying()){

            mediaPlayer.start();
            playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);

        }
        else {

            mediaPlayer.pause();
            playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }
}
