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

import java.util.ArrayList;
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
    DBDatame dbDatame;
    DBShopWorker dbShopWorker;
    ArrayList<ShopWorker> listToAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        dbDatame = new DBDatame(this);
        try {
            Datame datame = dbDatame.select(1);
        }catch (Exception e){
            dbDatame.insert(String.valueOf(0), String.valueOf(0), String.valueOf(0), String.valueOf(1), String.valueOf(1));
        }
        Datame datame = dbDatame.select(1);
        if(datame.getFactories() == null){
            dbDatame.insert(String.valueOf(0), String.valueOf(0), String.valueOf(0), String.valueOf(1), String.valueOf(1));
            datame = dbDatame.select(1);

        }
        dbShopWorker = new DBShopWorker(this);
        try {
            ShopWorker shopWorker = dbShopWorker.select(1);
        }catch (Exception e){
            dbShopWorker.insert(String.valueOf(1), String.valueOf(100));
            for (int i = 0; i < 49; i++) {
                dbShopWorker.insert(String.valueOf(0), String.valueOf(100*Math.pow(i, 2)));
            }
        }
        listToAdapter = new ArrayList<>();
        for (int i = 1; i < 51; i++) {
            ShopWorker shopWorker = dbShopWorker.select(i);
            listToAdapter.add(shopWorker);
        }
        ShopWorkerAdapter shopWorkerAdapter = new ShopWorkerAdapter(this, listToAdapter);
        listView2.setAdapter(shopWorkerAdapter);

        levelInt = Integer.parseInt(datame.getLevel());
        level.setText(String.valueOf(levelInt));
        moneyInt = Integer.parseInt(datame.getMoney());
        money.setText(String.valueOf(moneyInt));
        prgrs = Double.parseDouble(datame.getProgress());
        progressBar.setProgress((int) prgrs);
        factories = Integer.parseInt(datame.getFactories());
        multiplier = Double.parseDouble(datame.getMultiplier());
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
                listView2.setVisibility(View.VISIBLE);
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

        int n = dbDatame.update(new Datame(1, String.valueOf(levelInt), String.valueOf(moneyInt), String.valueOf(prgrs), String.valueOf(multiplier), String.valueOf(factories)));
        Log.d("MY", "обновил"+String.valueOf(n));
        super.onDestroy();
    }
}