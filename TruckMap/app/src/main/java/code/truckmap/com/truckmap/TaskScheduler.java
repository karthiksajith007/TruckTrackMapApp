package code.truckmap.com.truckmap;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by karthik on 12/20/2017.
 */

public class TaskScheduler {

    interface ITaskScheduler {
        void onScheduledTask(long scheduleID);
    }

    private enum TaskSchedulerState {Working, NotWorking};
    private TaskSchedulerState taskSchedulerState;

    private Timer timer;
    private ITaskScheduler taskInterface;
    private long timeIntervel;
    private long scheduleID;

    public TaskScheduler(ITaskScheduler taskInterface, long timeIntervel, long scheduleID) {
        this.taskInterface = taskInterface;
        this.timeIntervel = timeIntervel;
        this.scheduleID = scheduleID;
        taskSchedulerState = TaskSchedulerState.NotWorking;
    }

    public void startScheduler() {
        stopScheduler();
        timer = new Timer ();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (taskInterface != null) {
                    taskInterface.onScheduledTask(scheduleID);
                }
            }
        }, 0, timeIntervel/*5000*/);
        taskSchedulerState = TaskSchedulerState.Working;
    }

    public void pauseScheduler () {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void resumeScheduler () {
        if (taskSchedulerState == TaskSchedulerState.Working) {
            startScheduler();
        }
    }

    public void stopScheduler() {
        taskSchedulerState = TaskSchedulerState.NotWorking;
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }
}
