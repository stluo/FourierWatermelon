package com.example.steven.watermelon;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.List;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera;
    private TextureView mPreview;
    private MediaRecorder mMediaRecorder;
    private File mOutputFile;

    private boolean isRecording = false;
    private static final String TAG = "Recorder";
    private Button captureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        captureButton = (Button) findViewById(R.id.record);
        final TextView sweetness = findViewById(R.id.textNumber);

        final Button button = findViewById(R.id.record);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent browserIntent =
//                        new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com"));
//                startActivity(browserIntent);
                button.setVisibility(View.INVISIBLE);
                sweetness.setText("120%");
                sweetness.setVisibility(View.VISIBLE);
            }
        });
    }

    // Requesting permission to RECORD_AUDIO
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private MediaRecorder mRecorder = null;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};





}
