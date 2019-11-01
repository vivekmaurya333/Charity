package com.example.charity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminLogin extends Activity {
    EditText name,pass;
    Button loginB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        name = findViewById(R.id.adminname);
        pass = findViewById(R.id.adminpass);
        loginB = findViewById(R.id.loginbutton);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("admin") == true && pass.getText().toString().equals("admin"))
                {
                    //startActivity(new Intent(AdminLogin.this,MainActivity.class));
                    finish();
                }
            }
        });
    }
}
