package com.example.thiscopy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.net.URI;

public class MainActivity extends AppCompatActivity
{
    TextInputEditText input_email,input_password;
    RelativeLayout login_button;
    String Email_data="donga@gmail.com";
    String Password_data="1234";
    String Email_put="";
    String Password_put="";
    URI url;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        login_button = findViewById(R.id.login_button);

        login_button.setEnabled(false);

        input_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (charSequence != null)
                {
                    Email_put = charSequence.toString();
                    login_button.setEnabled(Email_put.equals(Email_data) &&  Password_put.equals(Password_data));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(charSequence != null)
                {
                    Password_put = charSequence.toString();
                    login_button.setEnabled(Email_put.equals(Email_data) &&  Password_put.equals(Password_data));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String Email = input_email.getText().toString();
                String Password = input_password.getText().toString();

                Intent intent = new Intent(MainActivity.this,Login.class);
                intent.putExtra("email",Email);
                intent.putExtra("password",Password);
                startActivity(intent);
            }
        });
    }
}