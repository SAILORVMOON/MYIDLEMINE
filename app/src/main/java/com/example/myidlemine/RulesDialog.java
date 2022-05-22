package com.example.myidlemine;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class RulesDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ЦЕЛЬ ИГРЫ")
                .setMessage("Цель игры заключается в покупке шахтёров и их улучшении. Шахтёры добывают деньги, за которые можно покупать самих шахтеров или бустеры для временного увеличения прибыли. Нижние шахтёры приносят больше прибыли.Бустеры действуют 15 минут.\nЧем больше накопишь денег, тем лучше.\nТак что, ВПЕРЁД!!!")
                .setIcon(R.drawable.scroll)
                .setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Закрываем окно
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
