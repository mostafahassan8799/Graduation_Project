package com.mostafahassan.graduationproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mostafahassan.graduationproject.databinding.FragmentHomeBinding;
import com.mostafahassan.graduationproject.ui.activities.BirthCertificateActivity;
import com.mostafahassan.graduationproject.ui.activities.IDCardActivity;
import com.mostafahassan.graduationproject.ui.activities.FamilyServiceActivity;
import com.mostafahassan.graduationproject.ui.activities.WeddingServiceActivity;

public class HomeFragment extends Fragment {
    FragmentHomeBinding fragmentHomeBinding;

    public HomeFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
    fragmentHomeBinding=FragmentHomeBinding.inflate(inflater,container, false);

        fragmentHomeBinding.cardViewIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), IDCardActivity.class));
            }
        });

        fragmentHomeBinding.cardViewBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BirthCertificateActivity.class));
            }
        });
        fragmentHomeBinding.cardViewPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FamilyServiceActivity.class));
            }
        });fragmentHomeBinding.cardViewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WeddingServiceActivity.class));
            }
        });
        return fragmentHomeBinding.getRoot();


    }
}