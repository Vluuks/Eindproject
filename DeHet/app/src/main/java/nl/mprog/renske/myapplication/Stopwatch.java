package nl.mprog.renske.myapplication;

// http://stackoverflow.com/questions/14393423/how-to-make-a-countdown-timer-in-java

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Stopwatch {
    static int interval;
    static Timer timer;
    public boolean timerstatus, finished;
    public TextView thetextview;


    public Stopwatch(int time, TextView textview){

        finished = false;
        thetextview = textview;
        interval = time;
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if(timerstatus != false) {
                    setTextView();
                }

            }
        }, delay, period);
    }


    private final int setInterval() {
        if (interval == 1) {
            timer.cancel();
            finished = true;
        }
        return --interval;
    }



    public void resumeTimer(){
        timerstatus = true;
    }

    public void cancelTimer(){
        timer.cancel();
    }


    public int pauseTimer(){
        timerstatus = false;
        int savedinterval = interval;
        return savedinterval;
    }

    public void setTextView(){

        // source: http://stackoverflow.com/questions/3280141/calledfromwrongthreadexception-only-the-original-thread-that-created-a-view-hie
        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable() {
            public void run() {
                thetextview.setText(Integer.toString(setInterval()));
            }
        });


    }
}