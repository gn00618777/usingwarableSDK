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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.util.Log;
import android.widget.TextView;

public class SensorsFragment extends Fragment {

    private View mView;

    private CheckBox adxlCheckBox;
    private CheckBox bmiCheckBox;
    private CheckBox heartCheckBox;
    private CheckBox vibratorCheckBox;

    private TextView adxlView;
    private TextView bmiView;
    private TextView heartView;

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
    public void onHiddenChanged(boolean hidd) {
        if (hidd) {

        } else {

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

        return mView;
    }

    public void refreshTextView(int type, float[] acc, float[] gyro){
        if(type == 0) {
            adxlView.setText("Acc:   " + String.format("%10f", acc[0]) + ", " +
                        String.format("%10f", acc[1]) + ", " +
                        String.format("%10f", acc[2]));
        }
        else if(type == 1){
            bmiView.setText("Acc:   " + String.format("%10f", acc[0]) + ", " +
                    String.format("%10f", acc[1]) + ", " +
                    String.format("%10f", acc[2]) + "\n" +
                    "Gyro: " + String.format("%10f", gyro[0]) + ", " +
                    String.format("%10f", gyro[1]) + ", " +
                    String.format("%10f", gyro[2]));
        }
    }
    public void refreshHeartTextView(int heart){
        heartView.setText("Heart: "+Integer.toString(heart)+"\n");
    }

}
