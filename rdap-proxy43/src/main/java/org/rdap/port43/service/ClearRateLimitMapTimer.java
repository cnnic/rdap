package org.rdap.port43.service;

import java.util.Timer;
import java.util.TimerTask;

/**
 * clear rate limit ip timer.
 * 
 * @author jiashuo
 * 
 */
public class ClearRateLimitMapTimer {
    /**
     * interval in milliseconds.
     */
    private static final long INTERVAL = 5000;
    /**
     * delay in milliseconds.
     */
    private static final long TIMER_DELAY = 1000 * 60 * 5;

    private static final Timer timer = new Timer();

    /**
     * call this method to start timer.
     */
    public static void schedule() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ConnectionControlService.clearIpMap();
            }
        }, TIMER_DELAY, INTERVAL);
    }

    /**
     * stop timer.
     */
    public static void stop() {
        timer.cancel();
        timer.purge();
    }

}
