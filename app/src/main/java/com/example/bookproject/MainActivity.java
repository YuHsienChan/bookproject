package com.example.bookproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button InBtn;
    private Button OutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InBtn =  (Button) findViewById(R.id.inbtn);
        OutBtn = (Button)findViewById(R.id.outbtn);
        InBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳轉頁面
                Intent intent = new Intent(MainActivity.this,In.class);
                startActivity(intent);
            }
        });

        OutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳轉頁面
                Intent intent = new Intent(MainActivity.this,out.class);
                startActivity(intent);
            }
        });

    }
}