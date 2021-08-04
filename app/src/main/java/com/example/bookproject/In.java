package com.example.bookproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class In extends AppCompatActivity {
    ListView mylist;
    FirebaseListAdapter adapter;

    private Button InB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);

        mylist=(ListView)findViewById(R.id.diary_list);
        Query query=FirebaseDatabase.getInstance().getReference().child("income");
        FirebaseListOptions<income> options=new FirebaseListOptions.Builder<income>()
                .setLayout(R.layout.income)
                .setLifecycleOwner(In.this)
                .setQuery(query,income.class)
                .build();
        adapter=new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {
                TextView name=v.findViewById(R.id.name);
                TextView money=v.findViewById(R.id.money);
                TextView date=v.findViewById(R.id.date);
                TextView time=v.findViewById(R.id.time);
                ImageView imageView=v.findViewById(R.id.image_1);
                income inc=(income)model;
                name.setText("收入名稱："+inc.getName().toString());
                money.setText("收入金額："+inc.getMoney().toString());
                date.setText("日期："+inc.getDate().toString());
                time.setText("時間："+inc.getTime().toString());
                Picasso.with(In.this).load(inc.getImg_uri().toString()).into(imageView);
            }
        };mylist.setAdapter(adapter);

        InB = findViewById(R.id.in_1);
        InB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳轉頁面
                Intent intent = new Intent(In.this,MainActivity2.class);
                startActivity(intent);
            }
        });

        mylist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(In.this)
                        .setTitle("刪除")
                        .setMessage("是否要刪除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.getRef(position).removeValue();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return false;
            }
        });
    }

    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}