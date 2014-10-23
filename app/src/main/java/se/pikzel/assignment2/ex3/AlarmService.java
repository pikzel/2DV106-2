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
     * Register the broadcast receiver
     */
    public void set(final Alarm alarm) {

        Intent intent = new Intent(broadcastName);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);

        long startTime = getStartTime(alarm);
        manager.set(AlarmManager.RTC_WAKEUP, startTime, alarmIntent);
        Log.d("Alarm", "Alarm " + alarm.getId() + " set at " + startTime);

        // Todo: When the alarm has rang, remove it from the list, OR it should be recurring every 24 hours
    }

    public void remove(final Alarm alarm) {
        Intent intent = new Intent(broadcastName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
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

