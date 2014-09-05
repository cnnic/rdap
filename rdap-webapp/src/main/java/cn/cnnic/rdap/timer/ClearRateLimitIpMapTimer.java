package cn.cnnic.rdap.timer;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import cn.cnnic.rdap.service.impl.ConnectionControlService;

/**
 * clear rate limit ip map timer.
 * 
 * @author jiashuo
 * 
 */
@Component
public class ClearRateLimitIpMapTimer {
    /**
     * interval in milliseconds.
     */
    private static final long INTERVAL = 1000 * 60 * 10;
    /**
     * delay in milliseconds.
     */
    private static final long TIMER_DELAY = 1000 * 60 * 5;

    /**
     * call this method to start timer.
     */
    @PostConstruct
    public void schedule() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ConnectionControlService.clearIpMap();
            }
        }, TIMER_DELAY, INTERVAL);
    }

}
