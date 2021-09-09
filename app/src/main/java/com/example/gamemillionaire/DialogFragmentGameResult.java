package com.example.gamemillionaire;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;

public class DialogFragmentGameResult extends DialogFragment implements IConst {
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Game game = (Game) getArguments().getSerializable(KEY_GAME);

        String message = "Вы ответили на " + game.getStep() + " вопросов\n" +
                    "и выиграли " + game.getWin();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("Новая игра", null)
                .setNegativeButton("Выход", null)
                .create();
    }
}
