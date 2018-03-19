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

public class FlashFragment extends Fragment {

    private View mView;
    private Button syncEraseButton;
    private Button syncAutoButton;

    // Container Activity must implement this interface
    public interface ListenForFlashFragment {
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

        syncEraseButton = (Button)mView.findViewById(R.id.sync_erase);
        syncAutoButton = (Button)mView.findViewById(R.id.sync_auto_sync);


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


}
