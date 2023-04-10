package com.mostafahassan.graduationproject.ui.ContactUsFragment;

import android.annotation.SuppressLint;
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

import com.mostafahassan.graduationproject.utilities.LoadingDialog;
import com.mostafahassan.graduationproject.utilities.DBHelper;
import com.mostafahassan.graduationproject.databinding.FragmentContactUsBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ContactUsFragment extends Fragment {
    FragmentContactUsBinding fragmentContactUsBinding;
    //Spinner serviceSpinner;
    EditText fullName, phoneNumber, email,notes;
    Button sendButton;
    String valueOfServiceType;
    PowerSpinnerView powerSpinnerView;
    DBHelper dbHelper;
    Connection con;
    LoadingDialog loadingDialog;


    public ContactUsFragment() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentContactUsBinding = FragmentContactUsBinding.inflate(inflater, container, false);
        fullName=fragmentContactUsBinding.fullnameEtContactUs;
        phoneNumber=fragmentContactUsBinding.phoneEtContactUs;
        powerSpinnerView=fragmentContactUsBinding.spinnerContactUsServiceServiceType;
        email=fragmentContactUsBinding.emailEtContactUs;
        notes=fragmentContactUsBinding.etEtContactUsNotes;
        sendButton=fragmentContactUsBinding.sendButtonContactUs;
        loadingDialog=new LoadingDialog(getActivity());
        String[] serviceTypes = {"بطاقه الرقم القومي", "شهادة الميلاد","وثيقه زواج","قيد عائلي"};
        List<String> listOFServiceTypes = new ArrayList<>(Arrays.asList(serviceTypes));
        powerSpinnerView.setItems(listOFServiceTypes);





        return fragmentContactUsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        powerSpinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int i, @org.jetbrains.annotations.Nullable String s, int i1, String t1) {
                valueOfServiceType = t1;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validPhone() | !validEmail()) {
                    return;
                } else {
                    checkConnection();
                    loadingDialog.startLoadingDialog();

                    Observable<String> todoObservable = Observable.create(emitter -> {


                        String message =  dbHelper.contactUS(fullName.getText().toString(), email.getText().toString(), phoneNumber.getText().toString(), notes.getText().toString(), valueOfServiceType);
                        emitter.onNext(message);

                    });
                    todoObservable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Throwable {
                                    fullName.setText("");
                                    phoneNumber.setText("");
                                    email.setText("");
                                    notes.setText("");
                                    loadingDialog.dismissDialog();

                                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                                }
                            },e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show());
                }
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
    private boolean validPhone() {
        if (phoneNumber.getText().toString().isEmpty()) {
            phoneNumber.setError("برجاء كتابه رقم المحمول");

            return false;
        } else if (phoneNumber.length() < 11) {
            phoneNumber.setError("الرجاء التأكد من كتابه رقم المحمول بصوره صحيحه");
            return false;
        } else {
            phoneNumber.setError(null);
            return true;
        }
    }
    private boolean validEmail()
    {
        String sEmail = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (sEmail.isEmpty()) {
            email.setError("برجاء كتابه البريد الالكتروني");
            return false;
        }
        else if (!sEmail.matches(emailPattern)) {
            email.setError("برجاء كتابه البريد الالكتروني بصوره صحيحه");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }
    }
    }




