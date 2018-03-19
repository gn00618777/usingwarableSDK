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

public class SelectTypeFragment extends Fragment {
    //UI
    private View mView;
    private TextView newDevice;
    private TextView diconnectDevice;
    private TextView connectDevice;
    private TextView deviceBound;
    private TextView deviceBoundAddress;
    private TextView deviceBoundStatus;
    private Button unBondButton;

    private String mDevice = null;
    private String mDeviceAddress = null;
    private Boolean mDeviceStatus = false;

    // Container Activity must implement this interface
    public interface ListenForSelectTypeFragment {
        public void onNewADevice();
        public void onDisconnectFromDevice();
        public void onConnectToDevice();
        public void onResetBoundRecord();
    }

    ListenForSelectTypeFragment mCallback;

    // lift-cycle ----------------------------------------------------------------------------------
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForSelectTypeFragment) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForPersonalInfoFragment");
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        deviceBound.setText("手環: "+mDevice);
        deviceBoundAddress.setText("位址: "+mDeviceAddress);

        if(mDeviceStatus == true) {
            diconnectDevice.setVisibility(View.VISIBLE);
            deviceBoundStatus.setText("狀態: 連線中");
            connectDevice.setVisibility(View.GONE);
        }
        else {
            connectDevice.setVisibility(View.VISIBLE);
            deviceBoundStatus.setText("狀態: 離線");
            diconnectDevice.setVisibility(View.GONE);
        }

        newDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onNewADevice();
            }
        });

        diconnectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onDisconnectFromDevice();
            }
        });

        connectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onConnectToDevice();
            }
        });

        unBondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onResetBoundRecord();
            }
        });


    } // onActivityCreated()

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.activity_select_type, null);
        }
        deviceBound = (TextView)mView.findViewById(R.id.device_bound);
        deviceBoundAddress = (TextView)mView.findViewById(R.id.device_bound_address);
        deviceBoundStatus = (TextView)mView.findViewById(R.id.device_bound_status);
        newDevice = (TextView)mView.findViewById(R.id.new_wearable);
        diconnectDevice = (TextView)mView.findViewById(R.id.disconnet);
        connectDevice = (TextView)mView.findViewById(R.id.connect);
        unBondButton = (Button)mView.findViewById(R.id.unbond_button);

        return mView;
    } // onCreateView()

    //function
    public void setDevice(String name, String address, boolean status){

        mDevice = name;
        mDeviceAddress = address;
        mDeviceStatus = status;

    }
}
