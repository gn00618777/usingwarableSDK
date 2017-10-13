package cwm.usingwearablesdk;

import android.bluetooth.BluetoothDevice;
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
import cwm.wearablesdk.CwmInformation;
import cwm.wearablesdk.BodySettings;
import cwm.wearablesdk.IntelligentSettings;
import cwm.wearablesdk.TabataSettings;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class MainActivity extends AppCompatActivity implements SelectTypeFragment.ListenForSelectTypeFragment,
RingBatteryFragment.ListenForRingStatusFragment, TimeSyncFragment.ListenForSyncTimeFragment,
        IntelligentFragment.ListenerForIntellignetFragment,PersonalInfoFragment.ListenForPersonalInfoFragment,
         TabataFragment.ListenForTabataFragment, RequestSleepFragment.ListenForRequestSleepFragment,
SwVersionFragment.ListenForSwVersionFragment, SleepFragment.ListenForSleepFragment{

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
    private TabataShowFragment mTabataShowFM = new TabataShowFragment();
    private SwVersionFragment mSwVersionFM = new SwVersionFragment();

    private String mDeviceName = null;
    private String mDeviceAddress = null;
    private boolean mDeviceStatus = false;
    private byte[] mValue;
    private int[] mParser;

    private BluetoothAdapter mBtAdapter = null;

    final int SHOWDATA_POSITION = 0;
    final int PERSONAL_POSITION = 1;
    final int SELECT_DEVICE_POSITION = 2;
    final int SLEEP_POSITION = 3;
    final int RING_BATTERY_POSITION = 5;
    final int TIME_SYNC_POSITION = 6;
    final int TABATA_WORK_POSITION = 7;
    final int TABATA_SHOW_POSITION = 8;
    final int SW_VERSION_POSITION = 9;


    // information
    private IntelligentSettings testSettings;
    private BodySettings testS1ettings;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 3;

    public CwmManager.InformationListener informationListener = new CwmManager.InformationListener() {
        @Override
        public void onGetCwmRunData(CwmInformation cwmInformation) {

        }

        @Override
        public void onGetCwmWalkData(CwmInformation cwmInformation) {
            String gesture = "";
            if(cwmInformation.getStatus() == 1){
                gesture = "static";
            }
            else if(cwmInformation.getStatus() == 2){
                gesture = "walk";
            }
            else if(cwmInformation.getStatus() == 3){
                gesture = "run";
            }
            else if(cwmInformation.getStatus() == 4){
                gesture = "bike";
            }
            mShowDataFM.setValue(cwmInformation.getWalkStep(),cwmInformation.getDistance(),
                    cwmInformation.getCalories(),gesture);
            if(mShowDataFM.isVisible()){
                resetFragments(SHOWDATA_POSITION);
            }

        }

        @Override
        public void onGetBikeData(CwmInformation cwmInformation) {

        }

        @Override
        public void onGetHeartData(CwmInformation cwmInformation) {
            mShowDataFM.setHeartValue(cwmInformation.getHeartBeat());
            if(mShowDataFM.isVisible()){
                resetFragments(SHOWDATA_POSITION);
            }
        }

        @Override
        public void onGetActivity(CwmInformation cwmInformation) {

            String Type = null;
            if(cwmInformation.getId() == 0x01)
                Type = "TAP";
            else if(cwmInformation.getId() == 0x02)
                Type = "WRIST";

            Toast.makeText(getApplicationContext(),Type,Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onGetBattery(CwmInformation cwmInformation){
            mRingBatteryFM.setValue(cwmInformation.getBattery());
            if(mRingBatteryFM.isVisible()){
                resetFragments(RING_BATTERY_POSITION);
            }
        }

        @Override
        public void onGetTabataResponse(CwmInformation cwmInformation){
            String status = "";
            String item = "";
            String count = "";
            String calories = "";
            String heartRate = "";
            String strength = null;

            if(cwmInformation.getTabataStatus() == 0){
                status = "stop";
            }
            else
                status = "start";

            if(cwmInformation.getExerciseItem() == 1){
                item = "Push Up";
            }
            else if(cwmInformation.getExerciseItem() == 2){
                item = "Crunch";
            }
            else if(cwmInformation.getExerciseItem() == 3){
                item = "Squart";
            }
            else if(cwmInformation.getExerciseItem() == 4){
                item = "Jumping Jack";
            }
            else if(cwmInformation.getExerciseItem() == 5){
                item = "Dips";
            }
            else if(cwmInformation.getExerciseItem() == 6){
                item = "High Knees Running";
            }
            else if(cwmInformation.getExerciseItem() == 7){
                item = "Lunges";
            }
            else if(cwmInformation.getExerciseItem() == 8){
                item = "Burpee";
            }
            else if(cwmInformation.getExerciseItem() == 9){
                item = "Step On Chair";
            }
            else if(cwmInformation.getExerciseItem() == 10){
                item = "Push Up Rotation";
            }

            count = Integer.toString(cwmInformation.getDoItemCount());
            calories = Integer.toString(cwmInformation.getCalories());
            heartRate = Integer.toString(cwmInformation.getHeartBeat());
            //strength = Integer.toString(cwmInformation.getStrength());

            mTabataShowFM.setTabataResultValue(status, item, count, calories, heartRate);

            if(mTabataShowFM.isVisible())
                resetFragments(TABATA_SHOW_POSITION);
            else
                setFragments(TABATA_SHOW_POSITION);
        }

        @Override
        public void onGetSwVersionResponse(CwmInformation cwmInformation){
            mSwVersionFM.setVersion(cwmInformation.getVersion());
            if(mSwVersionFM.isVisible()){
                resetFragments(SW_VERSION_POSITION);
            }
        }

        @Override
        public void onGetSleepLog(CwmInformation cwmInformation){
            int startSleepPos = 0;
            int getup = 0;
            mParser = cwmInformation.getSleepParser();
            StringBuilder builder1 = new StringBuilder();
            builder1.append("\nParser array:\n");
            for(int i = 0; i < cwmInformation.getParserLength() ; i++) {
                builder1.append(Integer.toString(mParser[i])+"\n");
            }
            for(int i = 0; i < cwmInformation.getParserLength() ; i+=2) {
                if(mParser[i] % 100 == 4) {
                    getup = i;
                    break;
                }
            }
            for(int i = 0; i < cwmInformation.getParserLength() ; i+=2) {
                if(mParser[i] == 100)
                    startSleepPos = i;
            }
            try {
                FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().toString() + "/Download/CwmSleepLog.txt", false);
                BufferedWriter bw = new BufferedWriter(fw); //將BufferedWeiter與FileWrite物件做連結
                for(int i = 0 ; i < cwmInformation.getParserLength() ; i++) {
                    bw.write(Integer.toString(mParser[i]));
                    bw.newLine();
                }
                bw.write("Start Sleep Time: "+Integer.toString(mParser[startSleepPos+2]/100)+":"+Integer.toString(mParser[startSleepPos+2]%100));
                bw.newLine();
                bw.write("Awake Time: "+Integer.toString(mParser[getup+2]/100)+":"+Integer.toString(mParser[getup+2]%100));
                bw.close();
            } catch (IOException e){
                e.printStackTrace();
            }

           // mSleepFM.setRawValue(builder.toString());
            mSleepFM.setParserValue(builder1.toString());
            if(mSleepFM.isVisible())
                resetFragments(SLEEP_POSITION);
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
    };

    private CwmManager.AckListener ackListener = new CwmManager.AckListener() {
        @Override
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
        settings.setGoal(mIntelligentFM.getAim());
        Log.d("bernie","AIM:"+Integer.toString(mIntelligentFM.getAim()));

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
    public void onSendTabataParameters(TabataSettings tabataSettings){
        cwmManager.CwmSendTabataParameters(tabataSettings);

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
        cwmManager.CwmRequestSleepLog();
        Log.d("bernie","press request sleep button");
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
        mFragments.add(mTabataShowFM);
        mFragments.add(mSwVersionFM);
        //testS1ettings.
        //testSettings.
        cwmManager = new CwmManager(this,wearableServiceListener, informationListener,ackListener);
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
