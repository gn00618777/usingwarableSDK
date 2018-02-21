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
import android.widget.TextView;
import android.widget.Toast;

public class RunFragment extends Fragment {

    private View mView;
    private CheckBox enableRunCheckbox;
    private TextView msgView;

    private int mStep = 0;
    private int mDistance = 0;
    private int mStepFreq = 0;
    private int mHeart = 0;
    private String mGesture = "";

    ListenForRunFragment mCallback;

    // Container Activity must implement this interface
    public interface ListenForRunFragment {
         void onRequesEnableRun(int enabled);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForRunFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForRunFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_run, null);
        }
        enableRunCheckbox = (CheckBox)mView.findViewById(R.id.enable_run);
        msgView = (TextView)mView.findViewById(R.id.run_msg);
        msgView.setText("姿態:"+mGesture+"\n"+"步數:"+Integer.toString(mStep)+"\n"+"距離:"+Integer.toString(mDistance)+"\n"+"步頻:"+Integer.toString(mStepFreq)+"\n"+"心律:"+Integer.toString(mHeart));
        enableRunCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mCallback.onRequesEnableRun(1);
                }
                else{
                    mCallback.onRequesEnableRun(0);
                }
            }
        });
        return mView;
    }

    public void setValue(int step, int distance, int freq, String gesture){
        mStep = step;
        mDistance = distance;
        mStepFreq = freq;
        mGesture = gesture;
    }

    public void setHeartValue(int heart){
        mHeart = heart;
    }
}
