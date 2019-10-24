package com.example.charity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class RegisterFragment extends Fragment {

    private EditText mTextUsername, mTextEmail, mTextMobile, mTextPassword, mTextCnfPassword;
    private Button mButtonRegister;
    private TextView mTextViewLogin;
    private LoginFragment loginFragment;
    private FragmentManager fragmentManager;
    private DatabaseHelper db;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPrefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        db = new DatabaseHelper(getActivity());
        mTextUsername = (EditText) view.findViewById(R.id.r_username);
        mTextEmail = (EditText) view.findViewById(R.id.r_email);
        mTextMobile = (EditText) view.findViewById(R.id.r_mobile);
        mTextPassword = (EditText) view.findViewById(R.id.r_password);
        mTextCnfPassword = (EditText) view.findViewById(R.id.r_cpassword);
        mButtonRegister = (Button) view.findViewById(R.id.r_button);
        mTextViewLogin = (TextView) view.findViewById(R.id.r_login);
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFragment = new LoginFragment();
                fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, loginFragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mTextUsername.getText().toString().trim();
                String email = mTextEmail.getText().toString().trim();
                long mobile = Long.parseLong(mTextMobile.getText().toString().trim());
                String password = mTextPassword.getText().toString().trim();
                String cpassword = mTextCnfPassword.getText().toString().trim();
                if (password.equals(cpassword)) {
                    long val = db.addUser(username, email, mobile, password);
                    if (val > 0) {
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("pref", "true");
                        editor.putString("pref_name", username);
                        editor.putString("pref_mail", email);
                        editor.putLong("pref_mobile", mobile);
                        editor.putString("pref_password", password);
                        editor.apply();
                        loginFragment = new LoginFragment();
                        fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, loginFragment);
                        fragmentTransaction.commit();
                        Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), "Registration Error", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Password is not Matching", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}