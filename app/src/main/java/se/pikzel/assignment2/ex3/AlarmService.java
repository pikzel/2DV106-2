package se.pikzel.assignment2.ex3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * @author Pontus Palmenäs
 */
public class AlarmService {
    private Context context;
    private static AlarmManager manager = null;
    private static final String broadcastName = "se.pikzel.assignment2.ex3.ALARM_BROADCAST";

    public AlarmService(Context context) {
        this.context = context;
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * Sets a repeating alarm that triggers every 24 hours until removed.
     */
    public void set(final Alarm alarm) {
        Intent intent = new Intent(broadcastName);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);

        long startTime = getStartTime(alarm);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, 1000*60*60*24, alarmIntent);
        Log.d("Alarm", "Alarm " + alarm.getId() + " set at " + startTime);
    }

    public void remove(final Alarm alarm) {
        Intent intent = new Intent(broadcastName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
        Log.d("Alarm", "Alarm " + alarm.getId() + " removed.");
    }

    private long getStartTime(final Alarm alarm) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        return calendar.getTimeInMillis();
    }
}

