package com.example.healthcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HealthArticlesDetailsActivity extends AppCompatActivity {

    TextView tv1;
    ImageView img;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles_details);

        tv1 = findViewById(R.id.textViewHADTitle);
        img = findViewById(R.id.imageViewHAD);
        btnBack = findViewById(R.id.buttonHADBack);

        Intent intent = getIntent();

        tv1.setText(intent.getStringExtra("text1"));
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            int resId = bundle.getInt("text2");
            img.setImageResource(resId);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HealthArticlesDetailsActivity.this,HealthArticlesActivity.class));
            }
        });

    }
}