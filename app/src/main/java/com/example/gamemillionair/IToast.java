package com.example.gamemillionair;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public interface IToast {
    int OFFSET_X = 0;
    int OFFSET_Y = 200;

    default void showToast(Context context, String message, int colorCode) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        View view = toast.getView();
        view.setBackgroundColor(colorCode);
        toast.setGravity(Gravity.TOP, OFFSET_X, OFFSET_Y);
        toast.show();
    }

    default void showToast(Context context, String message) {
        showToast(context, message, 0xFFE91E63);
    }
}
