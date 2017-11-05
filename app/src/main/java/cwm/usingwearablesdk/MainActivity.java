package cwm.usingwearablesdk;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import cwm.wearablesdk.CwmManager;
import cwm.wearablesdk.CwmEvents;
import cwm.wearablesdk.AckEvents;
import cwm.wearablesdk.BodySettings;
import cwm.wearablesdk.IntelligentSettings;
import cwm.wearablesdk.TabataObject;
import cwm.wearablesdk.TabataSettings;
import cwm.wearablesdk.ErrorEvents;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Environment;
import android.app.ProgressDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements SelectTypeFragment.ListenForSelectTypeFragment,
RingBatteryFragment.ListenForRingStatusFragment, TimeSyncFragment.ListenForSyncTimeFragment,
        IntelligentFragment.ListenerForIntellignetFragment,PersonalInfoFragment.ListenForPersonalInfoFragment,
         TabataFragment.ListenForTabataFragment, RequestSleepFragment.ListenForRequestSleepFragment,
SwVersionFragment.ListenForSwVersionFragment, SleepFragment.ListenForSleepFragment,
        TabataPrepareFragment.ListenForTabataPrepareFragment, TabataActionItemFragment.ListenForTabataActionItemFragment,
TabataIntervalFragment.ListenForTabataIntervalFragment, TabataShowFragment.ListenForTabataShowFragment,
FlashFragment.ListenForFlashFragment{

   private final int REQUEST_SELECT_DEVICE = 2;
    private final int WRITE_EXTERNAL_STORAGE = 4;

    //sdk
    private CwmManager cwmManager;
    //UI
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private NavigationView.OnNavigationItemSelectedListener mNavViewOnNavItemSelListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            navigateTo(item);
            return true;
        }
    };
    private int mNavIdx;
    private FragmentManager mFM;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private ShowDataFragment mShowDataFM = new ShowDataFragment();
    private PersonalInfoFragment mPersonalInfoFM = new PersonalInfoFragment();
    private SelectTypeFragment mSelectTypeFM = new SelectTypeFragment();
    private SleepFragment mSleepFM = new SleepFragment();
    private IntelligentFragment mIntelligentFM = new IntelligentFragment();
    private RingBatteryFragment mRingBatteryFM = new RingBatteryFragment();
    private TimeSyncFragment mTimeSyncFM = new TimeSyncFragment();
    private TabataFragment mTabataFM = new TabataFragment();
    private TabataPrepareFragment mTabataPrepareFM = new TabataPrepareFragment();
    private SwVersionFragment mSwVersionFM = new SwVersionFragment();
    private TabataShowFragment mTabataShowFM = new TabataShowFragment();
    private TabataActionItemFragment mTabataActionItemFM = new TabataActionItemFragment();
    private TabataIntervalFragment mTabataIntervalFM = new TabataIntervalFragment();
    private FlashFragment mFlashFM = new FlashFragment();

    private String mDeviceName = null;
    private String mDeviceAddress = null;
    private boolean mDeviceStatus = false;
    private byte[] mValue;
    private int[] mParser;

    private BluetoothAdapter mBtAdapter = null;

    private TabataSettings mTabataSettings;
    private int totalCycle = 0;
    private int totalPrepare = 0;
    private int totalInterval = 0;
    private int currnetCycle = 0;
    private int goalTimes = 0;
    private int totalItems = 0;
    private int curentDoneItems = 0;

    private Queue<TabataObject> mTabataQueue;

    private boolean isPressPrepareEnd = false;
    private boolean isPressActionItemEnd = false;
    private boolean isPressTabataDone = false;

    final int SHOWDATA_POSITION = 0;
    final int PERSONAL_POSITION = 1;
    final int SELECT_DEVICE_POSITION = 2;
    final int SLEEP_POSITION = 3;
    final int RING_BATTERY_POSITION = 5;
    final int TIME_SYNC_POSITION = 6;
    final int TABATA_WORK_POSITION = 7;
    final int TABATA_PREPARE_POSITION = 8;
    final int TABATA_ACTION_ITEM_POSITION  = 9;
    final int TABATA_SHOW_POSITION = 10;
    final int TABATA_INTERVAL_POSITION = 11;
    final int SW_VERSION_POSITION = 12;
    final int FLASH_TEST_POSITION = 13;

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
        TABATA_DONE
    };

    private ProgressDialog mProgressDialog = null;

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



    // information
    private IntelligentSettings testSettings;
    private BodySettings testS1ettings;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 3;

    public CwmManager.EventListener eventListener = new CwmManager.EventListener() {
        @Override
        public void onEventArrival(CwmEvents cwmEvents) {
            int id = cwmEvents.getId();
            String Type = "";
            switch (id){
                case 0xAF:
                    String gesture = "";
                    if(cwmEvents.getStatus() == 1){
                        gesture = "static";
                    }
                    else if(cwmEvents.getStatus() == 2){
                        gesture = "walk";
                    }
                    else if(cwmEvents.getStatus() == 3){
                        gesture = "run";
                    }
                    else if(cwmEvents.getStatus() == 4){
                        gesture = "bike";
                    }
                    mShowDataFM.setValue(cwmEvents.getWalkStep(),cwmEvents.getDistance(),
                            cwmEvents.getCalories(),gesture);
                    if(mShowDataFM.isVisible()){
                        resetFragments(SHOWDATA_POSITION);
                    }
                    break;

                case 0x04:
                    mShowDataFM.setHeartValue(cwmEvents.getHeartBeat());
                    if(mShowDataFM.isVisible()){
                        resetFragments(SHOWDATA_POSITION);
                    }
                    break;

                case 0x01:
                    Type = "TAP";
                    Toast.makeText(getApplicationContext(),Type,Toast.LENGTH_SHORT).show();
                    break;

                case 0x02:
                    Type = "WRIST";
                    Toast.makeText(getApplicationContext(),Type,Toast.LENGTH_SHORT).show();
                    break;

                case 0x06:
                    Type = "SHAKE";
                    Toast.makeText(getApplicationContext(),Type,Toast.LENGTH_SHORT).show();
                    break;

                case  0x08:
                    Type = "SIGNIFICANT";
                    Toast.makeText(getApplicationContext(),Type,Toast.LENGTH_SHORT).show();
                    break;

                case 0xED:
                    mRingBatteryFM.setValue(cwmEvents.getBattery());
                    if(mRingBatteryFM.isVisible()){
                        resetFragments(RING_BATTERY_POSITION);
                    }
                    break;
                case 0x90:
                    mSwVersionFM.setVersion(cwmEvents.getVersion());
                    if(mSwVersionFM.isVisible()){
                        resetFragments(SW_VERSION_POSITION);
                    }
                    break;
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
                    builder1.append("Start Sleep Time: "+Integer.toString(mParser[startSleepPos+2]/100)+":"+Integer.toString(mParser[startSleepPos+2]%100)+"\n");
                    builder1.append("Awake Time: "+Integer.toString(mParser[getup+2]/100)+":"+Integer.toString(mParser[getup+2]%100)+"\n");

                    /***************************************************************/
                    for(int i = 0; i <= getup ; i+=3) {
                      if(mParser[i] % 100 == 1) {
                         light.append("Day-"+Integer.toString(mParser[i+1]/100)+"/"+Integer.toString(mParser[i+1]%100)+" Time-"+
                                 Integer.toString(mParser[i+2]/100)+":"+Integer.toString(mParser[i+2]%100)+"\n");
                     }
                 }
                 for(int i = 0; i <= getup ; i+=3) {
                     if(mParser[i] % 100 == 2) {
                         deep.append("Day-"+Integer.toString(mParser[i+1]/100)+"/"+Integer.toString(mParser[i+1]%100)+" Time-"+
                                 Integer.toString(mParser[i+2]/100)+":"+Integer.toString(mParser[i+2]%100)+"\n");
                     }
                 }
                    /***************************************************************/
                 for(int i = 0; i < cwmEvents.getParserLength() ; i+=3) {
                     if(mParser[i] == 100)
                         startSleepPos = i;
                 }

                 builder1.append(light.toString()+"\n");
                 builder1.append(deep.toString()+"\n");

                 try {
                     FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().toString() + "/Download/CwmSleepLog.txt", false);
                     BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                     int count = 0;
                     for(int i = 0 ; i < cwmEvents.getParserLength() ; i++) {
                         count++;
                         bw.write(Integer.toString(mParser[i]));
                         bw.newLine();
                         if(count == 3) {
                             count = 0;
                             bw.newLine();
                         }
                     }

                     bw.write("Start Sleep Time: "+Integer.toString(mParser[startSleepPos+2]/100)+":"+Integer.toString(mParser[startSleepPos+2]%100));
                     bw.newLine();
                     bw.write("Awake Time: "+Integer.toString(mParser[getup+2]/100)+":"+Integer.toString(mParser[getup+2]%100));
                     bw.newLine();
                     bw.write(light.toString());
                     bw.newLine();
                     bw.write(deep.toString());
                     bw.close();
                 } catch (IOException e){
                     e.printStackTrace();
                 }
                mSleepFM.setParserValue(builder1.toString());
                 if(mSleepFM.isVisible())
                     resetFragments(SLEEP_POSITION);
                    break;

                case 0x05:
                    Log.d("bernie", "0x05");
                    if(isPressPrepareEnd == false && isPressPrepareEnd == false && isPressTabataDone == false) {
                        mTabataShowFM.setCycles(currnetCycle, totalCycle);
                        String status = "";
                        String item = "";
                        String count = "";
                        String calories = "";
                        String heartRate = "";
                        String strength = null;

                        Log.d("bernie", "tatabata status = " + Integer.toString(cwmEvents.getTabataStatus()));

                        if (cwmEvents.getTabataStatus() == 0) {
                            status = "stop";
                        } else if (cwmEvents.getTabataStatus() == 1) {
                            status = "start";
                        } else if (cwmEvents.getTabataStatus() == 2) {
                            status = "continue";
                        } else if (cwmEvents.getTabataStatus() == 3) {
                            status = "pause";
                        }

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
                        calories = Integer.toString(cwmEvents.getCalories());
                        heartRate = Integer.toString(cwmEvents.getHeartBeat());

                        if (status.equals("start")) {
                            previous_item = item;
                            previous_count = count;
                        } //else if (status.equals("continue")) {
                           // builder.append("item: " + previous_item + "  count: " + previous_count + "\n");
                           // mTabataShowFM.setHistory(builder.toString());
                        //}
                        // goal is achievement
                        if (cwmEvents.getDoItemCount() == goalTimes) {
                            mTabataShowFM.hideButton(false);
                            mTabataActionItemFM.setActionItemView("");
                            mTabataActionItemFM.setActionItemStartView("");
                            builder.append("item: " + previous_item + "  count: " + previous_count + "\n");
                            mTabataShowFM.setHistory(builder.toString());
                            if (mTabataShowFM.isVisible())
                                resetFragments(TABATA_SHOW_POSITION);
                            else
                                setFragments(TABATA_SHOW_POSITION);

                        }

                        mTabataShowFM.setTabataResultValue(status, item, count, calories, heartRate);

                        if (mTabataShowFM.isVisible())
                            resetFragments(TABATA_SHOW_POSITION);
                        else
                            setFragments(TABATA_SHOW_POSITION);
                    }
                    else if(isPressPrepareEnd == true || isPressActionItemEnd == true || isPressTabataDone == true) {
                        if(isPressTabataDone == true){
                            isPressTabataDone = false;
                            setFragments(TABATA_WORK_POSITION);
                        }
                        else {
                        isPressPrepareEnd = false;
                        isPressActionItemEnd = false;
                        setFragments(TABATA_ACTION_ITEM_POSITION);
                    }
                    }
                    break;
                case 0x21: // flash feedback command
                    mProgressDialog.dismiss();
                    mFlashFM.setReceivedStatus("true");
                    if(mFlashFM.isVisible())
                        resetFragments(FLASH_TEST_POSITION);
                    else
                        setFragments(FLASH_TEST_POSITION);
                    break;
                 default:
                    break;
            }
        }
    };

    public CwmManager.WearableServiceListener wearableServiceListener = new CwmManager.WearableServiceListener() {
        @Override
        public void onConnected() {
           mDeviceStatus = true;
           if(mSelectTypeFM.isVisible())
               resetFragments(SELECT_DEVICE_POSITION);
        }

        @Override
        public void onDisconnected() {
            mDeviceStatus = false;
            mSelectTypeFM.setDevice("", "", mDeviceStatus);
            if(mSelectTypeFM.isVisible())
                resetFragments(SELECT_DEVICE_POSITION);
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
            int id = ackEvents.getId();

            switch (id) {
                case 0x02:
                    Toast.makeText(getApplicationContext(),"Time has sync !",Toast.LENGTH_SHORT).show();
                    break;
                case 0x14:
                    Toast.makeText(getApplicationContext(),"Personal Info has sync !",Toast.LENGTH_SHORT).show();
                    break;
                case 0x12:
                    Toast.makeText(getApplicationContext(),"Intelligent has sync !",Toast.LENGTH_SHORT).show();
                    break;
                case 0x05:
                    break;
            }
        }
       /* @Override
        public void onSyncTimeAckArrival() {
            Toast.makeText(getApplicationContext(),"Time has sync !",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onSyncIntelligentAckArrival(){
            Toast.makeText(getApplicationContext(),"Intelligent has sync !",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onSyncPersonInfoAckArrival(){
            Toast.makeText(getApplicationContext(),"Personal Info has sync !",Toast.LENGTH_SHORT).show();
        }*/
    };

    public CwmManager.ErrorListener errorListener = new CwmManager.ErrorListener(){
        @Override
        public void onErrorArrival(ErrorEvents errorEvents){
            mProgressDialog.dismiss();
            int id = errorEvents.getId();
            int command = errorEvents.getCommand();
            int tag = errorEvents.getTag();
            String tagString = "";

            if(id == 0x01){ //header lost
               if(command == 0xBE)
                   Toast.makeText(getApplicationContext(), "Sleep Header Lost!", Toast.LENGTH_SHORT);
               else if(command == 0x1F)
                   Toast.makeText(getApplicationContext(), "OTA Header Lost!", Toast.LENGTH_SHORT);
               else if(command == 0x20) {
                   mFlashFM.setReceivedStatus("false");
                   if(mFlashFM.isVisible())
                       resetFragments(FLASH_TEST_POSITION);
                   else
                       setFragments(FLASH_TEST_POSITION);
                   Toast.makeText(getApplication(), "Flash Header Lost!", Toast.LENGTH_SHORT);
               }
            }
            else if(id == 0x02){ //packet lost
                if(command == 0xBE)
                    Toast.makeText(getApplicationContext(), "Sleep Packets Lost!", Toast.LENGTH_SHORT);
                else if(command == 0x21) {
                    if(tag == 0x0)
                        tagString = "Sync Start";
                    else if(tag == 0x01)
                        tagString = "Sync Success";
                    else if(tag == 0x02)
                        tagString = "Sync Fail";
                    mFlashFM.setReceivedStatus("false");
                    if(mFlashFM.isVisible())
                        resetFragments(FLASH_TEST_POSITION);
                    else
                        setFragments(FLASH_TEST_POSITION);
                    Toast.makeText(getApplicationContext(), "Flash Packets Lost! "+tagString+"button failed", Toast.LENGTH_SHORT);
                }
            }

        }
    };

    //Fragment Interfaces
    @Override
    public void onNewADevice(){
        if(mDeviceStatus == false){
            Log.d("bernie","CwmBleSearch");
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

    }
    @Override
    public void onRequestBattery(){
        if(mDeviceStatus != false) {
           cwmManager.CwmRequestBattery();
        }
    }

    @Override
    public void onRequestSyncTime(){
        if(mDeviceStatus != false)
            cwmManager.CwmSyncCurrentTime();
    }

    @Override
    public void onIntelligentSyncToRing(){
        IntelligentSettings settings = new IntelligentSettings();

        settings.setSedtentary(mIntelligentFM.getSendtaryRemindStatus());
        Log.d("bernie","sedantary:"+Boolean.toString(mIntelligentFM.getSendtaryRemindStatus()));
        settings.setOnWear(mIntelligentFM.getOnWearStatus());
        Log.d("bernie","onWear:"+Boolean.toString(mIntelligentFM.getOnWearStatus()));
        settings.setHangUp(mIntelligentFM.getHandUpStatus());
        Log.d("bernie","HandUp:"+Boolean.toString(mIntelligentFM.getHandUpStatus()));
        settings.setDoubleTap(mIntelligentFM.getDoubleTapStatus());
        Log.d("bernie","double:"+Boolean.toString(mIntelligentFM.getDoubleTapStatus()));
        settings.setWristSwitch(mIntelligentFM.getWritstScrollStatus());
        Log.d("bernie","Wrist:"+Boolean.toString(mIntelligentFM.getWritstScrollStatus()));
        settings.setShakeSwitch(mIntelligentFM.getShakeStatus());
        Log.d("bernie","Shake:"+Boolean.toString(mIntelligentFM.getShakeStatus()));
        settings.setSignificantSwitch(mIntelligentFM.getSignificantStatus());
        Log.d("bernie","Significant:"+Boolean.toString(mIntelligentFM.getSignificantStatus()));
        settings.setGoal(mIntelligentFM.getAim());
        Log.d("bernie","AIM:"+Integer.toString(mIntelligentFM.getAim()));
        settings.setSedentaryTime(mIntelligentFM.getSedentartTime());
        Log.d("bernie","Sed:"+Integer.toString(mIntelligentFM.getSedentartTime()));

        cwmManager.CwmSaveIntelligentSettings(settings);
        cwmManager.CwmSyncIntelligentSettings();
    }
    @Override
    public void onPersonalInfoSyncToRing(){
      BodySettings settings = new BodySettings();
        settings.setOld(mPersonalInfoFM.getOld());
        Log.d("bernie","old:"+Integer.toString(mPersonalInfoFM.getOld()));
        settings.setSex(mPersonalInfoFM.getSex().charAt(0));
        Log.d("bernie","sex:"+mPersonalInfoFM.getSex());
        settings.setHight(mPersonalInfoFM.getHigh());
        Log.d("bernie","hight:"+Integer.toString(mPersonalInfoFM.getHigh()));
        settings.setWeight(mPersonalInfoFM.getWeight());
        Log.d("bernie","weight:"+Integer.toString(mPersonalInfoFM.getWeight()));

        cwmManager.CwmSaveBodySettings(settings);
        cwmManager.CwmSyncBodySettings();
    }

    @Override
    public void onInitTabata(TabataSettings tabataSettings){
        mTabataSettings = tabataSettings;
        totalCycle = mTabataSettings.getCycle();
        totalPrepare = mTabataSettings.getPrepareTime();
        totalInterval = mTabataSettings.getIntervalTime();
        mTabataQueue = mTabataSettings.getExerciseItems();
        totalItems = mTabataSettings.getTotalItemsNumber();
        mTabataShowFM.setItems(curentDoneItems, totalItems);
        goalTimes = mTabataSettings.getActionTimes();
        cwmManager.CwmTabataCommand(ITEMS.TABATA_INIT.ordinal(), 0 , 0, 0);
        setFragments(TABATA_PREPARE_POSITION);

    }

    @Override
    public void onRequestSleepLog(){
        cwmManager.CwmRequestSleepLog();

    }

    @Override
    public void onRequestSwVersion(){
        cwmManager.CwmRequestSwVersion();
        Log.d("bernie","sw version");
    }

    @Override
    public void onRequestSleep(){
        if(mDeviceStatus == true)
           mProgressDialog = ProgressDialog.show(this,"要求睡眠資料","處理中...");
        cwmManager.CwmRequestSleepLog();
    }


    @Override
    public void onPressPrepareStartButton(){
        cwmManager.CwmTabataCommand(ITEMS.TABATA_PREPARE_START.ordinal(), 0 , 0, 0);
        mTabataPrepareFM.setPrepareStartView("start!");
        if(mTabataPrepareFM.isVisible())
            resetFragments(TABATA_PREPARE_POSITION);
        else
            setFragments(TABATA_PREPARE_POSITION);
    }
    @Override
    public void onPressPrepareCountButton(){
        if(totalPrepare >= 0) {
            cwmManager.CwmTabataCommand(ITEMS.TABATA_PREPARE_COUNT.ordinal(), totalPrepare, 0, 0);
            mTabataPrepareFM.setPrepareCountView(Integer.toString(totalPrepare));
        }
        if(mTabataPrepareFM.isVisible())
            resetFragments(TABATA_PREPARE_POSITION);
        else
            setFragments(TABATA_PREPARE_POSITION);
        if(totalPrepare >= 0)
          totalPrepare--;
    }
    @Override
    public void onPressPrepareEndButton(){
        cwmManager.CwmTabataCommand(ITEMS.TABATA_PREARE_END.ordinal(), 0 , 0, 0);
        isPressPrepareEnd = true;
        setFragments(TABATA_ACTION_ITEM_POSITION);

    }
    @Override
    public void onPressActionItemButton(){
        int itemPos;
        String itemName;
        TabataObject obj;

        if(mTabataQueue.size() != 0 ) {
            obj = mTabataQueue.poll();
            itemPos = obj.getItemPos();
            itemName = obj.getItemName();
            cwmManager.CwmTabataCommand(ITEMS.TABATA_ACTION_ITEM.ordinal(), 0, 0, itemPos);
            mTabataActionItemFM.setActionItemView(itemName);
            if (mTabataActionItemFM.isVisible()) {
                resetFragments(TABATA_ACTION_ITEM_POSITION);
            } else
                setFragments(TABATA_ACTION_ITEM_POSITION);
        }
        else{
            setFragments(TABATA_WORK_POSITION);
        }
    }
    @Override
    public void onPressActionItemStartButton(){
        cwmManager.CwmTabataCommand(ITEMS.TABATA_ACTION_START.ordinal(), 0 , 0, 0);
        mTabataActionItemFM.setActionItemStartView("start!");
        if(mTabataActionItemFM.isVisible())
            resetFragments(TABATA_ACTION_ITEM_POSITION);
        else
            setFragments(TABATA_ACTION_ITEM_POSITION);
    }
    @Override
    public void onPressActionItemEndButton(){
        isPressPrepareEnd = true;
        mTabataShowFM.hideButton(true);
        curentDoneItems++;
        mTabataShowFM.setItems(curentDoneItems, totalItems);
        cwmManager.CwmTabataCommand(ITEMS.TABATA_ACTION_END.ordinal(), 0 , 0, 0);
        if(mTabataQueue.size() != 0){
            totalInterval = mTabataSettings.getIntervalTime();
            setFragments(TABATA_INTERVAL_POSITION);
        }
        else{
            if(currnetCycle < totalCycle){
                currnetCycle++;
            }
            else{
                cwmManager.CwmTabataCommand(ITEMS.TABATA_DONE.ordinal(), 0 , 0, 0);
                setFragments(TABATA_WORK_POSITION);
            }
        }
    }
    @Override
    public void onPressItervalStartButton(){
        cwmManager.CwmTabataCommand(ITEMS.TABATA_REST_START.ordinal(), 0 , 0, 0);
        mTabataIntervalFM.setIntervalStartView("start!");
        if(mTabataIntervalFM.isVisible())
            resetFragments(TABATA_INTERVAL_POSITION);
        else
            setFragments(TABATA_INTERVAL_POSITION);
    }
    @Override
    public void onPressIntervalCountButton(){
        if(totalInterval >= 0) {
            cwmManager.CwmTabataCommand(ITEMS.TABATA_REST_COUNT.ordinal(), 0, totalInterval, 0);
            mTabataIntervalFM.setIntervalCountView(Integer.toString(totalInterval));
        }
        if(mTabataIntervalFM.isVisible())
            resetFragments(TABATA_INTERVAL_POSITION);
        else
            setFragments(TABATA_INTERVAL_POSITION);
        if(totalInterval >= 0)
            totalInterval--;
    }
    @Override
    public void onPressIntervalEndButton(){
        cwmManager.CwmTabataCommand(ITEMS.TABATA_REST_END.ordinal(), 0 , 0, 0);
        setFragments(TABATA_SHOW_POSITION);

    }
    @Override
    public void onPressTabataPauseButton(){
        cwmManager.CwmTabataCommand(ITEMS.TABATA_PAUSE.ordinal(), 0 , 0, 0);
    }

    @Override
    public void onPressTabataDoneButton(){
        cwmManager.CwmTabataCommand(ITEMS.TABATA_DONE.ordinal(), 0 , 0, 0);
        isPressTabataDone = true;
        mTabataQueue = null;
        mTabataQueue = mTabataSettings.getExerciseItems();
        mTabataPrepareFM.setPrepareCountView("");
        mTabataPrepareFM.setPrepareStartView("");
        mTabataShowFM.setCycles(0,0);
        mTabataShowFM.setItems(0,0);
        if(mTabataFM.isVisible())
            resetFragments(TABATA_WORK_POSITION);
        else
            setFragments(TABATA_WORK_POSITION);
    }

    @Override
    public void onPressSyncStartButton(){
        mFlashFM.setReceivedStatus("");
        if(mFlashFM.isVisible())
            resetFragments(FLASH_TEST_POSITION);
        else
            resetFragments(FLASH_TEST_POSITION);
        mProgressDialog = ProgressDialog.show(this,"正在 Start Sync","處理中...");
        cwmManager.CwmFlashSyncStart();
    }
    @Override
    public void onPressSyncSuccessButton(){
        mProgressDialog = ProgressDialog.show(this,"按下 Start Success","處理中...");
        cwmManager.CwmFlashSyncSuccess();
    }
    @Override
    public void onPressSyncFailButton(){
        mProgressDialog = ProgressDialog.show(this,"按下 Start Fail","處理中...");
        cwmManager.CwmFlashSyncFail();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("bernie","onCreate");
        // set toolbar to be action bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

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
        mFragments.add(mShowDataFM);
        mFragments.add(mPersonalInfoFM);
        mFragments.add(mSelectTypeFM);
        mFragments.add(mSleepFM);
        mFragments.add(mIntelligentFM);
        mFragments.add(mRingBatteryFM);
        mFragments.add(mTimeSyncFM);
        mFragments.add(mTabataFM);
        mFragments.add(mTabataPrepareFM);
        mFragments.add(mTabataActionItemFM);
        mFragments.add(mTabataShowFM);
        mFragments.add(mTabataIntervalFM);
        mFragments.add(mSwVersionFM);
        mFragments.add(mFlashFM);
        //testS1ettings.
        //testSettings.
        cwmManager = new CwmManager(this,wearableServiceListener, eventListener, ackListener, errorListener);
        statusCheck();
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("bernie","onPause");
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

    //function
    //- function
    private void navigateTo(MenuItem item) {
        mNavIdx = item.getItemId();
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.navigation_item_0:
                mToolbar.setTitle("計步/心跳/距離/卡路里");
                setFragments(SHOWDATA_POSITION);
                break;
            case R.id.navigation_item_1:
                mToolbar.setTitle("個人資料");
                setFragments(PERSONAL_POSITION);
                break;
            case R.id.navigation_item_2:
                mToolbar.setTitle("移除/新增手環");
                setFragments(SELECT_DEVICE_POSITION);
                break;
            case R.id.navigation_item_3:
                mToolbar.setTitle("睡眠資料");
                setFragments(SLEEP_POSITION);
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
                mToolbar.setTitle("時間同步");
                setFragments(TIME_SYNC_POSITION);
                break;
            case R.id.navigation_item_7:
                mToolbar.setTitle("TABATA");
                setFragments(TABATA_WORK_POSITION);
                break;
            case R.id.navigation_item_8:
                mToolbar.setTitle("裝置版本");
                setFragments(SW_VERSION_POSITION);
                break;
            case R.id.navigation_item_9:
                 buildAlertMessageSwichOTA();
                break;
            case R.id.navigation_item_10:
                setFragments(FLASH_TEST_POSITION);
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
    private void setPreferencedFragments(){
        mFM.beginTransaction().replace(R.id.frames, mIntelligentFM).commit();
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
}
