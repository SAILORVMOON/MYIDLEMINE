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

public class BoosterAdapter extends BaseAdapter {
    ArrayList<Booster> list;
    LayoutInflater inflater;
    int [] bst = new int[]{R.drawable.bstq, R.drawable.bstw, R.drawable.bste, R.drawable.bstr, R.drawable.bstt, R.drawable.bsty, R.drawable.bstu};
    String [] prices = new String[]{"100000", "125000", "160000", "215000", "285000", "370000", "450000"};
    String [] effects = new String[]{"X1.2", "X1.3", "X1.4", "X1.5", "X1.6", "X1.7", "X1.8"};

    public BoosterAdapter(Context context, ArrayList<Booster> list){
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
        view = inflater.inflate(R.layout.one_booster, viewGroup, false);
        Booster booster= (Booster) getItem(i);
        ImageView boosterIMG = view.findViewById(R.id.booster_img);
        boosterIMG.setImageResource(bst[i]);
        TextView price = view.findViewById(R.id.booster_price);
        price.setText(prices[i]);
        TextView effect = view.findViewById(R.id.effect);
        effect.setText(effects[i]);
        return view;
    }
}
