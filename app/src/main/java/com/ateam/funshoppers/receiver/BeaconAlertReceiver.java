

package com.ateam.funshoppers.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ateam.funshoppers.R;
import com.ateam.funshoppers.model.NotificationAction;
import com.ateam.funshoppers.ui.activity.MainNavigationActivity;
import com.ateam.funshoppers.util.Constants;
import com.ateam.funshoppers.util.NotificationBuilder;


public class BeaconAlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase(Constants.ALARM_NOTIFICATION_SHOW)) {
            NotificationAction notificationAction = intent.getParcelableExtra("NOTIFICATION");
            createNotification(context, context.getString(R.string.action_alarm_text_title),
                    notificationAction.getMessage(), notificationAction.getMessage(),
                    notificationAction.getRingtone(), notificationAction.isVibrate());

        }
    }

    private void createNotification(Context context, String title, String msgText, String msgAlert, String ringtone, boolean isVibrate) {

        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainNavigationActivity.class), 0);

        NotificationBuilder notificationBuilder = new NotificationBuilder(context);
        notificationBuilder.createNotification(R.mipmap.ic_launcher, title, notificationIntent);

        notificationBuilder.setMessage(msgText);
        notificationBuilder.setTicker(msgAlert);

        if (isVibrate) {
            notificationBuilder.setVibration();
        }

        if (ringtone != null && ringtone.length() > 0) {
            notificationBuilder.setRingtone(ringtone);
        }

        notificationBuilder.show(1);

    }
}





