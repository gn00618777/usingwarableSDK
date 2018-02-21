package cwm.usingwearablesdk;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by user on 2018/2/5.
 */

public class TelListener extends PhoneStateListener {
    private Context mContext;
    Intent intent;
    public TelListener(Context context){mContext = context;}
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING://此时为响铃状态
            break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                intent = new  Intent("com.github.gn00618777");
                intent.putExtra("Notification Code", NotificationListenerExampleService.InterceptedNotificationCode.TELE_OFF_HOOK);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                intent = new  Intent("com.github.gn00618777");
                intent.putExtra("Notification Code", NotificationListenerExampleService.InterceptedNotificationCode.TELE_IDLE);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                break;
        }
         super.onCallStateChanged(state, incomingNumber);
    }
}
