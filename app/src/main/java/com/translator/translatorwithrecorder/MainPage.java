package com.translator.translatorwithrecorder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class MainPage extends Fragment implements View.OnClickListener {

    private NavController navController;
    private ImageView Record_btn;
    private ImageView Translate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        Record_btn = view.findViewById(R.id.record_button);
        Translate=view.findViewById(R.id.translate_button);
        Record_btn.setOnClickListener(this);
        Translate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_button:
                navController.navigate(R.id.action_mainPage_to_recordFragment);
                break;
            case R.id.translate_button:
                navController.navigate(R.id.action_mainPage_to_translating);
                break;

        }


        }
    }


