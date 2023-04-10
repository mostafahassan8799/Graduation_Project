package com.mostafahassan.graduationproject.IDFragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mostafahassan.graduationproject.R;
import com.mostafahassan.graduationproject.databinding.FragmentIDServiceBinding;
import com.mostafahassan.graduationproject.ui.activities.MainActivity;
import com.mostafahassan.graduationproject.utilities.DBHelper;
import com.mostafahassan.graduationproject.utilities.LoadingDialog;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;


public class IDService extends Fragment {

    private static final int PICK_REQUEST_CODE_IMAGE_ONE = 1;
    private static final int PICK_REQUEST_CODE_IMAGE_TWO = 2;
    FragmentIDServiceBinding fragmentIDServiceBinding;
    String idData;
    AlertDialog alertDialog;
    Button okButton,payButton,cancelButton;
    SharedPreferences sharedPreferences;
    ImageView imageViewOne, imageViewTwo;
    Uri imageUri1,imageUri2;
    byte[] encodedImg1;
    byte[] encodedImg2=null;
    LoadingDialog loadingDialog;
    Bitmap bitmap;
    InputStream inputStream1,inputStream2;
    DBHelper dbHelper;
    Connection con;
    ByteArrayOutputStream byteArrayOutputStream1,byteArrayOutputStream2;
    final Calendar myCalendar = Calendar.getInstance();



    public IDService() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentIDServiceBinding = FragmentIDServiceBinding.inflate(inflater, container, false);
        imageViewOne= fragmentIDServiceBinding.img1IdService;
        imageViewTwo= fragmentIDServiceBinding.img2IdService;
        loadingDialog = new LoadingDialog(getActivity());


        String[] type = {"ذكر", "انثي"};
        String[] religion = {"مسلم", "مسيحي"};
        String[] status = {"اعزب", "متزوج"};
        String[] cardTypes = {"عادية", "سريعة", "VIP"};
        String[] serviceTypes = { "تحديث بيانات", "اول مرة", "بدل فاقد"};
        String[] payment = {"Visa","Master Card","Credit Card"};
        String[] governorate = {"القاهرة","الجيزه"};
        String[] regionCairo = { "مكتب سجل مدني ابو النمرس","مكتب سجل مدني الازبكية","العباسية","مكتب سجل مدني التبين","مكتب سجل مدني الحوامدية","مكتب سجل مدني الشيخ زايد"};



        List<String> listOFCardTypes = new ArrayList<>(Arrays.asList(cardTypes));
        fragmentIDServiceBinding.spinnerCardTypeIdService.setItems(listOFCardTypes);

        List<String> listOFType = new ArrayList<>(Arrays.asList(type));
        fragmentIDServiceBinding.spinnerTypeIdService.setItems(listOFType);

        List<String> listOFReligion = new ArrayList<>(Arrays.asList(religion));
        fragmentIDServiceBinding.spinnerReligionIdService.setItems(listOFReligion);

        List<String> listOFStatus = new ArrayList<>(Arrays.asList(status));
        fragmentIDServiceBinding.spinnerStatusIdService.setItems(listOFStatus);


        List<String> listOfGovernorate = new ArrayList<>(Arrays.asList(governorate));
        fragmentIDServiceBinding.spinnerGovernorateIdService.setItems(listOfGovernorate);

        List<String> listOFServiceTypes = new ArrayList<>(Arrays.asList(serviceTypes));
        fragmentIDServiceBinding.spinnerServiceTypeIdService.setItems(listOFServiceTypes);

        List<String> listOfPayment = new ArrayList<>(Arrays.asList(payment));
        fragmentIDServiceBinding.spinnerPaymentIdService.setItems(listOfPayment);
        List<String> listOfRegionCairo = new ArrayList<>(Arrays.asList(regionCairo));




