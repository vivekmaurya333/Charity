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

public class LoginFragment extends Fragment {
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";
    private EditText mTextEmail;
    private EditText mTextPassword;
    private Button mButtonLogin;
    private TextView mTextViewRegister;
    private RegisterFragment registerFragment;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHelper(getActivity());
        mPrefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mTextEmail = (EditText) view.findViewById(R.id.l_email);
        mTextPassword = (EditText) view.findViewById(R.id.l_password);
        mTextViewRegister = (TextView) view.findViewById(R.id.l_register);
        mButtonLogin = (Button) view.findViewById(R.id.l_button);
        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerFragment = new RegisterFragment();
                fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, registerFragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mTextEmail.getText().toString().trim();
                String password = mTextPassword.getText().toString().trim();
                Boolean res = db.checkUser(email, password);
                if(email.equals("admin") && password.equals("admin")){
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("pref", email);
                    editor.putString("pref_name", email);
                    editor.putString("pref_mail", email);
                    editor.putString("pref_password", password);
                    editor.apply();
                    AdminFragment adminFragment = new AdminFragment();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, adminFragment);
                    fragmentTransaction.commit();
                    Toast.makeText(getActivity(), "Admin signed in Successfully", Toast.LENGTH_SHORT).show();
                }
                else if(res){
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("pref", "true");
                    editor.apply();
                    homeFragment = new HomeFragment();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, homeFragment);
                    //fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    Toast.makeText(getActivity(), "Sign in Successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
