package com.example.myidlemine;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private ImageView main, shop, upgrade;
    private ListView listView1, listView2, listView3;
    private double prgrs = 0, multiplier = 1;
    private TextView money, level;
    private int factories = 1;
    Date date;
    Handler handler;
    long nowTime, defen, lastTime;
    int moneyInt, levelInt;
    DBData dbData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        dbData = new DBData(this);
        try {
            Data data = dbData.select(1);
        }catch (Exception e){
            dbData.insert(String.valueOf(0), String.valueOf(0), String.valueOf(0), String.valueOf(1), String.valueOf(1));
        }
        Data data = dbData.select(1);
        Log.d("MY", "получил"+String.valueOf(data.getId()));
        if(data.getFactories() == null){
            dbData.insert(String.valueOf(0), String.valueOf(0), String.valueOf(0), String.valueOf(1), String.valueOf(1));
            data = dbData.select(1);

        }
        levelInt = Integer.parseInt(data.getLevel());
        level.setText(String.valueOf(levelInt));
        moneyInt = Integer.parseInt(data.getMoney());
        money.setText(String.valueOf(moneyInt));
        prgrs = Double.parseDouble(data.getProgress());
        progressBar.setProgress((int) prgrs);
        factories = Integer.parseInt(data.getFactories());
        multiplier = Double.parseDouble(data.getMultiplier());
        TextView textView = findViewById(R.id.textView);
        textView.setText(data.getLevel() + data.getMoney() + data.getProgress() + data.getMultiplier() + data.getFactories());
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

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1){
                    case 1:{
                        moneyInt += msg.what * factories;
                        money.setText(String.valueOf(moneyInt));
                        prgrs += multiplier * factories * (defen);
                        progressBar.setProgress((int)prgrs);
                        defen = 0;
                        break;
                    }
                    case 2:{
                        break;
                    }
                    case 3:{
                        level.setText(String.valueOf(levelInt));
                        moneyInt += 50;
                        break;
                    }
                }


            }
        };
        SetterTime setterTime = new SetterTime();
        setterTime.start();
        CheckLevel checkLevel = new CheckLevel();
        checkLevel.start();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main: {
                prgrs += multiplier * factories;
                progressBar.setProgress((int) prgrs);
                money.setText(String.valueOf(moneyInt));
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
                defen = nowTime - lastTime;//разница секунд
                lastTime = date.getTime()/1000;//время которое будет старым
                Message msg = new Message();
                msg.arg1 = 1;
                msg.what = (int)defen;
                handler.sendMessage(msg);
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
                    Message msg = new Message();
                    msg.arg1 = 3;
                    handler.sendMessage(msg);
                    prgrs = 0;
                    progressBar.setProgress(0);
                    multiplier *= 0.9;
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    @Override
    protected void onDestroy() {

        int n = dbData.update(new Data(1, String.valueOf(levelInt), String.valueOf(moneyInt), String.valueOf(prgrs), String.valueOf(multiplier), String.valueOf(factories)));
        Log.d("MY", "обновил"+String.valueOf(n));
        super.onDestroy();
    }
}