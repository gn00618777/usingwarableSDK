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
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import cwm.wearablesdk.TabataSettings;

public class FlashFragment extends Fragment {

    private View mView;
    private Button syncStartButton;
    private Button syncSuccessButton;
    private Button syncFailButton;
    private Button syncEraseButton;
    private Button syncAutoButton;

    private TextView receivedStatus;
    private String status = "";


    // Container Activity must implement this interface
    public interface ListenForFlashFragment {
        public void onPressSyncStartButton();
        public void onPressSyncSuccessButton();
        public void onPressSyncFailButton();
        public void onPressSyncEraseButton();
        public void onPressSyncAutoButton();

    }

    private ListenForFlashFragment mCallback;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForFlashFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForFlashFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_flash, null);
        }

        syncStartButton = (Button)mView.findViewById(R.id.sync_start);
        syncSuccessButton = (Button)mView.findViewById(R.id.sync_sucess);
        syncFailButton = (Button)mView.findViewById(R.id.sync_failed);
        syncEraseButton = (Button)mView.findViewById(R.id.sync_erase);
        syncAutoButton = (Button)mView.findViewById(R.id.sync_auto_sync);

        receivedStatus = (TextView)mView.findViewById(R.id.received_status);
        receivedStatus.setText("Status: "+status);

        syncStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressSyncStartButton();
            }
        });
        syncSuccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressSyncSuccessButton();
            }
        });
        syncFailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressSyncFailButton();
            }
        });
        syncEraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressSyncEraseButton();
            }
        });
        syncAutoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressSyncAutoButton();
            }
        });


        return mView;
    }

    public void setReceivedStatus(String s){
        status = s;
    }




}
