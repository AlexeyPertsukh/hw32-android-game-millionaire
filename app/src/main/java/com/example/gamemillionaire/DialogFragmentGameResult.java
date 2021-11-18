package com.example.gamemillionaire;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;

import com.example.gamemillionair.R;
import com.example.gamemillionaire.constants.IConst;
import com.example.gamemillionaire.model_game_with_listeners.GameWithListeners;

public class DialogFragmentGameResult extends DialogFragment implements IConst, IToast {

    private DialogInterface.OnClickListener onClickNewGameListener;
    private DialogInterface.OnClickListener onClickQuitListener;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        assert getArguments() != null;
        GameWithListeners game = (GameWithListeners) getArguments().getSerializable(KEY_GAME);

        int numAnswer = game.getNumCorrectAnswers();
        String stringWordAnswer = "вопросов";
        if(numAnswer == 1) {
            stringWordAnswer = "вопрос";
        } else if(numAnswer == 2 || numAnswer == 3 || numAnswer == 4) {
            stringWordAnswer = "вопроса";
        }

        @SuppressLint("DefaultLocale") String message = String.format("Вы правильно ответили на %d %s и выиграли %d %s", numAnswer,
                stringWordAnswer,
                game.getWinningAmount(),
                MONEY_SIGN
                );


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Новая игра", onClickNewGameListener)
                .setNegativeButton("Выход", onClickQuitListener)
                .create();
    }

    public void setOnClickNewGameListener(DialogInterface.OnClickListener onClickNewGameListener) {
        this.onClickNewGameListener = onClickNewGameListener;
    }

    public void setOnClickQuitListener(DialogInterface.OnClickListener onClickQuitListener) {
        this.onClickQuitListener = onClickQuitListener;
    }
}
