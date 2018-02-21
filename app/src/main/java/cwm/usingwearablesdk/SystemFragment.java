package cwm.usingwearablesdk;

/**
 * Created by user on 2017/9/10.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import cwm.wearablesdk.settings.SystemSetting;

public class SystemFragment extends Fragment {

    private View mView;

    private RadioGroup osGroup;
    private RadioGroup formatGroup;

    //private EditText nameEdit;
    private EditText screenEdit;
    private EditText historyEdit;
    private EditText noDisturbStartEdit;
    private EditText noDisturbStopEdit;
    private EditText sleepStartEdit;
    private EditText sleepStopEdit;
    private SeekBar brightnessBar;
    private TextView brightnessView;
    private Button saveButton;
    private Button requestButton;
    private Button syncButton;

    private CheckBox pedometerCheckBox;
    private CheckBox distanceCheckBox;
    private CheckBox heartCheckBox;
    private CheckBox batteryCheckBox;
    private CheckBox callNotifyCheckBox;
    private CheckBox appNotifyCheckBox;
    private CheckBox mailNotifyCheckBox;
    private CheckBox msgNotifyCheckBox;
    private CheckBox heartSurvillanceCheckBox;
    private CheckBox vibrateSwitchCheckBox;
    private CheckBox noNotifyCheckBox;
    private CheckBox afternoonNoNotifyCheckBox;
    private CheckBox sleepDetectCheckBox;

    private int osType = 0;
    private int timeFormat = 0;
    private int historyDetect = 0;
    private int screenTimeOut = 0;

    private int screen = 0;
    private int functions = 0;

    private int noDisturbStartTime = 12;
    private int noDisturbStopTime = 14;

    private int sleepStartTime = 0;
    private int sleepStopTime = 0;

    private int brightnessProgress = 0;


    // Container Activity must implement this interface
    public interface ListenForSystemFragment {
        void onPressSaveSystemInfomation(int os, int format, int historyDetect, int screenTimeout,
                                         int screen, int functions, int noDisturbStart, int noDisturbStop,
                                         int sleepStartTime, int sleeepStopTime, int brightnessBar);
        void onSystemRequest();
        void onSycRequest();
    }

    private ListenForSystemFragment mCallback;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForSystemFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForCommandTestFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_system, null);
        }

        osGroup = (RadioGroup)mView.findViewById(R.id.os_group);
        formatGroup = (RadioGroup)mView.findViewById(R.id.time_format_group);
        saveButton = (Button)mView.findViewById(R.id.save);
        requestButton = (Button)mView.findViewById(R.id.request);
        syncButton = (Button)mView.findViewById(R.id.sync);

        sleepStartEdit = (EditText)mView.findViewById(R.id.sleep_start_time);
        sleepStopEdit = (EditText)mView.findViewById(R.id.sleep_stop_time);
        historyEdit = (EditText)mView.findViewById(R.id.history_detect_edit);
        screenEdit = (EditText)mView.findViewById(R.id.screen_timeout_edit);
        noDisturbStartEdit = (EditText)mView.findViewById(R.id.no_disturb_start_time);
        noDisturbStopEdit = (EditText)mView.findViewById(R.id.no_disturb_stop_time);
        brightnessBar = (SeekBar)mView.findViewById(R.id.brightness_bar);
        brightnessView = (TextView)mView.findViewById(R.id.brightness_view);
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               brightnessView.setText("亮度:"+Integer.toString(seekBar.getProgress()));
                brightnessProgress = seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        osGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.os_type_android:
                        osType = 0;
                        break;
                    case R.id.os_type_ios:
                        osType = 1;
                        break;
                }
            }
        });

        formatGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.time_fomat_0_23:
                        timeFormat = 0;
                        break;
                    case R.id.time_fomat_1_12:
                        timeFormat = 1;
                        break;
                }
            }
        });

        pedometerCheckBox = (CheckBox)mView.findViewById(R.id.pedometer);
        pedometerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    screen = screen | 1;
                else
                    screen = screen & ~1;
            }
        });
        distanceCheckBox = (CheckBox)mView.findViewById(R.id.distance);
        distanceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    screen = screen | 2;
                else
                    screen = screen & ~2;
            }
        });
        heartCheckBox = (CheckBox)mView.findViewById(R.id.heart);
        heartCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    screen = screen | 4;
                else
                    screen = screen & ~4;
            }
        });
        batteryCheckBox = (CheckBox)mView.findViewById(R.id.battery);
        batteryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    screen = screen | 8;
                else
                    screen = screen & ~8;
            }
        });
        callNotifyCheckBox = (CheckBox)mView.findViewById(R.id.call_notify);
        callNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    functions = functions | 1;
                else
                    functions = functions & ~1;
            }
        });
        appNotifyCheckBox = (CheckBox) mView.findViewById(R.id.app_notify);
        appNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    functions = functions | 2;
                else
                    functions = functions & ~2;
            }
        });
       mailNotifyCheckBox = (CheckBox) mView.findViewById(R.id.mail_notify);
        mailNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    functions = functions | 4;
                else
                    functions = functions & ~4;
            }
        });
        msgNotifyCheckBox = (CheckBox)mView.findViewById(R.id.msg_notify);
        msgNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    functions = functions | 8;
                else
                    functions = functions & ~8;
            }
        });
        heartSurvillanceCheckBox = (CheckBox)mView.findViewById(R.id.heart_survelliance);
        heartSurvillanceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    functions = functions | 16;
                else
                    functions = functions & ~16;
            }
        });
        vibrateSwitchCheckBox = (CheckBox)mView.findViewById(R.id.vibrate_switch);
        vibrateSwitchCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    functions = functions | 32;
                else
                    functions = functions & ~32;
            }
        });

        noNotifyCheckBox = (CheckBox)mView.findViewById(R.id.no_notify);
        noNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    functions = functions | 64;
                else
                    functions = functions & ~64;
            }
        });

        afternoonNoNotifyCheckBox = (CheckBox)mView.findViewById(R.id.afternoon_no_notify);
        afternoonNoNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    functions = functions | 256;
                    noDisturbStartEdit.setVisibility(View.VISIBLE);
                    noDisturbStopEdit.setVisibility(View.VISIBLE);
                }
                else {
                    functions = functions & ~256;
                    noDisturbStartEdit.setVisibility(View.INVISIBLE);
                    noDisturbStopEdit.setVisibility(View.INVISIBLE);
                }
            }
        });

        sleepDetectCheckBox = (CheckBox)mView.findViewById(R.id.sleep_detect);
        sleepDetectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    functions = functions | 128;
                    sleepStartEdit.setVisibility(View.VISIBLE);
                    sleepStopEdit.setVisibility(View.VISIBLE);
                }
                else {
                    functions = functions & ~128;
                    sleepStartEdit.setVisibility(View.INVISIBLE);
                    sleepStopEdit.setVisibility(View.INVISIBLE);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!historyEdit.getText().toString().matches("")) &&
                        (!screenEdit.getText().toString().matches("")) &&
                        screen != 0 && functions != 0){
                    historyDetect = Integer.parseInt(historyEdit.getText().toString());
                    screenTimeOut = Integer.parseInt(screenEdit.getText().toString());

                    if(noDisturbStopEdit.getVisibility() == View.VISIBLE){
                        noDisturbStartTime = Integer.valueOf(noDisturbStartEdit.getText().toString());
                        Log.d("bernie","no Disturb Start time: "+Integer.toString(noDisturbStartTime));
                        noDisturbStopTime = Integer.valueOf(noDisturbStopEdit.getText().toString());
                    }
                    else{
                        noDisturbStartTime = 12;
                        noDisturbStopTime = 14;
                    }

                    if(sleepStartEdit.getVisibility() == View.VISIBLE){
                        sleepStartTime = Integer.valueOf(sleepStartEdit.getText().toString());
                        sleepStopTime = Integer.valueOf(sleepStopEdit.getText().toString());
                    }
                    else{
                        sleepStartTime = 21;
                        sleepStopTime = 6;
                    }

                    mCallback.onPressSaveSystemInfomation(osType, timeFormat, historyDetect, screenTimeOut,screen, functions,
                            noDisturbStartTime,noDisturbStopTime,sleepStartTime,sleepStopTime,brightnessProgress);
                    Toast.makeText(getContext(),"資料已儲存",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(),"請填妥資料!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onSystemRequest();
            }
        });

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mCallback.onSycRequest();
            }
        });

        return mView;
    }

    @Override
    public  void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("HISTORY", Integer.parseInt(historyEdit.getText().toString()));
        editor.putInt("SCREEN", Integer.parseInt(screenEdit.getText().toString()));
        editor.apply();
    }

    @Override
    public void onResume(){
        super.onResume();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        historyEdit.setText(Integer.toString(preferences.getInt("HISTORY", 10)));
        screenEdit.setText(Integer.toString(preferences.getInt("SCREEN", 5)));
    }

    public void upadateSetting(SystemSetting system){

        historyEdit.setText(Integer.toString(system.getHistoryDetectPeriod()));
        screenEdit.setText(Integer.toString(system.getScreenTimeOut()));
        Log.d("bernie","functions:"+Integer.toString(system.getFunctions()));

        if((system.getScreens() & 1) != 0){
            pedometerCheckBox.setChecked(true);
        }
        else
            pedometerCheckBox.setChecked(false);

        if((system.getScreens() & 2) != 0){
            distanceCheckBox.setChecked(true);
        }
        else
            distanceCheckBox.setChecked(false);

        if((system.getScreens() & 4) != 0){
            heartCheckBox.setChecked(true);
        }
        else
            heartCheckBox.setChecked(false);

        if((system.getScreens() & 8) != 0){
            batteryCheckBox.setChecked(true);
        }
        else
            batteryCheckBox.setChecked(false);


        if((system.getFunctions() & 1) != 0){
            callNotifyCheckBox.setChecked(true);
        }
        else
            callNotifyCheckBox.setChecked(false);

        if((system.getFunctions() & 2) != 0){
            appNotifyCheckBox.setChecked(true);
        }
        else
            appNotifyCheckBox.setChecked(false);

        if((system.getFunctions() & 4) != 0){
            mailNotifyCheckBox.setChecked(true);
        }
        else
            mailNotifyCheckBox.setChecked(false);

        if((system.getFunctions() & 8) != 0){
            msgNotifyCheckBox.setChecked(true);
        }
        else
            msgNotifyCheckBox.setChecked(false);

        if((system.getFunctions() & 16) != 0){
            heartSurvillanceCheckBox.setChecked(true);
        }
        else
            heartSurvillanceCheckBox.setChecked(false);

        if((system.getFunctions() & 32) != 0){
            vibrateSwitchCheckBox.setChecked(true);
        }
        else
            vibrateSwitchCheckBox.setChecked(false);

        if((system.getFunctions() & 64) != 0){
            noNotifyCheckBox.setChecked(true);
        }
        else
            noNotifyCheckBox.setChecked(false);

        if((system.getFunctions() & 128) != 0){
            sleepDetectCheckBox.setChecked(true);
        }
        else
            sleepDetectCheckBox.setChecked(false);

        if((system.getFunctions() & 256) != 0){
            afternoonNoNotifyCheckBox.setChecked(true);
        }
        else
            afternoonNoNotifyCheckBox.setChecked(false);

        brightnessBar.setProgress(system.getBrightness());

    }

}
