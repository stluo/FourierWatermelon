package com.example.steven.watermelon;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.io.IOException;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    short[] audioData;

    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    Complex[] fftTempArray;
    Complex[] fftArray;
    int[] bufferData;
    int bytesRecorded;
    int[] result;

    double[] absNormalizedSignal;
    public KNN knn;

    private static final int ALL= 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //context = (Context)this;


        setButtonHandlers();

        bufferSize = AudioRecord.getMinBufferSize
                (RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING)*3;

        audioData = new short [bufferSize]; //short array that pcm data is put into.

        this.requestPermissions(permissions,ALL);

        List<List<Double>> list = new ArrayList<>();
        List<Double> temp = new ArrayList<>();
        for(int i = 0; i < 512; i++) {
            temp.add((double)0);
        }
        list.add(temp);
        list.add(temp);
        list.add(temp);
        List<Integer> sweetlist = new ArrayList<>();
        sweetlist.add(0);
        sweetlist.add(0);
        sweetlist.add(0);
        List<Integer> waterlist = new ArrayList<>();
        waterlist.add(0);
        waterlist.add(0);
        waterlist.add(0);

        knn = new KNN(list, list, sweetlist, waterlist);



        normal();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;


    }


    Button correct;
    Button incorrect;

    Button record;
    View popup;

    View sweetbubble;
    View waterbubble;

    TextView sweettext;
    TextView sweetlable;
    TextView watertext;
    TextView waterlable;



    private void setButtonHandlers() {
        popup = findViewById(R.id.popup);

        record = findViewById(R.id.record);
        ((Button)findViewById(R.id.record)).setOnTouchListener(btnTouch);
        correct = findViewById(R.id.correct);
        ((Button)findViewById(R.id.correct)).setOnClickListener(btnClick1);
        incorrect = findViewById(R.id.incorrect);
        ((Button)findViewById(R.id.incorrect)).setOnClickListener(btnClick2);

        sweetbubble = findViewById(R.id.displaysweetness);
        waterbubble = findViewById(R.id.displaywater);

        sweettext = findViewById(R.id.sweetnumber);
        sweetlable = findViewById(R.id.sweetlable);
        watertext = findViewById(R.id.waternumber);
        waterlable = findViewById(R.id.waterlable);



    }

    int show = 0;

    private View.OnClickListener btnClick1 = new View.OnClickListener() {
        public void onClick(View view) {



            if (show == 0) {
                knn.update_sweet(absNormalizedSignal, result[0]+5);
                show++;
            }
            else if (show == 1) {
                knn.update_water(absNormalizedSignal, result[1]+5);
                show++;
            }


            if (show == 2) {
                show = 0;
                normal();
            }


            return;
        }
    };

    private View.OnClickListener btnClick2 = new View.OnClickListener() {
        public void onClick(View view) {

            show++;
            if (show ==  2) {
                normal();
                //save();
                show = 0;
            }



            return;
        }
    };

    private void normal() {
        popup.setVisibility(View.VISIBLE);
        record.setVisibility(View.VISIBLE);
        correct.setVisibility(View.INVISIBLE);
        incorrect.setVisibility(View.INVISIBLE);
        sweetbubble.setVisibility(View.INVISIBLE);
        waterbubble.setVisibility(View.INVISIBLE);
        sweettext.setVisibility(View.INVISIBLE);
        sweetlable.setVisibility(View.INVISIBLE);
        watertext.setVisibility(View.INVISIBLE);
        waterlable.setVisibility(View.INVISIBLE);
    }

    private void show(int sweet, int water) {
        popup.setVisibility(View.INVISIBLE);
        record.setVisibility(View.INVISIBLE);
        correct.setVisibility(View.VISIBLE);
        incorrect.setVisibility(View.VISIBLE);
        sweetbubble.setVisibility(View.VISIBLE);
        waterbubble.setVisibility(View.VISIBLE);
        waterlable.setVisibility(View.VISIBLE);
        sweetlable.setVisibility(View.VISIBLE);

        String str = String.valueOf(sweet) + "%";
        sweettext.setText(str);
        String str2 = String.valueOf(water) + "%";
        watertext.setText(str2);


        sweettext.setVisibility(View.VISIBLE);
        //sweettext.bringToFront();
        watertext.setVisibility(View.VISIBLE);
        //watertext.bringToFront();
    }

    int array[];
    int array2[];
    int x = 0;

    private View.OnTouchListener btnTouch = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch ( motionEvent.getAction() ) {
                case MotionEvent.ACTION_DOWN: {
                    startRecording();

                    break;
                }

                case MotionEvent.ACTION_UP: {
                    stopRecording();


                    result = knn.KNN(absNormalizedSignal);
//                    sweettext.setText(String.valueOf(result[0]));
//                    sweettext.setVisibility(View.VISIBLE);

                    show(result[0],result[1]);
//                      


                    break;
                }

            }
            return true;
        }
    };

    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV);
    }


    public double[] calculateFFT(byte[] signal)
    {
        final int mNumberOfFFTPoints =1024;
        double mMaxFFTSample;
        double mPeakPos;

        double temp;
        Complex[] y;
        Complex[] complexSignal = new Complex[mNumberOfFFTPoints];
        double[] absSignal = new double[mNumberOfFFTPoints/2];

        for(int i = 0; i < mNumberOfFFTPoints; i++){
            temp = (double)((signal[2*i] & 0xFF) | (signal[2*i+1] << 8)) / 32768.0F;
            complexSignal[i] = new Complex(temp,0.0);
        }

        y = FFT.fft(complexSignal); // --> Here I use FFT class

        mMaxFFTSample = 0.0;
        mPeakPos = 0;
        for(int i = 0; i < (mNumberOfFFTPoints/2); i++)
        {
            absSignal[i] = Math.sqrt(Math.pow(y[i].re(), 2) + Math.pow(y[i].im(), 2));
            if(absSignal[i] > mMaxFFTSample)
            {
                mMaxFFTSample = absSignal[i];
                mPeakPos = i;
            }
        }

        return absSignal;
    }


    private String getTempFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        File tempFile = new File(filepath,AUDIO_RECORDER_TEMP_FILE);

        if(tempFile.exists())
            tempFile.delete();

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    private void startRecording(){
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);

        recorder.startRecording();

        isRecording = true;

        recordingThread = new Thread(new Runnable() {

            public void run() {
                writeAudioDataToFile();
            }
        },"AudioRecorder Thread");

        recordingThread.start();
    }

    private void writeAudioDataToFile(){

        byte data[] = new byte[bufferSize];
        String filename = getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int read = 0;
        if(null != os){
            while(isRecording){
                read = recorder.read(data, 0, bufferSize);
                if(read > 0){
                    absNormalizedSignal = calculateFFT(data);



                }

                if(AudioRecord.ERROR_INVALID_OPERATION != read){
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecording(){
        if(null != recorder){
            isRecording = false;

            recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }

        copyWaveFile(getTempFilename(),getFilename());
        // deleteTempFile();
    }

    private void deleteTempFile() {
        File file = new File(getTempFilename());
        file.delete();
    }

    private void copyWaveFile(String inFilename,String outFilename){
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 2;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            //AppLog.logString("File size: " + totalDataLen);

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while(in.read(data) != -1){
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {
        //another code

    }




    String serializedObject = "";

    public void save() {

        List<List<Double>> data = knn.getData();
        List<List<Double>> data2 = knn.getData2();

        // serialize the object
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(data);
            so.flush();
            serializedObject = bo.toString();



            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(serializedObject);
                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void unsave() {
        serializedObject = readFromFile(context);

        // deserialize the object
        try {
            byte b[] = serializedObject.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            List<List<Double>> obj = (List<List<Double>>)si.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }



}
