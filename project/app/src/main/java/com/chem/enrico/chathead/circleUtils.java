package com.chem.enrico.chathead;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;


/**
 * Created by Enrico on 24/04/2017.
 */

class circleUtils {

    static void createCircularBitmap(Context context, Resources resources, ImageView imageView, int circleColor, int radius) {

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(SpToPixels(context, radius), SpToPixels(context, radius), conf);

        imageView.setImageDrawable(createRoundedBitmapDrawable(bmp, circleColor, resources));
    }

    private static RoundedBitmapDrawable createRoundedBitmapDrawable(Bitmap bitmap, int circleColor, Resources mResources) {

        Bitmap b1;

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // Calculate the bitmap radius
        int bitmapRadius = Math.min(bitmapWidth, bitmapHeight) / 2;

        int bitmapSquare = Math.min(bitmapWidth, bitmapHeight);

        // Initializing a new empty bitmap.
        Bitmap roundedBitmap = Bitmap.createBitmap(bitmapSquare, bitmapSquare, Bitmap.Config.ARGB_8888);

        // Initialize a new Canvas to draw empty bitmap
        Canvas canvas = new Canvas(roundedBitmap);

        // Draw a solid color to canvas
        canvas.drawColor(circleColor);


        // Calculation to draw bitmap at the circular bitmap center position
        int x = bitmapSquare - bitmapWidth;
        int y = bitmapSquare - bitmapHeight;

        // Draw the bitmap to canvas
        // Bitmap will draw its center to circular bitmap center by keeping border spaces
        canvas.drawBitmap(bitmap, x, y, null);

        Paint p = new Paint();

        // Set paint color according to view color to make it always visible
        double darkness = 1 - (0.299 * Color.red(circleColor) + 0.587 * Color.green(circleColor) + 0.114 * Color.blue(circleColor)) / 255;

        if (darkness < 0.5) {

            b1 = BitmapFactory.decodeResource(mResources, R.drawable.ic_human_greeting_black_24dp);

        } else {
            b1 = BitmapFactory.decodeResource(mResources, R.drawable.ic_human_greeting_white_24dp);

        }

        //image size follow the circle radius
        Bitmap b2 = getResizedBitmap(b1, bitmapSquare / 2, bitmapSquare / 2);

        int x1 = (bitmapSquare - b2.getScaledWidth(canvas)) / 2;
        int y1 = (bitmapSquare - b2.getScaledHeight(canvas)) / 2;

        canvas.drawBitmap(b2, x1, y1, p);

        // Create a new RoundedBitmapDrawable
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, roundedBitmap);

        // Set the corner radius of the bitmap drawable
        roundedBitmapDrawable.setCornerRadius(bitmapRadius);

        roundedBitmapDrawable.setCircular(true);

        roundedBitmapDrawable.setAntiAlias(true);

        // Return the RoundedBitmapDrawable
        return roundedBitmapDrawable;
    }

    private static int SpToPixels(Context context, int width) {

        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        int density = Math.round(scaledDensity);
        return width * density;
    }

    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // Create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // Resize the bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // Recreate the new bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    static void createCircularDelete(Context context, Resources resources, ImageView imageView, int circleColor, int radius) {

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(SpToPixels(context, radius), SpToPixels(context, radius), conf);

        imageView.setImageDrawable(createRoundedBitmapDrawableCesso(bmp, circleColor, resources));
    }

    private static RoundedBitmapDrawable createRoundedBitmapDrawableCesso(Bitmap bitmap, int circleColor, Resources mResources) {

        Bitmap b1;

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // Calculate the bitmap radius
        int bitmapRadius = Math.min(bitmapWidth, bitmapHeight) / 2;

        int bitmapSquare = Math.min(bitmapWidth, bitmapHeight);

        // Initializing a new empty bitmap.
        Bitmap roundedBitmap = Bitmap.createBitmap(bitmapSquare, bitmapSquare, Bitmap.Config.ARGB_8888);

        // Initialize a new Canvas to draw empty bitmap
        Canvas canvas = new Canvas(roundedBitmap);

        // Draw a solid color to canvas
        canvas.drawColor(circleColor);


        // Calculation to draw bitmap at the circular bitmap center position
        int x = bitmapSquare - bitmapWidth;
        int y = bitmapSquare - bitmapHeight;

        // Draw the bitmap to canvas
        // Bitmap will draw its center to circular bitmap center by keeping border spaces
        canvas.drawBitmap(bitmap, x, y, null);

        Paint p = new Paint();
        
        b1 = BitmapFactory.decodeResource(mResources, R.drawable.ic_delete_forever_white_24dp);

        //image size follow the circle radius
        Bitmap b2 = getResizedBitmap(b1, bitmapSquare / 2, bitmapSquare / 2);

        int x1 = (bitmapSquare - b2.getScaledWidth(canvas)) / 2;
        int y1 = (bitmapSquare - b2.getScaledHeight(canvas)) / 2;

        canvas.drawBitmap(b2, x1, y1, p);

        // Create a new RoundedBitmapDrawable
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, roundedBitmap);

        // Set the corner radius of the bitmap drawable
        roundedBitmapDrawable.setCornerRadius(bitmapRadius);

        roundedBitmapDrawable.setCircular(true);

        roundedBitmapDrawable.setAntiAlias(true);

        // Return the RoundedBitmapDrawable
        return roundedBitmapDrawable;
    }
}
