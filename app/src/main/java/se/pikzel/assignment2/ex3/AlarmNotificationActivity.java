package se.pikzel.assignment2.ex3;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import se.pikzel.assignment2.R;

public class AlarmNotificationActivity extends Activity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_notice);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.start();
    }

    public void onClick(View view) {
        mediaPlayer.stop();
        setResult(RESULT_OK, new Intent());
        finish();
    }
}
