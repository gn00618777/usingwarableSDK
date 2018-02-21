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
    private Button syncEraseButton;
    private Button syncAutoButton;
    private Button eraseOledButton;
    private Button eraseBitMapButton;
    private Button eraseFontButton;

    // Container Activity must implement this interface
    public interface ListenForFlashFragment {
        public void onPressSyncEraseButton();
        public void onPressSyncAutoButton();
        public void onPressEraseOLEDButton();
        public void onPressEraseBitMapButton();
        public void onPressEraseFontButton();

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

        syncEraseButton = (Button)mView.findViewById(R.id.sync_erase);
        syncAutoButton = (Button)mView.findViewById(R.id.sync_auto_sync);
        eraseOledButton = (Button)mView.findViewById(R.id.oled_erase);
        eraseBitMapButton = (Button)mView.findViewById(R.id.bitmap_erase);
        eraseFontButton = (Button)mView.findViewById(R.id.font_erase);

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
        eraseOledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressEraseOLEDButton();
            }
        });

        eraseBitMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressEraseBitMapButton();
            }
        });

        eraseFontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressEraseFontButton();
            }
        });

        return mView;
    }


}