        fragmentIDServiceBinding.spinnerGovernorateIdService.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int i, @Nullable String s, int i1, String t1) {


                        fragmentIDServiceBinding.spinnerRegionIdService.setItems(listOfRegionCairo);

            }
        });
        fragmentIDServiceBinding.spinnerServiceTypeIdService.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int i, @Nullable String s, int i1, String t1) {
                switch (i1) {
                    case 0:
                        fragmentIDServiceBinding.tvIdServiceImgDetails.setText("1- صورة البطاقة الشخصية اخر اصدار 2- صوره مختومه بعنوان الوظيفه الجديده المراد استبدالها او وثيقه رسيمه بالعنوان الجديد المراد تغيره");
                        imageViewTwo.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fragmentIDServiceBinding.tvIdServiceImgDetails.setText("1- اصل شهادة الميلاد مميكنة 2- صورة بطاقة ضامن من الدرجة الاولى");
                        imageViewTwo.setVisibility(View.VISIBLE);

                        break;
                    case 2:
                        fragmentIDServiceBinding.tvIdServiceImgDetails.setText("  1- صورة بطاقة الرقم القومى اخر اصدار (او صورة الباسبور ساري او صورة رخصة القيادة او صوره شهاده الميلاد) 2- محضر فقد البطاقة");
                        imageViewTwo.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        fragmentIDServiceBinding.spinnerCardTypeIdService.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int i, @Nullable String s, int i1, String t1) {
                switch (i1){
                    case 0:
                        fragmentIDServiceBinding.tvResetDetails.setText("السعر : 80 جنيهاً\nالمتبقي 8 أيام علي الاستلام");
                        fragmentIDServiceBinding.tvResetDetails.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fragmentIDServiceBinding.tvResetDetails.setText("السعر : 100 جنيهاً\nالمتبقي 4 أيام علي الاستلام");
                        fragmentIDServiceBinding.tvResetDetails.setVisibility(View.VISIBLE);

                        break;
                    case 2:
                        fragmentIDServiceBinding.tvResetDetails.setText("السعر : 150 جنيهاً\nالمتبقي 2 أيام علي الاستلام");
                        fragmentIDServiceBinding.tvResetDetails.setVisibility(View.VISIBLE);

                        break;
                }
            }
        });
        fragmentIDServiceBinding.spinnerPaymentIdService.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int i, @Nullable String s, int i1, String t1) {

            }
        });

        imageViewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_REQUEST_CODE_IMAGE_ONE);

            }
        });
        imageViewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_REQUEST_CODE_IMAGE_TWO);

            }
        });
        loadIDFromSharedPreferences();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        fragmentIDServiceBinding.birthEtIdService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        return fragmentIDServiceBinding.getRoot();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        fragmentIDServiceBinding.birthEtIdService.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        fragmentIDServiceBinding.sendButtonIdService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();

                AlertDialog.Builder builderPay = new AlertDialog.Builder(getContext());
                final View customLayoutPay = getLayoutInflater().inflate(R.layout.payment_alert_dialog, null);
                builderPay.setView(customLayoutPay);
                AlertDialog dialogPay =builderPay.create();
                dialogPay.show();
                dialogPay.setCancelable(false);
                payButton = customLayoutPay.findViewById(R.id.button_pay);
                cancelButton = customLayoutPay.findViewById(R.id.button_cancel);
                payButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadingDialog.startLoadingDialog();
                                        uploadIDOrder();
                                        loadingDialog.startLoadingDialog();
                                        dialogPay.dismiss();


                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPay.dismiss();
                    }
                });
            }
        });
    }

    private void uploadIDOrder() {
        Observable<String> todoObservable = Observable.create(emitter -> {

            String message = dbHelper.idServiceUpload(fragmentIDServiceBinding.fullnameEtIdService.getText().toString()
                    ,fragmentIDServiceBinding.idEtIdService.getText().toString(),
                    fragmentIDServiceBinding.phone1EtIdService.getText().toString(),
                    fragmentIDServiceBinding.phone2EtIdService.getText().toString(),
                    fragmentIDServiceBinding.emailEtIdService.getText().toString(),
                    fragmentIDServiceBinding.spinnerGovernorateIdService.getText().toString(),
                    fragmentIDServiceBinding.spinnerRegionIdService.getText().toString(),
                    fragmentIDServiceBinding.addressEtIdService.getText().toString(),
                    fragmentIDServiceBinding.spinnerCardTypeIdService.getText().toString(),
                    fragmentIDServiceBinding.spinnerServiceTypeIdService.getText().toString(),
                    encodedImg1,encodedImg2,
                    fragmentIDServiceBinding.notesEtIdService.getText().toString(),
                    fragmentIDServiceBinding.spinnerPaymentIdService.getText().toString(),
                    "بطاقة الرقم القومي",fragmentIDServiceBinding.birthEtIdService.getText().toString(),
                    fragmentIDServiceBinding.spinnerReligionIdService.getText().toString(),
                    fragmentIDServiceBinding.spinnerStatusIdService.getText().toString(),
                    fragmentIDServiceBinding.qualificationEtIdService.getText().toString(),
                    fragmentIDServiceBinding.spinnerTypeIdService.getText().toString(),1);
            emitter.onNext(message);

        });
        todoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        loadingDialog.dismissDialog();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        final View customLayout = getLayoutInflater().inflate(R.layout.ok_alret_layout, null);
                        builder.setView(customLayout);
                        AlertDialog dialogOk =builder.create();
                        dialogOk.show();
                        dialogOk.setCancelable(false);
                        okButton = customLayout.findViewById(R.id.ok_button);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogOk.dismiss();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                            }
                        });
                        Log.i("here", "accept: "+s);
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                    }
                },e -> Log.i("hererxjava", "onViewCreated: "+e.getMessage()));

    }

    private void loadIDFromSharedPreferences() {
        Observable<String> todoObservable = Observable.create(emitter -> {

            sharedPreferences =getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
             idData = sharedPreferences.getString("id", null);
            emitter.onNext(idData);
        });
        todoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        Log.i("here", "accept: "+s);
                        fragmentIDServiceBinding.idEtIdService.setText(s);
                        fragmentIDServiceBinding.idEtIdService.setEnabled(false);

                    }
                },e -> Log.i("hererxjava", "onViewCreated: "+e.getMessage()));
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_REQUEST_CODE_IMAGE_ONE && resultCode == RESULT_OK) {
            if (data != null) {

                imageUri1 = data.getData();
                imageViewOne.setImageURI(imageUri1);


                try {
                    inputStream1 = getActivity().getContentResolver().openInputStream(imageUri1);
                    bitmap = BitmapFactory.decodeStream(inputStream1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (bitmap !=null){
                    this.imageViewOne.setImageBitmap(bitmap);
                    Bitmap bitmapImage1 =  ((BitmapDrawable) imageViewOne.getDrawable()).getBitmap();
                    byteArrayOutputStream1= new ByteArrayOutputStream();
                    bitmapImage1.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream1);
                    encodedImg1 = byteArrayOutputStream1.toByteArray();
                    /*encodedImg1 = Base64.encodeToString(byteArrayOutputStream1.toByteArray(), Base64.DEFAULT);*/
                }
            }
        } else if (requestCode == PICK_REQUEST_CODE_IMAGE_TWO && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri2 = data.getData();
                imageViewTwo.setImageURI(imageUri2);


                try {
                    inputStream2 = getActivity().getContentResolver().openInputStream(imageUri2);
                    bitmap = BitmapFactory.decodeStream(inputStream2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (bitmap !=null){
                    this.imageViewTwo.setImageBitmap(bitmap);
                    Bitmap bitmapImage1 =  ((BitmapDrawable) imageViewTwo.getDrawable()).getBitmap();
                    byteArrayOutputStream2= new ByteArrayOutputStream();
                    bitmapImage1.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream2);
                    encodedImg2 = byteArrayOutputStream2.toByteArray();

                    //encodedImg2 = Base64.encodeToString(byteArrayOutputStream2.toByteArray(), Base64.DEFAULT);

                }
            }


        }

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
