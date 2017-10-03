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

public class RingBatteryFragment extends Fragment {

    private View mView;
    private TextView batteryView;
    private Button syncButton;
    int mBattery = 0;

    ListenForRingStatusFragment mCallback;

    // Container Activity must implement this interface
    public interface ListenForRingStatusFragment {
        public void onRequestBattery();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForRingStatusFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForRingStausFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_ring_battery, null);
        }

        batteryView = (TextView)mView.findViewById(R.id.battery);
        syncButton = (Button) mView.findViewById(R.id.sync);

        batteryView.setText("電池電量: "+mBattery+"%");

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onRequestBattery();
            }
        });

        return mView;
    }

    public void setValue(int battery){
        mBattery = battery;
    }
}
