package com.example.myidlemine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class Game extends AppCompatActivity implements View.OnClickListener {

    String firstName, firstPassword;

    int [] bst = new int[]{R.drawable.bstq, R.drawable.bstw, R.drawable.bste, R.drawable.bstr, R.drawable.bstt, R.drawable.bsty, R.drawable.bstu};
    String [] prices = new String[]{"100000", "125000", "160000", "215000", "285000", "370000", "450000"};
    String [] effects = new String[]{"1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8"};

    private ProgressBar progressBar;
    private ImageView main, shop, upgrade, coin;
    private ListView listViewBoosters, listViewShop;
    private double prgrs = 0, multiplier = 1, workerMultiplier = 1;
    private TextView money, level;
    Date date;
    Handler handler, handler2;
    long nowTime, defen, lastTime;
    int levelInt;
    DBDatame dbDatame;
    DBShopWorker dbShopWorker;
    ArrayList<ShopWorker> listToAdapter;
    ArrayList<Booster> boosters;
    LinearLayout lin_shop, lin_boosters, linarrows;
    ShopWorkerAdapter shopWorkerAdapter;
    boolean check = false;
    double boost = 1.0, moneyInt;
    Button rules;
    RulesDialog rulesDialog;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init();
        listViewShop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(moneyInt-Integer.parseInt(listToAdapter.get(position).price)<0){
                    Toast.makeText(Game.this, "Это всё твоё, дружище, но только накопи сначала шекелей", Toast.LENGTH_LONG).show();
                }else{
                    moneyInt -= Long.parseLong(listToAdapter.get(position).price);
                    listToAdapter.get(position).level = String.valueOf((int) (Integer.parseInt(listToAdapter.get(position).level) + 1));
                    listToAdapter.get(position).price = String.valueOf((int)(Long.parseLong(listToAdapter.get(position).price) * (1 + 0.1 * Math.pow(position + 1, 2))));
                    shopWorkerAdapter.notifyDataSetChanged();
                    updWrkMlt();
                }

            }
        });
        updWrkMlt();
        boosters = new ArrayList<>();
        for (int i = 0; i < bst.length; i++) {
            Booster booster = new Booster(bst[i], prices[i], effects[i]);
            boosters.add(booster);
        }
        BoosterAdapter boosterAdapter = new BoosterAdapter(this, boosters);
        listViewBoosters.setAdapter(boosterAdapter);
        listViewBoosters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message msg = new Message();
                msg.arg1 = 10;
                msg.what = i;
                handler2.sendMessage(msg);
            }
        });
        rules = findViewById(R.id.rules);
        rules.setOnClickListener(this);
        rulesDialog = new RulesDialog();
        manager = getSupportFragmentManager();
    }

    protected void init() {
        lin_shop = findViewById(R.id.lin_shop);
        lin_boosters = findViewById(R.id.lin_boosters);
        linarrows = findViewById(R.id.linarrows);
        level = findViewById(R.id.level);
        progressBar = findViewById(R.id.progressBar);
        money = findViewById(R.id.money);
        listViewBoosters = findViewById(R.id.listview_boosters);
        listViewShop = findViewById(R.id.listview_shop);
        main = findViewById(R.id.boosters);
        shop = findViewById(R.id.shop);
        upgrade = findViewById(R.id.upgrade);
        main.setOnClickListener(this);
        shop.setOnClickListener(this);
        upgrade.setOnClickListener(this);
        coin = findViewById(R.id.coin);
        coin.setOnClickListener(this);
        dbDatame = new DBDatame(this);
        Datame datame;
        try {
            datame = dbDatame.select(1);
        }catch (Exception e){
            dbDatame.insert(String.valueOf(0), String.valueOf(0), String.valueOf(0), String.valueOf(1), String.valueOf(1), firstName, firstPassword);
            datame = dbDatame.select(1);
        }
        if(datame.getName() == null){
            dbDatame.insert(String.valueOf(0), String.valueOf(0), String.valueOf(0), String.valueOf(1), String.valueOf(1), firstName, firstPassword);
            datame = dbDatame.select(1);
        }
        levelInt = Integer.parseInt(datame.getLevel());
        level.setText(String.valueOf(levelInt));
        moneyInt = Integer.parseInt(datame.getMoney());
        money.setText(String.valueOf(moneyInt));
        prgrs = Double.parseDouble(datame.getProgress());
        progressBar.setProgress((int) prgrs);
        multiplier = Double.parseDouble(datame.getMultiplier());


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
                        moneyInt += msg.what * workerMultiplier * boost;
                        money.setText(String.valueOf((int) moneyInt));
                        prgrs += multiplier * (defen) * boost;
                        progressBar.setProgress((int)prgrs);
                        defen = 0;
                        break;
                    }
                    case 2:{
                        break;
                    }
                    case 3:{
                        level.setText(String.valueOf(levelInt));
                        moneyInt += 50*levelInt;
                        break;
                    }
                }
            }
        };
        SetterTime setterTime = new SetterTime();
        setterTime.start();
        CheckLevel checkLevel = new CheckLevel();
        checkLevel.start();
        handler2 = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1){
                    case 10:{
                        if (moneyInt-Integer.parseInt(prices[msg.what])<0){
                            Toast.makeText(Game.this, "Это всё твоё, дружище, но только накопи сначала шекелей", Toast.LENGTH_LONG).show();
                        }else{
                            if (!check){
                                boost = Double.parseDouble(effects[msg.what]);
                                BoosterWork boosterWork = new BoosterWork();
                                boosterWork.start();
                                moneyInt -= Integer.parseInt(prices[msg.what]);
                            }else {
                                Toast.makeText(Game.this, "no", Toast.LENGTH_SHORT).show();
                            }
                        }

                        break;
                    }
                }
            }
        };

        dbShopWorker = new DBShopWorker(this);
        try {
            ShopWorker shopWorker = dbShopWorker.select(1);
        }catch (Exception e){
            dbShopWorker.insert(String.valueOf(1), String.valueOf(100));
            for (int i = 2; i < 11; i++) {
                dbShopWorker.insert(String.valueOf(0), String.valueOf((int)(100*Math.pow(i, 2))));
            }
        }
        listToAdapter = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            ShopWorker shopWorker = dbShopWorker.select(i);
            listToAdapter.add(shopWorker);
        }
        shopWorkerAdapter = new ShopWorkerAdapter(this, listToAdapter);
        listViewShop.setAdapter(shopWorkerAdapter);

    }

    void updWrkMlt (){
        workerMultiplier = 1;
        for (int i = 0; i < listToAdapter.size(); i++) {
            workerMultiplier += 0.1 *(i+1) * Integer.parseInt(listToAdapter.get(i).level);
        }
        workerMultiplier += levelInt * 0.01;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop: {
                lin_shop.setVisibility(View.VISIBLE);
                lin_boosters.setVisibility(View.GONE);
                linarrows.setVisibility(View.GONE);
                break;
            }
            case R.id.boosters: {
                lin_shop.setVisibility(View.GONE);
                lin_boosters.setVisibility(View.VISIBLE);
                linarrows.setVisibility(View.GONE);
                break;
            }

            case R.id.upgrade: {
                lin_shop.setVisibility(View.GONE);
                lin_boosters.setVisibility(View.GONE);
                linarrows.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.coin:{
                moneyInt+=1;
                break;
            }
            case R.id.rules:{
                rulesDialog.show(manager, "rules");
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
        int n = dbDatame.update(new Datame(1, String.valueOf(levelInt), String.valueOf((int)moneyInt), String.valueOf(prgrs), String.valueOf(multiplier), null, null));
        super.onDestroy();
        //TODO
        for (int i = 1; i < 11; i++) {
            ShopWorker shopWorker = listToAdapter.get(i-1);
            dbShopWorker.update(shopWorker);
        }
    }
    class BoosterWork extends Thread{
        @Override
        public void run() {
            check = true;
            try {
                sleep(900000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boost = 1;
            check = false;
        }
    }
    @Override
    public void onBackPressed() {
    }
}
