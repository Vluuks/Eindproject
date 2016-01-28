// Renske Talsma, UvA 10896503, vluuks@gmail.com

package nl.mprog.renske.myapplication;

/**
 * Implementation of a countodwn timer that is used in the game.
 * source: http://stackoverflow.com/questions/14393423/how-to-make-a-countdown-timer-in-java
 */
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class Stopwatch {
    static int interval;
    static Timer timer;
    public boolean timerstatus, finished;
    public TextView thetextview;
    public TimerHandler callback;

    /**
     * Constructor.
     */
    public Stopwatch(int time, TextView textview, TimerHandler the_callback){
        callback = the_callback;
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

    /**
     * Set the amount of time that is left.
     */
    private final int setInterval() {
        if (interval == 1) {
            timer.cancel();
            finished = true;
            callback.onTimerFinish();
            
        }
        return --interval;
    }

    /**
     * Timer life cycle.
     */
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

    /**
     * Updates the TextView that displays the amount of time left.
     * source: http://stackoverflow.com/questions/3280141/calledfromwrongthreadexception-only-the-original-thread-that-created-a-view-hie
     */
    public void setTextView(){
        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable() {
            public void run() {
                thetextview.setText(Integer.toString(setInterval()));
            }
        });
    }
}