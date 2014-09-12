package org.restfulwhois.rdap.port43.service;

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

    /**
     * timer.
     */
    private static final Timer TIMER = new Timer();

    /**
     * call this method to start TIMER.
     */
    public static void schedule() {
        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                ConnectionControlService.clearIpMap();
            }
        }, TIMER_DELAY, INTERVAL);
    }

    /**
     * stop TIMER.
     */
    public static void stop() {
        TIMER.cancel();
        TIMER.purge();
    }

}
