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

public class TimeSyncFragment extends Fragment {

    private View mView;
    private Button syncButton;

    ListenForSyncTimeFragment mCallback;

    // Container Activity must implement this interface
    public interface ListenForSyncTimeFragment {
         void onRequestSyncTime();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForSyncTimeFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForRingStausFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_time_sync, null);
        }
        syncButton = (Button) mView.findViewById(R.id.sync);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onRequestSyncTime();
            }
        });

        return mView;
    }
}
