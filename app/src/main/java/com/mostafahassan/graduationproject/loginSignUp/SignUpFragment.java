package com.mostafahassan.graduationproject.loginSignUp;

import android.os.Bundle;
import android.os.Handler;
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
import com.mostafahassan.graduationproject.R;
import com.mostafahassan.graduationproject.utilities.DBHelper;
import com.mostafahassan.graduationproject.utilities.Validation;
import com.mostafahassan.graduationproject.databinding.FragmentSignUpBinding;

import java.sql.Connection;


public class SignUpFragment extends Fragment {
    FragmentSignUpBinding fragmentSignUpBinding;
    EditText id,fname,lname,phone,email,pass,confirmPassword;
    TextInputLayout fnameLayout,lnameLayout,emailLayout,phoneLayout,idLayout,passLayout,confirmPassLayout;
    Button registerButton , haveAccountButton;
    Validation validation;
    DBHelper dbHelper;
    Connection con;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false);
        id=fragmentSignUpBinding.idEtSignUp;
        fname=fragmentSignUpBinding.fnameEtSignUp;
        lname=fragmentSignUpBinding.lnameEtSignUp;
        email=fragmentSignUpBinding.emailEtSignUp;
        phone=fragmentSignUpBinding.phoneEtSignUp;
        pass=fragmentSignUpBinding.passEtSignUp;
        confirmPassword=fragmentSignUpBinding.confirmPassEtSignUp;
        registerButton=fragmentSignUpBinding.registerButtonSignUp;
        haveAccountButton=fragmentSignUpBinding.haveAccountButtonSignUp;
        fnameLayout=fragmentSignUpBinding.fnameSignUp;
        lnameLayout=fragmentSignUpBinding.lnameSignUp;
        emailLayout=fragmentSignUpBinding.emailSignUp;
        idLayout=fragmentSignUpBinding.idSignUp;
        passLayout=fragmentSignUpBinding.passSignUp;
        confirmPassLayout=fragmentSignUpBinding.confirmPassSignUp;
        phoneLayout=fragmentSignUpBinding.phoneSignUp;


        return fragmentSignUpBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkConnection();
        validation = new Validation() {
            @Override
            public boolean validID() {
                String IDNumber = idLayout.getEditText().getText().toString().trim();
                if (IDNumber.isEmpty()) {
                    id.setError("برجاء كتابه الرقم القومي");
                    return false;
                } else if (IDNumber.length() < 14) {
                    id.setError("الرجاء التأكد من كتابه الرقم القومي بصوره صحيحه");
                    return false;
                } else {
                    id.setError(null);
                    return true;
                }
            }

            @Override
            public boolean validPhone() {
                String phoneNumber = phoneLayout.getEditText().getText().toString().trim();
                if (phoneNumber.isEmpty()) {
                    phone.setError("برجاء كتابه رقم المحمول");

                    return false;
                } else if (phoneNumber.length() < 11) {
                    phone.setError("الرجاء التأكد من كتابه رقم المحمول بصوره صحيحه");
                    return false;
                } else {
                    phone.setError(null);
                    return true;
                }
            }

            @Override
            public boolean validPassword() {
                String passwordInput = passLayout.getEditText().getText().toString().trim();
                String confirmPasswordInput = confirmPassLayout.getEditText().getText().toString().trim();
                if (passwordInput.isEmpty() || confirmPasswordInput.isEmpty()) {
                    passLayout.setError("الرجاء كتابه كلمه السر");
                    return false;
                } else if (!(pass.getText().toString().equals(confirmPassword.getText().toString()))) {
                    confirmPassLayout.setError("كلمه السر غير متطابقه");
                    return false;
                } else {
                    passLayout.setError(null);
                    return true;
                }
            }

            @Override
            public boolean validName() {
                String sFName = fnameLayout.getEditText().getText().toString().trim();
                String sLName = lnameLayout.getEditText().getText().toString().trim();
                if (sFName.isEmpty()) {
                    fname.setError("برجاء كتابه الأسم الأول");
                    return false;
                } else if (sLName.isEmpty()) {
                    lname.setError("برجاء كتابه الأسم الأخير");
                    return false;
                } else {
                    fname.setError(null);
                    lname.setError(null);
                    return true;
                }
            }

            @Override
            public boolean validEmail() {
                String sEmail = email.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (sEmail.isEmpty()) {
                    email.setError("برجاء كتابه البريد الالكتروني");
                    return false;
                } else if (!sEmail.matches(emailPattern)) {
                    email.setError("برجاء كتابه البريد الالكتروني بصوره صحيحه");
                    return false;
                } else {
                    email.setError(null);
                    return true;
                }
            }

            @Override
            public boolean validAddress() {
                return true;
            }
        };

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validation.validID() | !validation.validPassword() | !validation.validName() | !validation.validEmail() | !validation.validPhone()) {
                    return;
                } else {
                    String message = dbHelper.register(id.getText().toString(), fname.getText().toString(), lname.getText().toString(),
                            email.getText().toString(), pass.getText().toString(), phone.getText().toString());
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    moveFromSignUpToLogin();
                }
            }
            });
        haveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveFromSignUpToLogin();
            }
        });

    }

    private void checkConnection() {
        dbHelper = new DBHelper();
        con = dbHelper.connectDB();
        if (con != null) {


        } else {
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
    /*private boolean validName() {
        String sFName = fnameLayout.getEditText().getText().toString().trim();
        String sLName = lnameLayout.getEditText().getText().toString().trim();
        if (sFName.isEmpty()) {
            fname.setError("برجاء كتابه الأسم الأول");
            return false;
        } else if (sLName.isEmpty()) {
            lname.setError("برجاء كتابه الأسم الأخير");
            return false;
        } else {
            fname.setError(null);
            lname.setError(null);
            return true;
        }
    }
    private boolean validEmail()
    {
        String sEmail = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (sEmail.isEmpty())
        {
            email.setError("برجاء كتابه البريد الالكتروني");
            return false;
        }
        else if (!sEmail.matches(emailPattern))
        {
            email.setError("برجاء كتابه البريد الالكتروني بصوره صحيحه");
            return false;
        }
        else {
            email.setError(null);
            return true;
        }
    }
    private boolean validID() {
        String IDNumber = idLayout.getEditText().getText().toString().trim();
        if (IDNumber.isEmpty()) {
            id.setError("برجاء كتابه الرقم القومي");

            return false;
        } else if (IDNumber.length() < 14) {
            id.setError("الرجاء التأكد من كتابه الرقم القومي بصوره صحيحه");
            return false;
        } else {
            id.setError(null);
            return true;
        }
    }

    private boolean validPhone() {
        String phoneNumber = phoneLayout.getEditText().getText().toString().trim();
        if (phoneNumber.isEmpty()) {
            phone.setError("برجاء كتابه رقم المحمول");

            return false;
        } else if (phoneNumber.length() < 11) {
            phone.setError("الرجاء التأكد من كتابه رقم المحمول بصوره صحيحه");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }
    private boolean validPassword() {
        String passwordInput = passLayout.getEditText().getText().toString().trim();
        String confirmPasswordInput = confirmPassLayout.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()||confirmPasswordInput.isEmpty())
        {
            passLayout.setError("الرجاء كتابه كلمه السر");
            return false;
        }
        else  if ( !(pass.getText().toString().equals(confirmPassword.getText().toString())))
        {
            confirmPassLayout.setError("كلمه السر غير متطابقه");
            return false;
        }
        else
        {
            passLayout.setError(null);
            return true;
        }


    }*/
    private void moveFromSignUpToLogin()
    {
        LoginFragment loginFragment =new LoginFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_id_container_login, loginFragment);
        fragmentTransaction.commit();
    }
}