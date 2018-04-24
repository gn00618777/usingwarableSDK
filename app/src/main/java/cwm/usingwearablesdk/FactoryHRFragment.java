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
import android.widget.TextView;

import cwm.wearablesdk.constants.ID;

public class FactoryHRFragment extends Fragment {

    private View mView;

    private Button hrGoldenTest;
    private Button hrTargetTest;
    private Button hrLightLeakTest;
    private Button clearHRResult;
    private TextView adcInterval;
    private TextView hrResult;

    String adcIntervalResult = "";
    String hrTestResult = "";

    private ListenForFactoryHRFragment mCallback;

    public interface ListenForFactoryHRFragment{
        void onPressHRTest(int test);
        void onPressClearHRTestResult();
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForFactoryHRFragment)context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForFactoryFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_factory_hr, null);
        }

        hrGoldenTest = (Button)mView.findViewById(R.id.hr_golden);
        hrGoldenTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressClearHRTestResult();
                mCallback.onPressHRTest(ID.HR_GOLDEN_TEST);
            }
        });
        hrTargetTest = (Button) mView.findViewById(R.id.hr_target);
        hrTargetTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressClearHRTestResult();
                mCallback.onPressHRTest(ID.HR_TARGET_TEST);
            }
        });
        hrLightLeakTest = (Button)mView.findViewById(R.id.hr_light_leask);
        hrLightLeakTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressClearHRTestResult();
                mCallback.onPressHRTest(ID.HR_LIGHT_LEAK_TEST);
            }
        });
        clearHRResult = (Button)mView.findViewById(R.id.clear_hr_reult);
        clearHRResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressClearHRTestResult();
            }
        });

        adcInterval = (TextView)mView.findViewById(R.id.adc_interval);
        adcInterval.setText(adcIntervalResult);
        hrResult = (TextView)mView.findViewById(R.id.hr_result);
        hrResult.setText(hrTestResult);
        return mView;
    }

    public void updateHRTestResult(String adcInterval, String result){
        adcIntervalResult = adcInterval;
        hrTestResult = result;
    }

}
