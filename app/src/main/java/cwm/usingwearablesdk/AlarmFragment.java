package cwm.usingwearablesdk;

/**
 * Created by user on 2017/9/10.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cwm.wearablesdk.settings.AlarmSetting;

public class AlarmFragment extends Fragment {

    private View mView;
    private Button saveButton1;
    private Button requestButton1;
    private Button syncButton1;
    private Button saveButton2;
    private Button requestButton2;
    private Button syncButton2;
    private Button saveButton3;
    private Button requestButton3;
    private Button syncButton3;
    private EditText hourEdit1;
    private EditText hourEdit2;
    private EditText hourEdit3;
    private EditText minuteEdit1;
    private EditText minuteEdit2;
    private EditText minuteEdit3;
    private EditText vibrateEdit1;
    private EditText vibrateEdit2;
    private EditText vibrateEdit3;
    private CheckBox week11G;
    private CheckBox week21G;
    private CheckBox week31G;
    private CheckBox week41G;
    private CheckBox week51G;
    private CheckBox week61G;
    private CheckBox week71G;

    private CheckBox week12G;
    private CheckBox week22G;
    private CheckBox week32G;
    private CheckBox week42G;
    private CheckBox week52G;
    private CheckBox week62G;
    private CheckBox week72G;

    private CheckBox week13G;
    private CheckBox week23G;
    private CheckBox week33G;
    private CheckBox week43G;
    private CheckBox week53G;
    private CheckBox week63G;
    private CheckBox week73G;

    private int week1G = 0;
    private int week2G = 0;
    private int week3G = 0;

    private AlarmSetting alarmSettings = new AlarmSetting();

    ListenForAlarmFragment mCallback;

    // Container Activity must implement this interface
    public interface ListenForAlarmFragment {
         void onSaveAlarmSettings(AlarmSetting alarm);
        void onAlarmRequest();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForAlarmFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForAlarmFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_alarm, null);
        }
        saveButton1 = (Button) mView.findViewById(R.id.save1_button);
        saveButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hourEdit1.getText().toString().matches("") &&
                        !minuteEdit1.getText().toString().matches("") &&
                        !vibrateEdit1.getText().toString().matches("")){

                    if((0 <= Integer.valueOf(hourEdit1.getText().toString()) &&
                            Integer.valueOf(hourEdit1.getText().toString()) <= 23) &&
                            ( 0 <= Integer.valueOf(minuteEdit1.getText().toString()) &&
                                    Integer.valueOf(minuteEdit1.getText().toString()) <= 59) &&
                            (Integer.valueOf(vibrateEdit1.getText().toString()) >= 0 &&
                                    Integer.valueOf(vibrateEdit1.getText().toString()) <= 255)){

                        alarmSettings.setTime(Integer.valueOf(hourEdit1.getText().toString()),
                                Integer.valueOf(minuteEdit1.getText().toString()), 1);
                        alarmSettings.setVibrate(Integer.valueOf(vibrateEdit1.getText().toString()), 1);
                        alarmSettings.setWeek(week1G, 1);

                        mCallback.onSaveAlarmSettings(alarmSettings);
                        Toast.makeText(getContext(),"已儲存",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(),"請確認資料是否合乎規定",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(),"請確認資料是否填妥",Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestButton1 = (Button) mView.findViewById(R.id.request1_button);
        requestButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onAlarmRequest();
            }
        });

        saveButton2 = (Button) mView.findViewById(R.id.save2_button);
        saveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hourEdit2.getText().toString().matches("") &&
                        !minuteEdit2.getText().toString().matches("") &&
                        !vibrateEdit2.getText().toString().matches("")){

                    if((0 <= Integer.valueOf(hourEdit2.getText().toString()) &&
                            Integer.valueOf(hourEdit2.getText().toString()) <= 23) &&
                            ( 0 <= Integer.valueOf(minuteEdit2.getText().toString()) &&
                                    Integer.valueOf(minuteEdit2.getText().toString()) <= 59) &&
                            (Integer.valueOf(vibrateEdit2.getText().toString()) >= 0 &&
                                    Integer.valueOf(vibrateEdit2.getText().toString()) <= 255)){

                        alarmSettings.setTime(Integer.valueOf(hourEdit2.getText().toString()),
                                Integer.valueOf(minuteEdit2.getText().toString()), 2);
                        alarmSettings.setVibrate(Integer.valueOf(vibrateEdit2.getText().toString()), 2);
                        alarmSettings.setWeek(week2G, 2);

                        mCallback.onSaveAlarmSettings(alarmSettings);
                        Toast.makeText(getContext(),"已儲存",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(),"請確認資料是否合乎規定",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(),"請確認資料是否填妥",Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestButton2 = (Button) mView.findViewById(R.id.request2_button);
        requestButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onAlarmRequest();
            }
        });
        saveButton3 = (Button) mView.findViewById(R.id.save3_button);
        saveButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hourEdit3.getText().toString().matches("") &&
                        !minuteEdit3.getText().toString().matches("") &&
                        !vibrateEdit3.getText().toString().matches("")){

                    if((0 <= Integer.valueOf(hourEdit3.getText().toString()) &&
                            Integer.valueOf(hourEdit3.getText().toString()) <= 23) &&
                            ( 0 <= Integer.valueOf(minuteEdit3.getText().toString()) &&
                                    Integer.valueOf(minuteEdit3.getText().toString()) <= 59) &&
                            (Integer.valueOf(vibrateEdit3.getText().toString()) >= 0 &&
                                    Integer.valueOf(vibrateEdit3.getText().toString()) <= 255)){

                        alarmSettings.setTime(Integer.valueOf(hourEdit3.getText().toString()),
                                Integer.valueOf(minuteEdit3.getText().toString()), 3);
                        alarmSettings.setVibrate(Integer.valueOf(vibrateEdit3.getText().toString()), 3);
                        alarmSettings.setWeek(week3G, 3);
                        mCallback.onSaveAlarmSettings(alarmSettings);
                        Toast.makeText(getContext(),"已儲存",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(),"請確認資料是否合乎規定",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(),"請確認資料是否填妥",Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestButton3 = (Button) mView.findViewById(R.id.request3_button);
        requestButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onAlarmRequest();
            }
        });
        hourEdit1 = (EditText) mView.findViewById(R.id.hour_edit_1);
        hourEdit2 = (EditText) mView.findViewById(R.id.hour_edit_2);
        hourEdit3 = (EditText) mView.findViewById(R.id.hour_edit_3);
        minuteEdit1 = (EditText) mView.findViewById(R.id.minute_edit_1);
        minuteEdit2 = (EditText) mView.findViewById(R.id.minute_edit_2);
        minuteEdit3 = (EditText) mView.findViewById(R.id.minute_edit_3);
        vibrateEdit1 = (EditText) mView.findViewById(R.id.vibrate_edit_1);
        vibrateEdit2 = (EditText) mView.findViewById(R.id.vibrate_edit_2);
        vibrateEdit3 = (EditText) mView.findViewById(R.id.vibrate_edit_3);

        week11G = (CheckBox) mView.findViewById(R.id.week1_1g);
        week11G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   week1G |= 2;
               }
               else
                   week1G &= ~2;
            }
        });

        week21G = (CheckBox) mView.findViewById(R.id.week2_1g);
        week21G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week1G |= 4;
                }
                else
                    week1G &= ~4;
            }
        });
        week31G = (CheckBox) mView.findViewById(R.id.week3_1g);
        week31G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week1G |= 8;
                }
                else
                    week1G &= ~8;
            }
        });
        week41G = (CheckBox) mView.findViewById(R.id.week4_1g);
        week41G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week1G |= 16;
                }
                else
                    week1G &= ~16;
            }
        });
        week51G = (CheckBox) mView.findViewById(R.id.week5_1g);
        week51G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week1G |= 32;
                }
                else
                    week1G &= ~32;
            }
        });
        week61G = (CheckBox) mView.findViewById(R.id.week6_1g);
        week61G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week1G |= 64;
                }
                else
                    week1G &= ~64;
            }
        });
        week71G = (CheckBox) mView.findViewById(R.id.week7_1g);
        week71G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week1G |= 1;
                }
                else
                    week1G &= ~1;
            }
        });

        week12G = (CheckBox)mView.findViewById(R.id.week1_2g);
        week12G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        week2G |= 2;
                    }
                    else
                        week2G &= ~2;

            }
        });
        week22G = (CheckBox)mView.findViewById(R.id.week2_2g);
        week22G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week2G |= 4;
                }
                else
                    week2G &= ~4;
            }
        });
        week32G = (CheckBox)mView.findViewById(R.id.week3_2g);
        week32G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week2G |= 8;
                }
                else
                    week2G &= ~8;
            }
        });
        week42G = (CheckBox)mView.findViewById(R.id.week4_2g);
        week42G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week2G |= 16;
                }
                else
                    week2G &= ~16;
            }
        });
        week52G = (CheckBox)mView.findViewById(R.id.week5_2g);
        week52G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week2G |= 32;
                }
                else
                    week2G &= ~32;
            }
        });
        week62G = (CheckBox)mView.findViewById(R.id.week6_2g);
        week62G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week2G |= 64;
                }
                else
                    week2G &= ~64;
            }
        });
        week72G = (CheckBox)mView.findViewById(R.id.week7_2g);
        week72G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week2G |= 1;
                }
                else
                    week2G &= ~1;
            }
        });

        week13G = (CheckBox)mView.findViewById(R.id.week1_3g);
        week13G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week3G |= 2;
                }
                else
                    week3G &= ~2;
            }
        });
        week23G = (CheckBox)mView.findViewById(R.id.week2_3g);
        week23G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week3G |= 4;
                }
                else
                    week3G &= ~4;
            }
        });
        week33G = (CheckBox)mView.findViewById(R.id.week3_3g);
        week33G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week3G |= 8;
                }
                else
                    week3G &= ~8;
            }
        });
        week43G = (CheckBox)mView.findViewById(R.id.week4_3g);
        week43G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week3G |= 16;
                }
                else
                    week3G &= ~16;
            }
        });
        week53G = (CheckBox)mView.findViewById(R.id.week5_3g);
        week53G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week3G |= 32;
                }
                else
                    week3G &= ~32;
            }
        });
        week63G = (CheckBox)mView.findViewById(R.id.week6_3g);
        week63G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week3G |= 64;
                }
                else
                    week3G &= ~64;
            }
        });
        week73G = (CheckBox)mView.findViewById(R.id.week7_3g);
        week73G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    week3G |= 1;
                }
                else
                    week3G &= ~1;
            }
        });

        return mView;
    }
    public void updateSetting(AlarmSetting settings){
        if(settings.getWeek(1) != 0){
            if((settings.getWeek(1) & 1) != 0) {
                week71G.setChecked(true);
                hourEdit1.setText(Integer.toString(settings.getHour(1)));
                minuteEdit1.setText(Integer.toString(settings.getMinute(1)));
                vibrateEdit1.setText(Integer.toString(settings.getVibrat(1)));
            }
            else
                week71G.setChecked(false);
            if((settings.getWeek(1) & 2) != 0) {
                week11G.setChecked(true);
                hourEdit1.setText(Integer.toString(settings.getHour(1)));
                minuteEdit1.setText(Integer.toString(settings.getMinute(1)));
                vibrateEdit1.setText(Integer.toString(settings.getVibrat(1)));
            }
            else
                week11G.setChecked(false);
            if((settings.getWeek(1) & 4) != 0) {
                week21G.setChecked(true);
                hourEdit1.setText(Integer.toString(settings.getHour(1)));
                minuteEdit1.setText(Integer.toString(settings.getMinute(1)));
                vibrateEdit1.setText(Integer.toString(settings.getVibrat(1)));
            }
            else
                week21G.setChecked(false);
            if((settings.getWeek(1) & 8) != 0) {
                week31G.setChecked(true);
                hourEdit1.setText(Integer.toString(settings.getHour(1)));
                minuteEdit1.setText(Integer.toString(settings.getMinute(1)));
                vibrateEdit1.setText(Integer.toString(settings.getVibrat(1)));
            }
            else
                week31G.setChecked(false);
            if((settings.getWeek(1) & 16) != 0) {
                week41G.setChecked(true);
                hourEdit1.setText(Integer.toString(settings.getHour(1)));
                minuteEdit1.setText(Integer.toString(settings.getMinute(1)));
                vibrateEdit1.setText(Integer.toString(settings.getVibrat(1)));
            }
            else
                week41G.setChecked(false);
            if((settings.getWeek(1) & 32) != 0) {
                week51G.setChecked(true);
                hourEdit1.setText(Integer.toString(settings.getHour(1)));
                minuteEdit1.setText(Integer.toString(settings.getMinute(1)));
                vibrateEdit1.setText(Integer.toString(settings.getVibrat(1)));
            }
            else
                week51G.setChecked(false);
            if((settings.getWeek(1) & 64) != 0) {
                week61G.setChecked(true);
                hourEdit1.setText(Integer.toString(settings.getHour(1)));
                minuteEdit1.setText(Integer.toString(settings.getMinute(1)));
                vibrateEdit1.setText(Integer.toString(settings.getVibrat(1)));
            }
            else
                week61G.setChecked(false);
        }
        if(settings.getWeek(2) != 0){
            if((settings.getWeek(2) & 1) != 0) {
                week72G.setChecked(true);
                hourEdit2.setText(Integer.toString(settings.getHour(2)));
                minuteEdit2.setText(Integer.toString(settings.getMinute(2)));
                vibrateEdit2.setText(Integer.toString(settings.getVibrat(2)));
            }
            else
                week72G.setChecked(false);
            if((settings.getWeek(2) & 2) != 0) {
                week12G.setChecked(true);
                hourEdit2.setText(Integer.toString(settings.getHour(2)));
                minuteEdit2.setText(Integer.toString(settings.getMinute(2)));
                vibrateEdit2.setText(Integer.toString(settings.getVibrat(2)));
            }
            else
                week12G.setChecked(false);
            if((settings.getWeek(2) & 4) != 0) {
                week22G.setChecked(true);
                hourEdit2.setText(Integer.toString(settings.getHour(2)));
                minuteEdit2.setText(Integer.toString(settings.getMinute(2)));
                vibrateEdit2.setText(Integer.toString(settings.getVibrat(2)));
            }
            else
                week22G.setChecked(false);
            if((settings.getWeek(2) & 8) != 0) {
                week32G.setChecked(true);
                hourEdit2.setText(Integer.toString(settings.getHour(2)));
                minuteEdit2.setText(Integer.toString(settings.getMinute(2)));
                vibrateEdit2.setText(Integer.toString(settings.getVibrat(2)));
            }
            else
                week32G.setChecked(false);
            if((settings.getWeek(2) & 16) != 0) {
                week42G.setChecked(true);
                hourEdit2.setText(Integer.toString(settings.getHour(2)));
                minuteEdit2.setText(Integer.toString(settings.getMinute(2)));
                vibrateEdit2.setText(Integer.toString(settings.getVibrat(2)));
            }
            else
                week42G.setChecked(false);
            if((settings.getWeek(2) & 32) != 0) {
                week52G.setChecked(true);
                hourEdit2.setText(Integer.toString(settings.getHour(2)));
                minuteEdit2.setText(Integer.toString(settings.getMinute(2)));
                vibrateEdit2.setText(Integer.toString(settings.getVibrat(2)));
            }
            else
                week52G.setChecked(false);
            if((settings.getWeek(2) & 64) != 0) {
                week62G.setChecked(true);
                hourEdit2.setText(Integer.toString(settings.getHour(2)));
                minuteEdit2.setText(Integer.toString(settings.getMinute(2)));
                vibrateEdit2.setText(Integer.toString(settings.getVibrat(2)));
            }
            else
                week62G.setChecked(false);
        }
        if(settings.getWeek(3) != 0){
            if((settings.getWeek(3) & 1) != 0) {
                week73G.setChecked(true);
                hourEdit3.setText(Integer.toString(settings.getHour(3)));
                minuteEdit3.setText(Integer.toString(settings.getMinute(3)));
                vibrateEdit3.setText(Integer.toString(settings.getVibrat(3)));
            }
            else
                week73G.setChecked(false);
            if((settings.getWeek(3) & 2) != 0) {
                week13G.setChecked(true);
                hourEdit3.setText(Integer.toString(settings.getHour(3)));
                minuteEdit3.setText(Integer.toString(settings.getMinute(3)));
                vibrateEdit3.setText(Integer.toString(settings.getVibrat(3)));
            }
            else
                week13G.setChecked(false);
            if((settings.getWeek(3) & 4) != 0) {
                week23G.setChecked(true);
                hourEdit3.setText(Integer.toString(settings.getHour(3)));
                minuteEdit3.setText(Integer.toString(settings.getMinute(3)));
                vibrateEdit3.setText(Integer.toString(settings.getVibrat(3)));
            }
            else
                week23G.setChecked(false);
            if((settings.getWeek(3) & 8) != 0) {
                week33G.setChecked(true);
                hourEdit3.setText(Integer.toString(settings.getHour(3)));
                minuteEdit3.setText(Integer.toString(settings.getMinute(3)));
                vibrateEdit3.setText(Integer.toString(settings.getVibrat(3)));
            }
            else
                week33G.setChecked(false);
            if((settings.getWeek(3) & 16) != 0) {
                week43G.setChecked(true);
                hourEdit3.setText(Integer.toString(settings.getHour(3)));
                minuteEdit3.setText(Integer.toString(settings.getMinute(3)));
                vibrateEdit3.setText(Integer.toString(settings.getVibrat(3)));
            }
            else
                week43G.setChecked(false);
            if((settings.getWeek(3) & 32) != 0) {
                week53G.setChecked(true);
                hourEdit3.setText(Integer.toString(settings.getHour(3)));
                minuteEdit3.setText(Integer.toString(settings.getMinute(3)));
                vibrateEdit3.setText(Integer.toString(settings.getVibrat(3)));
            }
            else
                week53G.setChecked(true);
            if((settings.getWeek(3) & 64) != 0) {
                week63G.setChecked(true);
                hourEdit3.setText(Integer.toString(settings.getHour(3)));
                minuteEdit3.setText(Integer.toString(settings.getMinute(3)));
                vibrateEdit3.setText(Integer.toString(settings.getVibrat(3)));
            }
            else
                week63G.setChecked(false);
        }
    }
}
