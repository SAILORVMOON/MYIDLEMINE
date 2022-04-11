package com.example.myidlemine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    ScrollView scrollView;
    ImageView main, shop, upgrade;
    ListView listView1, listView2, listView3;
    double prgrs = 0;
    TextView textView;
    double multier = 1;
    long time;
    String sec;
    Date date;
    MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        main.getLayoutParams().height = (int) (height*0.15);
        main.getLayoutParams().width = (int) (width*0.34);
        shop.getLayoutParams().height = (int) (height*0.15);
        shop.getLayoutParams().width = (int) (width*0.34);
        upgrade.getLayoutParams().height = (int) (height*0.15);
        upgrade.getLayoutParams().width = (int) (width*0.34);
    }




    protected void init(){
        progressBar = findViewById(R.id.progressBar);
        scrollView = findViewById(R.id.scrollView2);
        main = (ImageView) findViewById(R.id.main);
        main.setOnClickListener(this);
        shop = findViewById(R.id.shop);
        upgrade = findViewById(R.id.upgrade);
        listView1 = findViewById(R.id.listview1);
        listView2 = findViewById(R.id.listview2);
        listView3 = findViewById(R.id.listview3);
        textView = findViewById(R.id.textView);
        date = new Date();
        sec = "";
        AutoAdd autoAdd = new AutoAdd();
        autoAdd.start();
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main:{
                prgrs+= multier;
                progressBar.setProgress((int) prgrs);
                if(progressBar.getProgress() == 100){
                    prgrs = 0;
                    progressBar.setProgress(0);
                    multier *= 0.9;
                }
                textView.setText(sec);
                break;
            }
            case R.id.shop:{
                break;
            }
            case R.id.upgrade:{
                break;
            }
        }
    }

    class AutoAdd extends Thread{
        @Override
        public void run() {

            while(true){
                date = new Date();
                time = date.getTime();
                sec = String.valueOf(time);
            }
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            int i = 0;
            while (i < 10){

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            textView.setText(sec);
        }
    }
}