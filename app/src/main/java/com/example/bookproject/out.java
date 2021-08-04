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

public class out extends AppCompatActivity {
    ListView mylist;
    FirebaseListAdapter adapter;

    private Button outB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);

        mylist=(ListView)findViewById(R.id.diary_list);
        Query query= FirebaseDatabase.getInstance().getReference().child("expense");
        FirebaseListOptions<expense> options=new FirebaseListOptions.Builder<expense>()
                .setLayout(R.layout.expense)
                .setLifecycleOwner(out.this)
                .setQuery(query,expense.class)
                .build();
        adapter=new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {
                TextView name=v.findViewById(R.id.name);
                TextView money=v.findViewById(R.id.money);
                TextView date=v.findViewById(R.id.date);
                TextView time=v.findViewById(R.id.time);
                ImageView imageView=v.findViewById(R.id.image_1);
                expense exp=(expense)model;
                name.setText("支出名稱："+exp.getName().toString());
                money.setText("支出金額："+exp.getMoney().toString());
                date.setText("日期："+exp.getDate().toString());
                time.setText("時間："+exp.getTime().toString());
                Picasso.with(out.this).load(exp.getImg_uri().toString()).into(imageView);
            }
        };mylist.setAdapter(adapter);

        outB = findViewById(R.id.out_1);
        outB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳轉頁面
                Intent intent = new Intent(out.this,MainActivity3.class);
                startActivity(intent);
            }
        });

        mylist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(out.this)
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