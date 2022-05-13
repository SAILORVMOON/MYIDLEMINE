package com.example.myidlemine;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public String firstName, firstPassword;
    EditText idName, idPassword;
    Button button;
    DBDatame dbDatame;
    Datame datame;
    boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idName = findViewById(R.id.idName);
        idPassword = findViewById(R.id.idPassword);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        dbDatame = new DBDatame(this);
        try {datame = dbDatame.select(1);}  catch (Exception ignored){check = false;}
        if (check){Intent intent = new Intent(this, Game.class);
            startActivity(intent);}
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            firstName = String.valueOf(idName.getText());
            firstPassword = String.valueOf(idPassword.getText());
            dbDatame.insert(String.valueOf(0), String.valueOf(0), String.valueOf(0), String.valueOf(1), String.valueOf(1), firstName, firstPassword);
            datame = dbDatame.select(1);
            if (datame.getName() == null) {
                dbDatame.insert(String.valueOf(0), String.valueOf(0), String.valueOf(0), String.valueOf(1), String.valueOf(1), firstName, firstPassword);
                datame = dbDatame.select(1);
            }
            Intent intent = new Intent(this, Game.class);
            startActivity(intent);
        }
    }
}