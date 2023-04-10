package com.mostafahassan.graduationproject.loginSignUp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;
import com.mostafahassan.graduationproject.ui.activities.MainActivity;
import com.mostafahassan.graduationproject.R;
import com.mostafahassan.graduationproject.utilities.DBHelper;
import com.mostafahassan.graduationproject.databinding.FragmentLoginBinding;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class LoginFragment extends Fragment {
    FragmentLoginBinding fragmentLoginBinding;
    TextInputLayout id , pass;
    EditText idEt , passEt;
    Button buttonLogin , buttonRegister;
    Connection con;
    Statement st;
    ResultSet resultSet;

    DBHelper dbHelper;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        idEt = fragmentLoginBinding.idEtLogin;
        passEt = fragmentLoginBinding.passEtLogin;
        id=fragmentLoginBinding.idLogin;
        pass=fragmentLoginBinding.passLogin;
        buttonLogin = fragmentLoginBinding.loginButtonLogin;
        buttonRegister = fragmentLoginBinding.registerButtonLogin;


        return fragmentLoginBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkConnection();
        Bundle bundle =new Bundle();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validID() | !validPassword()) {
                    return;
                }else {

                    loginButton();
                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveFromLoginToSignUp();

            }
        });

    }

    private boolean validID() {
        String IDNumber = id.getEditText().getText().toString().trim();
        if (IDNumber.isEmpty()) {
            idEt.setError("برجاء كتابه الرقم القومي");

            return false;
        } else if (IDNumber.length() < 14) {
            idEt.setError("الرجاء التأكد كتابه الرقم القومي بصوره صحيحه");
            return false;
        } else {
            idEt.setError(null);
            return true;
        }
    }

    private boolean validPassword() {
        String passwordInput = pass.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty())
        {
            pass.setError("الرجاء التحقق من كلمه السر");
            return false;
        }else
        {
            pass.setError(null);
            return true;
        }


    }

    private void checkConnection()
    {
        dbHelper = new DBHelper();
        con= dbHelper.connectDB();
        if (con != null)
        {
            // if you want check connection
           /* Toast.makeText(getActivity(), "true", Toast.LENGTH_SHORT).show();*/
        }
        else {
            Toast.makeText(getActivity(), "الرجاء التحقق من الانترنت", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().finish();
                    System.exit(0);

                }
            }, 3000);

        }
    }
    private void loginButton ()
    {
        dbHelper = new DBHelper();
        con = dbHelper.connectDB();
        if (con == null) {
            Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
        } else {
            String query = "select * from AspNetUsers where UserName = '" + idEt.getText().toString() + "' and PasswordHash = '" + passEt.getText().toString() + "'";
            ResultSet resultSet = dbHelper.login(query);
            try {
                if (resultSet.next()) {
                  Intent intent = new Intent(getActivity(), MainActivity.class);
                  intent.putExtra("idKey",idEt.getText().toString());
                  intent.putExtra("passKey",passEt.getText().toString());
                    SharedPreferences sharedPref = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("id", idEt.getText().toString());
                    editor.putString("pass", passEt.getText().toString());
                    editor.apply();
                    editor.commit();

                    Log.i("here", "onCreate: "+sharedPref.getString("id", null) +  "  "+sharedPref.getString("pass", null));

                    startActivity(intent);
                   getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "الرقم القومي غير صحيح او كلمه المرور غير صحيحه", Toast.LENGTH_LONG).show();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    private void moveFromLoginToSignUp()
    {
        SignUpFragment signUpFragment =new SignUpFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_id_container_login, signUpFragment);
        fragmentTransaction.commit();
    }


}