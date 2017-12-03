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
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import cwm.wearablesdk.TabataSettings;

public class SensorsFragment extends Fragment {

    private View mView;

    private CheckBox adxlCheckBox;
    private CheckBox bmiCheckBox;
    private CheckBox heartCheckBox;
    private CheckBox pressureCheckBox;

    private TextView adxlView;
    private TextView bmiView;
    private TextView heartView;
    private TextView pressureView;

    private boolean adxlChecked = false;
    private boolean bmiChecked = false;

    private int sensorType = 0;


    // Container Activity must implement this interface
    public interface ListenForSensorFragment {
        void onPressCheckBox(int sensorType);
    }

    private ListenForSensorFragment mCallback;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForSensorFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForCommandTestFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_sensor, null);
        }

        adxlCheckBox = (CheckBox) mView.findViewById(R.id.checkbox_adxl);
        bmiCheckBox = (CheckBox) mView.findViewById(R.id.checkbox_bmi160);
        heartCheckBox = (CheckBox) mView.findViewById(R.id.checkbox_heart);
        pressureCheckBox = (CheckBox) mView.findViewById(R.id.checkbox_pressure);

        adxlView = (TextView)mView.findViewById(R.id.adxl_raw);
        bmiView = (TextView)mView.findViewById(R.id.bmi_raw);
        heartView = (TextView)mView.findViewById(R.id.heart_raw);
        pressureView = (TextView)mView.findViewById(R.id.pressure_raw);

        adxlCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                   sensorType = sensorType | 0x01;
                else {
                    sensorType = sensorType & ~(0x01);
                }

                mCallback.onPressCheckBox(sensorType);
            }
        });
        bmiCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    sensorType = sensorType | 0x02;
                else {
                    sensorType = sensorType & ~(0x02);
                }

                mCallback.onPressCheckBox(sensorType);
            }
        });
        heartCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    sensorType = sensorType | 0x04;
                else {
                    sensorType = sensorType & ~(0x04);
                }
                mCallback.onPressCheckBox(sensorType);
            }
        });
        pressureCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    sensorType = sensorType | 0x08;
                else {
                    sensorType = sensorType & ~(0x08);
                }

                mCallback.onPressCheckBox(sensorType);
            }
        });

        return mView;
    }

    public void refreshTextView(int type, float[] acc, float[] gyro, int trustLevel, int heart, int signal, float temperature, float pressure){
        Log.d("bernie","type is"+Integer.toString(type));
        if(type == 1)
           adxlView.setText("Acc:   "+String.format("%10f",acc[0])+", "+
                                    String.format("%10f",acc[1])+", "+
                                    String.format("%10f",acc[2]));
        else if(type == 2){
            bmiView.setText("Acc:   " + String.format("%10f",acc[0])+", "+
                    String.format("%10f",acc[1])+", "+
                    String.format("%10f",acc[2])+"\n"+
                    "Gyro: "+String.format("%10f",gyro[0])+", "+
                            String.format("%10f",gyro[1])+", "+
                            String.format("%10f",gyro[2]));
        }
        else if(type == 3){
            heartView.setText("Trust Level: "+Integer.toString(trustLevel)+"\n"+
                               "Heart: "+Integer.toString(heart)+"\n"+
                               "Signal: "+Integer.toString(signal));
        }
        else if(type == 4){
            pressureView.setText("Temperature: "+Float.toString(temperature)+"\n"+
                                 "Pressure: "+Float.toString(pressure));
        }
    }

}
