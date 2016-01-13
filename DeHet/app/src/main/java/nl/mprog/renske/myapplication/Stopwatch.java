package nl.mprog.renske.myapplication;

// http://stackoverflow.com/questions/14393423/how-to-make-a-countdown-timer-in-java

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Stopwatch {
    static int interval;
    static Timer timer;

    public void Stopwatch (int chosen_interval){
        interval = chosen_interval;

        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                System.out.println(setInterval());

            }
        }, delay, period);
    }


    private static final int setInterval() {
        if (interval == 1)
            timer.cancel();
        return --interval;
    }
}