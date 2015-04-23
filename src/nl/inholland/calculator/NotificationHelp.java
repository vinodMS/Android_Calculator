package nl.inholland.calculator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationHelp {
    private Context mContext;
    private int NOTIFICATION_ID = 1;
    private Notification notify;
    private NotificationManager notifyManager;
    private PendingIntent mContentIntent;
    private CharSequence mContentTitle;
    
    public NotificationHelp(Context context)
    {
        mContext = context;
    }

    /**
     * This method will Put the notification into the status bar
     */
    @SuppressWarnings("deprecation")
	public void createNotification() {
        //get the notification manager
        notifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        
        //create the notification
        int icon = android.R.drawable.stat_sys_download;
        
        long when = System.currentTimeMillis();
        notify = new Notification(icon, "Backing up data", when);
        CharSequence titel = "Backing up Data";
        CharSequence detail = "0% complete"; 
        
        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        mContentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);

        //add the additional content and intent to the notification
        notify.setLatestEventInfo(mContext, titel, detail, mContentIntent);
        

        //notification appears in the 'Ongoing events' section
        notify.flags = Notification.FLAG_ONGOING_EVENT;

        //Show notification
        notifyManager.notify(NOTIFICATION_ID, notify);

        // Cancel the notification after its selected
           notify.flags |= Notification.FLAG_AUTO_CANCEL;
    }

    /**
     * Receives progress updates from the background task and updates the status bar notification appropriately
     * @param percentageComplete
     */
    @SuppressWarnings("deprecation")
	public void progressUpdate(int percentageComplete) {
        //build up the new status message
        CharSequence contentText = percentageComplete + "% complete";
        //publish it to the status bar
        notify.setLatestEventInfo(mContext, mContentTitle, contentText, mContentIntent);
        notifyManager.notify(NOTIFICATION_ID, notify);
    }

    /**
     * called when the background task is complete, this removes the notification from the status bar.
     */
    public void completed()    {
        //remove the notification from the status bar
        notifyManager.cancel(NOTIFICATION_ID);
    }
}