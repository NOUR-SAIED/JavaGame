package utility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerUtility {
    private int timeRemaining; // Temps restant en secondes
    private Timer timer;
    private TimerListener listener;

    public TimerUtility(int duration, TimerListener listener) {
        this.timeRemaining = duration;
        this.listener = listener;

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeRemaining > 0) {
                    timeRemaining--;
                    listener.onTimeUpdate(timeRemaining);
                } else {
                    timer.stop();
                    listener.onTimeEnd();
                }
            }
        });
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public interface TimerListener {
        void onTimeUpdate(int timeRemaining);

        void onTimeEnd();
    }
}
