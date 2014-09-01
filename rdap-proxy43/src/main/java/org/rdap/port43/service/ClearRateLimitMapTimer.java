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

    /**
     * Data out of the collection will be stored query time
     */
    public static void schedule() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ConnectionControlService.clearIpMap();
            }
        }, TIMER_DELAY, INTERVAL);
    }

}
