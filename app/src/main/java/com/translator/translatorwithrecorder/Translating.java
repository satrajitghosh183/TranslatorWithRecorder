package com.translator.translatorwithrecorder;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;

public class Translating extends Fragment implements View.OnClickListener {

    private NavController navController;
    private ImageView Ai_button;
    private  ImageView Translate_button;
    private  ImageView upload_button;
    private  String Upload_Permission= Manifest.permission.READ_EXTERNAL_STORAGE;
    private int PERMISSION_CODE=1;
    StorageReference storageRef ;

    public Translating() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_translating, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        Ai_button= view.findViewById((R.id.imageView7));
        upload_button=view.findViewById(R.id.imageView5);

        upload_button.setOnClickListener(this);
        Ai_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageView7:
                navController.navigate(R.id.action_translating_to_audioListFragment);
                break;

            case R.id.imageView5:







        }

    }


}