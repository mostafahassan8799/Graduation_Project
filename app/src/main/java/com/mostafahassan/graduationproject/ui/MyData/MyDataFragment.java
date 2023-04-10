package com.mostafahassan.graduationproject.ui.MyData;

import android.content.Context;
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

import com.mostafahassan.graduationproject.utilities.LoadingDialog;
import com.mostafahassan.graduationproject.utilities.DBHelper;
import com.mostafahassan.graduationproject.utilities.Validation;
import com.mostafahassan.graduationproject.databinding.FragmentMyDataBinding;

import java.sql.Connection;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MyDataFragment extends Fragment {

    FragmentMyDataBinding fragmentMyDataBinding;
    EditText id, fname, lname, phone, email, pass;
    Button saveButton;
    Validation validation;
    SharedPreferences sharedPreferences;
    Connection con;
    DBHelper dbHelper;
    String idData;
    LoadingDialog loadingDialog;

    public MyDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMyDataBinding = FragmentMyDataBinding.inflate(inflater, container, false);
        id = fragmentMyDataBinding.idEtMydata;
        fname = fragmentMyDataBinding.fnameEtMydata;
        lname = fragmentMyDataBinding.lnameEtSignUp;
        phone = fragmentMyDataBinding.phoneEtMydata;
        email = fragmentMyDataBinding.emailEtMydata;
        pass = fragmentMyDataBinding.passEtMydata;
        saveButton =fragmentMyDataBinding.saveButtonMydata;
        loadingDialog = new LoadingDialog(getActivity());



        return fragmentMyDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkConnection();
        validation = new Validation() {
            @Override
            public boolean validID() {
             return true;
            }

            @Override
            public boolean validPhone() {
                String phoneNumber = fragmentMyDataBinding.phoneMydata.getEditText().getText().toString().trim();
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
                String passwordInput = fragmentMyDataBinding.passMydata.getEditText().getText().toString().trim();

                if (passwordInput.isEmpty())
                {
                    fragmentMyDataBinding.passMydata.setError("الرجاء كتابه كلمه السر");
                    return false;
                }
                else
                {
                    fragmentMyDataBinding.passMydata.setError(null);
                    return true;
                }
            }

            @Override
            public boolean validName() {
                String sFName = fragmentMyDataBinding.fnameMydata.getEditText().getText().toString().trim();
                String sLName = fragmentMyDataBinding.lnameMydata.getEditText().getText().toString().trim();
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
        Observable<String> todoObservable = Observable.create(emitter -> {

            sharedPreferences =getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
            idData = sharedPreferences.getString("id",null);
            emitter.onNext(idData);
        });
        todoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        Log.i("here", "accept: "+s);
                        id.setText(s);
                        id.setEnabled(false);

                    }
                },e -> Log.i("hererxjava", "onViewCreated: "+e.getMessage()));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  //
                if (!validation.validID() | !validation.validPassword() | !validation.validName() | !validation.validEmail() | !validation.validPhone() | !validation.validAddress()) {
                    return;
                }else {
                loadingDialog.startLoadingDialog();
                updateProfile();
                clearFields();
            }
            }
        });

    }

    private void clearFields() {
        fname.setText("");
        lname.setText("");
        phone.setText("");
        pass.setText("");
        email.setText("");
    }

    private void updateProfile() {
        Observable<String> todoObservable = Observable.create(emitter -> {


            String message =  dbHelper.updateProfile(idData,fname.getText().toString(),lname.getText().toString()
                    ,phone.getText().toString(),email.getText().toString(),pass.getText().toString());
            emitter.onNext(message);

        });
        todoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        loadingDialog.dismissDialog();

                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                    }
                },e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show());
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


}