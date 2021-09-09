package com.example.gamemillionaire;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.widget.Toast;

import com.example.gamemillionair.R;

public class DialogFragmentGameResult extends DialogFragment implements IConst {

    private OnClickNewGameListener onClickNewGameListener;
    private OnClickQuitListener onClickQuitListener;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        assert getArguments() != null;
        Game game = (Game) getArguments().getSerializable(KEY_GAME);

        String stringWordAnswer = "вопросов";
        if(game.getNumAnswer() == 1) {
            stringWordAnswer = "вопрос";
        } else if(game.getNumAnswer() == 2 || game.getNumAnswer() == 3 || game.getNumAnswer() == 4) {
            stringWordAnswer = "вопроса";
        }

        @SuppressLint("DefaultLocale") String message = String.format("Вы правильно ответили на %d %s и выиграли %d %s", game.getNumAnswer(),
                stringWordAnswer,
                game.getWin(),
                MONEY_SIGN
                );


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setMessage(message)
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

    //interfaces
    interface OnClickNewGameListener {
        void action();
    }

    interface OnClickQuitListener {
        void action();
    }


}
