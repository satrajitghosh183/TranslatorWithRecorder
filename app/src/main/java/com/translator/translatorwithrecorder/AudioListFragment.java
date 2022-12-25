package com.translator.translatorwithrecorder;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;


public class AudioListFragment extends Fragment implements AudioListAdapter.onItemListClick {

    private ConstraintLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    private RecyclerView audioList;
    private File[] allFiles;

    private AudioListAdapter audioListAdapter;
    private MediaPlayer mediaPlayer=null;
    private boolean isPlaying=false;
    private File fileToPlay;
   //UI Elements
   private ImageView playBtn;
   private TextView playerHeader;
   private TextView playerFileName;
   private SeekBar playerSeekBar;
   private Handler seekbarHandler;
   private Runnable updateSeekbar;


    public AudioListFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerSheet=view.findViewById(R.id.player_sheet);
        bottomSheetBehavior=BottomSheetBehavior.from(playerSheet);
        audioList=view.findViewById(R.id.audio_list_view);


        String path=getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory =new File(path);
        allFiles=directory.listFiles();
        audioListAdapter=new AudioListAdapter(allFiles,this);
        audioList.setHasFixedSize(true);
         playBtn=view.findViewById(R.id.imageView2);
         playerHeader=view.findViewById(R.id.player_header_title);
         playerFileName=view.findViewById(R.id.player_filename);

         playerSeekBar=view.findViewById(R.id.seekBar);



        audioList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioList.setAdapter(audioListAdapter);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState==BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    pauseAudio();
                }else{
                    if(fileToPlay!=null){
                        resumeAudio();
                    }

                }
            }
        });
        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(fileToPlay!=null) {
                    int progress = seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                    resumeAudio();
                }
                }
        });

    }

    @Override
    public void onClickListener(File file, int position) {
       Log.d("PLAY LOG","File Playing"+file.getName());
        fileToPlay=file;
       if(isPlaying){
           stopAudio();
           playAudio(fileToPlay);


       }else{

           playAudio(fileToPlay);

       }
    }
    private void resumeAudio(){
        mediaPlayer.start();
        playBtn.setImageDrawable(getActivity().getDrawable(R.drawable.pause_icon));
        isPlaying=true;
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar,0);

    }
    private void pauseAudio(){
       mediaPlayer.pause();
        playBtn.setImageDrawable(getActivity().getDrawable(R.drawable.play));
       isPlaying=false;
       seekbarHandler.removeCallbacks(updateSeekbar);
    }
    private void stopAudio() {

        playBtn.setImageDrawable(getActivity().getDrawable(R.drawable.play));
        playerHeader.setText("Stopped");
        isPlaying=false;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
        //Stop the audio
    }

    private void playAudio(File fileToPlay) {
        mediaPlayer=new MediaPlayer();
        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playBtn.setImageDrawable(getActivity().getDrawable(R.drawable.pause_icon));
        playerFileName.setText(fileToPlay.getName());
        playerHeader.setText("Playing");

        //To play the Audio


        isPlaying=true;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
                playerHeader.setText("Finished");

            }
        });
        playerSeekBar.setMax(mediaPlayer.getDuration());
        seekbarHandler=new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar,0);

    }

    private void updateRunnable() {
        updateSeekbar=new Runnable() {
            @Override
            public void run() {
                playerSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this,500);


            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
      if(isPlaying) {
          stopAudio();
      }
    }
}