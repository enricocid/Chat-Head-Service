package com.chem.enrico.chathead;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class colorDialog extends DialogFragment {

    //palettes
    ImageView red, pink, purple, deepPurple, indigo, blue, lightBlue, cyan, teal, green, lightGreen, lime, yellow, amber, orange, deepOrange, brown, grey, blueGrey, darkGrey;

    //seekbars
    SeekBar first, second, third;

    //r g b
    String erre, gi, bi;

    //color and alpha
    int RGB;

    //edittexts
    EditText editHEX, editR, editG, editB;

    //the views
    View dialogView, colorView;

    //textviews
    TextView hashtext, hashtag, rgb;

    //textwatchers
    TextWatcher hexTextWatcher, rTextWatcher, gTextWatcher, bTextWatcher;

    private ColorSelectedListener mColorSelectedListener;

    public colorDialog() {

    }

    //show dialog
    public static void showColorPicker(AppCompatActivity activity, int tag) {

        colorDialog dialog = new colorDialog();

        dialog.show(activity.getSupportFragmentManager(), String.valueOf(tag));
    }

    //shift down color a bit
    public static int shiftColor(int color, float fraction) {

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= fraction;

        return Color.HSVToColor(hsv);
    }

    //for activities

    //get complementary color
    public static int getComplementaryColor(int colorToInvert) {

        int r = Color.red(colorToInvert);
        int g = Color.green(colorToInvert);
        int b = Color.blue(colorToInvert);
        int red = 255 - r;
        int green = 255 - g;
        int blue = 255 - b;

        return android.graphics.Color.argb(255, red, green, blue);

    }

    //save color
    public static void setPickerColor(Context context, int dialogNumber, int color) {

        SharedPreferences preferenceColor;
        preferenceColor = context.getSharedPreferences(String.valueOf(dialogNumber), Context.MODE_PRIVATE);


        preferenceColor.edit()
                .clear()
                .apply();

        preferenceColor.edit()
                .putString("selectedColor", Integer.toString(color))
                .apply();
    }

    //retrieve color
    public static int getPickerColor(final Context context, final int dialogNumber) {

        Resources resources = context.getResources();
        SharedPreferences preferenceColor = context.getSharedPreferences(String.valueOf(dialogNumber), Context.MODE_PRIVATE);
        String colorValue = preferenceColor.getString("selectedColor", String.valueOf(colorUtils.randomColor(resources)));

        final int color = Integer.parseInt(colorValue);

        setPickerColor(context, dialogNumber, color);

        return color;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //dialog view
        final ViewGroup nullParent = null;

        dialogView = getActivity().getLayoutInflater().inflate(R.layout.color_dialog, nullParent);

        mColorSelectedListener = (ColorSelectedListener) getActivity();

        //format colors textviews
        hashtext = (TextView) dialogView.findViewById(R.id.hashtext);
        hashtag = (TextView) dialogView.findViewById(R.id.hashtag);
        rgb = (TextView) dialogView.findViewById(R.id.rgb);

        //format colors editext
        editHEX = (EditText) dialogView.findViewById(R.id.hex);
        editR = (EditText) dialogView.findViewById(R.id.r);
        editG = (EditText) dialogView.findViewById(R.id.g);
        editB = (EditText) dialogView.findViewById(R.id.b);

        //seekbars
        first = (SeekBar) dialogView.findViewById(R.id.first);
        second = (SeekBar) dialogView.findViewById(R.id.second);
        third = (SeekBar) dialogView.findViewById(R.id.third);

        //the view
        colorView = dialogView.findViewById(R.id.valuesView);

        //palettes
        red = (ImageView) dialogView.findViewById(R.id.red);
        pink = (ImageView) dialogView.findViewById(R.id.pink);
        purple = (ImageView) dialogView.findViewById(R.id.purple);
        deepPurple = (ImageView) dialogView.findViewById(R.id.deepPurple);
        indigo = (ImageView) dialogView.findViewById(R.id.indigo);
        blue = (ImageView) dialogView.findViewById(R.id.blue);
        lightBlue = (ImageView) dialogView.findViewById(R.id.lightBlue);
        cyan = (ImageView) dialogView.findViewById(R.id.cyan);
        teal = (ImageView) dialogView.findViewById(R.id.teal);
        green = (ImageView) dialogView.findViewById(R.id.green);
        lightGreen = (ImageView) dialogView.findViewById(R.id.lightGreen);
        lime = (ImageView) dialogView.findViewById(R.id.lime);
        yellow = (ImageView) dialogView.findViewById(R.id.yellow);
        amber = (ImageView) dialogView.findViewById(R.id.amber);
        orange = (ImageView) dialogView.findViewById(R.id.orange);
        deepOrange = (ImageView) dialogView.findViewById(R.id.deepOrange);
        brown = (ImageView) dialogView.findViewById(R.id.brown);
        grey = (ImageView) dialogView.findViewById(R.id.grey);
        blueGrey = (ImageView) dialogView.findViewById(R.id.blueGrey);
        darkGrey = (ImageView) dialogView.findViewById(R.id.darkGrey);

        //get r g b values
        erre = Integer.toString(first.getProgress());
        gi = Integer.toString(second.getProgress());
        bi = Integer.toString(third.getProgress());

        //retrieve alpha and color from preferences

        RGB = getPickerColor(getActivity(), Integer.valueOf(getTag()));

        //text watchers used to listen to edittext changes
        hexTextWatcher = new hexTextWatcher(getActivity(), colorView, hashtext, hashtag, rgb, editHEX, editR, editG, editB, first, second, third);
        rTextWatcher = new rgbTextWatcher(getActivity(), editR, first);
        gTextWatcher = new rgbTextWatcher(getActivity(), editG, second);
        bTextWatcher = new rgbTextWatcher(getActivity(), editB, third);

        //build the dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                seekbarUtils.initializeSeekBars(getActivity(), hexTextWatcher, rTextWatcher, gTextWatcher, bTextWatcher, first, second, third, colorView, hashtext, hashtag, rgb, editHEX, editR, editG, editB);
                paletteUtils.initializeMaterialPalette(getActivity(), getResources(), colorView, hashtext, hashtag, rgb, editHEX, editR, editG, editB, red, pink, purple, deepPurple, indigo, blue, lightBlue, cyan, teal, green, lightGreen, lime, yellow, amber, orange, deepOrange, brown, grey, blueGrey, darkGrey);
                editsUtils.initializeEditTextTouchListeners(editHEX, editR, editG, editB);
                editsUtils.initializeEditTextChangeListeners(hexTextWatcher, rTextWatcher, gTextWatcher, bTextWatcher, editHEX, editR, editG, editB);
                viewUtils.updateColorOnResume(getActivity(), colorView, hashtext, hashtag, rgb, editHEX, editR, editG, editB, erre, gi, bi, RGB);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mColorSelectedListener.onColorSelection(colorDialog.this, Color.argb(255, first.getProgress(), second.getProgress(), third.getProgress()));

                        dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);

            }
        });

        builder.setView(dialogView);

        return builder.create();

    }

    interface ColorSelectedListener {
        void onColorSelection(DialogFragment dialogFragment, @ColorInt int selectedColor);
    }
}

