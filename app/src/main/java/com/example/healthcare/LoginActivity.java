package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText edUsername, edPassword;
    Button btn;
    TextView tv;
    CheckBox cbRemember;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edPassword = findViewById(R.id.editTextLoginPassword);
        edUsername = findViewById(R.id.editTextLoginUsername);
        btn = findViewById(R.id.buttonLogin);
        tv = findViewById(R.id.textViewNewUSer);
        cbRemember = findViewById(R.id.checkBoxSaveLogin);

        sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);

        // Auto-load saved credentials if exist
        String savedUsername = sharedpreferences.getString("saved_username", "");
        String savedPassword = sharedpreferences.getString("saved_password", "");
        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            edUsername.setText(savedUsername);
            edPassword.setText(savedPassword);
            cbRemember.setChecked(true);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                Database db = new Database(getApplicationContext(), "healthcare", null, 1);

                if (username.length() == 0 || password.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Fill All Details", Toast.LENGTH_SHORT).show();
                } else {
                    if (db.login(username, password) == 1) {
                        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("username", username); // For session

                        if (cbRemember.isChecked()) {
                            editor.putString("saved_username", username);
                            editor.putString("saved_password", password);
                        } else {
                            editor.remove("saved_username");
                            editor.remove("saved_password");
                        }

                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Username and Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
