package com.example.charity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    private EditText mTextUsername, mTextMobile, mTextPassword, mTextCnfPassword;
    private Button mButtonSave;
    private ImageView imageView;

    private DatabaseHelper db;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        db = new DatabaseHelper(getApplicationContext());
        mTextUsername = findViewById(R.id.edit_username);
        mTextMobile = findViewById(R.id.edit_mobile);
        mTextPassword = findViewById(R.id.edit_password);
        mTextCnfPassword = findViewById(R.id.edit_cpassword);
        imageView = findViewById(R.id.edit_back);
        mButtonSave = findViewById(R.id.edit_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mTextUsername.getText().toString().trim();
                long mobile = Long.parseLong(mTextMobile.getText().toString().trim());
                SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String email = sp.getString("pref_mail", "");
                String password = mTextPassword.getText().toString().trim();
                String cpassword = mTextCnfPassword.getText().toString().trim();
                if (password.equals(cpassword)) {
                    long val = db.editUser(username, mobile, password, email);
                    if (val > 0) {
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("pref", "true");
                        editor.putString("pref_name", username);
                        editor.putLong("pref_mobile", mobile);
                        if(!password.equals("")) {
                            editor.putString("pref_password", password);
                        }
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Your Data is Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Registration Error", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password is not Matching", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
