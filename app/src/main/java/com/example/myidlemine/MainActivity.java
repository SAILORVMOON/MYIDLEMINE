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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private ImageView main, shop, upgrade;
    private ListView listView1, listView2, listView3;
    private double prgrs = 0, multipler = 1;
    private TextView money, level;
    private int factories = 1;
    Date date;
    Handler handler, handler2, handler3;
    long nowTime, defen, lastTime;
    int moneyInt, levelInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = (int) (size.y * 0.15);
        int width = (int) (size.x * 0.34);
        main.getLayoutParams().height = height;
        main.getLayoutParams().width = width;
        shop.getLayoutParams().height = height;
        shop.getLayoutParams().width = width;
        upgrade.getLayoutParams().height = height;
        upgrade.getLayoutParams().width = width;
    }


    protected void init() {

        progressBar = findViewById(R.id.progressBar);
        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        shop = findViewById(R.id.shop);
        shop.setOnClickListener(this);
        upgrade = findViewById(R.id.upgrade);
        listView1 = findViewById(R.id.listview1);
        listView2 = findViewById(R.id.listview2);
        listView3 = findViewById(R.id.listview3);
        money = findViewById(R.id.money);
        level = findViewById(R.id.level);
        date = new Date();
        lastTime = date.getTime()/1000;

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                moneyInt += msg.what * factories;

                money.setText(String.valueOf(moneyInt));
            }
        };
        handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                prgrs += multipler * factories * (defen);
            }
        };
        handler3 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                level.setText(levelInt);
            }
        };
        SetterTime setterTime = new SetterTime();
        setterTime.start();
        AutoAdd autoAdd = new AutoAdd();
        autoAdd.start();
        CheckLevel checkLevel = new CheckLevel();
        checkLevel.start();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main: {
                prgrs += multipler * factories;
                progressBar.setProgress((int) prgrs);
                money.setText(String.valueOf(money));
                break;
            }
            case R.id.shop: {
                factories ++;
                break;
            }
            case R.id.upgrade: {
                break;
            }
        }
    }

    class SetterTime extends Thread {
        @Override
        public void run() {
            while (true) {
                date = new Date();//новое время
                nowTime = date.getTime()/1000;//сейчашние секунды
                if(lastTime - nowTime != 0){
                    defen = nowTime - lastTime;//разница секунд
                }
                lastTime = date.getTime()/1000;//время которое будет старым
                handler.sendEmptyMessage((int)defen);
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class AutoAdd extends Thread{
        @Override
        public void run() {
            while (true){
                handler2.sendEmptyMessage((int) defen);
                defen = 0;
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    class CheckLevel extends Thread{
        @Override
        public void run() {
            while (true){
                if (progressBar.getProgress() >= 100) {
                    levelInt ++;
                    handler3.sendEmptyMessage(1);
                    prgrs = 0;
                    progressBar.setProgress(0);
                    multipler *= 0.9;
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}