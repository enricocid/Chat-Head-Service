package com.chem.enrico.chathead;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

class seekbarUtils {


    //color seekbars
    private static void initializeSeekBarsColors(SeekBar... seekBars) {

        for (SeekBar argb : seekBars) {

            final String tag = argb.getTag().toString();
            final int color = Color.parseColor(tag);

            argb.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            argb.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        }
    }

    //initialize seekbars
    static void initializeSeekBars(final Activity activity, final TextWatcher hexTextWatcher, final TextWatcher rTextWatcher, final TextWatcher gTextWatcher, final TextWatcher bTextWatcher, final SeekBar first, final SeekBar second, final SeekBar third, final View colorView, final TextView hashtext, final TextView hashtag, final TextView rgb, final EditText editHEX, final EditText editR, final EditText editG, final EditText editB) {

        initializeSeekBarsColors(first, second, third);

        first.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            int RGB;
            String updatedValue;

            @Override

            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {

                progress = progressValue;
                updatedValue = Integer.toString(progressValue);
                RGB = android.graphics.Color.argb(255, progress, second.getProgress(), third.getProgress());
                viewUtils.updateColorView(activity, colorView, hashtext, hashtag, rgb, editHEX, editR, editG, editB, RGB);
                viewUtils.updateRGBtexts(activity, updatedValue, editR);

            }

            @Override

            public void onStartTrackingTouch(SeekBar seekBar) {

                editsUtils.disableEditText(editHEX, editR, rTextWatcher, hexTextWatcher);

            }

            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {

                editsUtils.enableEditText(editHEX, editR, rTextWatcher, hexTextWatcher);


            }

        });

        second.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            int RGB;
            String updatedValue;


            @Override

            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {

                progress = progressValue;
                updatedValue = Integer.toString(progressValue);
                RGB = android.graphics.Color.argb(255, first.getProgress(), progress, third.getProgress());
                viewUtils.updateColorView(activity, colorView, hashtext, hashtag, rgb, editHEX, editR, editG, editB, RGB);
                viewUtils.updateRGBtexts(activity, updatedValue, editG);

            }

            @Override

            public void onStartTrackingTouch(SeekBar seekBar) {

                editsUtils.disableEditText(editHEX, editG, gTextWatcher, hexTextWatcher);


            }

            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {

                editsUtils.enableEditText(editHEX, editG, gTextWatcher, hexTextWatcher);


            }

        });

        third.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            int RGB;
            String updatedValue;


            @Override

            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {

                progress = progressValue;
                updatedValue = Integer.toString(progressValue);
                RGB = android.graphics.Color.argb(255, first.getProgress(), second.getProgress(), progress);

                viewUtils.updateColorView(activity, colorView, hashtext, hashtag, rgb, editHEX, editR, editG, editB, RGB);
                viewUtils.updateRGBtexts(activity, updatedValue, editB);

            }

            @Override

            public void onStartTrackingTouch(SeekBar seekBar) {
                editsUtils.disableEditText(editHEX, editB, bTextWatcher, hexTextWatcher);


            }

            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {

                editsUtils.enableEditText(editHEX, editB, bTextWatcher, hexTextWatcher);

            }
        });
    }

    static int updateSeekBarProgress(String RorGorB) {

        return Integer.parseInt(RorGorB);
    }

    static void updateSeekBarValue(final SeekBar RorGorB, final int value) {

        RorGorB.setProgress(value);
    }
}