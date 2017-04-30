package com.chem.enrico.chathead;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class mainActivity extends AppCompatActivity implements colorDialog.ColorSelectedListener {

    public final static int PERM_REQUEST_CODE_DRAW_OVERLAYS = 1;

    Button addChat, chatColor;

    Intent killingIntent;

    AppCompatSeekBar alphaBar, sizeBar;

    int alpha, size;

    TextView alphaText, sizeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize everything
        alpha = saveShit.getTransparency(getBaseContext());
        size = saveShit.getChatSize(getBaseContext());

        addChat = (Button) findViewById(R.id.add);

        chatColor = (Button) findViewById(R.id.cColor);
        alphaBar = (AppCompatSeekBar) findViewById(R.id.seekBarAlpha);
        sizeBar = (AppCompatSeekBar) findViewById(R.id.seekBarRadius);

        sizeText = (TextView) findViewById(R.id.size);
        alphaText = (TextView) findViewById(R.id.alpha);

        final String alphatext = "<b>" + getString(R.string.alpha) + "</b>" + alpha;

        setTextView(alphaText, alphatext);

        String sizetext = "<b>" + getString(R.string.size) + "</b>" + size;

        setTextView(sizeText, sizetext);

        alphaBar.setProgress(alpha);
        sizeBar.setProgress(size);

        // Add chat head button
        addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent notificationIntent = new Intent(mainActivity.this, listener.class);
                startService(notificationIntent);

                killingIntent = new Intent("addChatHead");

                sendBroadcast(killingIntent);

            }
        });

        // Chat head size SeekBar
        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            //int prog;
            int prog;

            String size;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                prog = progress;
                size = "<b>" + getString(R.string.size) + "</b>" + String.valueOf(prog);

                setTextView(sizeText, size);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (prog > 0) {

                    killingIntent = new Intent("chatSize");
                    killingIntent.putExtra("size", String.valueOf(prog));

                    saveShit.setChatSize(getBaseContext(), prog);
                    sendBroadcast(killingIntent);

                    setTextView(sizeText, size);

                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.zero), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        // Chat head alpha SeekBar
        alphaBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int prog;

            String alpha;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                prog = progress;

                if (prog == 0) {
                    Toast.makeText(getBaseContext(), getString(R.string.invisible), Toast.LENGTH_SHORT)
                            .show();
                }
                alpha = "<b>" + getString(R.string.alpha) + "</b>" + String.valueOf(prog);

                setTextView(alphaText, alpha);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (prog == 0) {
                    Toast.makeText(getBaseContext(), getString(R.string.invisible), Toast.LENGTH_SHORT)
                            .show();
                }

                killingIntent = new Intent("imageAlpha");
                killingIntent.putExtra("alpha", String.valueOf(prog));

                saveShit.setTransparency(getBaseContext(), prog);

                setTextView(alphaText, alpha);

                sendBroadcast(killingIntent);

            }
        });

        // Chat head color button
        chatColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                colorDialog.showColorPicker(mainActivity.this, 1);
            }
        });

        // Determine if we have overlay permissions, if not ask for permission
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(getBaseContext())) {
                Toast.makeText(getBaseContext(), getString(R.string.overlay), Toast.LENGTH_SHORT)
                        .show();
            } else {
                // if not construct intent to request permission
                final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                // request permission via start activity for result
                startActivityForResult(intent, PERM_REQUEST_CODE_DRAW_OVERLAYS);
            }
        }
    }


    // Color picker dialog
    @Override

    public void onColorSelection(DialogFragment dialogFragment, int color) {

        //Set the picker dialog's color
        colorDialog.setPickerColor(mainActivity.this, 1, color);

        killingIntent = new Intent("chatColor");

        killingIntent.putExtra("color", Integer.toString(color));

        sendBroadcast(killingIntent);
    }

    // Set alpha and size TextViews
    private void setTextView(TextView textView, String string) {

        if (Build.VERSION.SDK_INT >= 24) {
            textView.setText(Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY));
        } else {
            Html.fromHtml(string); // or for older api
        }
    }
}
