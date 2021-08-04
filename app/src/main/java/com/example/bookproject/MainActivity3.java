package com.example.bookproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.UUID;

public class MainActivity3 extends AppCompatActivity {
    private  FirebaseDatabase database;
    private DatabaseReference ref;
    private EditText name,money,date,time;
    expense Expense;
    Button button;
    private static final int pick_img=1;
    private Button choose_img;
    private ImageView img;
    private Uri img_uri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        name=findViewById(R.id.et_1);
        money=findViewById(R.id.et_2);
        date=findViewById(R.id.et_3);
        time=findViewById(R.id.et_4);
        button=findViewById(R.id.bt_3);
        choose_img=findViewById(R.id.bt_2);
        img=findViewById(R.id.iv_1);

        choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilechooser();
            }
        });

        storageReference= FirebaseStorage.getInstance().getReference();
        Expense=new expense();
        ref=database.getInstance().getReference().child("expense");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_uri == null) {
                    Expense.setName(name.getText().toString());
                    Expense.setMoney(money.getText().toString());
                    Expense.setDate(date.getText().toString());
                    Expense.setTime(time.getText().toString());
                    Expense.setImg_uri("無照片");

                    ref.push().setValue(Expense);
                    Toast.makeText(MainActivity3.this,"新增成功",Toast.LENGTH_SHORT).show();
                }
                if (img_uri != null) {
                    uploadFile();
                    Toast.makeText(MainActivity3.this,"新增成功",Toast.LENGTH_SHORT).show();
                }
            }
        });

        date.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                date.setInputType(InputType.TYPE_NULL); //關閉軟鍵盤

                final Calendar d = Calendar.getInstance();
                int mYear = d.get(Calendar.YEAR);
                int mMonth = d.get(Calendar.MONTH);
                int mDay = d.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(MainActivity3.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String format = setDateFormat(year,month,dayOfMonth);
                        date.setText(format);
                    }
                }, mYear,mMonth, mDay).show();

                return false;
            }
        });

        InputMethodManager imm =
                (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(date.getWindowToken(),0);

        time.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                time.setInputType(InputType.TYPE_NULL); //關閉軟鍵盤

                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                new TimePickerDialog(MainActivity3.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay + "：" + minute);
                    }
                }, hour, minute, false).show();

                return false;
            }
        });

        InputMethodManager imm1 =
                (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm1.hideSoftInputFromWindow(time.getWindowToken(),0);
    }

    private void openFilechooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,pick_img);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==pick_img&&resultCode==RESULT_OK&&date!=null&&data.getData()!=null)
        {
            img_uri=data.getData();
            Picasso.with(this).load(img_uri).into(img);
        }
    }

    private void uploadFile(){
        if(img_uri!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("上傳中...");
            progressDialog.show();

            final StorageReference storageReference1=storageReference.child("expense/"+ UUID.randomUUID().toString());
            storageReference1.putFile(img_uri)
                    .addOnSuccessListener(this,new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri dlUri = uri;
                                    expense expense=new expense(name.getText().toString(),money.getText().toString(),date.getText().toString(),time.getText().toString(),dlUri.toString());
                                    ref.push().setValue(expense);
                                }
                            });
                            Toast.makeText(MainActivity3.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        }})
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity3.this,"失敗："+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("上傳了 "+(int)progress+"%");
                        }
                    });
        }
    }

    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }
}