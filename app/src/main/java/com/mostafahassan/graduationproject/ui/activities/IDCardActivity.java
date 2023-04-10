package com.mostafahassan.graduationproject.ui.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mostafahassan.graduationproject.R;
import com.mostafahassan.graduationproject.databinding.ActivityIDCardBinding;
import com.mostafahassan.graduationproject.ui.MyData.MyDataFragment;

import com.mostafahassan.graduationproject.IDFragments.IDService;

public class IDCardActivity extends AppCompatActivity {
        Button positive_custom , negative_custom;
        ActivityIDCardBinding activityIDCardBinding;
        AlertDialog dialog;
        //dexter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIDCardBinding = ActivityIDCardBinding.inflate(getLayoutInflater());
        setContentView(activityIDCardBinding.getRoot());
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customLayout);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
        positive_custom = customLayout.findViewById(R.id.bu_custom_dialog_yes);
        negative_custom = customLayout.findViewById(R.id.bu_custom_dialog_no);
        positive_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MyDataFragment myDataFragment =new MyDataFragment();
                fragmentTransaction.replace(R.id.fragment_id_container,myDataFragment);
                fragmentTransaction.commit();
                Toast.makeText(IDCardActivity.this, "برجاء تحديث بياناتك باللغة العربية...", Toast.LENGTH_LONG).show();
            }
        });
        negative_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                IDService idServiceFragment =new IDService();
                fragmentTransaction.replace(R.id.fragment_id_container,idServiceFragment);
                fragmentTransaction.commit();
                Toast.makeText(IDCardActivity.this, "برجاء كتابه بياناتك باللغة العربية...", Toast.LENGTH_LONG).show();

            }
        });




    }

}
