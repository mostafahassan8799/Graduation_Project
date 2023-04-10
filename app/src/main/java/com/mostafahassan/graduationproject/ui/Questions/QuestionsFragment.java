package com.mostafahassan.graduationproject.ui.Questions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mostafahassan.graduationproject.R;


public class QuestionsFragment extends Fragment {

    public QuestionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }
}