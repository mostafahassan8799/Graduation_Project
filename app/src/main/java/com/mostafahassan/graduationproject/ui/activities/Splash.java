package com.mostafahassan.graduationproject.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mostafahassan.graduationproject.R;
import com.mostafahassan.graduationproject.databinding.SplashLayoutBinding;
import com.mostafahassan.graduationproject.loginSignUp.LoginSignUpActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Splash extends AppCompatActivity {
    Animation bottomAnimation,topAnimation;
    SplashLayoutBinding splashLayoutBinding;
    TextView textView;
    ImageView imageView;
    AlertDialog dialog;

    String idShared,passShared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNetworkConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("برجاء التحقق من الاتصال بالأنترنت والمحاوله مره اخري...")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            Intent intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(intent);
                        }
                    });

            dialog = builder.create();
            dialog.show();

        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            splashLayoutBinding = SplashLayoutBinding.inflate(getLayoutInflater());
            setContentView(splashLayoutBinding.getRoot());
            textView = splashLayoutBinding.tvSplash;
            imageView = splashLayoutBinding.ivSplash;
            bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
            topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_anim);
            imageView.setAnimation(topAnimation);
            textView.setAnimation(bottomAnimation);



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Observable<Integer> todoObservable = Observable.create(emitter -> {
                        SharedPreferences sharedPref = getSharedPreferences("Login",Context.MODE_PRIVATE);
                        Log.i("here", "onCreate: "+sharedPref.getString("id", null) +  "  "+sharedPref.getString("pass", null));
                        if (sharedPref.getString("id", null) !=null && sharedPref.getString("pass", null) != null) {
                            emitter.onNext(1);
                        }
                        else {
                            emitter.onNext(0);

                        }

                    });
                    todoObservable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(integer -> {
                                if (integer==1){
                                    startActivity(new Intent(Splash.this, MainActivity.class));
                                    Log.i("here", "onCreate: "+1);
                                }else {
                                    Log.i("here", "onCreate: "+0);

                                    startActivity(new Intent(Splash.this, LoginSignUpActivity.class));
                                }
                                finish();


                            });
                }
            }, 4000);






        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();


    }



    }
