package com.translator.translatorwithrecorder;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RecordFragment extends Fragment implements View.OnClickListener {
    private NavController navController;
    private ImageView record_list_btn;
    private ImageView record_btn;
    private TextView fileNameText;
    private boolean isRecording=false;
    private String recordPermission= Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE=2;
    private MediaRecorder mediaRecorder;
    private String recordFile;

    private Chronometer timer;
    StorageReference storageReference;
    private String recordPath;
    private ProgressDialog progressDialog;

    public RecordFragment()
    {

    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        record_list_btn=view.findViewById(R.id.record_list_button);
        record_btn=view.findViewById(R.id.record_btn);
        timer=view.findViewById(R.id.record_timer);
        fileNameText=view.findViewById(R.id.record_file_name);
        record_list_btn.setOnClickListener(this);
        record_btn.setOnClickListener(this);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_list_button:


                if(isRecording){
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(getContext());
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                            isRecording=false;

                        }
                    });
                    alertDialog.setNegativeButton("CANCEL", null);
                    alertDialog.setTitle("Audio Still Recording");
                    alertDialog.setMessage("Are you sure you want to stop recording");
                    alertDialog.create().show();
                }
                else{

                    navController.navigate(R.id.action_recordFragment_to_audioListFragment);

                }
                break;
            case R.id.record_btn:
                if(isRecording){
                    //Stop Recording
                    stopRecording();
                    record_btn.setImageDrawable(getResources().getDrawable(R.drawable.not_recording));
                    isRecording=false;
                }
                else {

                    //Start Recording
                    if (checkPermissions()) {
                        startRecording();
                        isRecording = true;
                        record_btn.setImageDrawable(getResources().getDrawable(R.drawable.icon_recording_2));

                    }
                }

                break;
        }

    }

    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        recordPath=getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();
        recordFile="Recording_"+formatter.format(now)+".3gp";
        fileNameText.setText("Recording,File Name "+recordFile);



        mediaRecorder= new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath+"/"+recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        }catch (IOException e){
            e.printStackTrace();

        }

        mediaRecorder.start();


    }

    private void stopRecording() {
        fileNameText.setText("Recording Stopped,File Saved "+recordFile);
        timer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;
        uploadAudio();

    }

    private void uploadAudio() {
        StorageReference filepath =storageReference.child("Audio").child(recordPath);
        Uri uri=Uri.fromFile(new File(recordPath));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }


    private boolean checkPermissions() {
       if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED)
       {return true;
    }else{
           ActivityCompat.requestPermissions(getActivity(),new String[]{recordPermission},PERMISSION_CODE);
           return false;

       }
}

    @Override
    public void onStop() {
        super.onStop();
        if(isRecording){
        stopRecording();
        }
    }
}