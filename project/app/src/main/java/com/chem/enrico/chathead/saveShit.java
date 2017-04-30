package com.chem.enrico.chathead;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Enrico on 28/04/2017.
 */

class saveShit {

    //save transparency
    static void setTransparency(Context context, int alpha) {

        SharedPreferences preferenceColor;
        preferenceColor = context.getSharedPreferences(String.valueOf("alphaChange"), Context.MODE_PRIVATE);


        preferenceColor.edit()
                .clear()
                .apply();

        preferenceColor.edit()
                .putString("selectedAlpha", Integer.toString(alpha))
                .apply();
    }

    //retrieve border
    static int getTransparency(Context context) {

        SharedPreferences preferenceColor = context.getSharedPreferences("alphaChange", Context.MODE_PRIVATE);
        String alphaValue = preferenceColor.getString("selectedAlpha", String.valueOf(200));

        return Integer.parseInt(alphaValue);
    }


    //save radius
    static void setChatSize(Context context, int radius) {

        SharedPreferences preferenceColor;
        preferenceColor = context.getSharedPreferences(String.valueOf("sizeChange"), Context.MODE_PRIVATE);


        preferenceColor.edit()
                .clear()
                .apply();

        preferenceColor.edit()
                .putString("selectedSize", Integer.toString(radius))
                .apply();
    }

    //retrieve radius
    static int getChatSize(Context context) {

        SharedPreferences preferenceColor = context.getSharedPreferences("sizeChange", Context.MODE_PRIVATE);
        String radiusValue = preferenceColor.getString("selectedSize", String.valueOf(60));

        return Integer.parseInt(radiusValue);
    }

}
