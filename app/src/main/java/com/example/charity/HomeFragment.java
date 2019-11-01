package com.example.charity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private static final String PREFS_NAME = "PrefsFile";
    private SharedPreferences mPrefs;
    private static final int IMAGEREQ = 1;

    private ImageView imageView;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHelper(getActivity());
        TextView mTextViewName, mTextViewEmail, mTextViewMobile, mTextViewEdit, mTextViewSO;
        mTextViewSO = view.findViewById(R.id.tv_signout);
        mPrefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        LinearLayout linearLayout = view.findViewById(R.id.contributions);
        mTextViewName = view.findViewById(R.id.username);
        mTextViewEmail = view.findViewById(R.id.mail);
        mTextViewMobile = view.findViewById(R.id.mobile);
        mTextViewEdit = view.findViewById(R.id.edit);
        imageView = view.findViewById(R.id.user_imgview2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGEREQ);
            }
        });
        mTextViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                startActivity(intent);
            }
        });
        mTextViewSO.setOnClickListener(new View.OnClickListener() {
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
        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(sp.getString("pref", "").equals("true")) {
            ArrayList values;
            values = db.getUserData(sp.getString("pref_mail", ""));
            mTextViewName.setText("Hi, " + values.get(0));
            mTextViewEmail.setText((String)values.get(1));
            mTextViewMobile.setText("+91-" + String.valueOf(sp.getLong("pref_mobile", 0)));
        }
        ArrayList values;
        values = db.getUserInfo(sp.getString("pref_mail", ""));
        if(values.size() != 0) {
            for (int i = 0; i < values.size(); i = i + 3) {
                LinearLayout main = new LinearLayout(getActivity());
                main.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                main.setOrientation(LinearLayout.HORIZONTAL);
                main.setPadding(20, 30, 20, 20);
                ImageView img = new ImageView(getActivity());
                img.setImageBitmap((Bitmap) values.get(i));
                img.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
                TextView cat = new TextView(getActivity());
                cat.setText((String) values.get(i+1));
                cat.setGravity(Gravity.CENTER);
                main.setPadding(20, 30, 20, 20);
                TextView title = new TextView(getActivity());
                title.setText((String) values.get(i+2));
                cat.setGravity(Gravity.CENTER);
                main.setPadding(20, 30, 20, 20);
                linearLayout.addView(main);
                main.addView(img);
                main.addView(cat);
                main.addView(title);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if(requestCode == IMAGEREQ && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
