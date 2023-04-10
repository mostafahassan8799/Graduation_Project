package com.mostafahassan.graduationproject.loginSignUp;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mostafahassan.graduationproject.R;

public class LoginSignUpActivity extends AppCompatActivity {

    LoginFragment loginFragment =new LoginFragment();
    SignUpFragment signUpFragment =new SignUpFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_id_container_login, loginFragment);
            fragmentTransaction.commit();
        }

}