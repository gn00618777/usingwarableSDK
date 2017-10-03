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

public class RequestSleepFragment extends Fragment {

    private View mView;
    private Button syncButton;

    ListenForRequestSleepFragment mCallback;

    // Container Activity must implement this interface
    public interface ListenForRequestSleepFragment {
         void onRequestSleepLog();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForRequestSleepFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForRequestSleepFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_request_sleep, null);
        }
        syncButton = (Button) mView.findViewById(R.id.request);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onRequestSleepLog();
            }
        });

        return mView;
    }
}
