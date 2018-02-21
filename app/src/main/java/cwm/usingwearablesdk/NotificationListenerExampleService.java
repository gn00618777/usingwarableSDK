package cwm.usingwearablesdk;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import cwm.wearablesdk.NotificationData;

/**
 * Created by user on 2018/1/16.
 */

public class NotificationListenerExampleService extends NotificationListenerService {

    private static final class ApplicationPackageNames {
        public static final String QQ_PACK_NAME = "jp.naver.line.android";
        public static final String WECHRT_PACK_NAME = "com.tencent.mm";
        public static final String DOBAN_PACK_NAME = "com.skype.raider";
        public static final String GOOGLE_EMAIL_NAME = "com.google.android.gm";
        public static final String TELEPHONE_PACK_NAME = "com.sonymobile.android.dialer";//"com.android.incallui";
        public static final String NEWS_PACK_NAME = "com.sonyericsson.conversations";
        public static final String TELE_SERVER_PACK_NAME = "com.android.server.telecom";
    }

    public static final class InterceptedNotificationCode {
        public static final int QQ_CODE = 0;
        public static final int WECHART_CODE = 1;
        public static final int DOBAN_CODE = 2;
        public static final int GMAIL_CODE = 3;
        public static final int TELE_CODE = 4;
        public static final int NEW_CODE = 5;
        public static final int TELE_SERVER_CODE = 6;
        public static final int TELE_OFF_HOOK = 7;
        public static final int TELE_IDLE = 8;
        public static final int OTHER_NOTIFICATIONS_CODE = 9;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        int notificationCode = matchNotificationCode(sbn);

        if(notificationCode == InterceptedNotificationCode.QQ_CODE ||
           notificationCode == InterceptedNotificationCode.WECHART_CODE ||
           notificationCode == InterceptedNotificationCode.DOBAN_CODE ||
                notificationCode == InterceptedNotificationCode.GMAIL_CODE||
                notificationCode == InterceptedNotificationCode.TELE_CODE ||
                notificationCode == InterceptedNotificationCode.NEW_CODE ||
                notificationCode == InterceptedNotificationCode.TELE_SERVER_CODE){
            Bundle bundle =  sbn.getNotification().extras;
            String title = bundle.getString(Notification.EXTRA_TITLE);
            String content = bundle.getString(Notification.EXTRA_TEXT);
            Intent intent = new  Intent("com.github.gn00618777");
            intent.putExtra("Notification Code", notificationCode);
            intent.putExtra(Notification.EXTRA_TITLE, title);
            intent.putExtra(Notification.EXTRA_TEXT, content);
             //Log.d("bernie","title:"+title);
             //Log.d("bernie","content:"+content);

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    private int matchNotificationCode(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
       // Log.d("bernie",packageName);
        if(packageName.equals(ApplicationPackageNames.QQ_PACK_NAME)){
            return(InterceptedNotificationCode.QQ_CODE);
        }
        else if(packageName.equals(ApplicationPackageNames.WECHRT_PACK_NAME)){
            return(InterceptedNotificationCode.WECHART_CODE);
        }
        else if(packageName.equals(ApplicationPackageNames.DOBAN_PACK_NAME)){
            return(InterceptedNotificationCode.DOBAN_CODE);
        }
        else if(packageName.equals(ApplicationPackageNames.GOOGLE_EMAIL_NAME)){
            return (InterceptedNotificationCode.GMAIL_CODE);
        }
        else if(packageName.equals(ApplicationPackageNames.TELEPHONE_PACK_NAME)){
            return (InterceptedNotificationCode.TELE_CODE);
        }
        else if(packageName.equals(ApplicationPackageNames.NEWS_PACK_NAME)){
            return (InterceptedNotificationCode.NEW_CODE);
        }
        else if(packageName.equals(ApplicationPackageNames.TELE_SERVER_PACK_NAME)){
            return (InterceptedNotificationCode.TELE_SERVER_CODE);
        }
        else{
            return(InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE);
        }
    }


}
