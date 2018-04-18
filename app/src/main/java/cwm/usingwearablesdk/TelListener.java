package cwm.usingwearablesdk;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by user on 2018/2/5.
 */

public class TelListener extends PhoneStateListener {
    public  final String NAME = "CONTACT_NAME";
    public  final String NUMBER = "CONTACT_NUMBER";
    private Context mContext;
    Intent intent;
    public TelListener(Context context){mContext = context;}
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING://此时为响铃状态
                Log.d("bernie","Tele Listener calling of ringing: "+incomingNumber);
                String name = lookingForContact(incomingNumber);
                intent = new  Intent("com.github.gn00618777");
                intent.putExtra("Notification Code", NotificationListenerExampleService.InterceptedNotificationCode.TELE_CODE);
                intent.putExtra(NAME, name);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d("bernie","Tele Listener calling off hook");
                intent = new  Intent("com.github.gn00618777");
                intent.putExtra("Notification Code", NotificationListenerExampleService.InterceptedNotificationCode.TELE_OFF_HOOK);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d("bernie","Tele Listener calling idle");
                intent = new  Intent("com.github.gn00618777");
                intent.putExtra("Notification Code", NotificationListenerExampleService.InterceptedNotificationCode.TELE_IDLE);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                break;
        }
         super.onCallStateChanged(state, incomingNumber);
    }
    public String lookingForContact(String number){
        String name = "";
        String phoneNumber = "";
        Cursor contacts_name = mContext.getContentResolver().query(
               // ContactsContract.Contacts.CONTENT_URI,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                 null,
                 null,
                 null,
                 null);

        while (contacts_name.moveToNext()) {
            phoneNumber = contacts_name.getString(contacts_name.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber = phoneNumber.replace(" ", "");
            if(phoneNumber.equals(number)) {
                name = contacts_name.getString(contacts_name.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                return name;
            }
            else {
                name = number;
            }
        }
         return name;
    }
}
