package com.example.android.sunshine.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends Activity {

    private TextView mTimeTextView;
    private TextView mDateTextView;
    private ImageView mIconImageView;
    private TextView mHighTemperatureTextView;
    private TextView mLowTemperatureTextView;

    private Thread mTimeKeepingThread;
    private Runnable mTimeKeepingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTimeTextView = (TextView) stub.findViewById(R.id.time_text_view);
                mDateTextView = (TextView) stub.findViewById(R.id.date_text_view);
                mIconImageView = (ImageView) stub.findViewById(R.id.icon_image_view);
                mHighTemperatureTextView = (TextView) stub.findViewById(R.id.high_temperature_text_view);
                mLowTemperatureTextView = (TextView) stub.findViewById(R.id.low_temperature_text_view);

                mTimeKeepingThread = null;
                mTimeKeepingRunnable = new TimeKeeperRunner();
                mTimeKeepingThread = new Thread(mTimeKeepingRunnable);
                mTimeKeepingThread.start();
            }
        });
    }

    public void updateTimestamps() {
        // Just use the user's locale to show the date and time
        final java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(this);
        final java.text.DateFormat timeFormat = DateFormat.getTimeFormat(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                mTimeTextView.setText(timeFormat.format(calendar.getTime()));
                mDateTextView.setText(dateFormat.format(calendar.getTime()));
            }
        });
    }

    class TimeKeeperRunner implements Runnable{
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    updateTimestamps();
                    // sleep for 15 seconds
                    Thread.sleep(1000 * 15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
