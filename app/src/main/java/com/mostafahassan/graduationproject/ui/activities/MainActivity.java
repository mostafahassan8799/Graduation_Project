package com.mostafahassan.graduationproject.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.mostafahassan.graduationproject.R;
import com.mostafahassan.graduationproject.databinding.ActivityMainBinding;
import com.mostafahassan.graduationproject.loginSignUp.LoginSignUpActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private long pressedTime;
    DrawerLayout drawer;
    NavController navController;
    ActivityMainBinding activityMainBinding;
    Intent intent;
    String getID, getPass;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        intent=getIntent();
        getID = intent.getStringExtra("idKey");
        getPass = intent.getStringExtra("passKey");




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


         drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_mydata, R.id.nav_follow_request,
                R.id.nav_last_news,R.id.nav_questions,R.id.nav_log_out)
                .setOpenableLayout(drawer)
                .build();
//        navigationView.setNavigationItemSelectedListener(this);

         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                if (id == R.id.nav_log_out) {
                    startActivity(new Intent(MainActivity.this, LoginSignUpActivity.class));
                    getSharedPreferences("Login", MODE_PRIVATE)
                            .edit().clear()
                            .apply();
                    finish();
                } else {
                    //This is for maintaining the behavior of the Navigation view
                    NavigationUI.onNavDestinationSelected(menuItem, navController);
                    //This is for closing the drawer after acting on it
                    drawer.closeDrawer(GravityCompat.START);
                }
                return true;

            }
        });

        

    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }


}