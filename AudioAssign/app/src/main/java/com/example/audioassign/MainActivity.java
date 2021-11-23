package com.example.audioassign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.example.audioassign.databinding.ActivityMainBinding;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;
    private static String audioFilePath;
    private boolean isRecording = false;
    private static final int RECORD_REQUEST_CODE = 101;
    private static final int STORAGE_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        audioSetup();

    }

    protected boolean hasMicrophone(){
        PackageManager packageManager = this.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);

    }

    private void audioSetup(){
        if(!hasMicrophone()){
            binding.stopPlayButton.setEnabled(false);
            binding.recordStopButton.setEnabled(false);
            binding.recordStartButton.setEnabled(false);
            binding.startPlayButton.setEnabled(false);
        }else{
            binding.startPlayButton.setEnabled(false);
            binding.stopPlayButton.setEnabled(false);
        }

        audioFilePath = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath()+"/myaudio.3gp";
        requestPermission(Manifest.permission.RECORD_AUDIO, RECORD_REQUEST_CODE);
    }

    public void startRecording(View view){
        isRecording = true;
        binding.stopPlayButton.setEnabled(false);
        binding.recordStopButton.setEnabled(true);
        binding.recordStartButton.setEnabled(false);
        binding.startPlayButton.setEnabled(false);
        try{
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
        }catch(Exception e){
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    public void stopRecording(View view){
        // isRecording = true;
        binding.stopPlayButton.setEnabled(false);
        binding.recordStopButton.setEnabled(false);
        binding.recordStartButton.setEnabled(false);
        binding.startPlayButton.setEnabled(true);

        if(isRecording){
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        }
        else{
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void stopPlay(View view){
        binding.stopPlayButton.setEnabled(false);
        binding.recordStopButton.setEnabled(false);
        binding.recordStartButton.setEnabled(true);
        binding.startPlayButton.setEnabled(true);

        if(isRecording){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        }
        else{
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void startPlay(View view) throws IOException {
        binding.stopPlayButton.setEnabled(true);
        binding.recordStopButton.setEnabled(false);
        binding.recordStartButton.setEnabled(false);
        binding.startPlayButton.setEnabled(false);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(audioFilePath);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    protected void requestPermission(String permissionType, int requestCode){
        int permission = ContextCompat.checkSelfPermission(this, permissionType);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{permissionType}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case RECORD_REQUEST_CODE:{
                if(grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    binding.recordStartButton.setEnabled(false);
                    Toast.makeText(this, "RECORD PERMISSION REQUIRED", Toast.LENGTH_LONG).show();
                }
                else{
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_REQUEST_CODE);
                }
            }

            case STORAGE_REQUEST_CODE:{
                if(grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "EXTERNAL STORAGE PERMISSION REQUIRED", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}