package com.example.charity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class AdminFragment extends Fragment {
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = view.findViewById(R.id.lin_layout);
        DatabaseHelper db = new DatabaseHelper(getContext());
        mPrefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        db.getAllInfo();
        ArrayList values;
        values = db.getAllInfo();
        if(values.size() != 0) {
            for (int i = 0; i < values.size(); i = i + 5) {
                LinearLayout main = new LinearLayout(getActivity());
                main.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                main.setOrientation(LinearLayout.HORIZONTAL);
                main.setPadding(20, 30, 20, 20);
                TextView username = new TextView(getActivity());
                username.setText((String) values.get(i));
                username.setGravity(Gravity.CENTER);
                TextView email = new TextView(getActivity());
                email.setText((String) values.get(i+1));
                email.setGravity(Gravity.CENTER);
                ImageView img = new ImageView(getActivity());
                img.setImageBitmap((Bitmap) values.get(i+2));
                img.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
                TextView cat = new TextView(getActivity());
                cat.setText((String) values.get(i+3));
                cat.setGravity(Gravity.CENTER);
                main.setPadding(20, 30, 20, 20);
                TextView title = new TextView(getActivity());
                title.setText((String) values.get(i+4));
                cat.setGravity(Gravity.CENTER);
                main.setPadding(20, 30, 20, 20);
                linearLayout.addView(main);
                main.addView(username);
                main.addView(email);
                main.addView(img);
                main.addView(cat);
                main.addView(title);
            }
        }
        Button b = new Button(getActivity());
        b.setText("Sign Out");
        linearLayout.addView(b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString("pref", "false");
                editor.apply();
                LoginFragment loginFragment = new LoginFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, loginFragment);
                fragmentTransaction.commit();
            }
        });
    }
}
