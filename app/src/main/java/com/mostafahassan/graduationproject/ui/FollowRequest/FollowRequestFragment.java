package com.mostafahassan.graduationproject.ui.FollowRequest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.mostafahassan.graduationproject.databinding.FragmentFollowRequestBinding;
import com.mostafahassan.graduationproject.utilities.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FollowRequestFragment extends Fragment {
    FragmentFollowRequestBinding followRequestBinding;
    SharedPreferences sharedPreferences;
    String idData;
    boolean isConfirmed = false;
    String getOrderDate = "";
    DBHelper dbHelper;
    Connection con;
    public FollowRequestFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

      followRequestBinding =FragmentFollowRequestBinding.inflate(inflater,container,false);
      checkConnection();
      loadIDFromSharedPreferences();
      String[] descriptionData = {"إرسال الطلب", "معالجة الطلب", "تأكيد الطلب"};
      followRequestBinding.stateProgressBar.setStateDescriptionData(descriptionData);


        return followRequestBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderCheck();

    }

    private void orderCheck() {
        loadIDFromSharedPreferences();
        dbHelper = new DBHelper();
        con = dbHelper.connectDB();
        if (con!=null){
            try {
                String query = "select FullName,OrderCivilId,OrderType,Date,Address,service_type,payment_method,Confirmed  from RegisterCivil where CardID ='"+idData+"'";
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if(resultSet.next()){
                    followRequestBinding.tvNoOrder.setVisibility(View.GONE);
                    followRequestBinding.stateProgressBar.setVisibility(View.VISIBLE);
                    followRequestBinding.tvName.setVisibility(View.VISIBLE);
                    followRequestBinding.tvOrderDate.setVisibility(View.VISIBLE);
                    followRequestBinding.tvAddress.setVisibility(View.VISIBLE);
                    followRequestBinding.tvPay.setVisibility(View.VISIBLE);
                    followRequestBinding.tvServiceType.setVisibility(View.VISIBLE);
                    followRequestBinding.tvOrderType.setVisibility(View.VISIBLE);
                    followRequestBinding.tvOrderNumber.setVisibility(View.VISIBLE);
                    followRequestBinding.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                    followRequestBinding.tvDataName.setText(resultSet.getString("FullName"));
                    followRequestBinding.tvDataOrderNumber.setText(resultSet.getString("OrderCivilId"));
                    followRequestBinding.tvDataOrderType.setText(resultSet.getString("OrderType"));
                    followRequestBinding.tvDataAddress.setText(resultSet.getString("Address"));
                    followRequestBinding.tvDataServiceType.setText(resultSet.getString("service_type"));
                    followRequestBinding.tvDataPay.setText(resultSet.getString("payment_method"));
                    followRequestBinding.tvDataOrderDate.setText(resultSet.getString("Date"));
                    isConfirmed = resultSet.getBoolean("Confirmed");
                    
                    if (resultSet.getString("service_type").equals("عادية")){
                       getOrderDate = resultSet.getString("Date");
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date orderDate = format.parse(getOrderDate);
                            Date currentDate = Calendar.getInstance().getTime();
                            String dayOrderDate= (String) DateFormat.format("dd",orderDate);
                            String dayCurrent= (String) DateFormat.format("dd",currentDate);
                            int numOfOrderDate = Integer.parseInt(dayOrderDate.trim());
                            int numOfCurrentDay = Integer.parseInt(dayCurrent.trim());
                            if (Math.abs(numOfOrderDate - numOfCurrentDay) >= 4)
                            {
                                followRequestBinding.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                                //TODO: here
                            }

                            Log.i("date", "orderCheck: "+orderDate);
                            Log.i("date1", "orderCheck: "+currentDate);
                            Log.i("num1", "orderCheck: "+numOfOrderDate);
                            Log.i("num2", "orderCheck: "+numOfCurrentDay);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else if (resultSet.getString("service_type").equals("سريعة")){
                        getOrderDate = resultSet.getString("Date");
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date orderDate = format.parse(getOrderDate);
                            Date currentDate = Calendar.getInstance().getTime();
                            String dayOrderDate= (String) DateFormat.format("dd",orderDate);
                            String dayCurrent= (String) DateFormat.format("dd",currentDate);
                            int numOfOrderDate = Integer.parseInt(dayOrderDate.trim());
                            int numOfCurrentDay = Integer.parseInt(dayCurrent.trim());
                            if (Math.abs(numOfOrderDate - numOfCurrentDay) >= 2)
                            {
                                followRequestBinding.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                                //TODO: here
                            }

                            Log.i("date", "orderCheck: "+orderDate);
                            Log.i("date1", "orderCheck: "+currentDate);
                            Log.i("num1", "orderCheck: "+numOfOrderDate);
                            Log.i("num2", "orderCheck: "+numOfCurrentDay);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }else if(resultSet.getString("service_type").equals("VIP")){
                        getOrderDate = resultSet.getString("Date");
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date orderDate = format.parse(getOrderDate);
                            Date currentDate = Calendar.getInstance().getTime();
                            String dayOrderDate= (String) DateFormat.format("dd",orderDate);
                            String dayCurrent= (String) DateFormat.format("dd",currentDate);
                            int numOfOrderDate = Integer.parseInt(dayOrderDate.trim());
                            int numOfCurrentDay = Integer.parseInt(dayCurrent.trim());
                            if (Math.abs(numOfOrderDate - numOfCurrentDay) >= 1)
                            {
                                followRequestBinding.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);

                                //TODO: here
                            }

                            Log.i("date", "orderCheck: "+orderDate);
                            Log.i("date1", "orderCheck: "+currentDate);
                            Log.i("num1", "orderCheck: "+numOfOrderDate);
                            Log.i("num2", "orderCheck: "+numOfCurrentDay);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    //here is confirmed
                    if (isConfirmed){
                        followRequestBinding.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        followRequestBinding.stateProgressBar.setAllStatesCompleted(true);
                    }
                    Log.i("isconfirm", "orderCheck: "+isConfirmed);


                }
                else {
                    Toast.makeText(getActivity(), "لايوجد طلبات بهذا الرقم القومي", Toast.LENGTH_SHORT).show();
                }
                
            }catch (SQLException throwables)
            {
                throwables.printStackTrace();

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

                    }
                },e -> Log.i("hererxjava", "onViewCreated: "+e.getMessage()));
    }
}