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
    private CheckBox vibratorCheckBox;

    private TextView adxlView;
    private TextView bmiView;
    private TextView heartView;
   // private TextView pressureView;

    private boolean adxlChecked = false;
    private boolean bmiChecked = false;

    private float gyroData1 = 0;
    private float gyroData2 = 0;
    private float gyroData3 = 0;
    private float adxlAccData1 = 0;
    private float adxlAccData2 = 0;
    private float adxlAccData3 = 0;
    private float bmiAccData1 = 0;
    private float bmiAccData2 = 0;
    private float bmiAccData3 = 0;

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
        vibratorCheckBox = (CheckBox) mView.findViewById(R.id.checkbox_vibrator);

        adxlView = (TextView)mView.findViewById(R.id.adxl_raw);
        bmiView = (TextView)mView.findViewById(R.id.bmi_raw);
        heartView = (TextView)mView.findViewById(R.id.heart_raw);
        //pressureView = (TextView)mView.findViewById(R.id.pressure_raw);

        adxlCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                   sensorType = sensorType | 0x01;
                else {
                    sensorType = sensorType & ~(0x01);
                }
                Log.d("bernie","sensorType is"+Integer.toString(sensorType));
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
                Log.d("bernie","sensorType is"+Integer.toString(sensorType));
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
                Log.d("bernie","sensorType is"+Integer.toString(sensorType));
                mCallback.onPressCheckBox(sensorType);
            }
        });
        vibratorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    sensorType = sensorType | 0x10;
                else {
                    sensorType = sensorType & ~0x10;
                }
                mCallback.onPressCheckBox(sensorType);
            }
        });
       /* if(sensorType == 0){
            //Log.d("bernie","app adxl:"+Float.toString(adxlAccData1)+" "+Float.toString(adxlAccData2)+" "+Float.toString(adxlAccData3));
            adxlView.setText("Acc:   " + String.format("%10f", adxlAccData1) + ", " +
                    String.format("%10f", adxlAccData2) + ", " +
                    String.format("%10f", adxlAccData3));
        }*/
       /* else if (sensorType == 1) {
            //Log.d("bernie","app bmi:"+Float.toString(bmiAccData1)+" "+Float.toString(bmiAccData2)+" "+Float.toString(bmiAccData3));
            bmiView.setText("Acc:   " + String.format("%10f", bmiAccData1) + ", " +
                    String.format("%10f", bmiAccData2) + ", " +
                    String.format("%10f", bmiAccData3) + "\n" +
                    "Gyro: " + String.format("%10f", gyroData1) + ", " +
                    String.format("%10f", gyroData2) + ", " +
                    String.format("%10f", gyroData3));
        }*/

        return mView;
    }

    public void refreshTextView(int type, float[] acc, float[] gyro){
        //sensorType = type;
        if(type == 0) {
            //Log.d("bernie","app adxl:"+Float.toString(adxlAccData1)+" "+Float.toString(adxlAccData2)+" "+Float.toString(adxlAccData3));
            adxlView.setText("Acc:   " + String.format("%10f", acc[0]) + ", " +
                        String.format("%10f", acc[1]) + ", " +
                        String.format("%10f", acc[2]));

          //  adxlAccData1 = acc[0];
          //  adxlAccData2 = acc[1];
          //  adxlAccData3 = acc[2];
        }
        else if(type == 1){
           // Log.d("bernie","app gyro:"+Float.toString(gyro[0])+" "+Float.toString(gyro[1])+" "+Float.toString(gyro[2]));
            bmiView.setText("Acc:   " + String.format("%10f", acc[0]) + ", " +
                    String.format("%10f", acc[1]) + ", " +
                    String.format("%10f", acc[2]) + "\n" +
                    "Gyro: " + String.format("%10f", gyro[0]) + ", " +
                    String.format("%10f", gyro[1]) + ", " +
                    String.format("%10f", gyro[2]));
            //gyroData1 = gyro[0];
            //gyroData2 = gyro[1];
            //gyroData3 = gyro[2];
            //bmiAccData1 = acc[0];
            //bmiAccData2 = acc[1];
            //bmiAccData3 = acc[2];

        }
    }
    public void refreshHeartTextView(int heart){
        heartView.setText("Heart: "+Integer.toString(heart)+"\n");
    }

}
