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

    private OnClickNewGameListener onClickNewGameListener;
    private OnClickQuitListener onClickQuitListener;
    private DialogInterface.OnCancelListener onCancelListener;

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
                .setPositiveButton("Новая игра", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(onClickNewGameListener != null) {
                            onClickNewGameListener.action();
                        }
                    }
                })
                .setNegativeButton("Выход", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(onClickQuitListener != null) {
                            onClickQuitListener.action();
                        }
                    }
                })
                .create();




    }


    public void setOnClickNewGameListener(OnClickNewGameListener onClickNewGameListener) {
        this.onClickNewGameListener = onClickNewGameListener;
    }

    public void setOnClickQuitListener(OnClickQuitListener onClickQuitListener) {
        this.onClickQuitListener = onClickQuitListener;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    //interfaces
    interface OnClickNewGameListener {
        void action();
    }

    interface OnClickQuitListener {
        void action();
    }




}
