package cwm.usingwearablesdk;

import android.app.Notification;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.location.LocationManager;
import android.content.Context;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;

import cwm.wearablesdk.settings.AlarmSetting;
import cwm.wearablesdk.CwmManager;
import cwm.wearablesdk.events.CwmEvents;
import cwm.wearablesdk.events.AckEvents;
import cwm.wearablesdk.settings.BodySettings;
import cwm.wearablesdk.constants.ID;
import cwm.wearablesdk.settings.IntelligentSettings;
import cwm.wearablesdk.NotificationData;
import cwm.wearablesdk.settings.SystemSetting;
import cwm.wearablesdk.TabataSettings;
import cwm.wearablesdk.events.ErrorEvents;
import cwm.wearablesdk.TabataTask;
import cwm.wearablesdk.constants.Type;
import cwm.wearablesdk.settings.UserConfig;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.ProgressDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SelectTypeFragment.ListenForSelectTypeFragment,
RingBatteryFragment.ListenForRingStatusFragment, IntelligentFragment.ListenerForIntellignetFragment,
        PersonalInfoFragment.ListenForPersonalInfoFragment, TabataFragment.ListenForTabataFragment,
        SwVersionFragment.ListenForSwVersionFragment,
        TabataActionItemFragment.ListenForTabataActionItemFragment,
        TabataShowFragment.ListenForTabataShowFragment,
        FlashFragment.ListenForFlashFragment, SensorsFragment.ListenForSensorFragment,
        SystemFragment.ListenForSystemFragment,
        AlarmFragment.ListenForAlarmFragment, FactoryFragment.ListenForFactoryFragment,
        RunFragment.ListenForRunFragment, BaseMapFragment.ListenForBaseMapFragment{

   private final int REQUEST_SELECT_DEVICE = 2;
    private final int READ_PHONE_STATE = 3;
    private final int WRITE_EXTERNAL_STORAGE = 4;
    private final int READ_CONTACT = 5;
    private final int RECEIVE_SMS = 6;
    private final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    //sdk
    private CwmManager cwmManager;
    //UI
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private NavigationView.OnNavigationItemSelectedListener mNavViewOnNavItemSelListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //do reinitial
            if(mTabataPrepareFM.isVisible() ||
                    mTabataActionItemFM.isVisible() || mTabataShowFM.isVisible()) {
                if(timer != null)
                    timer.cancel();
                //requestHandler.removeCallbacks(requestTask);
                mTabataActionItemFM.setActionCountView("0");
                cwmManager.CwmTabataCommand(ITEMS.TABATA_DONE.ordinal(), 0, 0, 0);
                curentDoneItems = 0;
                mTabataQueue = null;
                mTabataShowFM.setItems(0, 0);
                mTabataActionItemFM.setActionCountView("0");
            }
            navigateTo(item);
            return true;
        }
    };
    private int mNavIdx;
    private boolean outSide = false;
    private FragmentManager mFM;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private PersonalInfoFragment mPersonalInfoFM = new PersonalInfoFragment();
    private SelectTypeFragment mSelectTypeFM = new SelectTypeFragment();
    private IntelligentFragment mIntelligentFM = new IntelligentFragment();
    private RingBatteryFragment mRingBatteryFM = new RingBatteryFragment();
    private TabataFragment mTabataFM = new TabataFragment();
    private TabataPrepareFragment mTabataPrepareFM = new TabataPrepareFragment();
    private SwVersionFragment mSwVersionFM = new SwVersionFragment();
    private TabataShowFragment mTabataShowFM = new TabataShowFragment();
    private TabataActionItemFragment mTabataActionItemFM = new TabataActionItemFragment();
    private TabataIntervalFragment mTabataIntervalFM = new TabataIntervalFragment();
    private FlashFragment mFlashFM = new FlashFragment();
    private SensorsFragment mSensorsFM = new SensorsFragment();
    private SystemFragment mSystemFM = new SystemFragment();
    private AlarmFragment mAlarmFM = new AlarmFragment();
    private FactoryFragment mFactory = new FactoryFragment();
    private RunFragment mRunFM = new RunFragment();
    private BaseMapFragment mBaseFM = new BaseMapFragment();

    private String mDeviceName = null;
    private String mDeviceAddress = null;
    private boolean mDeviceStatus = false;
    private byte[] mValue;
    private int[] mParser;

    private BluetoothAdapter mBtAdapter = null;

    private Timer timer;
    private int countDown;

    private TabataSettings mTabataSettings;
    private int totalPrepare = 0;
    private int totalInterval = 0;
    private int goalTimes = 0;
    private int totalItems = 0;
    private int curentDoneItems = 0;
    private TabataTask firstTask = null;
    private TabataTask laterTask = null;

    private String comment = "";

    private Queue<TabataTask> mTabataQueue;

    private boolean isTabataInitStart = false;
    private boolean isTabataPrepareStart = false;
    private boolean isTabataPrepareCount = false;
    private boolean isTabataPrepareEnd = false;
    private boolean isTabataActionItem = false;
    private boolean isTabataActionStart = false;
    private boolean isTabataActionEnd = false;
    private boolean isTabataIntervalStart = false;
    private boolean isTabataIntervalCount = false;
    private boolean isTabataIntervalEnd = false;
    private boolean isTabataDone = false;
    private boolean tabataHasDone = false;
    private boolean tabataActionEnd = false;

    UserConfig config = new UserConfig();

    int globalHeartRate = 0;
    int accumulationCalories = 0;


    TelephonyManager telM = null;

    private ProgressBar mProgressBar;

    private TelListener telListener = new TelListener(this);

    //tabata request related
    private Handler requestHandler = new Handler();
   /* private Runnable requestTask = new Runnable() {
        @Override
        public void run() {
            cwmManager.CwmTabataCommand(ITEMS.TABATA_REQUEST.ordinal(), 0, 0, 0);
            requestHandler.postDelayed(requestTask,1000);
        }
    };*/

    float[] accData = new float[3];
    float[] gyroData = new float[3];
    private boolean adxlK = false;
    private boolean bmiaccK = false;
    private boolean bmigyroK = false;
    int progress = 1;

    //Notification InComing Call
    private boolean onRing = false;

    final int SYSTEM_POSITION = 0;
    final int PERSONAL_POSITION = 1;
    final int SELECT_DEVICE_POSITION = 2;
    final int RING_BATTERY_POSITION = 4;
    final int TABATA_WORK_POSITION = 5;
    final int TABATA_PREPARE_POSITION = 6;
    final int TABATA_ACTION_ITEM_POSITION  = 7;
    final int TABATA_SHOW_POSITION = 8;
    final int TABATA_INTERVAL_POSITION = 9;
    final int SW_VERSION_POSITION = 10;
    final int FLASH_TEST_POSITION = 11;
    final int SENSORS_POSITION = 12;
    final int ALARM_POSITION = 13;
    final int FACTORY_POSITION = 14;
    final int RUN_POSITION = 15;
    final int BASE_MAP_POSITION = 16;

    public enum ITEMS{
        TABATA_INIT,
        TABATA_PAUSE,
        TABATA_PREPARE_START,
        TABATA_PREPARE_COUNT,
        TABATA_PREARE_END,
        TABATA_REST_START,
        TABATA_REST_COUNT,
        TABATA_REST_END,
        TABATA_ACTION_ITEM,
        TABATA_ACTION_START,
        TABATA_ACTION_END,
        TABATA_REQUEST,
        TABATA_DONE,
        TABATA_RESUME
    };

    public enum GESTURE{
        STEP_COINTER,
        CUSTOMISED_PEDOMETER,
        SIGNIFICANT_MOTION,
        HAND_UP,
        TAP,
        WATCH_TAKE_OFF,
        ACTIVITY_RECOGNITION,
        SLEEPING,
        SEDENTARY,
        WRIST_SCROLL,
        SHAKE,
        FALL,
        FLOOR_CLIMBED,
        SKIPPING,
    }

    public enum NOTIFICATION{
        NULL,
        CALL,
        SOCIAL,
        EMAIL,
        NEWS,
        MISSING_CALL,
        PICK_UP
    }
    public enum SOCIAL_APP{
        QQ,
        WECHART,
        DOBAN,
        OTHER
    }

    private ProgressDialog mProgressDialog;

    private Handler connectHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            cwmManager.CwmBleDisconnect();
            cwmManager.CwmBleClose();
            mProgressDialog.dismiss();
        }
    };
    String previous_item = "";
    String previous_count = "";
    StringBuilder builder = new StringBuilder();
    StringBuilder selfTestBuilder = new StringBuilder();
    StringBuilder calibrateBuilder = new StringBuilder();

    private AlertDialog enableNotificationListenerAlertDialog;

    // information
    private IntelligentSettings testSettings;
    private BodySettings testS1ettings;

    private final int PERMISSION_REQUEST_FINE_LOCATION = 3;

    public CwmManager.EventListener eventListener = new CwmManager.EventListener() {
        @Override
        public void onEventArrival(CwmEvents cwmEvents) {
            int msgType = cwmEvents.getMsgType();
            int message_id;
            String gestureEvent = "";

            switch (msgType){
                case Type.SYSTTEM_INFORMATION:
                    message_id = cwmEvents.getMessageID();
                    switch (message_id){
                        case ID.USER_CONFIG_INFO:
                            BodySettings body = cwmEvents.getBody();
                            IntelligentSettings intelligent = cwmEvents.getIntelligent();
                            SystemSetting system = cwmEvents.getSystemSetting();
                            AlarmSetting alarm = cwmEvents.getAlarmSetting();

                            if(mSystemFM.isVisible()){
                                mSystemFM.upadateSetting(system);
                                resetFragments(SYSTEM_POSITION);
                            }
                            if(mIntelligentFM.isVisible()){
                                mIntelligentFM.updateSetting(intelligent);
                                resetPreferencedFragments();
                            }
                            if(mPersonalInfoFM.isVisible()){
                                mPersonalInfoFM.updateSetting(body);
                                resetFragments(PERSONAL_POSITION);
                            }

                            if(mAlarmFM.isVisible()){
                                mAlarmFM.updateSetting(alarm);
                                resetFragments(ALARM_POSITION);
                            }

                            String os = "";
                            String format = "";

                            boolean[] gesture = intelligent.getGesture();

                            if(system.getOsType() == 0)
                                os = "Android";
                            else
                                os = "iOS";
                            if(system.getTimeFormat() == 0)
                                format = "0-23";
                            else
                                format = "1-12";
                            StringBuilder builder = new StringBuilder();
                            builder.append("系統: "+os+"\n");
                            builder.append("時間格式: "+format+"時"+"\n");
                            builder.append("歷史資料儲存週期: "+Integer.toString(system.getHistoryDetectPeriod())+"\n");
                            builder.append("顯屏時間: "+Integer.toString(system.getScreenTimeOut())+"\n");
                            builder.append("顯示頁面:"+Integer.toString(system.getScreens())+"\n");
                            builder.append("功能:"+Integer.toString(system.getFunctions())+"\n");
                            builder.append("算法:"+Integer.toString(intelligent.getGestureValue())+"\n");
                            builder.append("睡眠偵測區間: "+Integer.toString(system.getSleepStart())+"~"+Integer.toString(system.getSleepStop())+"時"+"\n");
                            builder.append("午休勿擾區間: "+Integer.toString(system.getNoDisturbStart())+"~"+Integer.toString(system.getNoDisturbStop())+"時"+"\n");
                            builder.append("性別: "+Integer.toString(body.getSex())+"\n");
                            builder.append("年齡: "+Integer.toString(body.getOld())+"\n");
                            builder.append("身高: "+Integer.toString(body.getHight())+"\n");
                            builder.append("體重: "+Integer.toString(body.getWeight())+"\n");
                            builder.append("步數目標: "+Integer.toString(intelligent.getGoal())+"\n");
                            builder.append("久坐提醒時間: "+Integer.toString(intelligent.getTime())+"\n");
                            builder.append("亮度: "+Integer.toString(system.getBrightness())+"\n");
                            builder.append("開啟的鬧鐘:");
                            if(alarm.getWeek(1) != 0){
                                builder.append("\n鬧鐘1:");
                                if((alarm.getWeek(1) & 1) != 0)
                                    builder.append(" 日 ");
                                if((alarm.getWeek(1) & 2) != 0)
                                    builder.append(" 一 ");
                                if((alarm.getWeek(1) & 4) != 0)
                                    builder.append(" 二 ");
                                if((alarm.getWeek(1) & 8) != 0)
                                    builder.append("  三 ");
                                if((alarm.getWeek(1) & 16) != 0)
                                    builder.append("  四 ");
                                if((alarm.getWeek(1) & 32) != 0)
                                    builder.append("  五 ");
                                if((alarm.getWeek(1) & 64) != 0)
                                    builder.append("  六 ");
                                builder.append(Integer.toString(alarm.getHour(1))+":"+Integer.toString(alarm.getMinute(1)));
                            }
                            if(alarm.getWeek(2) != 0){
                                builder.append("\n鬧鐘2:");
                                if((alarm.getWeek(2) & 1) != 0)
                                    builder.append(" 日 ");
                                if((alarm.getWeek(2) & 2) != 0)
                                    builder.append(" 一 ");
                                if((alarm.getWeek(2) & 4) != 0)
                                    builder.append(" 二 ");
                                if((alarm.getWeek(2) & 8) != 0)
                                    builder.append("  三 ");
                                if((alarm.getWeek(2) & 16) != 0)
                                    builder.append("  四 ");
                                if((alarm.getWeek(2) & 32) != 0)
                                    builder.append("  五 ");
                                if((alarm.getWeek(2) & 64) != 0)
                                    builder.append("  六 ");
                                builder.append(Integer.toString(alarm.getHour(2))+":"+Integer.toString(alarm.getMinute(2)));
                            }
                            if(alarm.getWeek(3) != 0){
                                builder.append("\n鬧鐘3:");
                                if((alarm.getWeek(3) & 1) != 0)
                                    builder.append(" 日 ");
                                if((alarm.getWeek(3) & 2) != 0)
                                    builder.append(" 一 ");
                                if((alarm.getWeek(3) & 4) != 0)
                                    builder.append(" 二 ");
                                if((alarm.getWeek(3) & 8) != 0)
                                    builder.append("  三 ");
                                if((alarm.getWeek(3) & 16) != 0)
                                    builder.append("  四 ");
                                if((alarm.getWeek(3) & 32) != 0)
                                    builder.append("  五 ");
                                if((alarm.getWeek(3) & 64) != 0)
                                    builder.append("  六 ");
                                builder.append(Integer.toString(alarm.getHour(3))+":"+Integer.toString(alarm.getMinute(3)));
                            }
                            if(alarm.getWeek(4) != 0){
                                builder.append("\n鬧鐘4:");
                                if((alarm.getWeek(4) & 1) != 0)
                                    builder.append(" 日 ");
                                if((alarm.getWeek(4) & 2) != 0)
                                    builder.append(" 一 ");
                                if((alarm.getWeek(4) & 4) != 0)
                                    builder.append(" 二 ");
                                if((alarm.getWeek(4) & 8) != 0)
                                    builder.append("  三 ");
                                if((alarm.getWeek(4) & 16) != 0)
                                    builder.append("  四 ");
                                if((alarm.getWeek(4) & 32) != 0)
                                    builder.append("  五 ");
                                if((alarm.getWeek(4) & 64) != 0)
                                    builder.append("  六 ");
                                builder.append(Integer.toString(alarm.getHour(4))+":"+Integer.toString(alarm.getMinute(4)));
                            }
                            if(alarm.getWeek(5) != 0){
                                builder.append("\n鬧鐘5:");
                                if((alarm.getWeek(5) & 1) != 0)
                                    builder.append(" 日 ");
                                if((alarm.getWeek(5) & 2) != 0)
                                    builder.append(" 一 ");
                                if((alarm.getWeek(5) & 4) != 0)
                                    builder.append(" 二 ");
                                if((alarm.getWeek(5) & 8) != 0)
                                    builder.append("  三 ");
                                if((alarm.getWeek(5) & 16) != 0)
                                    builder.append("  四 ");
                                if((alarm.getWeek(5) & 32) != 0)
                                    builder.append("  五 ");
                                if((alarm.getWeek(5) & 64) != 0)
                                    builder.append("  六 ");
                                builder.append(Integer.toString(alarm.getHour(5))+":"+Integer.toString(alarm.getMinute(5)));
                            }
                            if(alarm.getWeek(6) != 0){
                                builder.append("\n鬧鐘6:");
                                if((alarm.getWeek(6) & 1) != 0)
                                    builder.append(" 日 ");
                                if((alarm.getWeek(6) & 2) != 0)
                                    builder.append(" 一 ");
                                if((alarm.getWeek(6) & 4) != 0)
                                    builder.append(" 二 ");
                                if((alarm.getWeek(6) & 8) != 0)
                                    builder.append("  三 ");
                                if((alarm.getWeek(6) & 16) != 0)
                                    builder.append("  四 ");
                                if((alarm.getWeek(6) & 32) != 0)
                                    builder.append("  五 ");
                                if((alarm.getWeek(6) & 64) != 0)
                                    builder.append("  六 ");
                                builder.append(Integer.toString(alarm.getHour(6))+":"+Integer.toString(alarm.getMinute(6)));
                            }
                            builder.append("\n");
                            builder.append("--------------算法清單--------------"+"\n");
                            if(gesture[GESTURE.STEP_COINTER.ordinal()])
                                builder.append("Step Counter: true\n");
                            if(gesture[GESTURE.CUSTOMISED_PEDOMETER.ordinal()])
                                builder.append("Customized Pedometer: true\n");
                            if(gesture[GESTURE.SIGNIFICANT_MOTION.ordinal()])
                                builder.append("Significant Motion: true\n");
                            if(gesture[GESTURE.HAND_UP.ordinal()])
                                builder.append("Hand up: true\n");
                            if(gesture[GESTURE.TAP.ordinal()])
                                builder.append("Tap: true\n");
                            if(gesture[GESTURE.WATCH_TAKE_OFF.ordinal()])
                                builder.append("On Wear: true\n");
                            if(gesture[GESTURE.ACTIVITY_RECOGNITION.ordinal()])
                                builder.append("Activity Recognition: true\n");
                            if(gesture[GESTURE.SLEEPING.ordinal()])
                                builder.append("Sleeping: true\n");
                            if(gesture[GESTURE.SEDENTARY.ordinal()])
                                builder.append("Sedentary: true\n");
                            if(gesture[GESTURE.WRIST_SCROLL.ordinal()])
                                builder.append("Wrist Scroll: true\n");
                            if(gesture[GESTURE.SHAKE.ordinal()])
                                builder.append("Shake: true\n");
                            if(gesture[GESTURE.FALL.ordinal()])
                                builder.append("Fall: true\n");
                            if(gesture[GESTURE.FLOOR_CLIMBED.ordinal()])
                                builder.append("Floor Climbed: true\n");
                            if(gesture[GESTURE.SKIPPING.ordinal()])
                                builder.append("Skipping: true\n");

                            final AlertDialog.Builder configBuilder = new AlertDialog.Builder(MainActivity.this);
                            configBuilder.setTitle("裝置使用者組態資訊");
                            configBuilder.setMessage(builder.toString());
                            configBuilder.setPositiveButton(android.R.string.ok, null);
                            configBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }
                            });
                            configBuilder.show();

                            break;
                        case ID.BATTERY_INFO:
                            mRingBatteryFM.setValue(cwmEvents.getBattery());
                            if(mRingBatteryFM.isVisible()){
                                resetFragments(RING_BATTERY_POSITION);
                            }
                            break;
                        case ID.DEVICE_VERSION_INFO:
                            mSwVersionFM.setVersion(cwmEvents.getVersion());
                            if(mSwVersionFM.isVisible()){
                                resetFragments(SW_VERSION_POSITION);
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case Type.SENSOR_GESTURE_REPORT_MESSAGE:
                    message_id = cwmEvents.getMessageID();
                    switch (message_id){
                        case ID.ACCELERATION_RAW_DATA_REPORT:
                            int sensorTag = cwmEvents.getSensorTag();
                            accData = cwmEvents.getSensorAccData();
                            mSensorsFM.refreshTextView(sensorTag, accData, gyroData);
                            if(mSensorsFM.isVisible()){
                                resetFragmentsII(SENSORS_POSITION);
                            }
                            break;
                        case ID.GYRO_RAW_DATA_REPORT:
                            sensorTag = cwmEvents.getSensorTag();
                            gyroData = cwmEvents.getSensorGyroData();

                            mSensorsFM.refreshTextView(sensorTag, accData, gyroData);
                            if(mSensorsFM.isVisible()){
                                resetFragmentsII(SENSORS_POSITION);
                            }
                            break;
                        case ID.WRIST_SCROLL_EVENT_RESPONSE_MESSAGE:
                            gestureEvent = "WRIST SCROLL";
                            Toast.makeText(getApplicationContext(),gestureEvent,Toast.LENGTH_SHORT).show();
                            break;
                        case ID.HEART_RATE_RAW_DATA_REPORT:
                            globalHeartRate = cwmEvents.getHeartBeat();
                            mRunFM.setHeartValue(globalHeartRate);
                            mTabataActionItemFM.setHeartRate(globalHeartRate);

                            if(mRunFM.isVisible()){
                                resetFragments(RUN_POSITION);
                            }
                            if(mTabataActionItemFM.isVisible()){
                                resetFragmentsII(TABATA_ACTION_ITEM_POSITION);
                            }
                            if(mSensorsFM.isVisible()){
                                mSensorsFM.refreshHeartTextView(cwmEvents.getHeartBeat());
                                resetFragments(SENSORS_POSITION);
                            }
                            break;
                        case ID.ACTIVITY_PEDOMETER_DATA_REPORT_MESSAGE:
                            String gesture = "";
                            if(cwmEvents.getStatus() == 1){
                                gesture = "靜止";
                            }
                            else if(cwmEvents.getStatus() == 2){
                                gesture = "走路";
                            }
                            else if(cwmEvents.getStatus() == 3){
                                gesture = "跑步";
                            }
                            else if(cwmEvents.getStatus() == 4){
                                gesture = "騎車";
                            }

                            mRunFM.setValue(cwmEvents.getStepCount(), 0/*cwmEvents.getDistance()*/,
                                    cwmEvents.getStepFreq(), /*gesture*/"");

                            if(mRunFM.isVisible()){
                                resetFragments(RUN_POSITION);
                            }
                            break;
                        case ID.TABATA_RESPONSE_MESSAGE:
                            if(tabataActionEnd == false) {
                                String item = "";
                                String count = "";
                                String calories = "";
                                String strength = "";
                                int initCode;

                                if (cwmEvents.getExerciseItem() == 1) {
                                    item = "Push Up";
                                } else if (cwmEvents.getExerciseItem() == 2) {
                                    item = "Crunch";
                                } else if (cwmEvents.getExerciseItem() == 3) {
                                    item = "Squart";
                                } else if (cwmEvents.getExerciseItem() == 4) {
                                    item = "Jumping Jack";
                                } else if (cwmEvents.getExerciseItem() == 5) {
                                    item = "Dips";
                                } else if (cwmEvents.getExerciseItem() == 6) {
                                    item = "High Knees Running";
                                } else if (cwmEvents.getExerciseItem() == 7) {
                                    item = "Lunges";
                                } else if (cwmEvents.getExerciseItem() == 8) {
                                    item = "Burpee";
                                } else if (cwmEvents.getExerciseItem() == 9) {
                                    item = "Step On Chair";
                                } else if (cwmEvents.getExerciseItem() == 10) {
                                    item = "Push Up Rotation";
                                }

                                count = Integer.toString(cwmEvents.getDoItemCount());
                                accumulationCalories += cwmEvents.getTabataCalories();
                                calories = Integer.toString(accumulationCalories);
                                initCode = cwmEvents.getTabataInitCode();
                                strength = Integer.toString(cwmEvents.getStrength());

                                previous_item = item;
                                previous_count = count;

                                mTabataShowFM.setTabataResultValue("", item, count, calories, strength);

                                // goal is achievement
                                if (cwmEvents.getDoItemCount() >= goalTimes && goalTimes != 0) {
                                    //if (isPopup == false) {
                                        goalTimes = 0;
                                        //requestHandler.removeCallbacks(requestTask);
                                        Log.d("bernie", "app remove requestTask goal times:" + Integer.toString(goalTimes) + "cwmEvents.getDoItemCount(): " + Integer.toString(cwmEvents.getDoItemCount()));
                                        builder.append("item: " + previous_item + "  count: " + previous_count + "\n");
                                        mTabataShowFM.setHistory(builder.toString());
                                        mTabataActionItemFM.setActionCountView("0");
                                        mTabataActionItemFM.setStrength(strength);
                                        mTabataActionItemFM.setCalories(calories);

                                        sendActionItemEnd();
                                        return;
                                    //} else
                                     //   isPopup = false;
                                }
                                //mTabataShowFM.setTabataResultValue("", item, count, calories, strength);
                                Log.d("bernie","12313");

                                if (tabataHasDone == false) {
                                    mTabataActionItemFM.setActionCountView(count);
                                    mTabataActionItemFM.setStrength(strength);
                                    mTabataActionItemFM.setCalories(calories);
                                    resetFragmentsII(TABATA_ACTION_ITEM_POSITION);
                                }
                            }

                            break;
                        case ID.SPORT_RESPONSE_MESSAGE:
                            mRunFM.setHeartValue(cwmEvents.getHeartBeat());
                            mRunFM.setValue(cwmEvents.getStepCount(),0,cwmEvents.getStepFreq(),"要求跑步步數!");
                            if(mRunFM.isVisible()){
                                resetFragments(RUN_POSITION);
                            }
                            break;
                    }
                    break;
                case Type.COMMAND_RESPONSE:
                    break;
                case Type.HISTORY_DATA_RESPONSE:
                    message_id = cwmEvents.getMessageID();
                    int max = 0;
                    int current = 0;
                    switch (message_id){
                        case ID.HISTORY_PACKAGES:
                            if(cwmEvents.getMaxPackages() == 0){
                                Toast.makeText(getApplicationContext(),"已沒有歷程資料了",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mProgressBar.setMax(cwmEvents.getMaxPackages());
                                cwmManager.CwmSyncStart();
                            }
                            break;
                        case ID.SYNC_DONE:
                            mProgressDialog.onStart();
                            mProgressBar.setProgress(0);
                            Toast.makeText(getApplicationContext(),"同步歷程資料完畢!",Toast.LENGTH_SHORT).show();

                            break;
                        case ID.HISTORY_ERASE_DONE:
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"歷程清除完畢",Toast.LENGTH_SHORT).show();
                            break;
                        case ID.SLEEP_HISTORY:
                        case ID.LIFE_HISTORY:
                        case ID.LOG_HISTORY:
                            max = cwmEvents.getMaxPackages();
                            current = cwmEvents.getCurrentPackages();
                            Log.d("bernie","current:"+Integer.toString(current)+" max:"+Integer.toString(max));
                            if(max != 0){
                               if(current < max)
                                  mProgressBar.setProgress(current);
                               else if(current == max) {
                                  mProgressDialog.onStart();
                                  mProgressBar.setProgress(0);
                               }
                                cwmManager.CwmSyncSucces();
                            }
                            break;
                        case ID.SYNC_ABORTED:
                            Toast.makeText(getApplicationContext(),"同步歷史訊息失敗",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case Type.FACTORY_RESPONSE:
                    message_id = cwmEvents.getMessageID();
                    String sensor = "";
                    switch (message_id){
                        case ID.SELF_TEST_RESULT:
                            mProgressDialog.dismiss();
                            String selfResult = "";
                            Log.d("bernie"," self-test id:"+Integer.toString(cwmEvents.getSensorID()));
                            if(cwmEvents.getSensorID() == ID.ADXL_ACC){
                                sensor = "Adxl-Acc: " ;
                            }
                            else if(cwmEvents.getSensorID() == ID.BMI160_ACC){
                                sensor = "Bmi160-Acc: ";
                            }
                            else if(cwmEvents.getSensorID() == ID.BMI160_GYRO){
                                sensor = "Bmi160-Gyro: ";
                            }
                            else if(cwmEvents.getSensorID() == ID.HEART_RATAE){
                                sensor = "Heart: ";
                            }
                            else if(cwmEvents.getSensorID() == ID.VIBRATE_RATE){
                                sensor = "Vibrate: ";
                            }
                            else if(cwmEvents.getSensorID() == ID.BUTTON){
                                sensor = "Button: ";
                            }
                            else if(cwmEvents.getSensorID() == ID.OLED){
                                sensor = "OLED: ";
                            }
                            if(cwmEvents.getSensorID() == ID.FLASH){
                                sensor = "Flash: ";
                            }

                            Log.d("bernie","self test:"+Integer.toString(cwmEvents.getSelfTestResult()));

                            if(cwmEvents.getSelfTestResult() == 0){
                                selfResult = "Varify ID faile";
                            }
                            else if(cwmEvents.getSelfTestResult() == 1){
                                selfResult = "Varify Data faile";
                            }
                            else if(cwmEvents.getSelfTestResult() == 2){
                                selfResult = "Pass";
                            }
                            else if(cwmEvents.getSelfTestResult() == 3){
                                selfResult = "Finished";
                            }

                            selfTestBuilder.append(sensor+selfResult);
                            selfTestBuilder.append("\n");
                            if(mFactory.isVisible()){
                                mFactory.updateResult(selfTestBuilder.toString(), calibrateBuilder.toString());
                                resetFragments(FACTORY_POSITION);
                            }
                            break;
                        case ID.CALIBRATION_RESULT:
                            Log.d("bernie","accData:"+Float.toString(accData[0])+" "+Float.toString(accData[1]));
                            mProgressDialog.dismiss();
                            String calibrateStatus = "";
                            String biasValue = "";
                            if(cwmEvents.getSensorID() == ID.ADXL_ACC){
                                sensor = "Adxl-Acc " ;
                            }
                            else if(cwmEvents.getSensorID() == ID.BMI160_ACC) {
                                sensor = "Bmi-Acc ";
                            }
                            else if(cwmEvents.getSensorID() == ID.BMI160_GYRO){
                                 sensor = "Bmi-Gyro ";
                            }
                            if(cwmEvents.getCalibrateStatus() == 0){
                               calibrateStatus = "CALIB_STATUS_INPROGRESS ";
                            }
                            else if(cwmEvents.getCalibrateStatus() == 1){
                                calibrateStatus = "CALIB_STATUS_PASS ";
                            }
                            else if(cwmEvents.getCalibrateStatus() == 2){
                                calibrateStatus = "CALIB_STATUS_OUT_OF_RANGE ";
                            }
                            else if(cwmEvents.getCalibrateStatus() == 3){
                                calibrateStatus = "CALIB_STATUS_FAIL";
                            }
                            else if(cwmEvents.getCalibrateStatus() == 4){
                                calibrateStatus = "CALIB_STATUS_OUT ";
                            }
                            else if(cwmEvents.getCalibrateStatus() == 5){
                                calibrateStatus = "CALIB_STATUS_NO_TEST_ITEM";
                            }
                            if((cwmEvents.getCalibrateStatus() == 1) && (cwmEvents.getSensorID() == ID.ADXL_ACC) && (adxlK == false)) {
                                progress = 2;
                                adxlK = true;
                                biasValue = "Bias: " + Float.toString(cwmEvents.getBias().getX()) + " " +
                                        Float.toString(cwmEvents.getBias().getY()) + " " +
                                        Float.toString(cwmEvents.getBias().getZ());
                            }
                            if(cwmEvents.getCalibrateStatus() == 1 && cwmEvents.getSensorID() == ID.BMI160_ACC && bmiaccK == false) {
                                progress = 3;
                                bmiaccK = true;
                                biasValue = "Bias: " + Float.toString(cwmEvents.getBias().getX()) + " " +
                                        Float.toString(cwmEvents.getBias().getY()) + " " +
                                        Float.toString(cwmEvents.getBias().getZ());
                            }
                            if(cwmEvents.getCalibrateStatus() == 1 && cwmEvents.getSensorID() == ID.BMI160_GYRO && bmigyroK == false) {
                                progress = 4;
                                bmigyroK = true;
                                biasValue = "Bias: " + Float.toString(cwmEvents.getBias().getX()) + " " +
                                        Float.toString(cwmEvents.getBias().getY()) + " " +
                                        Float.toString(cwmEvents.getBias().getZ());
                            }
                            if(cwmEvents.getCalibrateStatus() != 1){
                                biasValue = "-1, -1, -1";
                            }
                            calibrateBuilder.append(sensor+calibrateStatus+biasValue);
                            calibrateBuilder.append("\n");
                            Log.d("bernie","progress:"+Integer.toString(progress));
                            mFactory.updateProgress(progress);

                            if(mFactory.isVisible()){
                                mFactory.updateResult(selfTestBuilder.toString(), calibrateBuilder.toString());
                                resetFragments(FACTORY_POSITION);
                            }
                            break;
                        case ID.MAP_ERASE_DONE:
                            mProgressDialog.dismiss();
                            if(cwmEvents.getMapId() == 1)
                              Toast.makeText(getApplicationContext(),"OLED 刪除完畢",Toast.LENGTH_SHORT).show();
                            else if(cwmEvents.getMapId() == 2)
                                Toast.makeText(getApplicationContext(),"BITMAP 刪除完畢",Toast.LENGTH_SHORT).show();
                            else if(cwmEvents.getMapId() == 3)
                                Toast.makeText(getApplicationContext(),"FONT刪除完畢", Toast.LENGTH_SHORT).show();
                            break;
                        case ID.MAP_WRITE_DONE:
                            float currentProgress = cwmEvents.getCurrentMapSize();
                            float maxSize = 352256;//cwmEvents.getMaxMapSize();
                            //Log.d("bernie","ak maxsize:"+Float.toString(maxSize));
                            mProgressDialog.setMessage("進度: "+Float.toString(currentProgress / maxSize * 100));
                            if(currentProgress != maxSize)
                                cwmManager.CwmSendBitMap();
                            if(currentProgress == maxSize)
                                mProgressDialog.dismiss();
                            break;
                    }
                    break;
                default:
                    break;
            }

            /*switch (id){
                case 0xBE:
                    mProgressDialog.dismiss();
                    int startSleepPos = 0;
                    int getup = 0;
                    StringBuilder light = new StringBuilder();
                    StringBuilder deep = new StringBuilder();

                    light.append("Light Sleep:\n");
                    deep.append("Deep Sleep:\n");

                    mParser = cwmEvents.getSleepParser();
                    StringBuilder builder1 = new StringBuilder();

                    for(int i = 0; i < cwmEvents.getParserLength() ; i+=3) {
                        if(mParser[i] % 100 == 4) {
                            getup = i;
                            break;
                        }
                    }

                    builder1.append("Parser:\n");
                    builder1.append("Start Sleep Time: " + Integer.toString(mParser[startSleepPos + 2] / 100) + ":" + Integer.toString(mParser[startSleepPos + 2] % 100) + "\n");
                    builder1.append("Awake Time: " + Integer.toString(mParser[getup + 2] / 100) + ":" + Integer.toString(mParser[getup + 2] % 100) + "\n");

                    /***************************************************************/
                 /*   for (int i = 0; i <= getup; i += 3) {
                        if (mParser[i] % 100 == 1) {
                            light.append("Day-" + Integer.toString(mParser[i + 1] / 100) + "/" + Integer.toString(mParser[i + 1] % 100) + " Time-" +
                                    Integer.toString(mParser[i + 2] / 100) + ":" + Integer.toString(mParser[i + 2] % 100) + "\n");
                        }
                    }
                    for (int i = 0; i <= getup; i += 3) {
                        if (mParser[i] % 100 == 2) {
                            deep.append("Day-" + Integer.toString(mParser[i + 1] / 100) + "/" + Integer.toString(mParser[i + 1] % 100) + " Time-" +
                                    Integer.toString(mParser[i + 2] / 100) + ":" + Integer.toString(mParser[i + 2] % 100) + "\n");
                        }
                    }
                    /***************************************************************/
                 /*   for (int i = 0; i < cwmEvents.getParserLength(); i += 3) {
                        if (mParser[i] == 100)
                            startSleepPos = i;
                    }

                    builder1.append(light.toString() + "\n");
                    builder1.append(deep.toString() + "\n");

                    try {
                        FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().toString() + "/Download/CwmSleepLog.txt", false);
                        BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                        int count = 0;
                        for (int i = 0; i < cwmEvents.getParserLength(); i++) {
                            count++;
                            bw.write(Integer.toString(mParser[i]));
                            bw.newLine();
                            if (count == 3) {
                                count = 0;
                                bw.newLine();
                            }
                        }

                        bw.write("Start Sleep Time: " + Integer.toString(mParser[startSleepPos + 2] / 100) + ":" + Integer.toString(mParser[startSleepPos + 2] % 100));
                        bw.newLine();
                        bw.write("Awake Time: " + Integer.toString(mParser[getup + 2] / 100) + ":" + Integer.toString(mParser[getup + 2] % 100));
                        bw.newLine();
                        bw.write(light.toString());
                        bw.newLine();
                        bw.write(deep.toString());
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mSleepFM.setParserValue(builder1.toString());
                    if (mSleepFM.isVisible())
                        resetFragments(SLEEP_POSITION);
                    break;
                default:
                    break;
            }*/
        }
    };

    public CwmManager.WearableServiceListener wearableServiceListener = new CwmManager.WearableServiceListener() {
        @Override
        public void onConnected() {
           mDeviceStatus = true;
           if(mSelectTypeFM.isVisible())
               resetFragments(SELECT_DEVICE_POSITION);

            if(mTabataActionItemFM.isVisible()){
                mTabataActionItemFM.setConnectStatus(mDeviceStatus);
                resetFragmentsII(TABATA_ACTION_ITEM_POSITION);
            }
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }

        @Override
        public void onDisconnected() {
            Log.d("bernie","apk onDisconnected");
            mProgressDialog.dismiss();
            mDeviceStatus = false;
            mSelectTypeFM.setDevice("", "", mDeviceStatus);
            if(mSelectTypeFM.isVisible())
                resetFragments(SELECT_DEVICE_POSITION);
            if(mTabataActionItemFM.isVisible()){
                //requestHandler.removeCallbacks(requestTask);
                mTabataActionItemFM.setConnectStatus(mDeviceStatus);
                resetFragmentsII(TABATA_ACTION_ITEM_POSITION);
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Set<BluetoothDevice> set = mBtAdapter.getBondedDevices();
                    if(set.size() != 0){
                      for (BluetoothDevice device : set) {
                          mProgressDialog = ProgressDialog.show(MainActivity.this, "正在連線", "處理中...");
                          cwmManager.CwmBleConnect(device.getAddress());
                          break;
                      }
                    }
                }
            },1500);

        }

        @Override
        public void onServiceDiscovery(String deviceName, String deviceAddress) {
            if(mProgressDialog != null) {
                mProgressDialog.dismiss();
                connectHandler.removeCallbacks(runnable);
            }
            mDeviceName = deviceName;
            mDeviceAddress = deviceAddress;
            mDeviceStatus = true;
            mSelectTypeFM.setDevice(mDeviceName, mDeviceAddress, mDeviceStatus);
            if(mSelectTypeFM.isVisible())
                resetFragments(SELECT_DEVICE_POSITION);

            if(isAddressTableExists()) {
                boolean needReBond = deviceEverBonded(deviceAddress);
                if(needReBond){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("綁定資訊");
                    builder.setMessage("綁定失敗! 請進行重新綁定流程");
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                               @Override
                                public void onDismiss(DialogInterface dialog) {

                               }
                            });
                    builder.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cwmManager.CwmUnBond();
                        }
                    },1000);
                }

            }

        }

        @Override
        public void onNotSupport() {

        }

        @Override
        public void onUnknownProblem(){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
            builder.setTitle("Unknown Problem");
            builder.setMessage("Please try reboot your phone or wait wearable a minute");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            builder.show();
        }
    };

    private CwmManager.AckListener ackListener = new CwmManager.AckListener() {
        @Override
        public void onAckArrival(AckEvents ackEvents) {
            int msgType = ackEvents.getType() & 0xFF;
            int id = ackEvents.getId();
            switch (msgType) {
                case Type.SYSTEM_INFORMATION_COMMAND:

                    switch (id){
                        case ID.USER_CONFIG_INFO:
                            Toast.makeText(getApplicationContext(),"同步使用者組態成功",Toast.LENGTH_SHORT).show();
                            break;
                        case ID.BATTERY_INFO:
                            break;
                        case ID.DEVICE_VERSION_INFO:
                            break;
                        case ID.RESET_USERCONFIG:
                            Toast.makeText(getApplicationContext(),"回復成原廠設定",Toast.LENGTH_SHORT).show();
                            break;
                        case ID.UNBOND:
                            Toast.makeText(getApplicationContext(),"解除綁定回應",Toast.LENGTH_SHORT).show();
                            BluetoothDevice device = mBtAdapter.getRemoteDevice(mDeviceAddress);
                            unpairDevice(device);
                            removeAddressTable();

                            break;
                    }
                    break;
                case Type.SENSOR_GESTURE_COMMAND:
                    switch (id){
                        case ID.TABATA_RESPONSE_MESSAGE:
                            if(isTabataInitStart){
                                isTabataInitStart = false;
                                isTabataPrepareStart = true;
                                cwmManager.CwmTabataCommand(ITEMS.TABATA_PREPARE_START.ordinal(), 0 , 0, 0);
                            }
                            else if(isTabataPrepareStart){
                                isTabataPrepareStart = false;
                                mTabataPrepareFM.setPrepareCommentView(firstTask.getTabataSettings().getItemName());
                                setFragments(TABATA_PREPARE_POSITION);
                                tabataPrepareWork();
                            }
                            else if(isTabataPrepareCount){
                                //Log.d("bernie","tabata prepare count:0");
                                isTabataPrepareCount = false;
                                isTabataPrepareEnd = true;
                                cwmManager.CwmTabataCommand(ITEMS.TABATA_PREARE_END.ordinal(), 0, 0, 0);
                            }
                            else if(isTabataPrepareEnd){
                                //Log.d("bernie","tabata prepare end:");
                                isTabataPrepareEnd = false;
                                if(firstTask != null)
                                    doFirstTabataTask();
                                else
                                    selectActionItemFromQueue();
                            }
                            else if(isTabataActionItem){
                                //Log.d("bernie","tabata action item:");
                                isTabataActionItem = false;
                                mTabataActionItemFM.setConnectStatus(true);
                                setFragments(TABATA_ACTION_ITEM_POSITION);
                                if(firstTask != null)
                                    mTabataActionItemFM.setActionItemView(firstTask.getTabataSettings().getItemName());
                                else
                                    mTabataActionItemFM.setActionItemView(laterTask.getTabataSettings().getItemName());
                                mTabataActionItemFM.setActionItemCommentView(comment);
                                resetFragmentsII(TABATA_ACTION_ITEM_POSITION);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTabataActionItemFM.setActionItemStartView("start");
                                        resetFragmentsII(TABATA_ACTION_ITEM_POSITION);
                                    }
                                },1000);

                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        isTabataActionStart = true;
                                         cwmManager.CwmTabataCommand(ITEMS.TABATA_ACTION_START.ordinal(), 0, 0, 0);
                                    }
                                },1000);
                                firstTask = null;

                            }
                            else if(isTabataActionStart){
                                isTabataActionStart = false;
                                tabataActionEnd = false;
                                //requestHandler.post(requestTask);
                            }
                            else if(isTabataActionEnd){
                                isTabataActionEnd = false;
                                selectActionItemFromQueue();
                            }
                            else if(isTabataIntervalStart){
                                isTabataIntervalStart = false;
                                countDown = totalInterval;
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        if(countDown >= 0) {
                                            Log.d("bernie","timer interval count"+Integer.toString(countDown));
                                            mTabataIntervalFM.setIntervalNextAction(laterTask.getTabataSettings().getItemName());
                                            mTabataIntervalFM.setIntervalCountView(Integer.toString(countDown));
                                            if(mTabataIntervalFM.isVisible())
                                                resetFragments(TABATA_INTERVAL_POSITION);
                                            if(countDown == 0){
                                                timer.cancel();
                                                isTabataIntervalCount = true;
                                            }
                                              cwmManager.CwmTabataCommand(ITEMS.TABATA_REST_COUNT.ordinal(), 0, countDown, 0);
                                            countDown--;
                                        }
                                    }
                                }, 0, 1000);
                            }
                            else if(isTabataIntervalCount){
                                isTabataIntervalCount = false;
                                mTabataIntervalFM.setIntervalCountView("0");
                                resetFragments(TABATA_INTERVAL_POSITION);
                                isTabataIntervalEnd = true;
                                cwmManager.CwmTabataCommand(ITEMS.TABATA_REST_END.ordinal(), 0, 0, 0);

                                Handler handler = new Handler();
                                Handler handler1 = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTabataActionItemFM.setActionItemStartView("start");
                                        if (mTabataActionItemFM.isVisible()) {
                                            resetFragmentsII(TABATA_ACTION_ITEM_POSITION);
                                        } else
                                            setFragments(TABATA_ACTION_ITEM_POSITION);
                                    }
                                },1000);
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        cwmManager.CwmTabataCommand(ITEMS.TABATA_ACTION_START.ordinal(), 0, 0, 0);
                                    }
                                },1000);

                            }
                            else if(isTabataIntervalEnd){
                                isTabataIntervalEnd = false;
                                mTabataActionItemFM.setActionItemView(laterTask.getTabataSettings().getItemName());
                                mTabataActionItemFM.setActionItemCommentView(comment);

                                int itemPos = laterTask.getTabataSettings().getItemPos();
                                totalInterval = laterTask.getTabataSettings().getIntervalTime();
                                isTabataActionItem = true;
                                 cwmManager.CwmTabataCommand(ITEMS.TABATA_ACTION_ITEM.ordinal(), 0, 0, itemPos);
                                Log.d("bernie","tabata action item start");
                            }
                            else if(isTabataDone){
                                isTabataDone = false;
                                curentDoneItems = 0;
                                mTabataQueue = null;
                                mTabataShowFM.setItems(0,0);
                                if(mTabataFM.isVisible())
                                    resetFragments(TABATA_WORK_POSITION);
                                else
                                    setFragments(TABATA_WORK_POSITION);
                            }
                            break;
                        default:
                            Toast.makeText(getApplicationContext(),"ACK",Toast.LENGTH_SHORT).show();
                            break;

                    }
                    break;
                case 0x86:
                    switch (id){
                       case ID.PICK_UP:
                        Toast.makeText(getApplicationContext(),"pick up ack",Toast.LENGTH_SHORT).show();
                        break;
                        case ID.MISSING_CALL:
                            Toast.makeText(getApplicationContext(),"missing call ack",Toast.LENGTH_SHORT).show();
                            break;
                        case ID.INCOMING_CALL:
                            Toast.makeText(getApplicationContext(),"incoming call ack",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case 0x14:
                    Toast.makeText(getApplicationContext(),"Personal Info has sync !",Toast.LENGTH_SHORT).show();
                    break;
                case 0x12:
                    Toast.makeText(getApplicationContext(),"Intelligent has sync !",Toast.LENGTH_SHORT).show();
                    break;
                case 0x05:

                case Type.HISTORY_DTAA_COMMAND:
                    break;
                case Type.COMMAND_COMMAND:
                    break;
                case Type.FACTORY_DATA_COMMAND:
                    switch (id){
                        case ID.UPDATE_BASEMAP:

                            break;
                    }
                    break;
            }
        }
    };

    public void tabataPrepareWork(){
        countDown = totalPrepare;
        //開始倒數
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(countDown >= 0) {
                    mTabataPrepareFM.setPrepareActionView(firstTask.getTabataSettings().getItemName()+"/"+comment);
                    mTabataPrepareFM.setPrepareCountView(Integer.toString(countDown));
                    if(mTabataPrepareFM.isVisible())
                        resetFragments(TABATA_PREPARE_POSITION);
                    if(countDown == 0){
                        //Log.d("bernie","getCountDown");
                        timer.cancel();
                        isTabataPrepareCount = true;
                    }
                    cwmManager.CwmTabataCommand(ITEMS.TABATA_PREPARE_COUNT.ordinal(), countDown, 0, 0);
                    countDown--;
                }
            }
        }, 0, 1000);
    }

    public CwmManager.ErrorListener errorListener = new CwmManager.ErrorListener(){
        @Override
        public void onErrorArrival(ErrorEvents errorEvents){
            int id = errorEvents.getErrorId();
            int cmdType = errorEvents.getMsgCmdType();
            int cmdId = errorEvents.getId();

            if(id == 0x01){ //header lost

            }
            else if(id == 0x02){ //packet lost
                if(cmdType == 0x03 && cmdId == 0x02)
                    cwmManager.CwmSyncFail();
                if(cmdType == 0x03 && cmdId == 0x04)
                    cwmManager.CwmSyncFail();
                if(cmdType == 0x03 && cmdId == 0x01)
                    cwmManager.CwmSyncFail();;
            }
            else if(id == 0x03) { //Checksum error

                cwmManager.CwmReSendBitMap();
            }
        }
    };

    //Fragment Interfaces
    @Override
    public void onNewADevice(){
        if(mDeviceStatus == false){
            cwmManager.CwmBleSearch();
        }
    }
    @Override
    public void onDisconnectFromDevice(){
           cwmManager.CwmBleDisconnect();
    }
    @Override
    public void onConnectToDevice(){

    }
    @Override
    public void onResetBoundRecord(){
      cwmManager.CwmUnBond();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onRequesEnableRun(int enable){
        Log.d("bernie","set run:"+Integer.toString(enable));
        if(enable == 0)
           cwmManager.CwmEnableRun(1, 0);
        if(enable == 1)
            cwmManager.CwmEnableRun(1, 1);
    }
    @Override
    public void onRequestBattery(){
        if(mDeviceStatus != false) {
           cwmManager.CwmRequestBattery();
        }
    }

    @Override
    public void onAlarmRequest(){
        cwmManager.CwmRequestUserConfig();
    }

    @Override
    public void onSystemRequest(){cwmManager.CwmRequestUserConfig();}

    @Override
    public void onPressSelfTestButton(int componet){
        mProgressDialog = ProgressDialog.show(this,"開始自我檢測","檢測中...");
       cwmManager.CwmFactory(ID.SELF_TEST_RESULT, componet);
    }
    @Override
    public void onPressClearSelfTestTResultButton(){
        if(mFactory.isVisible()){
            selfTestBuilder = new StringBuilder();
            mFactory.updateResult(selfTestBuilder.toString(),calibrateBuilder.toString());
            resetFragments(FACTORY_POSITION);
        }
    }

    @Override
    public void onPressSync100(int cmd, float para){
        cwmManager.CwmEnableRun(cmd, para);
    }

    @Override
    public void onPressRequestAddUp(int cmd){
        cwmManager.CwmEnableRun(cmd, 0);
    }

    @Override
    public void onSycRequest(){
        cwmManager.CwmSendUserConfig(config);
    }

    @Override
    public void onResetDefault(){
        Log.d("bernie","reseeett");
       cwmManager.CwmResetUserConfig();
    }

    @Override
    public void onPressCalibrateButton(int componet){
        mProgressDialog = ProgressDialog.show(this,"開始校正","校正中...");
        cwmManager.CwmFactory(ID.CALIBRATION_RESULT, componet);
    }

    @Override
    public void onPressClearCalibrateButton(){
        if(mFactory.isVisible()){
            calibrateBuilder = new StringBuilder();
            mFactory.updateResult(selfTestBuilder.toString(), calibrateBuilder.toString());
            resetFragments(FACTORY_POSITION);
        }
    }

    @Override
    public void onPressRecordSensorDataButton(){
        cwmManager.CwmFactory(ID.RECORD_SENSOR_DATA, 0);
    }


    @Override
    public void onIntelligentSaveToRing(){
        IntelligentSettings settings = new IntelligentSettings();
        int index;
        boolean status;
        int targetStep;
        int sedentaryRemind;

        index = GESTURE.SEDENTARY.ordinal();
        status = mIntelligentFM.getSendtaryRemindStatus();
        if(status)
          settings.enableGesture(index);
        else
          settings.disableGesture(index);

        Log.d("bernie","sedantary:"+Boolean.toString(status));

        index = GESTURE.WATCH_TAKE_OFF.ordinal();
        status = mIntelligentFM.getOnWearStatus();
        if(status)
            settings.enableGesture(index);
        else
            settings.disableGesture(index);

        Log.d("bernie","onWear:"+Boolean.toString(mIntelligentFM.getOnWearStatus()));

        index = GESTURE.HAND_UP.ordinal();
        status = mIntelligentFM.getHandUpStatus();
        if(status)
            settings.enableGesture(index);
        else
            settings.disableGesture(index);
        Log.d("bernie","HandUp:"+Boolean.toString(mIntelligentFM.getHandUpStatus()));


        index = GESTURE.WRIST_SCROLL.ordinal();
        status = mIntelligentFM.getWritstScrollStatus();
        if(status)
            settings.enableGesture(index);
        else
            settings.disableGesture(index);

        Log.d("bernie","Wrist:"+Boolean.toString(mIntelligentFM.getWritstScrollStatus()));

        targetStep = mIntelligentFM.getAim();
        settings.setGoal(targetStep);
        Log.d("bernie","Target:"+Integer.toString(mIntelligentFM.getAim()));

        sedentaryRemind = mIntelligentFM.getSedentartTime();
        settings.setSedentaryTime(sedentaryRemind);
        Log.d("bernie","Sed:"+Integer.toString(mIntelligentFM.getSedentartTime()));

        config.setIntelligentSetting(settings);

    }

    @Override
    public void onIntelligentRequest(){
        cwmManager.CwmRequestUserConfig();
        Log.d("bernie","requestIntelligent");
    }

    @Override
    public void onPersonalRequest(){
        cwmManager.CwmRequestUserConfig();
    }
    @Override
    public void onPersonalInfoSaveToUserConfig(){

        int gender;
        int age;
        int height;
        int weight;

        if(mPersonalInfoFM.getSex().charAt(0) == 'm' || mPersonalInfoFM.getSex().charAt(0) == 'M')
            gender = 0;
        else
            gender = 1;
        age = mPersonalInfoFM.getOld();
        height = mPersonalInfoFM.getHigh();
        weight = mPersonalInfoFM.getWeight();

        BodySettings body = new BodySettings();
        body.setSex(gender);
        body.setOld(age);
        body.setHight(height);
        body.setWeight(weight);

        config.setPersonProfile(body);

        Log.d("bernie","gender:"+Integer.toString(gender));
        Log.d("bernie","age:"+Integer.toString(age));
        Log.d("bernie","hight:"+Integer.toString(height));
        Log.d("bernie","weight:"+Integer.toString(weight));

    }

    @Override
    public void onSoftReset(){
        cwmManager.CwmSoftReset();
    }

    @Override
    public void onInitTabata(Queue<TabataTask> mTabataQ){
        tabataHasDone = false;
        mTabataQueue = mTabataQ;
        int size = mTabataQueue.size();
        Log.d("bernie","size is "+Integer.toString(size));
        isTabataInitStart = true;
        firstTask = mTabataQ.poll();
        if(firstTask.getTabataSettings().getItemName().equals("Push Up"))
            comment = "伏地挺身";
        else if(firstTask.getTabataSettings().getItemName().equals("Crunch"))
            comment = "捲腹";
        else if(firstTask.getTabataSettings().getItemName().equals("Squart"))
            comment = "深蹲";
        else if(firstTask.getTabataSettings().getItemName().equals("Jumping Jack"))
            comment = "開合跳";
        else if(firstTask.getTabataSettings().getItemName().equals("Dips"))
            comment = "椅子三頭肌稱體";
        else if(firstTask.getTabataSettings().getItemName().equals("High Kniess Running"))
            comment = "原地提膝踏步";
        else if(firstTask.getTabataSettings().getItemName().equals("Lunges"))
            comment = "前屈深蹲";
        else if(firstTask.getTabataSettings().getItemName().equals("Burpees"))
            comment = "波比跳";
        else if(firstTask.getTabataSettings().getItemName().equals("Step On Chair"))
            comment = "登階運動";
        else if(firstTask.getTabataSettings().getItemName().equals("PushUp Rotation"))
            comment = "T型伏地挺身";

        mTabataSettings = firstTask.getTabataSettings();
        totalPrepare = mTabataSettings.getPrepareTime();
        Log.d("bernie","Tabata prepare is"+Integer.toString(totalPrepare));
        totalInterval = mTabataSettings.getIntervalTime();
        Log.d("bernie","Tabata interval is"+Integer.toString(totalInterval));
        totalItems = size;
        mTabataShowFM.setItems(curentDoneItems, totalItems);
        goalTimes = mTabataSettings.getActionTimes();
        Log.d("bernie","Tabata goal is"+Integer.toString(goalTimes));
        cwmManager.CwmTabataCommand(ITEMS.TABATA_INIT.ordinal(), 0 , 0, 0);
    }


    @Override
    public void onRequestSwVersion(){
        cwmManager.CwmRequestSwVersion();
    }

    public void doFirstTabataTask(){

        int itemPos = firstTask.getTabataSettings().getItemPos();;

        totalInterval = firstTask.getTabataSettings().getIntervalTime();
        isTabataActionItem = true;
        cwmManager.CwmTabataCommand(ITEMS.TABATA_ACTION_ITEM.ordinal(), 0, 0, itemPos);
    }

    public void selectActionItemFromQueue(){

        if(mTabataQueue.size() != 0) {
            laterTask = mTabataQueue.poll();
            Log.d("bernie","selectActionItemFromQueue:"+laterTask.getTabataSettings().getItemName());
            if(laterTask.getTabataSettings().getItemName().equals("Push Up"))
                comment = "伏地挺身";
            else if(laterTask.getTabataSettings().getItemName().equals("Crunch"))
                comment = "捲腹";
            else if(laterTask.getTabataSettings().getItemName().equals("Squart"))
                comment = "深蹲";
            else if(laterTask.getTabataSettings().getItemName().equals("Jumping Jack"))
                comment = "開合跳";
            else if(laterTask.getTabataSettings().getItemName().equals("Dips"))
                comment = "椅子三頭肌稱體";
            else if(laterTask.getTabataSettings().getItemName().equals("High Kniess Running"))
                comment = "原地提膝踏步";
            else if(laterTask.getTabataSettings().getItemName().equals("Lunges"))
                comment = "前屈深蹲";
            else if(laterTask.getTabataSettings().getItemName().equals("Burpees"))
                comment = "波比跳";
            else if(laterTask.getTabataSettings().getItemName().equals("Step On Chair"))
                comment = "登階運動";
            else if(laterTask.getTabataSettings().getItemName().equals("PushUp Rotation"))
                comment = "T型伏地挺身";
            mTabataIntervalFM.setIntervalActionComment(comment);
            setFragments(TABATA_INTERVAL_POSITION);
            totalInterval = laterTask.getTabataSettings().getIntervalTime();
            goalTimes = laterTask.getTabataSettings().getActionTimes();
            isTabataIntervalStart = true;
            cwmManager.CwmTabataCommand(ITEMS.TABATA_REST_START.ordinal(), 0, 0, 0);
        }
        else{
            Toast.makeText(getApplicationContext(),"You have accomplished a round",Toast.LENGTH_SHORT).show();
            cwmManager.CwmTabataCommand(ITEMS.TABATA_DONE.ordinal(), 0, 0, 0);
            accumulationCalories = 0;
            setFragments(TABATA_SHOW_POSITION);
        }
    }

    public void sendActionItemEnd(){
        curentDoneItems++;
        mTabataShowFM.setItems(curentDoneItems, totalItems);
        Log.d("bernie","sendActionItemEnd");
        isTabataActionEnd = true;
        tabataActionEnd = true;
        cwmManager.CwmTabataCommand(ITEMS.TABATA_ACTION_END.ordinal(), 0 , 0, 0);
    }

    @Override
    public void onPressTabataPauseButton(){
        cwmManager.CwmTabataCommand(ITEMS.TABATA_PAUSE.ordinal(), 0 , 0, 0);
    }

    @Override
    public void onSaveAlarmSettings(AlarmSetting setting){
        cwmManager.CwmSyncStart();
        config.setAlarmSetting(setting);
    }

    @Override
    public void onStartUpdateBaseMap(){
          cwmManager.CwmUpdateBitMapInit();
          mProgressDialog = ProgressDialog.show(this,"開始更新底圖","進度: 0%");
         cwmManager.CwmSendBitMap();
    }

    @Override
    public void onPressTabataDoneButton(){
        tabataHasDone = true;
        mTabataActionItemFM.setActionCountView("0");
        Log.d("bernie","onPress Tabata Done");
        if(timer != null)
            timer.cancel();
        isTabataDone = true;
        //requestHandler.removeCallbacks(requestTask);
        cwmManager.CwmTabataCommand(ITEMS.TABATA_DONE.ordinal(), 0 , 0, 0);
        accumulationCalories = 0;
        mTabataActionItemFM.setCalories(Integer.toString(accumulationCalories));
        mTabataActionItemFM.setHeartRate(0);
        mTabataActionItemFM.setActionCountView("0");
        mTabataActionItemFM.setStrength("0");

        curentDoneItems = 0;
        mTabataQueue = null;
        mTabataShowFM.setItems(0,0);
        if(mTabataFM.isVisible())
            resetFragments(TABATA_WORK_POSITION);
        else
            setFragments(TABATA_WORK_POSITION);

    }

    @Override
    public void onPressTabataResumeButton(){
        cwmManager.CwmTabataCommand(ITEMS.TABATA_RESUME.ordinal(), 0, 0, 0);
    }

    @Override
    public void onPressSyncAutoButton(){
        cwmManager.CwmSyncRequest();
    }

    @Override
    public void onPressEraseOLEDButton(){
        mProgressDialog = ProgressDialog.show(this,"按下清除OLED鍵","處理中...");
        cwmManager.CwmEraseBaseMap(1);
    }

    @Override
    public void onPressEraseBitMapButton(){
        mProgressDialog = ProgressDialog.show(this,"按下清除BitMap鍵","處理中...");
        cwmManager.CwmEraseBaseMap(2);
    }

    @Override
    public void onPressEraseFontButton(){
        mProgressDialog = ProgressDialog.show(this,"按下清除FontLib鍵","處理中...");
        cwmManager.CwmEraseBaseMap(3);
    }

    @Override
    public void onPressCheckBox(int sensorType){
      cwmManager.CwmSensorReport(sensorType);
    }

    @Override
    public void onPressSaveSystemInfomation(int osType, int format, int historyDetect,
                                            int screenTimeout, int screens, int functions, int noDisturbStart
    ,int noDisturbStop, int sleepStartTime, int sleepStopTime, int brightnessProgress){
        SystemSetting systemSetting = new SystemSetting();

        systemSetting.setOSType(osType);
        systemSetting.setTimeFormat(format);
        systemSetting.setHistoryDetectPeriod(historyDetect);
        systemSetting.setScreenTimeOut(screenTimeout);
        systemSetting.setScreens(screens);
        systemSetting.setFunctions(functions);
        Log.d("bernie","functions:"+Integer.toString(historyDetect));
        systemSetting.setSleepTimeInterval(sleepStartTime, sleepStopTime);
        systemSetting.setNoDisturbInterval(noDisturbStart, noDisturbStop);
        systemSetting.setBrightness(brightnessProgress);


        config.setSystemSetting(systemSetting);

        Log.d("bernie","osType: "+Integer.toString(osType));
        Log.d("bernie","format: "+Integer.toString(format));
        Log.d("bernie","function:"+Integer.toString(functions));
        Log.d("bernie","historyDetect: "+Integer.toString(historyDetect));
        Log.d("bernie", "screen time out:"+Integer.toString(screenTimeout));
        Log.d("bernie","screens:"+Integer.toString(screens));
        Log.d("bernie","sleep interval: "+Integer.toString(sleepStartTime)+" "+Integer.toString(sleepStopTime));
    }

    @Override
    public void onPressSyncEraseButton(){
        mProgressDialog = ProgressDialog.show(this,"按下清除歷程鍵","處理中...");
        cwmManager.CwmEraseLog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("bernie","on Create");
        setContentView(R.layout.activity_main);
        // set toolbar to be action bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
        // set navigation item selected behavior
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.navigation_view);
        mNavView.setNavigationItemSelectedListener(mNavViewOnNavItemSelListener);
        // set tool bar toggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.toolbar_openDrawer, R.string.toolbar_closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mFM = super.getSupportFragmentManager();
        mFragments.add(mSystemFM); //0
        mFragments.add(mPersonalInfoFM); //1
        mFragments.add(mSelectTypeFM);//2
        mFragments.add(mIntelligentFM);//3
        mFragments.add(mRingBatteryFM);//4
        mFragments.add(mTabataFM);//5
        mFragments.add(mTabataPrepareFM);//6
        mFragments.add(mTabataActionItemFM);//7
        mFragments.add(mTabataShowFM);//8
        mFragments.add(mTabataIntervalFM);///9
        mFragments.add(mSwVersionFM);//10
        mFragments.add(mFlashFM);//11
        mFragments.add(mSensorsFM);//12
        mFragments.add(mAlarmFM);//13
        mFragments.add(mFactory);//14
        mFragments.add(mRunFM);//15
        mFragments.add(mBaseFM);//16

        cwmManager = new CwmManager(this,wearableServiceListener, eventListener, ackListener, errorListener);
        statusCheck();

        setFragments(SELECT_DEVICE_POSITION);

        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }

        telM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telM.listen(telListener, PhoneStateListener.LISTEN_CALL_STATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(NotificationReceiver, makeGattUpdateIntentFilter());
        registerReceiver(NewsReceiver, makeNewstFilter());
        registerReceiver(bluetoothReceiver, makeBluetoothFilter());

        final Set<BluetoothDevice> set = mBtAdapter.getBondedDevices();

        if(set.size() != 0){
            mProgressDialog = ProgressDialog.show(this,"正在連線","處理中...");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for(BluetoothDevice device : set) {
                        cwmManager.CwmBleConnect(device.getAddress());
                        break;
                    }
                }
            },1000);
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("bernie","on pause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("bernie", "on stop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("bernie","on Destroy");
        if(timer != null)
            timer.cancel();
       // requestHandler.removeCallbacks(requestTask);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(NotificationReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(NewsReceiver);
        cwmManager.CwmTabataCommand(ITEMS.TABATA_DONE.ordinal(),0,0,0);
        cwmManager.CwmReleaseResource();
        telM.listen(telListener, PhoneStateListener.LISTEN_NONE);
        finish();
    }


    @Override
    public void onBackPressed() {
        if(timer != null)
          timer.cancel();
        //requestHandler.removeCallbacks(requestTask);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(NotificationReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(NewsReceiver);
        cwmManager.CwmTabataCommand(ITEMS.TABATA_DONE.ordinal(),0,0,0);
        finish();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_DEVICE:
                //When the DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mProgressDialog = ProgressDialog.show(this,"正在連線","處理中...");

                    connectHandler.postDelayed(runnable,15000);

                    cwmManager.CwmBleConnect(deviceAddress);
                }
                break;

            default:    break;
        }
    } // onActivityResult()

    public void onConnect(final View view){
        cwmManager.CwmBleSearch();
    }

    public void onDisconnect(final View view){
        cwmManager.CwmBleDisconnect();
    }

    private void navigateTo(MenuItem item) {
        mNavIdx = item.getItemId();
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.navigation_item_0:
                mToolbar.setTitle("系統設定");
                setFragments(SYSTEM_POSITION);
                break;
            case R.id.navigation_item_1:
                mToolbar.setTitle("個人資料");
                setFragments(PERSONAL_POSITION);
                break;
            case R.id.navigation_item_2:
                mToolbar.setTitle("移除/新增手環");
                setFragments(SELECT_DEVICE_POSITION);
                break;
            case R.id.navigation_item_4:
                mToolbar.setTitle("智慧應用");
                setPreferencedFragments();
                break;
            case R.id.navigation_item_5:
                mToolbar.setTitle("手環電量");
                setFragments(RING_BATTERY_POSITION);
               break;
            case R.id.navigation_item_6:
                mToolbar.setTitle("訓練");
                setFragments(TABATA_WORK_POSITION);
                break;
            case R.id.navigation_item_7:
                mToolbar.setTitle("裝置版本");
                setFragments(SW_VERSION_POSITION);
                break;
            case R.id.navigation_item_8:
                 buildAlertMessageSwichOTA();
                break;
            case R.id.navigation_item_10:
                mToolbar.setTitle("工程模式");
                setFragments(SENSORS_POSITION);
                break;
            case R.id.navigation_item_11:
                mToolbar.setTitle("鬧鐘設定");
                setFragments(ALARM_POSITION);
                break;
            case R.id.navigation_item_12:
                mToolbar.setTitle("工廠模式");
                setFragments(FACTORY_POSITION);
                break;
            case R.id.navigation_item_13:
                mToolbar.setTitle("跑步模式");
                setFragments(RUN_POSITION);
                break;
            case R.id.navigation_item_14:
                mToolbar.setTitle("歷程測試");
                setFragments(FLASH_TEST_POSITION);
                break;
            case R.id.navigation_item_15:
                mToolbar.setTitle("更新底圖");
                setFragments(BASE_MAP_POSITION);
                break;
            default: break;
        }
        mDrawerLayout.closeDrawers();
    } // navigateTo

    private void setFragments(int position){
        mFM.beginTransaction().replace(R.id.frames, mFragments.get(position), "t"+position).commit();
    } // setFragments()
    private void resetFragments(int position) {
        mFM.beginTransaction().detach(mFragments.get(position)).attach(mFragments.get(position)).commitAllowingStateLoss();
    }
    private void resetFragmentsII(int position){
        mFM.beginTransaction().hide(mFragments.get(position)).show(mFragments.get(position)).commitAllowingStateLoss();
    }
    private void setPreferencedFragments(){
        mFM.beginTransaction().replace(R.id.frames, mIntelligentFM).commit();
    }
    private void resetPreferencedFragments(){
        mFM.beginTransaction().detach(mFragments.get(4)).attach(mFragments.get(4)).commit();
    }

    private void statusCheck(){
        // setup bluetooth
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(this, "BT is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        final LocationManager localManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (!localManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        //Android 6.0 and later need permission of loacation in order to scan BLE device.
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
                }
            });
            builder.show();
        }
        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("External Storage");
            builder.setMessage("Please grant external storage to access file.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                }
            });
            builder.show();
        }
        if(this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Phone State");
            builder.setMessage("Please grant to access phone state");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                   @Override
                     public void onDismiss(DialogInterface dialog) {
                       requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                    }
             });
             builder.show();
        }
        if(this.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Contact Info");
            builder.setMessage("Please grant to access contact on the phone");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
            public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT);
                }
             });
            builder.show();
        }
        if(this.checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("SMS Info");
            builder.setMessage("Please grant to access SMS from phone");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, RECEIVE_SMS);
                }
            });
            builder.show();
        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        Toast.makeText(MainActivity.this, "You have limition of ble devices scanning!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void buildAlertMessageSwichOTA() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(mDeviceStatus == true) {
            builder.setMessage("The Device will switch to OTA mode when you press the Yes. Do you \n" +
                    "want to keep going?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            cwmManager.CwmSwitchOTA();
                            final Intent newIntent = new Intent(getApplication(), DfuActivity.class);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(newIntent);
                            Toast.makeText(getApplicationContext(), "Please check your device is connecting", Toast.LENGTH_LONG);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            //do nothing
                        }
                    });
        }
        else{
            builder.setMessage("Please check your device is connecting")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            //do nothing
                        }
                    });
        }
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(requestCode) {
            case WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do nothing
                } else {
                    builder.setMessage("This app will close when you refuse the access external storage file permission")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    finish();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }
                break;
            case PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do nothing
                } else {
                    builder.setMessage("This app will close when you refuse the access location permission")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    finish();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }
                break;
        }
    }

    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }

    private final BroadcastReceiver NewsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("bernie","SMS!!");
            NotificationData data = new NotificationData();
            data.setNotifyId(NOTIFICATION.NEWS.ordinal());
            cwmManager.CwmNotification(data);
        }
    };

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();            // Get intent's action string
            Bundle extras = intent.getExtras();            // Get all the Intent's extras
            if (extras == null) return;                    // All intents of interest have ex
            switch (action) {
                case "android.bluetooth.device.action.BOND_STATE_CHANGED": {
                    bondStateChanged(
                            extras.getInt("android.bluetooth.device.extra.BOND_STATE", -1),
                            (BluetoothDevice) extras.get("android.bluetooth.device.extra.DEVICE"));
                    break;
                }
            }
        }
    };

    private final BroadcastReceiver NotificationReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
           final  int code = intent.getIntExtra("Notification Code",0);
            final String title = intent.getStringExtra(Notification.EXTRA_TITLE);
            final String contactName = intent.getStringExtra("CONTACT_NAME");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(code == NotificationListenerExampleService.InterceptedNotificationCode.QQ_CODE){
                        NotificationData data = new NotificationData();
                        data.setNotifyId(NOTIFICATION.SOCIAL.ordinal());
                        data.setAppIdentifier(SOCIAL_APP.QQ.ordinal());
                        data.setAppName("QQ");
                        cwmManager.CwmNotification(data);
                    }
                    else if(code == NotificationListenerExampleService.InterceptedNotificationCode.WECHART_CODE){
                        NotificationData data = new NotificationData();
                        data.setNotifyId(NOTIFICATION.SOCIAL.ordinal());
                        data.setAppIdentifier(SOCIAL_APP.WECHART.ordinal());
                        data.setAppName("WeChat");
                        cwmManager.CwmNotification(data);
                    }
                    else if(code == NotificationListenerExampleService.InterceptedNotificationCode.DOBAN_CODE){
                        NotificationData data = new NotificationData();
                        data.setNotifyId(NOTIFICATION.SOCIAL.ordinal());
                        data.setAppIdentifier(SOCIAL_APP.DOBAN.ordinal());
                        data.setAppName("Doban");
                        cwmManager.CwmNotification(data);
                    }
                    else if(code == NotificationListenerExampleService.InterceptedNotificationCode.GMAIL_CODE){
                        NotificationData data = new NotificationData();
                        data.setNotifyId(NOTIFICATION.EMAIL.ordinal());
                        cwmManager.CwmNotification(data);
                    }
                    else if(code == NotificationListenerExampleService.InterceptedNotificationCode.TELE_CODE){
                        onRing = true;
                        NotificationData phoneData = new NotificationData();
                        phoneData.setNotifyId(NOTIFICATION.CALL.ordinal());
                        phoneData.setPersonName(contactName);
                        cwmManager.CwmNotification(phoneData);
                    }
                    else if(code == NotificationListenerExampleService.InterceptedNotificationCode.TELE_SERVER_CODE){
                        NotificationData data = new NotificationData();
                        data.setNotifyId(NOTIFICATION.MISSING_CALL.ordinal());
                        //data.setPersonName(title);
                        cwmManager.CwmNotification(data);
                    }
                    else if(code == NotificationListenerExampleService.InterceptedNotificationCode.TELE_OFF_HOOK){
                        onRing = false;
                        NotificationData data = new NotificationData();
                        data.setNotifyId(NOTIFICATION.PICK_UP.ordinal());
                        cwmManager.CwmNotification(data);

                    }
                    else if(code == NotificationListenerExampleService.InterceptedNotificationCode.TELE_IDLE){
                        if(onRing == true){
                            Log.d("bernie","Missing call");
                            onRing = false;
                            NotificationData data = new NotificationData();
                            data.setNotifyId(NOTIFICATION.MISSING_CALL.ordinal());
                            cwmManager.CwmNotification(data);
                        }
                    }
                    else if(code == NotificationListenerExampleService.InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE){
                        NotificationData data = new NotificationData();
                        data.setNotifyId(NOTIFICATION.SOCIAL.ordinal());
                        data.setAppIdentifier(SOCIAL_APP.OTHER.ordinal());
                        data.setAppName("赖的测试");
                        cwmManager.CwmNotification(data);
                    }
                }
            });

        }
    };

    // Intent Filter -------------------------------------------------------------------------------
    private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.github.gn00618777");
        return intentFilter;
    }
    private IntentFilter makeNewstFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        return intentFilter;
    }

    private IntentFilter makeBluetoothFilter(){
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        return intentFilter;
    }

    public void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
           // Log.e(TAG, e.getMessage());
        }
    }

    private void bondStateChanged(int state, BluetoothDevice device) {
        switch (state) {
            case BluetoothDevice.BOND_NONE:
                break;
            case BluetoothDevice.BOND_BONDING:
                break;
            case BluetoothDevice.BOND_BONDED:
                // TODO: trigger changes in how widget handles clicks and displays this device
                if(device.getName().equals("SmartBand")){
                    Log.d("bernie", "Bluetooth bond state changed to " + state + " for " + device.getName());
                    recordAddress(device.getAddress());
                }
        }
    }

    private boolean deviceEverBonded(String address){
        boolean isEverBonded = false;
        boolean needResetBond = false;
        final Set<BluetoothDevice> set = mBtAdapter.getBondedDevices();
        if(set.size() != 0){
            for (BluetoothDevice device : set) {
                if(device.getAddress().equals(address)){
                    isEverBonded = true;
                    needResetBond = false;
                    break;
                }
            }

            if(!isEverBonded){
                //檢查資料庫是否綁定過
                String mac =readBondedRecord();
                if(mac.equals(address))
                    needResetBond = true;
            }
        }
        else{
            //檢查資料庫是否綁定過
            String mac = readBondedRecord();
            if(mac.equals(address)) {
                needResetBond = true;
            }
        }
        return needResetBond;
    }

    private String readBondedRecord(){
        BufferedReader br = null;
        String response = "";
        try {
            StringBuffer output = new StringBuffer();
            String fullPath = getCacheDir().getPath();
            String savePath = fullPath + File.separator +"bondedAddress"+".txt";
            br = new BufferedReader(new FileReader(savePath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            response = output.toString();
            br.close();

        } catch(FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
        return response;
    }
    private void recordAddress(String address){
        try {

            String fullPath = getCacheDir().getPath();
            String savePath = fullPath + File.separator + "/"+"bondedAddress"+".txt";

            File file = new File(savePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(address);
            bw.newLine();

            bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private boolean isAddressTableExists(){
        String fullPath = getCacheDir().getPath();
        String savePath = fullPath + File.separator + "/"+"bondedAddress"+".txt";

        File file = new File(savePath);
        return file.exists();
    }

    private void removeAddressTable(){
        String fullPath = getCacheDir().getPath();
        String savePath = fullPath + File.separator + "/"+"bondedAddress"+".txt";

        File file = new File(savePath);
        if(file.exists())
            file.delete();
    }
}
