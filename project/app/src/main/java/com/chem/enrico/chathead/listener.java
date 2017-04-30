package com.chem.enrico.chathead;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


public class listener extends Service {

    Notification serviceNotification;
    WindowManager wm;
    Intent broadcastIntent;

    View topLeftView;
    ImageView overlayButton, overlayDelete;

    BroadcastReceiver mReceiver;

    int alpha;
    private float offsetX, offsetY;
    private int originalXPos, originalYPos, chatColor, size;
    private boolean moving;

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        //the service will be restarted if killed
        broadcastIntent = new Intent("dontKillMe");
        sendBroadcast(broadcastIntent);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Make a foreground notification to avoid android kills the service
        serviceNotification = new NotificationCompat.Builder(this)
                .setOngoing(false)
                .build();
        startForeground(101, serviceNotification);

        // Register receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("chatColor");
        filter.addAction("imageAlpha");
        filter.addAction("chatSize");
        filter.addAction("addChatHead");

        mReceiver = new killReceiver();
        registerReceiver(mReceiver, filter);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize ImageViews
        overlayButton = new ImageView(getBaseContext());
        overlayDelete = new ImageView(getBaseContext());

        // Don't add chat head if already there
        if (overlayButton.isAttachedToWindow()) {
            Toast.makeText(getBaseContext(), getString(R.string.already), Toast.LENGTH_SHORT)
                    .show();
        } else {
            initializeChatHead();
        }
    }

    // Determine if two views are overlapped
    private boolean isViewOverlapping(View firstView, View secondView) {
        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];

        firstView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        firstView.getLocationOnScreen(firstPosition);
        secondView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        secondView.getLocationOnScreen(secondPosition);

        return firstPosition[0] < secondPosition[0] + secondView.getMeasuredWidth()
                && firstPosition[0] + firstView.getMeasuredWidth() > secondPosition[0]
                && firstPosition[1] < secondPosition[1] + secondView.getMeasuredHeight()
                && firstPosition[1] + firstView.getMeasuredHeight() > secondPosition[1];
    }

    // Remove chat head
    private void removeChatHead(WindowManager wm, ImageView overlayButton, ImageView overlayDelete, View topLeftView) {

        wm.removeView(overlayButton);
        wm.removeView(overlayDelete);
        wm.removeView(topLeftView);
    }

    // Initialize chat head
    private void initializeChatHead() {

        // Get chat head color, alpha and size
        chatColor = colorDialog.getPickerColor(getBaseContext(), 1);
        alpha = saveShit.getTransparency(getBaseContext());
        size = saveShit.getChatSize(getBaseContext());

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        overlayButton.setImageAlpha(alpha);
        circleUtils.createCircularBitmap(getBaseContext(), getResources(), overlayButton, chatColor, saveShit.getChatSize(getBaseContext()));
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.START | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        wm.addView(overlayButton, params);

        topLeftView = new View(this);
        final WindowManager.LayoutParams topLeftParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.START | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);

        WindowManager.LayoutParams bottomMiddleParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        bottomMiddleParams.gravity = Gravity.BOTTOM | Gravity.CENTER;

        overlayDelete.setVisibility(View.GONE);
        bottomMiddleParams.y = 20;
        circleUtils.createCircularDelete(getBaseContext(), getResources(), overlayDelete, Color.RED, 50);
        wm.addView(overlayDelete, bottomMiddleParams);

        overlayButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Reveal delete button
                overlayDelete.setVisibility(View.VISIBLE);

                if (isViewOverlapping(overlayButton, overlayDelete)) {

                    removeChatHead(wm, overlayButton, overlayDelete, topLeftView);

                    Toast.makeText(getBaseContext(), getString(R.string.shit), Toast.LENGTH_SHORT)
                            .show();

                    stopForeground(true);
                    stopSelf();
                    Intent notificationService = new Intent(listener.this, listener.class);
                    stopService(notificationService);

                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    float x = event.getRawX();
                    float y = event.getRawY();

                    moving = false;

                    int[] location = new int[2];
                    overlayButton.getLocationOnScreen(location);

                    originalXPos = location[0];
                    originalYPos = location[1];

                    offsetX = originalXPos - x;
                    offsetY = originalYPos - y;

                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    int[] topLeftLocationOnScreen = new int[2];
                    topLeftView.getLocationOnScreen(topLeftLocationOnScreen);

                    float x = event.getRawX();
                    float y = event.getRawY();

                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) overlayButton.getLayoutParams();

                    int newX = (int) (offsetX + x);
                    int newY = (int) (offsetY + y);

                    if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                        return false;
                    }

                    params.x = newX - (topLeftLocationOnScreen[0]);
                    params.y = newY - (topLeftLocationOnScreen[1]);

                    wm.updateViewLayout(overlayButton, params);
                    moving = true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    overlayDelete.setVisibility(View.GONE);

                    if (moving) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    // use this as an inner class like here or as a top-level class
    public class killReceiver extends BroadcastReceiver {

        // constructor
        public killReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // Add chat head
            if (action.equals("addChatHead")) {

                if (overlayButton.isAttachedToWindow()) {

                    Toast.makeText(getBaseContext(), getString(R.string.already), Toast.LENGTH_SHORT)
                            .show();

                } else {

                    Intent notificationService = new Intent(listener.this, listener.class);
                    startService(notificationService);

                }
            }

            // Change chat head color
            if (action.equals("chatColor")) {
                String colorValue = intent.getExtras().getString("color");

                int color = Integer.parseInt(colorValue);
                removeChatHead(wm, overlayButton, overlayDelete, topLeftView);

                circleUtils.createCircularBitmap(context, context.getResources(), overlayButton, color, size);
                chatColor = color;

                initializeChatHead();
            }

            // Change chat head alpha
            if (action.equals("imageAlpha")) {

                String prog = intent.getExtras().getString("alpha");

                if (overlayButton.isAttachedToWindow()) {
                    int progress = Integer.parseInt(prog);

                    overlayButton.setImageAlpha(progress);
                }
            }

            // Change chat head size
            if (action.equals("chatSize")) {

                String prog = intent.getExtras().getString("size");

                if (overlayButton.isAttachedToWindow()) {

                    int progress = Integer.parseInt(prog);

                    removeChatHead(wm, overlayButton, overlayDelete, topLeftView);
                    circleUtils.createCircularBitmap(context, context.getResources(), overlayButton, chatColor, progress);

                    size = progress;

                    initializeChatHead();
                }
            }
        }
    }
}
