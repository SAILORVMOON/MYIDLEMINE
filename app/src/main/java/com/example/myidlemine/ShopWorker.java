package com.example.myidlemine;

import android.widget.ImageView;
import android.widget.TextView;

public class ShopWorker {
    ImageView buy, worker;
    TextView price;

    public ShopWorker() {
    }

    public ShopWorker(int price) {
        this.price.setText(price);
        this.buy.setImageResource(R.drawable.gnom);
        this.worker.setImageResource(R.drawable.gnom);
    }
}
