package com.example.myidlemine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShopWorkerAdapter extends BaseAdapter {

    ArrayList<ShopWorker> list;
    LayoutInflater inflater;

    public ShopWorkerAdapter(Context context, ArrayList<ShopWorker> list){
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.one_shop_worker, viewGroup, false);
        ShopWorker shopWorker = (ShopWorker) getItem(i);
        ImageView imageView = view.findViewById(R.id.worker);
        if (shopWorker.getLevel().equals("0")){
            imageView.setImageResource(R.drawable.sad);
        }else {
            imageView.setImageResource(R.drawable.happy);
        }
        TextView textView = view.findViewById(R.id.price);
        textView.setText(shopWorker.getPrice());
        return view;
    }
}
