package com.example.charity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    private Fragment fragment;
    private CharityFragment charityFragment;
    private DonateFragment donateFragment;
    private SocialFragment socialFragment;
    private static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //getSupportActionBar().setElevation(0);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#13CE66' size='7'>Profile</font>"));
        bottomNav = findViewById(R.id.bottom_navigation);
        charityFragment = new CharityFragment();
        donateFragment = new DonateFragment();
        socialFragment = new SocialFragment();
        onCreateHelper();
        setFragment(fragment);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_profile:
                        onCreateHelper();
                        setFragment(fragment);
                        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#13CE66'>Profile</font>"));
                        return true;
                    /*case R.id.nav_charity:
                        setFragment(charityFragment);
                        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#13CE66'>Charity</font>"));
                        return true;*/
                    case R.id.nav_donate:
                        setFragment(donateFragment);
                        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#13CE66'>Donate</font>"));
                        return true;
                    /*case R.id.nav_admin:
                        setFragment(adminFragment);
                        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#13CE66'>Social</font>"));
                        return true;*/
                        default:
                            return false;
                }
            }
        });
    }

    private void onCreateHelper() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(sp.getString("pref", "").equals("true")) {
            fragment = new HomeFragment();
        }
        else if(sp.getString("pref", "").equals("admin")){
            fragment = new AdminFragment();
        }
        else {
            fragment = new LoginFragment();
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
